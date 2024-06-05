/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package loudest_mindset.repository;

import java.util.List;
import loudest_mindset.model.Listening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface ListeningRepository extends JpaRepository< Listening, Long> {

    @Query("SELECT l.id FROM Listening l WHERE l.senderId = ?1 AND l.receiverId = ?2")
    public List<Long> getIdfromListening(long senderId, long receiverId);

    public boolean existsBySenderIdAndReceiverId(long senderId, long receiverId);

    public List<Listening> findByReceiverId(Long userId);
}
