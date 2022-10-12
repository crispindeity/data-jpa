package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import study.datajpa.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = Member.of("member1", 10, teamA);
        Member member2 = Member.of("member2", 11, teamA);
        Member member3 = Member.of("member3", 20, teamB);
        Member member4 = Member.of("member4", 22, teamB);

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        entityManager.flush();
        entityManager.clear();

        List<Member> members = entityManager.createQuery(
                "select m from Member m", Member.class
        ).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void jpaEventEntity() throws InterruptedException {
        Member memberA = Member.from("teamA");
        memberRepository.save(memberA);

        Thread.sleep(100);
        memberA.updateUsername("member2");

        entityManager.flush();
        entityManager.clear();

        Member findMember = memberRepository.findById(memberA.getId()).get();

        System.out.println("findMember = " + findMember);
    }
}
