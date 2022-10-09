package study.datajpa.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    void testMember() {
        Member member = Member.from("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        Member member1 = Member.from("member1");
        Member member2 = Member.from("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertSoftly(test -> {
            assertThat(findMember1).isEqualTo(member1);
            assertThat(findMember2).isEqualTo(member2);

            List<Member> all = memberRepository.findAll();
            assertThat(all).hasSize(2);

            long count = memberRepository.count();
            assertThat(count).isEqualTo(2);

            memberRepository.delete(member1);
            memberRepository.delete(member2);
            long afterCount = memberRepository.count();
            assertThat(afterCount).isZero();
        });
    }

    @Test
    void findByUsernameAndAgeGreaterThanTest() {
        Member member1 = Member.of("AAA", 10, null);
        Member member2 = Member.of("AAA", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void testNamedQuery() {
        Member member1 = Member.from("AAA");

        memberRepository.save(member1);

        List<Member> result = memberRepository.findByUsername("AAA");

        assertThat(result.get(0).getUsername()).isEqualTo(member1.getUsername());
    }
}
