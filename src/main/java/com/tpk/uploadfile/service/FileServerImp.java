package com.tpk.uploadfile.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileServerImp implements StoredFileService {

    private final Path rootLocation;

    public FileServerImp(@Value("${path.location}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
    }

    @SneakyThrows
    @Override
    public void saveFileToFolder(MultipartFile multipartFile){
        if (multipartFile.isEmpty()) {
            throw new Exception("Multipart File Is Empty");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @SneakyThrows
    @Override
    public Resource getFile(String filename) {
        Path filePath = Paths.get(rootLocation.toUri()).resolve(filename).normalize();
        Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
        if (!resource.exists()) {
            resource.isReadable();
        }
        return resource;
    }

    @Override
    public boolean deleteFile(String fileName) {
        Path filePath = Paths.get(rootLocation.toUri().resolve(fileName).normalize());
        try {
            Files.delete(filePath);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
