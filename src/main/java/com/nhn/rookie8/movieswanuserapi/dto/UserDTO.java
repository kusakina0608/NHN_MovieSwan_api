package com.nhn.rookie8.movieswanuserapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    private String uid;

    private String password;

    private String name;

    private String email;

    private String url;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
