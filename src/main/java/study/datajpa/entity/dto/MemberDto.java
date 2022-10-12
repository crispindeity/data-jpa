package study.datajpa.entity.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import study.datajpa.entity.Member;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName());
    }
}
