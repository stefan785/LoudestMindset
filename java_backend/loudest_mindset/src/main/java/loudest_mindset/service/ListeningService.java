/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.service;

//import java.util.List;
import loudest_mindset.model.Listening;
import loudest_mindset.repository.ListeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class ListeningService {

    @Autowired
    private ListeningRepository listeningRepository;

    public void listenToUser(Listening listening) {

        //check if already listening:
        long senderId = listening.getSenderId();

        long receiverId = listening.getReceiverId();

        boolean alreadyListening = listeningRepository.existsBySenderIdAndReceiverId(senderId, receiverId);

        if (!alreadyListening) {

            listeningRepository.save(listening);
        }

    }

    public void unlistenToUser(Listening listening) {

        long senderId = listening.getSenderId();

        long receiverId = listening.getReceiverId();

        long listeningId = listeningRepository.getIdfromListening(senderId, receiverId).get(0);

        listeningRepository.deleteById(listeningId);
    }
}
