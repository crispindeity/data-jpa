package study.datajpa.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.entity.dto.MemberDto;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        return memberRepository.findById(id).orElseThrow().getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public List<MemberDto> list(@PageableDefault(size = 25, sort = "age", direction = Sort.Direction.DESC) Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(1 - 1, 25, Sort.by("age").descending());
        return memberRepository.findPageMember(pageable).stream().map(MemberDto::from).collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        Team team = teamRepository.save(new Team("one team"));
        for (int i = 0; i < 100; i++) {
            memberRepository.save(Member.of("user" + i, i, team));
        }

    }
}
