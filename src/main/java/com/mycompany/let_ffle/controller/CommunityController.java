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

	// 게시물 목록 조회
	@GetMapping("/getBoardList")
	// 사용자가 pageNo를 제공하지 않으면 pageNo의 값을 기본값인 1로 지정
	public Map<String, Object> getBoardList(@RequestParam(defaultValue = "1") int pageNo) {
		// boardCount() 메소드를 호출해 게시판의 총 게시글 수를 가져오기
		// Pager 객체에게 전달하기 위해 totalRows 변수에 저장
		int totalRows = communityService.boardCount();

		// Pager 객체에 한 페이지에 보여질 게시물 수(5), 페이지네이션에 표시할 페이지 수(5), 총 게시물 수(totalRows), 현재
		// 페이지 번호(pageNo)를 제공
		Pager pager = new Pager(5, 5, totalRows, pageNo);

		// 사용자가 요청한 pageNo에 해당하는 게시물을 List로 가져오기
		List<Board> list = communityService.selectByBoardList(pager);

		// JSON 응답을 생성 -> board와 pager를 모두 제공해야 하므로 Map을 이용
		Map<String, Object> map = new HashMap<>();
		// 게시글 목록(list)을 응답에 추가
		map.put("board", list);
		// 페이지를 응답에 추가
		map.put("pager", pager);

		// JSON 응답 반환
		return map;
	}

	// 게시글 작성
	@PostMapping("/createBoard")
	public Board createBoard(Board board, Authentication authentication) {
		// 첨부파일 포함 여부를 확인
		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			// 첨부파일이 있는 경우
			// 첨부파일을 Multipartfile 객체에 저장
			MultipartFile mf = board.getBattach();
			// board 객체의 battachoname, battachtype 필드 값으로 각각 파일명과 확장자 타입을 저장
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			try {
				// board 객체의 battachdata 필드에 바이너리 데이터(실제 첨부 파일)를 저장
				board.setBattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// Authentication 객체를 이용해 요청을 보낸 사용자의 mid를 게시글 작성자로 세팅
		board.setMid(authentication.getName());
		// 게시글 조회수를 0으로 세팅
		board.setBhitcount(0);
		// 게시글 삭제여부를 기본값(true)로 설정
		board.setBenabled(true);

		// communityService에 처리를 위임
		communityService.insertBoard(board);

		// 응답 생성
		// null 처리가 필요한 필드

		// 저장한 Board 객체를 반환
		return board;
	}

	// 게시글 상세 보기
	@GetMapping("/readBoard")
	public Board readBoard(int bno) {
		Board board = communityService.readBoard(bno);
		return board;
	}

	// 게시글 수정
	@PutMapping("/updateBoard")
	public Board updateBoard(Board board) {

		// 첨부파일 포함 여부를 확인
		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			// 첨부파일이 있는 경우
			// 첨부파일을 Multipartfile 객체에 저장
			MultipartFile mf = board.getBattach();
			// board 객체의 battachoname, battachtype 필드 값으로 각각 파일명과 확장자 타입을 저장
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			try {
				// board 객체의 battachdata 필드에 바이너리 데이터(실제 첨부 파일)를 저장
				board.setBattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// communityService에 게시글 수정 처리를 위임
		communityService.updateBoard(board);

		// 수정된 Board 객체를 반환
		return board;
	}

	// 게시글 삭제
	@PutMapping("/deleteBoard")
	public void deleteBoard(int bno) {
		// communityService에게 해당 게시글의 benabled 값을 false로 변경 요청
		communityService.enabledBoard(bno);
	}

	// 게시물에 작성된 댓글들을 가져오기
	@GetMapping("/getCommentList")
	public Map<String, Object> getCommentList(@RequestParam(defaultValue = "1") int pageNo, int bno) {
		
		// 해당 게시물에 달린 댓글 갯수를 totalRows에 저장
		int totalRows = communityService.getCommentCount(bno);
		
		// Pager 객체에 한 페이지에 보여질 게시물 수(5), 페이지네이션에 표시할 페이지 수(5), 총 게시물 수(totalRows), 현재
		// 페이지 번호(pageNo)를 제공
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		
		// 사용자가 요청한 pageNo에 해당하는 댓글을 List로 가져오기
		List<BoardComment> list = communityService.getCommentList(pager, bno);
		
		// JSON 응답을 생성 -> comment와 pager를 모두 제공해야 하므로 Map을 이용
		Map<String, Object> map = new HashMap<>();
		// 댓글 목록(list)을 응답에 추가
		map.put("comment", list);
		// 페이저를 응답에 추가
		map.put("pager", pager);

		// map 반환
		return map;
	}

	// 댓글 작성
	@PostMapping("/createComment")
	public BoardComment createComment(BoardComment boardComment, Authentication authentication) {

		// 댓글 작성을 요청한 사용자의 mid를 댓글 작성자로 세팅
		boardComment.setMid(authentication.getName());
		
		// communityService에 요청 처리를 위임
		communityService.insertComment(boardComment);
		
		// 저장된 BoardComment 객체를 반환
		return boardComment;
	}

	// 댓글 삭제
	@PutMapping("/deleteComment")
	public void deleteComment(int cno) {
		// communityService에게 해당 댓글의 cenabled 값을 false로 변경 요청
		communityService.deleteComment(cno);
	}

}
