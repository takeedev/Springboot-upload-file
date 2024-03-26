package com.tpk.uploadfile.controller;

import com.tpk.uploadfile.service.StoredFileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/service")
public class UploadFileController {

    private final StoredFileService storedFileService;

    @PostMapping(value = "/upload/file")
    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        storedFileService.saveFileToFolder(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {

        if (filename.isEmpty()) {
            throw new IOException("Filename is Empty");
        }

        Resource resource = storedFileService.getFile(filename);

        MediaType mediaType = getMediaType(resource);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.setContentLength(resource.contentLength());
        headers.setContentType(mediaType);

        return new ResponseEntity<>(new InputStreamResource(resource.getInputStream())
                , headers
                , HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteFile(@RequestBody String filename) {
        boolean isDelete = storedFileService.deleteFile(filename);
        return ResponseEntity.status(HttpStatus.OK).body(isDelete);
    }

    private static MediaType getMediaType(Resource resource) {
        String filename = Objects.requireNonNull(resource.getFilename()).toLowerCase();
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (filename.contains(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (filename.contains(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (filename.contains(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        } else if (filename.contains(".json")) {
            mediaType = MediaType.APPLICATION_JSON;
        } else if (filename.contains(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }
        return mediaType;
    }

}
