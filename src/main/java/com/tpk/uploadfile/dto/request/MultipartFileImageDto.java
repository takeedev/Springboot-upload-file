package com.tpk.uploadfile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MultipartFileImageDto {

    private MultipartFile imageMobile;
    private MultipartFile imageTablet;
    private MultipartFile imageDesktop;

}
