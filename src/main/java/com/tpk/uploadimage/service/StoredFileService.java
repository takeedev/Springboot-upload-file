package com.tpk.uploadimage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StoredFileService {

    void saveFileToFolder(MultipartFile multipartFile) throws Exception;

}
