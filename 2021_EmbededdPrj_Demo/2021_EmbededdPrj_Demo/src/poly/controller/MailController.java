package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.dto.MailDTO;
import poly.service.IMailService;
import poly.util.CmmUtil;

/* @Auth 최별규
 * @Version 1.1
 * 메일 발송 테스트를 위한 Controller
 *  ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.01.11   |  최별규     | 초안 작성
 * |              |             |
 * */

@Controller
public class MailController {
	private Logger log = Logger.getLogger(this.getClass().getName());
	//-------------------------------------------------리소스 선언부-----------------------------------------------------
	@Resource(name="MailService") 
	IMailService mailService;
	//--------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------메일 페이지 반환-------------------------------------------------
	@RequestMapping(value="mail/mailPage") 
	public String mailPage() throws Exception{
		log.info(this.getClass().getName() + "mailPage Start!!");
		log.info(this.getClass().getName() + "mailPage end!!");
		return "/mail/mailPage";
	}
	//--------------------------------------------------------메일 발송--------------------------------------------------
	@RequestMapping(value="mail/sendMailProc")
	public String sendMailProc(HttpServletRequest request, ModelMap model) {
		log.info(this.getClass().getName() + "send Mail Proc Start!!");
		//웹 url로부터 전달받는 값
		String title = CmmUtil.nvl(request.getParameter("title"));
		String email = CmmUtil.nvl(request.getParameter("email"));
		String contents = CmmUtil.nvl(request.getParameter("contents"));
		String msg = "";
		String url = "";
		//로그 찍어보기
		log.info("title : " + title);
		log.info("email : " + email);
		log.info("contents : " + contents);
		
		MailDTO pDTO = new MailDTO();//메일 발송을 하기 위한 정보를 담기 위해 DTO 객체 생성
		pDTO.setTitle(title);//title 저장
		pDTO.setToMail(email);//email 저장
		pDTO.setContents(contents);//contents저장
		
		int res = mailService.doSendMail(pDTO);//메일 발송하기
		pDTO = null;
		
		if(res == 1) {
			msg = "성공";
			url = "/main/index.do";
		} else {
			msg = "실패";
			url = "/mail/mailPage.do";
		}
		//메일 발송 결과를 jsp에 전달
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		log.info(this.getClass().getName() + "send Mail Proc end!!");
		return "/user/redirect";
	}
	
}
