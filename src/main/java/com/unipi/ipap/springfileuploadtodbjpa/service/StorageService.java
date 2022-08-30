package com.unipi.ipap.springfileuploadtodbjpa.service;

import com.unipi.ipap.springfileuploadtodbjpa.entity.FileData;
import com.unipi.ipap.springfileuploadtodbjpa.entity.ImageData;
import com.unipi.ipap.springfileuploadtodbjpa.repository.FileDataRepository;
import com.unipi.ipap.springfileuploadtodbjpa.repository.StorageRepository;
import com.unipi.ipap.springfileuploadtodbjpa.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Slf4j
@Service
public class StorageService {

    @Value(value = "${folder.path}")
    private String folderPath;

    private final StorageRepository storageRepository;
    private final FileDataRepository fileDataRepository;

    public StorageService(StorageRepository storageRepository, FileDataRepository fileDataRepository) {
        this.storageRepository = storageRepository;
        this.fileDataRepository = fileDataRepository;
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

    public boolean uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = System.getProperty("user.home") + this.folderPath + "\\" + file.getOriginalFilename();
        log.info("Home folder is: {}", file);

        FileData fileData = FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .build();

        FileData fileResult = fileDataRepository.save(fileData);

        file.transferTo(new File(filePath));

        if (Objects.nonNull(fileResult.getName())) {
            log.info("File uploaded successfully: {}", filePath);
        }
        return true;
    }

    public byte[] downloadImageFromFileSystem(String fileName) {
        return fileDataRepository.findByName(fileName)
                .map(FileData::getFilePath)
                .map(filePath -> {
                    try {
                        return Files.readAllBytes(new File(filePath).toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}
