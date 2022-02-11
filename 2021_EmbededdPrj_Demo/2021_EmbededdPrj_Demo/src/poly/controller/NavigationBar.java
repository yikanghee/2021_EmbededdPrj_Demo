package poly.controller;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.dto.MongoDTO;
import poly.mongo.IMongoMapper;

/* @Auth 최별규
 * @Version 1.1
 * ?
 *  ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.99.00   |      | 
 * 
 * */

@Controller
public class NavigationBar {
	private Logger log = Logger.getLogger(this.getClass().getName());
	//--------------------------------------------------------------리소스선언부--------------------------------------------------------	
	@Resource(name="MongoMapper")
	private IMongoMapper mongoMapper;	
	//----------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------차트매핑-----------------------------------------------------------
	@RequestMapping(value="chart/present")
	public String Mongo(HttpServletRequest request, ModelMap model) throws Exception { 
		log.info(this.getClass().getName() + "Mongo Start!");
		List<MongoDTO> rList = mongoMapper.getCollectionsData("schemas");
		Iterator<MongoDTO> it = rList.iterator();
		int num = 0;
		while (it.hasNext()) { 
			MongoDTO dto = it.next();
			if (num == 0) {
				model.addAttribute("Date", dto.getDate());
				model.addAttribute("SENSOR_NUM", dto.getSENSOR_NUMBER());
				model.addAttribute("SENSOR_DATA", dto.getSENSOR_DATA());
			}
			log.info(num + " 번째 Date : " + dto.getDate()); 
			log.info(num + " 번째 SENSOR_NUM : " + dto.getSENSOR_NUMBER()); 
			log.info(num + " 번째 SENSOR_DATA : " + dto.getSENSOR_DATA());
			
			num++;
		}
		log.info(this.getClass().getName() + ".Mongo End!");
		return "/chart/present";
	}
	//--------------------------------------------------------------------------
	@RequestMapping(value="chart/weeks")
	public String Weeks() throws Exception{
		log.info(Logger.getLogger(this.getClass().getName()));
		return "/chart/weeks";
	}
	//--------------------------------------------------------------------------	
	@RequestMapping(value="chart/month")
	public String Month() throws Exception{
		log.info(Logger.getLogger(this.getClass().getName()));
		return "/chart/month";
	}
	//--------------------------------------------------------------------------
	@RequestMapping(value="main/mypage")
	public String mypage() throws Exception{
		log.info(Logger.getLogger(this.getClass().getName()));
		return "/main/mypage";
	}

}
