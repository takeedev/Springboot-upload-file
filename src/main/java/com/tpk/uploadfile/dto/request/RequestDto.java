package com.tpk.uploadfile.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestDto{

    private String text;
    private List<MultipartFileImageDto> images;

}
