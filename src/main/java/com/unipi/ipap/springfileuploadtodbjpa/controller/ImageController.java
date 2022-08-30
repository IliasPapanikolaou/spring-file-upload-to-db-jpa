package com.unipi.ipap.springfileuploadtodbjpa.controller;

import com.unipi.ipap.springfileuploadtodbjpa.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final StorageService storageService;

    public ImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Store images in database.
     * More secure access and less frequently used images
     */
    @PostMapping
    public ResponseEntity<Boolean> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storageService.uploadImage(file));
    }

    /**
     * Retrieve images from database
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(storageService.downloadImage(fileName));
    }

    /**
     * Store images in filesystem
     * faster access where images are used frequently
     */
    @PostMapping("/filesystem")
    public ResponseEntity<Boolean> uploadImageToFileSystem(@RequestParam("image")MultipartFile file) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storageService.uploadImageToFileSystem(file));
    }

    /**
     * Retrieve images from database
     */
    @GetMapping("/filesystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(storageService.downloadImageFromFileSystem(fileName));
    }
}
