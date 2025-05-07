package com.tpk.uploadfile.utils;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Component
public class UploadUtils {

    public MediaType getMediaType(Resource resource) {
        String filename = Objects.requireNonNull(resource.getFilename()).toLowerCase();
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (filename.contains(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (filename.contains(".jpg")) {
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

    public String encodeImageToBase64(Resource resource) throws IOException {
        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        String mimeType = getMediaType(resource).toString();
        return "data:" + mimeType + ";base64," + base64;
    }

}
