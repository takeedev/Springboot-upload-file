package com.tpk.uploadfile.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
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

    @Override
    public void saveFileToFolder(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()) {
            throw new Exception("Multipart File Is Empty");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String resultPath = String.valueOf(this.rootLocation.resolve(filename));
            System.out.println(resultPath);
            Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public Resource getFile(String filename) {
        Path filePath = Paths.get(rootLocation.toUri()).resolve(filename).normalize();
        try {
            Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
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
