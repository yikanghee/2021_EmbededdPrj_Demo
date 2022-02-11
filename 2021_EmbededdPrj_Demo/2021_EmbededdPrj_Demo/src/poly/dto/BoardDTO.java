package poly.dto;

/* @Auth 최별규
 * @Version 1.1
 * 게시판 데이터를 담고 데이터베이스에 저장하기 위한 객체 DTO
 *  ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.07.26   |  최별규     | 초안 작성
 * |              |             |
 * */
// 캡슐화를 위해 모두 private로 데이터 접근은 get, set 메서드로 한다.
public class BoardDTO {
	//-------------------------------------변수 선언 부--------------------------------------
	private String no;	     // => 게시판 번호
	private String title;    // => 게시판 제목
	private String contents; // => 게시판 내용
	private String reg_id;   // => 게시판 등록자 아이디
	private String reg_dt;   // => 게시판 등록일
	private String chg_dt;   // => 게시판 수정일
	//---------------------------------Get, Set ----------------------------------------------
	public String getNo() {	
		return no;
	}
	public void setNo(String no) {
		this.no = no;	
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getChg_dt() {
		return chg_dt;
	}
	public void setChg_dt(String chg_dt) {
		this.chg_dt = chg_dt;
	}
	

}
