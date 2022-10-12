package study.datajpa.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;
import study.datajpa.repository.custom.MemberRepositoryCustom;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<Member> findMemberCustom() {
        return entityManager.createQuery(
                "select m from Member m", Member.class
                ).getResultList();
    }
}
