package com.nhn.rookie8.movieswanuserapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserIdNameDTO {

    private String uid;

    private String name;
}
