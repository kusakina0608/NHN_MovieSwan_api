package com.nhn.rookie8.movieswanuserapi.lib;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonResponse {

    private Integer httpCode;

    private boolean error;

    private String message;

    private Object content;
}
