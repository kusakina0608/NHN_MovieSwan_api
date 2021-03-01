package com.nhn.rookie8.movieswanuserapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserBasicDTO {

    private String uid;

    private String password;

    private String name;

    private String email;

    private String url;

}
