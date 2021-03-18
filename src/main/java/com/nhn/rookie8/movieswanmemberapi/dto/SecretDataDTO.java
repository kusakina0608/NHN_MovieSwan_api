package com.nhn.rookie8.movieswanmemberapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
// TODO: 최소 Account, Ticket 은 분리해야 합니다. 이후 관심사에 따라 분리해 주면 더 좋습니다. origin, id url 등을 전부 관심사 별로 따로 분리
// TODO: TestCase를 만들어서 SKM 접근 테스트해보세요.
// 보안은 불편하게. SKM 장애가 난다면?? 직접 Application.propertie 에서 주입받아서 사용할 수도 있다(planB)
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


