package com.nhn.rookie8.movieswanmemberapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO {

    private Integer httpCode;

    private boolean error;

    private Integer errorCode;

    private String message;

    private Object content;
}
