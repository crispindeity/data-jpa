package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
