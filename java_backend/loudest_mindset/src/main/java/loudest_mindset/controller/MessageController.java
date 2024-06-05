/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.controller;

import java.util.List;
import loudest_mindset.model.Message;
import loudest_mindset.model.ResponseMessage;
import loudest_mindset.model.User;
import loudest_mindset.repository.MessageRepository;
import loudest_mindset.service.MessageService;
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
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    private final MessageService messageService;

    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping(value = "/createMessage")
    public ResponseEntity<?> createMessage(@RequestHeader String apiKey, @RequestBody Message message) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(message.getSenderId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            messageService.saveMessage(message);

            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getReceivedMessages/{userId}")
    public ResponseEntity<?> getReceivedMessages(@RequestHeader String apiKey, @PathVariable Long userId) {

        ResponseMessage responseMessage = new ResponseMessage("Messages not found");

        try {

            User user = userService.getUserById(userId);

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }
            List<Message> allMessages = messageRepository.findMessagesByReceiverId(userId);

            if (allMessages.isEmpty()) {

                throw new Exception("Messages not found");
            }

            return new ResponseEntity<>(allMessages, HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getSentMessages/{userId}")
    public ResponseEntity<?> getSentMessages(@RequestHeader String apiKey, @PathVariable Long userId) {

        ResponseMessage responseMessage = new ResponseMessage("Messages not found");

        try {

            User user = userService.getUserById(userId);

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }
            List<Message> allMessages = messageRepository.findMessagesBySenderId(userId);

            if (allMessages.isEmpty()) {

                throw new Exception("Messages not found");
            }

            return new ResponseEntity<>(allMessages, HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/editMessage")
    public ResponseEntity<?> editMessage(@RequestHeader String apiKey, @RequestBody Message message) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(message.getSenderId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            message.setTimestamp();

            //messageRepository.updateMessage(message.getText(), message.getId());
            messageRepository.save(message);

            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/deleteMessage/{messageId}")
    public ResponseEntity<?> deleteMessage(@RequestHeader String apiKey, @PathVariable long messageId) {

        ResponseMessage errorMessage = new ResponseMessage("Invalid input");
        try {

            Message message = messageRepository.findById(messageId).get();

            User user = userService.getUserById(message.getSenderId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            message.setText("Message deleted");

            messageRepository.save(message);

            ResponseMessage responseMessage = new ResponseMessage("Message deleted");

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
