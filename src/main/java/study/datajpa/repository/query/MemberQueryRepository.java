package study.datajpa.repository.query;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager entityManager;

    public List<Member> findAllMembers() {
        return entityManager.createQuery(
                "select m from Member m", Member.class
        ).getResultList();
    }
}
