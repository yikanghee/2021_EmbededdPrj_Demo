package poly.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.mongo.IMongoMapper;

/* @Auth 최별규
 * @Version 1.1
 * ?
 * ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.00.00   |  홍길동     |  초안 작성
 * |              |             |
 * */

@Controller
public class MongoTableController {
	private Logger log = Logger.getLogger(this.getClass().getName());
	//---------------------------------------------------------------Resource 선언부--------------------------------------------
	@Resource(name="MongoMapper")
	private IMongoMapper mongoMapper;	
	//--------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------
	@RequestMapping(value="table/present")
	public String TablePresent() throws Exception{
		log.info(Logger.getLogger(this.getClass().getName()));
		return "/table/present";
	}
	//-----------------------------------------------------------------
	@RequestMapping(value="table/weeks")
	public String TableWeeks() throws Exception{
		log.info(Logger.getLogger(this.getClass().getName()));
		return "/table/weeks";
	}
	//-----------------------------------------------------------------
	@RequestMapping(value="table/month")
	public String TableMonth() throws Exception{
		log.info(Logger.getLogger(this.getClass().getName()));
		return "/table/month";
	}
}
