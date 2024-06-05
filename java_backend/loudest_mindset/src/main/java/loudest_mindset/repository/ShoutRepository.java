/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package loudest_mindset.repository;

import java.util.List;
import loudest_mindset.model.Shout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author user
 */
public interface ShoutRepository extends JpaRepository<Shout , Long>{
    

    
    @Query("SELECT s FROM Shout s WHERE s.userId = ?1")
    public List<Shout> findShoutsByUserId(Long userId);
}
