package com.restaurant.controller;

import com.restaurant.dto.MemberFormDto;
import com.restaurant.entity.Member;
import com.restaurant.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    //마이페이지-회원 정보 수정
    /*
    @GetMapping(value = "/mypage/{memberId}")
    public String memberDtl(@PathVariable("memberId") Long memberId, Model model){
        try{
            MemberFormDto memberFormDto = memberService.getMemberDtl(memberId);
            model.addAttribute("memberFormDto", memberFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            model.addAttribute("memberFormDto", new MemberFormDto());
            return "member/memberUpdateForm";
        }
        return "member/memberUpdateForm";
    }

    @PostMapping(value = "/mypage/{memberId}")
    public String memberUpdate(@Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult,Model model, PasswordEncoder passwordEncoder){
        if(bindingResult.hasErrors()){
            return "member/memberUpdateForm";
        }
        try{
            memberService.updateMember(memberFormDto, passwordEncoder);

        }catch (Exception e){
            model.addAttribute("errorMessage", "정보 수정 중 에러가 발생하였습니다.");
            return "member/memberUpdateForm";

        }
        return "redirect:/";

    }*/
    @GetMapping("/mypage")
    public String mypageForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/mypageForm";
    }
}
