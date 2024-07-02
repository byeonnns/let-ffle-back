package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		log.info("pageNo : " + pageNo);
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
		log.info("실행");
		log.info("board : " + board.toString());
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
		// JSON으로 변환되지 않는 필드는 NULL 처리함
		board.setBattach(null);
		board.setBattachdata(null);

		// 저장한 Board 객체를 반환
		return board;
	}

	// 게시글 상세 보기
	@GetMapping("/boardDetail/{bno}")
	public Board boardDetail(@PathVariable int bno) {
		Board board = communityService.readBoard(bno);

		// JSON으로 변환되지 않는 필드는 NULL 처리함 (Multipart, binary 데이터)
		board.setBattach(null);
		board.setBattachdata(null);

		return board;
	}

	// 게시글 첨부 다운로드용
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/battach/{bno}")
	public void battach(@PathVariable int bno, HttpServletResponse response) {

		// 해당 Board 객체 가져오기
		Board board = communityService.selectBoardByBno(bno);

		try {
			// 첨부파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위해 응답 헤더에 내용을 추가
			String fileName = new String(board.getBattachoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			// 파일 타입을 헤더에 추가
			response.setContentType(board.getBattachtype());

			// Response Body에 파일 데이터 출력
			OutputStream os = response.getOutputStream();
			os.write(board.getBattachdata());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	// 게시글 수정
	@PutMapping("/updateBoard")
	public Board updateBoard(Board board, Authentication authentication) {
		
		log.info("board : " + board.toString());
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
		
		board.setMid(authentication.getName());

		// communityService에 게시글 수정 처리를 위임
		communityService.updateBoard(board);

		// JSON으로 변환되지 않는 필드는 NULL 처리함 (Multipart, binary 데이터)
		board.setBattach(null);
		board.setBattachdata(null);

		// 수정된 Board 객체를 반환
		return board;
	}

	// 게시글 삭제
	@PutMapping("/deleteBoard/{bno}")
	public void deleteBoard(@PathVariable int bno) {
		log.info("실행");
		log.info("bno : " + bno);
		// communityService에게 해당 게시글의 benabled 값을 false로 변경 요청
		communityService.deleteBoard(bno);
	}

	// 게시물에 작성된 댓글들을 가져오기
	@GetMapping("/getCommentList/{bno}")
	public List<BoardComment> getCommentList(@PathVariable int bno) {

		// 사용자가 요청한 pageNo에 해당하는 댓글을 List로 가져오기
		List<BoardComment> list = communityService.getCommentList(bno);

		
		return list;
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
	@PutMapping("/deleteComment/{cno}")
	public void deleteComment(@PathVariable int cno) {
		// communityService에게 해당 댓글의 cenabled 값을 false로 변경 요청
		communityService.deleteComment(cno);
	}

}
