/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.controller;

//import java.net.http.HttpResponse;
import loudest_mindset.model.User;
//import loudest_mindset.repository.UserRepository;
//import java.util.List;
import loudest_mindset.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import loudest_mindset.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author user
 */
@RestController
public class UserController {

    //@Autowired
    //private UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * For testing purposes
     *
     * @return welcome
     */
    @GetMapping(value = "/")
    public String homepage() {

        return "<h1>welcome</h1>";
    }

    @PostMapping(value = "/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {

        ResponseMessage message = new ResponseMessage("invalid input");

        try {

            boolean isValid = userService.checkNewUser(user);

            boolean isUnique = userService.checkUniqueData(user);

            if (!isValid) {

                throw new Exception("userdata invalid");
            }

            if (!isUnique) {

                message = new ResponseMessage("Username or Email taken");

                throw new Exception(message.getText());
            }

            // set id to MIN_VALUE to prevent overwriting
            user.setId(Long.MIN_VALUE);

            userService.saveUser(user);

            // password does never leave server
            user.setPassword("secret");

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping(value = "/getUserByName/{userName}")
    public ResponseEntity<?> getUserByName(@PathVariable String userName) {

        ResponseMessage responseMessage = new ResponseMessage("User not found");

        try {

            User foundUser = userService.getUserByName(userName);

            if (foundUser.getName().length() <= 0) {

                throw new Exception(responseMessage.getText());
            }

            // password does never leave server
            foundUser.setPassword("secret");

            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getUserById/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {

        ResponseMessage responseMessage = new ResponseMessage("User not found");

        try {

            User foundUser = userService.getUserById(userId);

            if (foundUser.getName().length() <= 0) {

                throw new Exception(responseMessage.getText());
            }

            // password does never leave server
            foundUser.setPassword("secret");

            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/updateUser")
    public ResponseEntity<?> updateUser(@RequestHeader String apiKey, @RequestBody User user) {

        ResponseMessage responseMessage = new ResponseMessage("Invalid data");

        try {

            boolean validKey = userService.checkPassword(apiKey, user.getId());

            boolean validUpdate = userService.checkValidUpdate(user);

            if (!validKey || !validUpdate) {

                throw new Exception(responseMessage.getText());
            }

            userService.updateUser(user);

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader String apiKey, @PathVariable long id) {

        ResponseMessage responseMessage = new ResponseMessage("user deleted");

        try {

            User user = userService.getUserById(id);

            if (!user.checkPassword(apiKey)) {

                throw new Exception("Invalid key");
            }

            userService.deleteUser(id);

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception ex) {

            responseMessage.setText("cannot delete user");

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        }

    }

}
