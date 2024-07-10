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

	// 게시물 목록 가져오기
	@GetMapping("/getBoardList")
	public Map<String, Object> getBoardList(@RequestParam(defaultValue = "1") int pageNo, 
			@RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String word) {
		int totalRows = 0;
		
		// 검색어가 있으면 해당 검색어가 포함된 게시물 갯수를 세고, 없으면 전체 게시물 갯수를 세기 
		if (!word.equals("")) {
			totalRows = communityService.getBoardCountByWord(searchType, word);
		} else {
			totalRows = communityService.boardCount();
		}
		
		Pager pager = new Pager(10, 5, totalRows, pageNo);

		List<Board> list = communityService.getBoardList(pager, searchType ,word);

		// JSON 응답 생성 -> board와 pager를 모두 제공해야 하므로 Map을 이용
		Map<String, Object> map = new HashMap<>();
		map.put("board", list);
		map.put("pager", pager);

		return map;
	}
	
	// 해당 카테고리의 게시물 목록만 가져오기
	@GetMapping("/getBoardListByCategory/{category}")
	public Map<String, Object> getBoardListByCategory(@RequestParam(defaultValue = "1") int pageNo, 
			@PathVariable String category) {
		int totalRows = communityService.getBoardCountByCategory(category);

		Pager pager = new Pager(10, 5, totalRows, pageNo);

		// 사용자가 요청한 pageNo에 해당하는 게시물을 List로 가져오기
		List<Board> list = communityService.getBoardListByCategory(pager, category);

		// JSON 응답을 생성 -> board와 pager를 모두 제공해야 하므로 Map을 이용
		Map<String, Object> map = new HashMap<>();
		map.put("board", list);
		map.put("pager", pager);
		
		return map;
	}

	// 게시글 작성
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/createBoard")
	public Board createBoard(Board board, Authentication authentication) {
		// 첨부파일 포함 여부를 확인
		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			MultipartFile mf = board.getBattach();
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			try {
				board.setBattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// Authentication 객체를 이용해 요청을 보낸 사용자의 mid를 게시글 작성자로 세팅
		board.setMid(authentication.getName());

		communityService.insertBoard(board);

		// JSON 응답 생성 : JSON으로 변환되지 않는 필드는 NULL 처리함
		board.setBattach(null);
		board.setBattachdata(null);

		return board;
	}

	// 게시글 읽기
	@GetMapping("/boardDetail/{bno}")
	public Board boardDetail(@PathVariable int bno) {
		Board board = communityService.readBoard(bno);

		// JSON으로 변환되지 않는 필드는 NULL 처리함
		board.setBattach(null);
		board.setBattachdata(null);

		return board;
	}

	// 게시글 첨부 다운로드
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/battach/{bno}")
	public void battach(@PathVariable int bno, HttpServletResponse response) {
		Board board = communityService.getBoardByBno(bno);

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
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/updateBoard")
	public Board updateBoard(Board board, Authentication authentication) {
		// 첨부파일 포함 여부를 확인
		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			MultipartFile mf = board.getBattach();
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			try {
				board.setBattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}
		
		board.setMid(authentication.getName());

		communityService.updateBoard(board);

		// JSON으로 변환되지 않는 필드는 NULL 처리함
		board.setBattach(null);
		board.setBattachdata(null);
		
		return board;
	}

	// 게시글 삭제
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/deleteBoard/{bno}")
	public void deleteBoard(@PathVariable int bno) {
		// 해당 게시글의 benabled 값을 false로 변경
		communityService.deleteBoard(bno);
	}

	// 게시물에 작성된 댓글들을 가져오기
	@GetMapping("/getCommentList/{bno}")
	public List<BoardComment> getCommentList(@PathVariable int bno) {
		// 사용자가 요청한 pageNo에 해당하는 댓글을 List로 가져오기
		return communityService.getCommentList(bno);
	}

	// 댓글 작성
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/createComment")
	public void createComment(BoardComment boardComment, Authentication authentication) {
		// 댓글 작성을 요청한 사용자의 mid를 댓글 작성자로 세팅
		boardComment.setMid(authentication.getName());

		communityService.insertComment(boardComment);
	}

	// 댓글 삭제
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/deleteComment/{cno}")
	public void deleteComment(@PathVariable int cno) {
		// communityService에게 해당 댓글의 cenabled 값을 false로 변경
		communityService.deleteComment(cno);
	}
}
