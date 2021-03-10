package com.nhn.rookie8.movieswanmemberapi.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberAuthDTO {

    @JsonProperty("id")
    @JsonAlias("memberId")
    private String memberId;

    private String password;

}
