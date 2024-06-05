/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package loudest_mindset.repository;

import java.util.List;
import loudest_mindset.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface MessageRepository extends JpaRepository<Message , Long>{

    public List<Message> findMessagesByReceiverId(Long userId);

    public List<Message> findMessagesBySenderId(Long userId);
    
    @Query("UPDATE Message m SET m.text = ?1 where m.id = ?2")
    public void updateMessage(String text , Long id);
}
