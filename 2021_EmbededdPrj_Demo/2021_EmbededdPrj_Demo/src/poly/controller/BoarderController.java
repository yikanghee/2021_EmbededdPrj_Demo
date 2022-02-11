package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.BoardDTO;
import poly.service.IBoardService;
import poly.util.CmmUtil;

/* @Auth 최별규
 * @Version 1.1
 * 게시판 사용하진 않으나, 추가 기능을 위한 정의해 놓았음
 * ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.01.01   |  최별규     |  초안 작성
 * | 2021.09.13   |  최별규     |  주석 업데이트
 * */  

@Controller 
public class BoarderController {
	private Logger log = Logger.getLogger(this.getClass().getName());
	//----------------------------------리소스 선언부-------------------------------------
	@Resource(name = "BoardService")
	private IBoardService boardService;
	//-------------------------------------------------------------------------------------
	//-----------------------------------------게시판페이지 보여주는 메서드-----------------
	@RequestMapping(value = "board/boardIndex")
	public String boardIndesPage(ModelMap model) throws Exception {
		log.info(this.getClass().getName() + "boardIndex Start!!");
		List<BoardDTO> rList = new ArrayList<BoardDTO>();//List형태로 메모리 저장하는 메소드
		try {	//try , catch , finally로 예외문
			log.info("DB에서 데이터 불러오는 중");	//try 문이 돌았을떄 첫번쨰 메세지
			rList = boardService.getBoardList();	//rList에 boardService 의 getBoardList를 가져옴
			int cnt = 0;
			for(BoardDTO e : rList) {	//rList에 값이 들어온것을 e에 순서대로 넣어줌
				cnt ++;		//값이 들어온 것을 숫자로 표현
				log.info("###############" + cnt + "################");
				log.info("no : " + e.getNo());
				log.info("title : " + e.getTitle());
				log.info("chg_dt : " + e.getChg_dt());
				log.info("reg_id : " + e.getReg_id());
				log.info("reg_dt : " + e.getReg_dt());
				log.info("###########################################");
			}
		} catch (Exception e) {
			log.info(e.getStackTrace());
		} finally {	//무조건 실행
			log.info("컨트롤러로 보내기 성공");
			model.addAttribute("rList", rList);
		}
		log.info(this.getClass().getName() + "boardIndex End!!");
		return "/board/board";
	}
	//-----------------------------------------게시판 작성 페이지----------------------------------------
	@RequestMapping(value = "board/boardWrite")
	public String boardWritePage() throws Exception {
		log.info(this.getClass().getName() + "boardWrite Start!!");
		log.info(this.getClass().getName() + "boardWrite End!!");
		return "/board/boardWrite";
	}
	//------------------------------------게시판 등록 기능--------------------------------------------------
	@SuppressWarnings("null")
	@RequestMapping(value = "board/boardWriteProc")
	public String boardWriteProc(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {
		log.info(this.getClass().getName() + "boardWriteProc Start!!");
		//------------------------------------게시판 정보를 가져오는 변수 선언부----------------------------------
		String title = CmmUtil.nvl(request.getParameter("title"));
		String contents = CmmUtil.nvl(request.getParameter("contents"));
		String regId = CmmUtil.nvl((String) session.getAttribute("id")); // 로그인 할 때 등록된 세션값을 가져옴
		//---------------------------------------------------------------------------------------------------------
		log.info("title : " + title);
		log.info("contents : " + contents);
		log.info("user ID : " + regId);
		//--------------------------------------리다이렉트 시 사용할 변수-------------------------------------------
		String msg = "";
		String url = "";
		//----------------------------------------------------------------------------------------------------------
		int res = 0;
		BoardDTO pDTO = null;
		try {
			//------------------------------------DB에 저장하기 전 DTO에 모음------------------------------------------
			pDTO = new BoardDTO();
			pDTO.setTitle(title);
			pDTO.setContents(contents);
			pDTO.setReg_id(regId);
			//---------------------------------------------------------------------------------------------------------
			// DTO에 객체로 모아진 데이터를 DB에 저장 시키는 기능을 호출함(서비스)
			res = boardService.setBoardList(pDTO);
			log.info("res : " + res);
			//----------------------------------------------------------------------------------------------------------
			pDTO = null; // 객체 사용 종료 후 리소스 닫아줌
			if (res == 0) {
				log.info("서비스 확인 바람");
				msg = "게시판 작성 실패";
				url = "/board/boardWrite.do";
			} else {
				log.info("이상 무");
				msg = "게시판 등록을 왆료하였습니다.";
				url = "/board/boardIndex.do";
			}
		} catch (Exception e) {
			log.info(e.getStackTrace());
		} finally {
			//--------------------------------JSP 리다이렉트를 위해 Model 객체에 값을 담음----------------------------------
			model.addAttribute("msg", msg);
			model.addAttribute("url", url);
		}
		log.info(this.getClass().getName() + "boardWrite End!!");
		return "/user/redirect";
	}
	//-----------------------------------------------게시판 상세 보기--------------------------------------------------------
	@RequestMapping(value="board/boardDetail")
	public String updateBoard(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception{
		log.info(this.getClass().getName()+ ".boardDetil Start!");
		String no = CmmUtil.nvl(request.getParameter("no").toString());
		String id = CmmUtil.nvl(session.getAttribute("id").toString());
		log.info("게시판 번호 : " + no);
		
		BoardDTO pDTO = null;
		BoardDTO rDTO = new BoardDTO();
		
		try {
			pDTO = new BoardDTO();
			pDTO.setNo(no);
			rDTO = boardService.boardDetail(pDTO);
			pDTO = null;
		} catch (Exception e) {
			log.info(e.getStackTrace());
		} finally {
			model.addAttribute("rDTO", rDTO);
			session.setAttribute("id", id);
			log.info("JSP로 DTO 보내기 성공");
		}
		log.info(this.getClass().getName()+ ".boardDetil END!");
		return "/board/BoardDetail";
	}
	//-----------------------------------------게시판 삭제하기 ajax로 권한 확인 후 삭제 응답하기-------------------------------
	/*원래 컨트롤러의 리턴은 JSP 페이지 였으나 비동기 통신 방법인 ajax에서는 데이터가 리턴이 됨
	*/
	@RequestMapping(value="board/boardDelete", method = RequestMethod.GET)
	public @ResponseBody String boardDelete(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception{
		log.info(this.getClass().getName() + "boardDelete Start!!");
		String user_id = CmmUtil.nvl(session.getAttribute("id").toString()); // 로그인 한 사용자 
		String reg_id = CmmUtil.nvl(request.getParameter("reg_id").toString()); // 게시글에 있는 작성자 ajax로 받아옴
		String no = CmmUtil.nvl(request.getParameter("no").toString()); // 게시글에 있는 글 번호 ajax로 받아옴
		String msg = "";
		
		log.info("user_id : " + user_id);
		log.info("reg_id : " + reg_id);
		
		int res = boardService.boardDelete(user_id, no);
		log.info("res : " + res);
		
		if(res == 1) {
			log.info("삭제 성공");
			msg = "complete";
		} else {
			log.info("삭제 실패");
			msg = "noAuth";
		}
		log.info(this.getClass().getName() + "boardDelete End!!");
		return msg;
	}
	// 게시판 수정 하기 자바스크립트로 사용자 확인 후 넘어왔기 때문에 별도로 유효성 검사 하지 않겠음 -> 다시 해주는 게 좋음
	@RequestMapping(value="board/boardUpdate", method=RequestMethod.GET)
	public String boardUpdate(HttpServletRequest request, ModelMap model) throws Exception {
		log.info(this.getClass().getName() + "boardUpdate Start!!");
		String no = CmmUtil.nvl(request.getParameter("no").toString());
		String title = CmmUtil.nvl(request.getParameter("title").toString());
		String contents = CmmUtil.nvl(request.getParameter("contents").toString());
		// String user_id = CmmUtil.nvl(request.getParameter("sessionId").toString()); 디비에서 한 번 거 확인 할 때 사용
		log.info("no : " + no);
		log.info("title : " + title);
		log.info("contents : " + contents);
		String msg = "";
		String url = "";
		
		BoardDTO pDTO = new BoardDTO();
		pDTO.setNo(no);
		pDTO.setTitle(title);;
		pDTO.setContents(contents);;
		
		int res = boardService.boardUpdate(pDTO);
		log.info("controller res : " + res);
		
		pDTO = null;
		if(res == 1) {
			log.info("수정 성공");
			msg = "수정이 완료되었습니다.";
			url = "/board/boardIndex.do";
		} else {
			log.info("수정 실패");
			msg = "수정이 실패 하였습니다.";
			url = "/board/boardIndex.do";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		log.info(this.getClass().getName() + "boardUpdate End!!");
		return "user/redirect";
	}

}
