/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package loudest_mindset.repository;

import java.util.List;
import loudest_mindset.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



/**
 *
 * @author user
 */
public interface PhotoRepository extends JpaRepository<Photo , Long>{
    
    @Query("SELECT p from Photo p WHERE p.userId = ?1")
    List<Photo> findPhotosByUserId(long userId);
    
}
