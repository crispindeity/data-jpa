package study.datajpa.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.entity.dto.MemberDto;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager entityManager;

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

    @Test
    void paging() {
        memberRepository.save(Member.of("member1", 10, null));
        memberRepository.save(Member.of("member2", 10, null));
        memberRepository.save(Member.of("member3", 10, null));
        memberRepository.save(Member.of("member4", 10, null));
        memberRepository.save(Member.of("member5", 10, null));
        memberRepository.save(Member.of("member6", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isZero();
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void slicing() {
        memberRepository.save(Member.of("member1", 10, null));
        memberRepository.save(Member.of("member2", 10, null));
        memberRepository.save(Member.of("member3", 10, null));
        memberRepository.save(Member.of("member4", 10, null));
        memberRepository.save(Member.of("member5", 10, null));
        memberRepository.save(Member.of("member6", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);

        List<Member> content = slice.getContent();

        assertThat(content).hasSize(3);
        assertThat(slice.getNumber()).isZero();
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();
    }

    @Test
    void bulkUpdateTest() {
        memberRepository.save(Member.of("member1", 10, null));
        memberRepository.save(Member.of("member2", 19, null));
        memberRepository.save(Member.of("member3", 20, null));
        memberRepository.save(Member.of("member4", 21, null));
        memberRepository.save(Member.of("member5", 40, null));
        memberRepository.save(Member.of("member6", 41, null));

        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> user = memberRepository.findByUsername("username5");
        Member member = user.get(0);
        System.out.println("member.getUsername() = " + member.getUsername());

        assertThat(resultCount).isEqualTo(4);
    }

    @Test
    void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = Member.of("member1", 10, teamA);
        Member member2 = Member.of("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHint() {
        Member member1 = memberRepository.save(Member.of("member1", 10, null));

        entityManager.flush();
        entityManager.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.updateUsername("member2");

        entityManager.flush();
    }

    @Test
    void lock() {
        Member member1 = memberRepository.save(Member.of("member1", 10, null));

        entityManager.flush();
        entityManager.clear();

        List<Member> member11 = memberRepository.findLockByUsername("member1");
    }
}
