package com.restaurant.service;

import com.restaurant.config.CustomUserDetails;
import com.restaurant.dto.MemberFormDto;
import com.restaurant.entity.Member;
import com.restaurant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        /*return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();*/
        return new CustomUserDetails(member);
    }

    // 마이페이지-회원정보 수정
    @Transactional(readOnly = true)
    public MemberFormDto getMemberDtl(Long memberId){
        Member member = memberRepository.findById(memberId) //혹은 findByEmail
                .orElseThrow(EntityNotFoundException::new);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        return memberFormDto;
    }
   /* public Long updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) throws Exception{
        Member member = memberRepository.findById(memberFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        member.updateMember(memberFormDto, passwordEncoder);

        return member.getId();
        //return memberRepository.save(member);
    }*/

    public Member findMemberByPrincipal(Principal principal) {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        if(member == null) {
            throw new EntityNotFoundException("해당 회원을 찾을 수 없습니다.");
        }
        return member;
    }


    public Member updateMember(Member member){
       return memberRepository.save(member);
   }

    public void deleteMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            memberRepository.deleteByEmail(email);
        } else {
            throw new RuntimeException("해당 이메일로 등록된 계정이 존재하지 않습니다.");
        }
    }
}
