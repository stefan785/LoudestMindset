/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package loudest_mindset.repository;

import java.util.List;
import loudest_mindset.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author user
 */
public interface UserRepository extends JpaRepository<User , Long>{
    
    //Function below can be used without query if needed
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> getUserByEmail(String email);
    
    Optional<User> getUserByName(String name);
    
    Optional<User> getUserById(Long Id);
    
    List<User> findUserByEmail(String email);
    
    List<User> findUserByName(String name);
    
}
