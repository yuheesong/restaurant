package com.restaurant.controller;

import com.restaurant.dto.MemberCheckDto;
import com.restaurant.dto.MemberFormDto;
import com.restaurant.dto.RestFormDto;
import com.restaurant.entity.Member;
import com.restaurant.entity.Rest;
import com.restaurant.entity.Star;
import com.restaurant.service.MemberService;
import com.restaurant.service.RestService;
import com.restaurant.service.StarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.List;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final RestService restService;
    private final PasswordEncoder passwordEncoder;

    private final StarService starService;

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
    public String loginMember(Model model, @CookieValue(value="rememberedEmail", required=false) String rememberedEmail){
        if (rememberedEmail != null) {
            model.addAttribute("rememberedEmail", rememberedEmail);
        }
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

    @GetMapping(value = "/mypage/check")
    public String mypageCheckForm(Model model){
        model.addAttribute("memberCheckDto", new MemberCheckDto());
        return "member/mypageCheckForm";
    }

    @PostMapping(value = "/mypage/check")
    public String mypageCheckForm(@Valid MemberCheckDto memberCheckDto,
                               BindingResult bindingResult, Model model, Principal principal){

        if(bindingResult.hasErrors()){
            return "member/mypageCheckForm";
        }
        if(!memberCheckDto.getPassword().equals(memberCheckDto.getPassword2())){
            bindingResult.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "member/mypageCheckForm";
        }

        try {
            // 현재 로그인한 사용자의 정보를 가져옴
            Member currentMember = memberService.findMemberByPrincipal(principal);

            // 사용자의 비밀번호와 입력받은 비밀번호가 일치하는지 확인
            if(!passwordEncoder.matches(memberCheckDto.getPassword(), currentMember.getPassword())) {
                model.addAttribute("passwordError", "비밀번호가 일치하지 않습니다.");
                return "member/mypageCheckForm";
            }

            // 일치하면 memberUpdateForm으로 이동
            return "redirect:/members/mypage/update/" + currentMember.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "해당 회원을 찾을 수 없습니다.");
            return "member/mypageCheckForm";
        }
    }
    @GetMapping(value = "/star")
    @ResponseBody
    public ResponseEntity<String>  star(@RequestParam("restId") Long restId){
        Rest rest = restService.getRestDtl(restId).createRest();
        Member member = restService.findMember();
        Star star = new Star();
        star.setMember(member);
        star.setRest(rest);
        int status = starService.star(star);
        return ResponseEntity.ok(Integer.toString(status));

    }

    @GetMapping(value = "/mypage/star")
    public String findStar(Model model , Pageable pageable){
        Member member = memberService.findMember();
        Page<Star> star = starService.findStar(member, pageable);
        model.addAttribute("star",star.getContent());
        model.addAttribute("page",star);

        return "member/mypageStar";
    }



}
