package com.nhn.rookie8.movieswanmemberapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDTO {

    private String memberId;

    private String password;

    private String name;

    private String email;

    private String url;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
