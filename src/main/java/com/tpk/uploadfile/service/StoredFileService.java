package com.tpk.uploadfile.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StoredFileService {

    void saveFileToFolder(MultipartFile multipartFile) throws Exception;

    Resource getFile(String filename);

    boolean deleteFile(String fileName);
}
