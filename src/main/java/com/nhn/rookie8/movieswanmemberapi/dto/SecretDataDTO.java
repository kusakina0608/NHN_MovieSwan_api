package com.nhn.rookie8.movieswanmemberapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecretDataDTO {

    private Ticket ticket;
    private Account account;

    @Data
    public static class Ticket {
        private String secretKey;
        private ObjectStorage objectStorage;
        private Redis redis;
        private Database database;

        @Data
        public static class Database {
            private String password;
            private String username;
        }
    }

    @Data
    public static class ObjectStorage {
        private String password;
        private String username;
        private String tenantId;
    }

    @Data
    public static class Redis {
        private String password;
    }

    @Data
    public static class Account {

        private String secretKey;
        private Database database;

        @Data
        public static class Database {
            private String password;
            private String username;
        }
    }
}


