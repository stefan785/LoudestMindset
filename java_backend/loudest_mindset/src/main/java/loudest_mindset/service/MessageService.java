/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.service;

import loudest_mindset.model.Message;
import loudest_mindset.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void saveMessage(Message message) {

        message.setTimestamp();

        messageRepository.save(message);

    }
}