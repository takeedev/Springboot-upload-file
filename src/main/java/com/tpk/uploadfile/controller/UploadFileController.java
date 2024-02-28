package com.tpk.uploadfile.controller;

import com.tpk.uploadfile.service.StoredFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/service")
public class UploadFileController {

    private final StoredFileService storedFileService;

    @PostMapping(value = "/upload/file")
    public ResponseEntity fileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        storedFileService.saveFileToFolder(file);
        return ResponseEntity.ok().build();
    }

}
