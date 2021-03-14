package com.nhn.rookie8.movieswanmemberapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecretKeyManagerDTO {

    private Body body;
    private Header header;

    @Data
    public static class Body {
        private String secret;
    }

    @Data
    public static class Header {
        private boolean isSuccessful;
        private String resultMessage;
        private int resultCode;
    }
}