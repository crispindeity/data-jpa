package study.datajpa.repository.custom;


import java.util.List;

import study.datajpa.entity.Member;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
