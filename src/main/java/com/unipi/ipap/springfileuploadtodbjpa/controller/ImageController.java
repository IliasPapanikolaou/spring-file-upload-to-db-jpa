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

    @PostMapping
    public ResponseEntity<Boolean> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storageService.uploadImage(file));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(storageService.downloadImage(fileName));
    }

}
