package com.tpk.uploadfile.utils;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.util.Objects;

public class UploadUtils {

    public MediaType getMediaType(Resource resource) {
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
