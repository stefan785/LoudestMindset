/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.service;

import loudest_mindset.model.Comment;
import loudest_mindset.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment) {

        comment.setTimestamp();

        commentRepository.save(comment);

    }
}
