package com.nhn.rookie8.movieswanmemberapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecretAccountDataDTO {

    private Database database;
    private String secretKey;

    @Data
    public static class Database {
        private String password;
        private String username;
    }
}


