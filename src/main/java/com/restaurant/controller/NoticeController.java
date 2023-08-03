package com.restaurant.controller;


import com.restaurant.entity.Board;
import com.restaurant.entity.Comment;
import com.restaurant.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value ="/notice")
public class NoticeController {

    @Autowired
    private final BoardService boardService;



    //공지사항 전체조회
    @GetMapping("")
    public String NoticeList(Pageable pageable,Model model){
        Page<Board> boards = boardService.NoticeList(pageable);
        model.addAttribute("BoardList",boards.getContent());
        model.addAttribute("page",boards);
        return "notice/notice";
    }
    //공지사항 상세조회
    @GetMapping("/{p_id}")
    public String NoticeDetail(@PathVariable int p_id,Model model){
        Board board = boardService.NoticeDetail(p_id);
        System.out.println("상세조회"+"asd");
        model.addAttribute("board",board);
        return "notice/notice-detail";
    }
    //공지사항 작성페이지로 이동
    @GetMapping("/write")
    public String NoticeWrite(){
        return "notice/notice-write";
    }

    //공지사항작성
    @PostMapping("/write")
    public String NoticeWrite(@RequestParam("title") String title, @RequestParam("contents") String contents){
        System.out.println("title:"+title +"contents:"+contents);
        Board board = boardService.BoardWrite(title, contents,0);

        return "redirect:/notice/" + board.getP_id();
    }


    //공지사항검색
    @GetMapping("/search")
    public String NoticeSearch(@RequestParam(name = "search", required = false) String search,Pageable pageable,Model model){
        if (search==null||search.isEmpty()){
            NoticeList(pageable,model);
            return "notice/notice";
        }else {
            Page<Board> boards = boardService.NoticeSearch(search, pageable);
            model.addAttribute("BoardList",boards.getContent());
            model.addAttribute("page", boards);
            return "notice/notice";
        }

    }
    //공지사항수정페이지로 이동
    @GetMapping("/update/{p_id}")
    public String  NoticeUpdate(@PathVariable("p_id") int p_id, Model model){
        Board board1 = boardService.BoardDetail(p_id);
        model.addAttribute("board",board1);
        return "notice/notice-update";
    }

    //공지사항수정
    @PostMapping("/update/{p_id}")
    public String  NoticeUpdate1(@PathVariable("p_id") int p_id, @ModelAttribute Board board){
        boardService.BoardUpdate(board);
        return "redirect:/notice/" + board.getP_id();
    }

    //공지사항삭제
    @GetMapping("/delete/{p_id}")
    public String NoticeDelete(@PathVariable("p_id") int p_id){
        boardService.BoardDelete(p_id);
        return "redirect:/notice";
    }
}

