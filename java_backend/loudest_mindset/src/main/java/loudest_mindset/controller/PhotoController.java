/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loudest_mindset.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import loudest_mindset.model.Photo;
import loudest_mindset.model.User;
import loudest_mindset.repository.PhotoRepository;
import loudest_mindset.service.PhotoService;
import loudest_mindset.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import io.micrometer.common.lang.Nullable;

/**
 *
 * @author user
 */
@RestController
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    private final UserService userService;

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService, UserService userService) {

        this.photoService = photoService;

        this.userService = userService;
    }

    /**
     * Download function for photos(image-data) via photo URL
     *
     * @param photoHash
     * @param userId
     * @param photoName
     * @return image-file of the photo if successful
     * @throws IOException
     */
    @GetMapping(value = "/uploadedPhotos/{userId}/{photoHash}/{photoName}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    ResponseEntity<?> downloadPhoto(@PathVariable String photoHash,
            @PathVariable long userId, @PathVariable String photoName) throws IOException {

        Path imagePath = Paths.get("uploadedPhotos/" + userId + "/" + photoHash + "/" + photoName);

        try {

            byte[] downloadedPhoto = Files.readAllBytes(imagePath);

            return new ResponseEntity<>(downloadedPhoto, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>("Photo not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/uploadPhoto/{userId}")
    public ResponseEntity<?> uploadPhoto(@RequestHeader String apiKey, @PathVariable long userId,
            @RequestParam("photo") MultipartFile multipartFile) throws Exception {

        try {

            //check if user has the rights to upload in that name
            User user = userService.getUserById(userId);

            boolean hasRights = user.checkPassword(apiKey);

            if (!hasRights) {

                throw new Exception("wrong apiKey");

            }

            //get the File
            @SuppressWarnings("null")
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            //create unique directory
            String fileNameAsSHA256 = DigestUtils.sha256Hex(fileName + LocalDateTime.now().toString());

            Path uploadDirectory = Paths.get("uploadedPhotos/" + userId + "/" + fileNameAsSHA256);

            Files.createDirectories(uploadDirectory);

            Path fileDirectory = uploadDirectory.resolve(fileName);

            Path write = Files.write(fileDirectory, multipartFile.getBytes());

            //create new Photo object and save it
            Photo photo = new Photo();

            photo.setUrl(write.toString());

            photo.setUserId(userId);

            photoRepository.save(photo);

            return new ResponseEntity<>(photo, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>("Invalid Input", HttpStatus.BAD_REQUEST);
        }

    }

    // get getPhoto/photoid
    @GetMapping(value = "/getPhoto/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable long photoId) {

        try {

            Photo photo = photoService.getPhoto(photoId);

            return new ResponseEntity<>(photo, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>("Photos not found", HttpStatus.NOT_FOUND);

        }
    }

    // get getAllPhotos/userid
    @GetMapping(value = "/getAllPhotos/{userId}")
    public ResponseEntity<?> getAllPhotos(@PathVariable long userId) {

        try {

            List<Photo> allPhotos = photoService.getAllPhotos(userId);

            return new ResponseEntity<>(allPhotos, HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>("Photos not found", HttpStatus.NOT_FOUND);

        }

    }

    // delete deletePhoto/photoId
    @DeleteMapping(value = "/deletePhoto/{photoId}")
    public ResponseEntity<?> deletePhoto(@RequestHeader String apiKey, @PathVariable long photoId) throws Exception {

        try {

            Photo photo = photoService.getPhoto(photoId);

            User user = userService.getUserById(photo.getUserId());

            boolean passwordMatch = user.checkPassword(apiKey);

            if (!passwordMatch) {

                throw new Exception("wrong apiKey");
            }

            photoRepository.delete(photo);

            return new ResponseEntity<>("Photo deleted", HttpStatus.OK);

        } catch (Exception ex) {

            return new ResponseEntity<>("Invalid Input", HttpStatus.BAD_REQUEST);
        }
    }

}
