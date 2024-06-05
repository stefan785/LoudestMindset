/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.service;

import loudest_mindset.model.Shout;
import loudest_mindset.repository.ShoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class ShoutService {

    @Autowired
    private ShoutRepository shoutRepository;

    public void saveShout(Shout shout) {

        shout.setTimestamp();

        shoutRepository.save(shout);

    }
}
