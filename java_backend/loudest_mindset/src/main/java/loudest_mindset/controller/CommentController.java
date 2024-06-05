/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.controller;

import java.util.List;
import loudest_mindset.model.Comment;
import loudest_mindset.model.ResponseMessage;
import loudest_mindset.model.User;
import loudest_mindset.repository.CommentRepository;
import loudest_mindset.service.CommentService;
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
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    private final CommentService commentService;

    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping(value = "/createComment")
    public ResponseEntity<?> createComment(@RequestHeader String apiKey, @RequestBody Comment comment) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(comment.getUserId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            commentService.saveComment(comment);

            return new ResponseEntity<>(comment, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getComments/{shoutId}")
    public ResponseEntity<?> getComments(@PathVariable Long shoutId) {

        ResponseMessage responseMessage = new ResponseMessage("Comments not found");

        try {
            List<Comment> allComments = commentRepository.findCommentsByShoutId(shoutId);

            if (allComments.isEmpty()) {

                throw new Exception(responseMessage.getText());
            }

            return new ResponseEntity<>(allComments, HttpStatus.OK);
        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/editComment")
    public ResponseEntity<?> editComment(@RequestHeader String apiKey, @RequestBody Comment comment) throws Exception {

        ResponseMessage responseMessage = new ResponseMessage("Invalid input");

        try {

            User user = userService.getUserById(comment.getUserId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            commentService.saveComment(comment);

            return new ResponseEntity<>(comment, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/deleteComment/{commentId}")
    public ResponseEntity<?> deleteComment(@RequestHeader String apiKey, @PathVariable long commentId) {

        ResponseMessage errorMessage = new ResponseMessage("Invalid input");
        try {

            Comment comment = commentRepository.findById(commentId).get();

            User user = userService.getUserById(comment.getUserId());

            boolean validKey = user.checkPassword(apiKey);

            if (!validKey) {

                throw new Exception("Invalid key");
            }

            comment.setText("Comment deleted");

            commentRepository.save(comment);

            ResponseMessage message = new ResponseMessage("comment deleted");

            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
