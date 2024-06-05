/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.controller;

import java.util.List;
import loudest_mindset.model.ResponseMessage;
import loudest_mindset.model.Shout;
import loudest_mindset.model.User;
import loudest_mindset.repository.ShoutRepository;
import loudest_mindset.service.ShoutService;
import loudest_mindset.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author user
 */
@RestController
public class ShoutController {

    @Autowired
    private ShoutRepository shoutRepository;

    private final ShoutService shoutService;

    private final UserService userService;

    @Autowired
    public ShoutController(ShoutService shoutService, UserService userService) {
        this.shoutService = shoutService;
        this.userService = userService;
    }

    @PostMapping(value = "/createShout")
    public ResponseEntity<?> createShout(@RequestHeader String apiKey, @RequestBody Shout shout) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(shout.getUserId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            shoutService.saveShout(shout);

            return new ResponseEntity<>(shout, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getShouts/{userId}")
    public ResponseEntity<?> getShouts(@PathVariable Long userId) {

        ResponseMessage responseMessage = new ResponseMessage("Shouts not found");

        try {
            List<Shout> allShouts = shoutRepository.findShoutsByUserId(userId);

            if (allShouts.isEmpty()) {

                throw new Exception("Shouts not found");
            }

            return new ResponseEntity<>(allShouts, HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/editShout")
    public ResponseEntity<?> editShout(@RequestHeader String apiKey, @RequestBody Shout shout) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(shout.getUserId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            shout.setTimestamp();

            shoutRepository.save(shout);

            return new ResponseEntity<>(shout, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/deleteShout/{shoutId}")
    public ResponseEntity<?> deleteShout(@RequestHeader String apiKey, @PathVariable long shoutId) {

        ResponseMessage errorMessage = new ResponseMessage("Invalid input");

        try {

            Shout shout = shoutRepository.findById(shoutId).get();

            User user = userService.getUserById(shout.getUserId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            shout.setText("Shout deleted");

            shoutRepository.save(shout);

            ResponseMessage responseMessage = new ResponseMessage("Shout deleted");

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
