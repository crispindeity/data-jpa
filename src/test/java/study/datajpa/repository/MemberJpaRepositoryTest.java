package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    void memberTest() {
        Member member = Member.from("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(member.getId());

        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        Member member1 = Member.from("member1");
        Member member2 = Member.from("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertSoftly(test -> {
            assertThat(findMember1).isEqualTo(member1);
            assertThat(findMember2).isEqualTo(member2);

            List<Member> all = memberJpaRepository.findAll();
            assertThat(all).hasSize(2);

            long count = memberJpaRepository.count();
            assertThat(count).isEqualTo(2);

            memberJpaRepository.delete(member1);
            memberJpaRepository.delete(member2);
            long afterCount = memberJpaRepository.count();
            assertThat(afterCount).isZero();
        });
    }

    @Test
    void findByUsernameAndAgeGreaterThanTest() {
        Member member1 = Member.of("AAA", 10, null);
        Member member2 = Member.of("AAA", 20, null);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void testNamedQuery() {
        Member member1 = Member.from("AAA");

        memberJpaRepository.save(member1);

        List<Member> result = memberJpaRepository.findByUsername("AAA");

        assertThat(result.get(0).getUsername()).isEqualTo(member1.getUsername());

    }

    @Test
    void paging() {
        memberJpaRepository.save(Member.of("member1", 10, null));
        memberJpaRepository.save(Member.of("member2", 10, null));
        memberJpaRepository.save(Member.of("member3", 10, null));
        memberJpaRepository.save(Member.of("member4", 10, null));
        memberJpaRepository.save(Member.of("member5", 10, null));
        memberJpaRepository.save(Member.of("member6", 10, null));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(10);

        assertThat(members).hasSize(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void bulkUpdateTest() {
        memberJpaRepository.save(Member.of("member1", 10, null));
        memberJpaRepository.save(Member.of("member2", 19, null));
        memberJpaRepository.save(Member.of("member3", 20, null));
        memberJpaRepository.save(Member.of("member4", 21, null));
        memberJpaRepository.save(Member.of("member5", 40, null));
        memberJpaRepository.save(Member.of("member6", 41, null));

        int resultCount = memberJpaRepository.bulkAgePlus(20);

        entityManager.clear();

        assertThat(resultCount).isEqualTo(4);
    }
}
