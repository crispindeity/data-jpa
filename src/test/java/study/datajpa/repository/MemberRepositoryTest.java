package study.datajpa.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.entity.dto.MemberDto;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

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

    @Test
    void testQuery() {
        Member member1 = Member.of("AAA", 10, null);

        memberRepository.save(member1);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    void findUsernameList() {
        Member aaa = Member.from("AAA");
        Member bbb = Member.from("BBB");

        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        Team teamA = new Team("TeamA");
        teamRepository.save(teamA);

        Member memberA = Member.of("AAA", 10, teamA);
        memberRepository.save(memberA);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames() {
        Member aaa = Member.from("AAA");
        Member bbb = Member.from("BBB");

        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<Member> users = memberRepository.findByNames(List.of("AAA", "BBB"));

        for (Member user : users) {
            System.out.println("user = " + user);
        }
    }

    @Test
    void returnTypeTest() {
        Member aaa = Member.from("AAA");
        Member bbb = Member.from("BBB");

        memberRepository.save(aaa);
        memberRepository.save(bbb);

        Member findMember = memberRepository.findMemberByUsername("AAA");
        System.out.println("findMember = " + findMember);
    }
}
