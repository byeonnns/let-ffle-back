package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.BoardComment;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.service.CommunityService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/community")
public class CommunityController {

	@Autowired
	private CommunityService communityService;

	@GetMapping("/getBoardList")
	public Map<String, Object> getBoardList(@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = communityService.BoardCount();
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Board> list = communityService.SelectByBoardList(pager);
		Map<String, Object> map = new HashMap<>();
		map.put("Board", list);
		map.put("Pager", pager);
		return map;
	}

	@PostMapping("/createBoard")
	public Board createBoard(Board board, Authentication authentication) {
		// 첨부파일이 포함 여부 확인
		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			// 포함이 된 경우 첨부파일을 multipartfile 객체에 저장
			MultipartFile mf = board.getBattach();
			// board 객체의 oname, type 필드값으로 각각 확장명과 확장자명 저장
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			try {
				board.setBattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}
		board.setMid(authentication.getName());
		board.setBhitcount(0);
		board.setBenabled(true);
		communityService.insertBoard(board);
		// 응답 생성
		// null 처리가 필요한 필드
		return board;
	}

	@GetMapping("/readBoard")
	public Board readBoard(Board board) {
		board = communityService.readBoard(board.getBno());
		return board;
	}

	@PutMapping("/updateBoard")
	public Board updateBoard(Board board, Authentication authentication) {

		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			MultipartFile mf = board.getBattach();
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			try {
				board.setBattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		communityService.updateBoard(board);
		return board;
	}

	@PutMapping("/deleteBoard")
	public Board deleteBoard(Board board) {
		// 게시글 완전 삭제가 아닌 benabled의 값을 바꿔주기
		communityService.enabledBoard(board.getBno());
		return null;
	}

	@GetMapping("/getCommentList")
	public Map<String, Object> getCommentList(@RequestParam(defaultValue = "1") int pageNo, int bno) {
		int totalRows = communityService.getCommentCount();
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<BoardComment> list = communityService.getCommentList(pager, bno);
		Map<String, Object> map = new HashMap<>();
		map.put("comment", list);
		map.put("pager", pager);

		return map;
	}

	@PostMapping("/createComment")
	public BoardComment createComment(BoardComment boardComment, Authentication authentication) {
		
		boardComment.setMid(authentication.getName());
		communityService.insertComment(boardComment);
		return boardComment;
	}

	@PutMapping("/deleteComment")
	public void deleteComment(int cno) {
		// 댓글 완전 삭제가 아닌 cenabled의 값을 바꿔주기
		communityService.deleteComment(cno);
	}

}
