package com.restaurant.service;



import com.restaurant.constant.Role;
import com.restaurant.entity.Board;
import com.restaurant.entity.Comment;
import com.restaurant.entity.Member;
import com.restaurant.repository.BoardRepository;
import com.restaurant.repository.BoardRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BoardService {

    @Autowired
    BoardRepositoryImpl boardRepository;
    @Autowired
    BoardRepository br;

    Date now =new Date();


    //전체조회
    public Page<Board> BoardList(Pageable pageable){
        Page<Board> list = boardRepository.BoardList(pageable);
        return list;
    }

    //상세페이지
    public Board BoardDetail(int p_id){
        Board board = boardRepository.BoardDetail(p_id);
        return board;
    }
    //게시물작성
    public Board BoardWrite(String title,String contents){
        int status=0;
        Board board = new Board();
        Member member = new Member();
        member.setId(1L);
        board.setM_id(member);
        board.setTitle(title);
        board.setContents(contents);
        board.setRole(Role.USER);
        board.setCreate_date(now);
        Board board2 = br.save(board);

        return board2;
    }

    //게시물수정
    public int BoardUpdate(Board board){
        int i = boardRepository.BoardUpdate(board);
        return i;
    }

    //게시물삭제
    public int BoardDelete(@PathVariable int p_id){
        int i = boardRepository.BoardDelete(p_id);
        return i;

    }

    //댓글조회
    public List<Comment> CommentList(Board board){
        List<Comment> comments = boardRepository.CommentList(board);
        return comments;
    }
    //댓글작성
    public int CommentWrite(String comment, int p_id){
        Comment com =new Comment();
        Board board = boardRepository.BoardDetail(p_id);
        com.setComment(comment);
        com.setP_id(board);
        com.setCreate_date(now);
        int i = boardRepository.CommentWrite(com);

        return i;
    }
    //댓글삭제
    public int CommentDelete(int c_id){
        int i = boardRepository.CommentDelete(c_id);
        return i;
    }
    //좋아요

    /*
    //공지사항 전체조회
    public Page<Board>NoticeList(Pageable pageable){
        Page<Board> NoticeList = boardRepository.NoticeList(pageable);
        return NoticeList;
    }
    //공지사랑 상세조회
    public Board NoticeDetail(@PathVariable int p_id){
        Board noticeDetail = boardRepository.NoticeDetail(p_id);
        return noticeDetail;
    }

     */

    //검색
    public Page<Board> search(String searchKeyword,Pageable pageable){
        if (searchKeyword==null){
            boardRepository.BoardList(pageable);
        }
        Page<Board> search = boardRepository.search(searchKeyword,pageable);
        return search;
    }
}
