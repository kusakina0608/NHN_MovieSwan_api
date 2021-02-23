package com.nhn.rookie8.movieswanuserapi.dto;

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

    private String message;

    private Object content;
}
