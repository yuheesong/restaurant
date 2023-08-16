package com.restaurant.controller;

import com.restaurant.dto.MemberFormDto;
import com.restaurant.entity.Member;
import com.restaurant.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

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

        if(!memberFormDto.getPassword().equals(memberFormDto.getPassword2())){
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
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
    @GetMapping(value = "/mypage")
    public String memberDtl(@PathVariable("memberId") Long memberId, Model model){
        try{
            MemberFormDto memberFormDto = memberService.getMemberDtl(memberId);
            model.addAttribute("memberFormDto", memberFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            model.addAttribute("memberFormDto", new MemberFormDto());
            return "member/mypageForm";
        }
        return "member/mypageForm";
    }

    @PostMapping(value = "/mypage")
    public String memberUpdate(@Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult,Model model, PasswordEncoder passwordEncoder){
        if(bindingResult.hasErrors()){
            return "member/mypageForm";
        }
        try{
            memberService.updateMember(memberFormDto, passwordEncoder);

        }catch (Exception e){
            model.addAttribute("errorMessage", "정보 수정 중 에러가 발생하였습니다.");
            return "member/mypageForm";

        }
        return "redirect:/";

    }*/
    @GetMapping("/mypage")
    public String mypageForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/mypageForm";
    }

   /* @GetMapping(value = "/mypage/update")
    public String memberDtl(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberUpdateForm";
    }*/

    @GetMapping(value = "/mypage/update/{memberId}")
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

    @PostMapping(value = "/mypage/update")
    public String memberUpdate(@Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult, Model model, Principal principal){

        if(bindingResult.hasErrors()){
            return "member/memberUpdateForm";
        }
        if(!memberFormDto.getPassword().equals(memberFormDto.getPassword2())){
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/memberUpdateForm";
        }

        try {
            // 현재 로그인한 사용자의 정보를 가져옴
            Member currentMember = memberService.findMemberByPrincipal(principal);

            // 가져온 멤버의 정보를 업데이트
            currentMember.updateMember(memberFormDto, passwordEncoder);

            memberService.updateMember(currentMember);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "해당 회원을 찾을 수 없습니다.");
            return "member/memberUpdateForm";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberUpdateForm";
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{email}")
    public String deleteMember(@PathVariable String email, HttpServletRequest request, HttpServletResponse response) {
        try {
            memberService.deleteMember(email);
            // Spring Security 로그아웃
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/?deleteSuccess=true";  // 메인 페이지로 리다이렉트 with parameter
        } catch (RuntimeException e) {
            return "errorPage";
        }
    }



}
