package com.tpk.uploadfile.controller;

import com.tpk.uploadfile.dto.ImageDto;
import com.tpk.uploadfile.dto.ResponseBody;
import com.tpk.uploadfile.dto.request.MultipartFileImageDto;
import com.tpk.uploadfile.dto.request.RequestDto;
import com.tpk.uploadfile.service.StoredFileService;
import com.tpk.uploadfile.utils.UploadUtils;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/service")
public class UploadFileController {

    private final StoredFileService storedFileService;

    private final UploadUtils uploadUtils;

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
        MediaType mediaType = uploadUtils.getMediaType(resource);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.setContentLength(resource.contentLength());
        headers.setContentType(mediaType);

        return new ResponseEntity<>(new InputStreamResource(resource.getInputStream())
                , headers
                , HttpStatus.OK);
    }

    @PostMapping(value = "/api/announcements", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseBody> createAnnouncement(@ModelAttribute RequestDto request) throws Exception {
        for (int i = 0; i < request.getImages().size(); i++) {
            MultipartFileImageDto imageSet = request.getImages().get(i);

            String mobilePath = uploadUtils.saveImage(imageSet.getImageMobile(), "mobile");
            String tabletPath = uploadUtils.saveImage(imageSet.getImageTablet(), "tablet");
            String desktopPath = uploadUtils.saveImage(imageSet.getImageDesktop(), "desktop");
            storedFileService.saveFileToFolder(imageSet.getImageMobile());
            storedFileService.saveFileToFolder(imageSet.getImageTablet());
            storedFileService.saveFileToFolder(imageSet.getImageDesktop());
        }
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<ResponseBody> getImage() throws IOException {
        Resource resource = storedFileService.getFile("sdf.png");
        String image1 = uploadUtils.encodeImageToBase64(resource);
        String image2 = uploadUtils.encodeImageToBase64(resource);
        List<ImageDto> listImage = new ArrayList<>();
        ImageDto imageDto = ImageDto.builder().imageA(image1).imageB(image2).build();
        ImageDto imageDto2 = ImageDto.builder().imageA(image1).imageB(image2).build();
        listImage.add(imageDto);
        listImage.add(imageDto2);
        ResponseBody responseBody = new ResponseBody("Test", listImage);
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteFile(@RequestBody String filename) {
        boolean isDelete = storedFileService.deleteFile(filename);
        return ResponseEntity.status(HttpStatus.OK).body(isDelete);
    }

}
