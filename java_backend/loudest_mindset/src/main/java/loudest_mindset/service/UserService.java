/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.service;

import java.util.List;
import loudest_mindset.model.User;
import loudest_mindset.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 *
 *
 *         This class contains the business logic for User-API functions
 */
@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Ckecks if the apiKey matches the Passowrd of that userID
     *
     * @param apiKey
     * @param userId
     * @return true if it matches
     */
    public boolean checkPassword(String apiKey, long userId) {

        User user = userRepository.findById(userId).get();

        return user.checkPassword(apiKey);
    }

    public User getUserByName(String name) {

        List<User> foundUsers = userRepository.findUserByName(name);

        return foundUsers.get(0);
    }

    public User getUserById(long id) {

        return userRepository.findById(id).get();
    }

    /**
     * Checks if the new User object contains valid data
     *
     * @param user
     * @return true if it contains name, email , password
     */
    public boolean checkNewUser(User user) {

        // check for length:
        boolean hasName = (user.getName().length() > 0);

        boolean hasEmail = (user.getEmail().length() > 0);

        boolean hasPassword = (user.getPassword().length() > 0);

        boolean isValid = (hasName && hasEmail && hasPassword);
        // boolean isUnique = checkUniqueData(user);

        return isValid;

    }

    /**
     * Checks if the required data is unique
     *
     * @param user
     * @return true if unique
     */
    public boolean checkUniqueData(User user) {

        List<User> usersByEmail = userRepository.findUserByEmail(user.getEmail());

        List<User> usersByName = userRepository.findUserByName(user.getName());

        // If the lists are empty: no entry yet
        if (usersByName.isEmpty() && usersByEmail.isEmpty()) {

            return true;
        }

        System.out.println("Input data not unique");

        return false;
    }

    /**
     * Checks if the new name/email crashes with existsing names
     *
     * @param userName
     * @return true if update allowed
     */
    public boolean checkValidUpdate(User user) {

        // get old data
        User oldUser = userRepository.findById(user.getId()).get();

        // case: unique data unchanged
        boolean sameName = user.getName().equals(oldUser.getName());

        boolean sameEmail = user.getEmail().equals(oldUser.getEmail());

        if (sameName && sameEmail) {

            return true;
        }

        // check if name or email already exists in database:
        List<User> usersByEmail = userRepository.findUserByEmail(user.getEmail());

        List<User> usersByName = userRepository.findUserByName(user.getName());

        // name already in database
        if (!sameName && !usersByName.isEmpty()) {

            System.out.println("name for update taken");

            return false;
        }

        // email already in database
        if (!sameEmail && !usersByEmail.isEmpty()) {

            System.out.println("email for update taken");

            return false;
        }

        // if we are here: all checks passed:
        return true;

    }

    /**
     * User CAN NOT be actually deleted from DB, just renamed and made unable to
     * interact - Status 99
     *
     * @param id
     * @return true if user was "deleted" as described above
     */
    public boolean deleteUser(long id) {

        try {

            User deletedUser = userRepository.findById(id).get();

            String deletedUserName = "deletedUser" + deletedUser.getId().toString();

            String deletedUserEmail = "deletedEmail" + deletedUser.getId().toString();

            deletedUser.setName(deletedUserName);

            deletedUser.setEmail(deletedUserEmail);

            deletedUser.setStatus(99);

            userRepository.save(deletedUser);

            return true;

        } catch (Exception ex) {

            System.out.println("User with id" + id + " cannot be deleted");

            return false;
        }

    }

    public boolean saveUser(User user) {

        userRepository.save(user);

        return true;
    }

    public boolean updateUser(User user) {

        userRepository.save(user);

        return true;
    }

    public User getUserByEmail(String email) {

        try {

            Optional<User> user = userRepository.getUserByEmail(email);

            return user.get();

        } catch (Exception e) {

            System.out.println("email not unique");
        }

        // failure:
        return null;

    }

}
