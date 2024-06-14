package com.mycompany.let_ffle.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Comment;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/board")
public class BoardController {

	@GetMapping("/getBoardList")
	public Map<String, Object> getBoardList() {
		
		// 페이저 필요
		
		// 페이저에 게시글 객체 담기
		
		return null;
	}
	
	@PostMapping("createBoard")
	public Board createBoard() {
		
		return null;
	}
	
	@GetMapping("readBoard")
	public Board readBoard() {
		
		return null;
	}
	
	@PutMapping("updateBoard")
	public Board updateBoard() {
		
		return null;
	}
	
	@PutMapping("deleteBoard")
	public Board deleteBoard() {
		// 게시글 완전 삭제가 아닌 benabled의 값을 바꿔주기
	
		return null;
	}
	
	@GetMapping("getCommentList")
	public Comment getCommentList() {
		
		return null;
	}

	@PostMapping("createComment")
	public Comment createComment() {
		
		return null;
	}
	
	@PutMapping("deleteComment")
	public Comment deleteComment() {
		// 댓글 완전 삭제가 아닌 benabled의 값을 바꿔주기
		
		return null;
	}

}
