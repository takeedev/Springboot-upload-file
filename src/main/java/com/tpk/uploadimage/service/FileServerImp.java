package com.tpk.uploadimage.service;

import org.springframework.beans.factory.annotation.Value;
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
    
}
