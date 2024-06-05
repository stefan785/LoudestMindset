/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package loudest_mindset.repository;

import java.util.List;
import loudest_mindset.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface CommentRepository extends JpaRepository<Comment , Long>{

    @Query("SELECT c FROM Comment c WHERE c.shoutId = ?1")
    public List<Comment> findCommentsByShoutId(Long shoutId);
    
}
