package com.restaurant.repository;



import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.restaurant.constant.Role;
import com.restaurant.entity.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;


@Repository
@Transactional
public class BoardRepositoryImpl {

    JPAQueryFactory query;

    public BoardRepositoryImpl(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }
    int pageNumber = 1; // 가져올 페이지 번호
    int pageSize = 10; // 한 페이지에 보여줄 레코드 개수
    Date now =new Date();

    private final QBoard qboard = QBoard.board;
    private final QComment qComment = new QComment("comment"); // 또는 QComment qComment = new QComment("comment");
    private final QMember qMember = new QMember("member");

    //전체조회
    public Page<Board> BoardList(Pageable pageable){
        QueryResults<Board> list = query.selectFrom(qboard).where(qboard.delete_date.isNull()).where(qboard.role.eq(Role.USER))
                .orderBy(qboard.create_date.asc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(list.getResults(),pageable,list.getTotal());
    }

    //상세페이지
    public Board BoardDetail(int p_id){
        Board board = query.selectFrom(qboard).where(qboard.p_id.eq(p_id)).fetchOne();
        return board;
    }
    //작성페이지
    public int BoardWrite(Board board){

        long i = query.insert(qboard)
                .set(qboard.m_id,board.getM_id())
                .set(qboard.title,board.getTitle())
                .set(qboard.contents,board.getContents())
                .set(qboard.role,Role.USER)
                .set(qboard.create_date,board.getCreate_date())
                .execute();

        return (int)i;
    }
    //게시물수정
    public int BoardUpdate(Board board){
        long i = query.update(qboard).where(qboard.p_id.eq(board.getP_id()))
                .set(qboard.title, board.getTitle())
                .set(qboard.contents, board.getContents())
                .set(qboard.modify_date,now)
                .execute();

        return (int)i;
    }
    //게시물삭제
    public int BoardDelete(int p_id){
        long i = query.update(qboard)
                .set(qboard.delete_date, now)
                .where(qboard.p_id.eq(p_id))
                .execute();
        return (int)i;
    }
    //댓글조회
    public List<Comment> CommentList(Board board){
        List<Comment> list = query.selectFrom(qComment).where(qComment.p_id.eq(board)).where(qComment.delete_date.isNull())
                .orderBy(qComment.create_date.asc()).fetch();
        return list;
    }
    //댓글작성
    public int CommentWrite(Comment comment){
        long i = query.insert(qComment)
                .set(qComment.comment,comment.getComment())
                .execute();

        return (int)i;
    }
    //댓글삭제
    public int CommentDelete(int c_id){
        long i = query.update(qComment)
                .set(qComment.delete_date, now)
                .where(qComment.c_id.eq(c_id))
                .execute();
        return (int)i;
    }
    //좋아요
    //공지사항 전체조회
    public Page<Board> NoticeList(Pageable pageable){
        QueryResults<Board> list = query.selectFrom(qboard).join(qMember).where(qboard.role.eq(Role.ADMIN)).where(qboard.delete_date.isNull())
                .orderBy(qboard.create_date.asc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(list.getResults(),pageable,list.getTotal());
    }
    //공지사랑 상세조회
    public Board NoticeDetail(int p_id){
        Board board = query.selectFrom(qboard).where(qboard.p_id.eq(p_id)).fetchOne();
        return board;
    }



    //자유게시판검색
    public Page<Board> search(String searchKeyword,Pageable pageable){

        QueryResults<Board> list = query.selectFrom(qboard).where(qboard.title.contains(searchKeyword)).where(qboard.delete_date.isNull())
                .where(qboard.role.eq(Role.USER))//조건문추가해야댐
                .orderBy(qboard.create_date.asc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(list.getResults(),pageable,list.getTotal());
    }
    //공지사항검색
    public Page<Board> NoticeSearch(String searchKeyword,Pageable pageable){

        QueryResults<Board> list = query.selectFrom(qboard).where(qboard.title.contains(searchKeyword)).where(qboard.delete_date.isNull())
                .where(qboard.role.eq(Role.ADMIN))//조건문추가해야댐
                .orderBy(qboard.create_date.asc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(list.getResults(),pageable,list.getTotal());
    }

    //조회수 증가
    public void View(int p_id,int view){
        query.update(qboard)
                .where(qboard.p_id.eq(p_id))
                .set(qboard.view,view)
                .execute();
    }
}
