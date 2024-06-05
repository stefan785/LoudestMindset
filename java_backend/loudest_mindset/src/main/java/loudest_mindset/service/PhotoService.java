/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.service;

import java.util.List;
import loudest_mindset.model.Photo;
import loudest_mindset.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo getPhoto(long photoId) {

        Photo photo = photoRepository.findById(photoId).get();
        
        return photo;
    }
    
    
    public List<Photo> getAllPhotos(long userId){
        
       return photoRepository.findPhotosByUserId(userId);
       
    }
}
