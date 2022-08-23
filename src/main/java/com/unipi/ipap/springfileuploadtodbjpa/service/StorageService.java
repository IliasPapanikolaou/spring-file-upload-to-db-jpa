package com.unipi.ipap.springfileuploadtodbjpa.service;

import com.unipi.ipap.springfileuploadtodbjpa.entity.ImageData;
import com.unipi.ipap.springfileuploadtodbjpa.repository.StorageRepository;
import com.unipi.ipap.springfileuploadtodbjpa.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Slf4j
@Service
public class StorageService {

    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public boolean uploadImage(MultipartFile file) throws IOException {
        storageRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());

        log.info("File uploaded successfully: {}", file.getOriginalFilename());
        return true;
    }

    public byte[] downloadImage(String fileName) {
        return storageRepository.findByName(fileName)
                .map(ImageData::getImageData)
                .map(ImageUtils::decompressImage)
                .orElseThrow(EntityNotFoundException::new);
    }
}
