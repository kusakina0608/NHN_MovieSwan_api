package com.nhn.rookie8.movieswanmemberapi.repository;

import com.nhn.rookie8.movieswanmemberapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
