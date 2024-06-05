/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.controller;

import java.util.List;
import loudest_mindset.model.Listening;
import loudest_mindset.model.ResponseMessage;
import loudest_mindset.model.User;
import loudest_mindset.repository.ListeningRepository;
import loudest_mindset.service.ListeningService;
import loudest_mindset.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author user
 */
@RestController
public class ListeningController {

    @Autowired
    private ListeningRepository listeningRepository;

    private final ListeningService listeningService;

    private final UserService userService;

    @Autowired
    public ListeningController(ListeningService listeningService, UserService userService) {
        this.listeningService = listeningService;
        this.userService = userService;
    }

    //in this case: i am listening, i am the receiver
    @PostMapping(value = "/listenToUser")
    public ResponseEntity<?> listenToUser(@RequestHeader String apiKey, @RequestBody Listening listening) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(listening.getReceiverId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            listeningService.listenToUser(listening);

            return new ResponseEntity<>(listening, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getListening/{userId}")
    public ResponseEntity<?> getListening(@PathVariable Long userId) {

        ResponseMessage responseMessage = new ResponseMessage("Listening data not found");

        try {

            List<Listening> listeningData = listeningRepository.findByReceiverId(userId);

            return new ResponseEntity<>(listeningData, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(value = "/unlistenToUser")
    public ResponseEntity<?> unlistenToUser(@RequestHeader String apiKey, @RequestBody Listening listening) throws Exception {

        ResponseMessage errorMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(listening.getReceiverId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            listeningService.unlistenToUser(listening);

            ResponseMessage responseMessage = new ResponseMessage("unlistening to user");

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

}
