package study.datajpa.entity.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

}
