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

	// getBoardList 컨트롤러가 요청될 시 실행
	@GetMapping("/getBoardList")
	// 클라이언트(사용자)로부터 pageNo 요청받아 Map<String, Object> 형식의 데이터로 반환
	// pageNo 파라미터를 HTTP 요청에서 추출해 pageNo 변수에 할당 사용자가 pageNo를 제공하지 않으면 pageNo을 기본값 1로 지정 
	public Map<String, Object> getBoardList(@RequestParam(defaultValue = "1") int pageNo) {
		// communityService 메소드를 호출해 게시판의 총 게시물 수를 가져와 totalRows 변수에 반환
		int totalRows = communityService.BoardCount();

		// pager 객체를 생성 후 한 페이지에 보여질 게시물 수(5), 페이지네이션에 표시할 페이지 수(5), 총 게시물 수(totalRows), 현재 페이지 번호(pageNo)를 매개변수로 받음
		Pager pager = new Pager(5, 5, totalRows, pageNo);

		// communityService메소드를 호출해 pager 객체에 게시물 목록을 가져와 list 변수에 반환
		List<Board> list = communityService.SelectByBoardList(pager);
		// Map<String, Object> 객체를 생성 ( 게시판 목록과 페이지 정보를 담을 떄 사용 ) 
		Map<String, Object> map = new HashMap<>();
		// 게시판 목록(list)을 map에 추가 
		map.put("Board", list);
		// 페이지를 (page) map에 추가
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
	// 게시물에 작성된 댓글들을 가져오기
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
	//게시물에 댓글 작성하기
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
