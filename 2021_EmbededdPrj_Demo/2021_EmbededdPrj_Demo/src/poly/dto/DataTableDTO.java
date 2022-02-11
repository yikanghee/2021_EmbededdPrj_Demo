package poly.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

/* @Auth 최별규
 * @Version 1.1
 * ?
 *  ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.07.26   |  최별규     | 초안 작성
 * |              |             |
 * */
// 캡슐화를 위해 모두 private로 데이터 접근은 get, set 메서드로 한다.
public class DataTableDTO {
	//-------------------------------------변수 선언 부--------------------------------------
	private List data;
	//---------------------------------Get, Set ----------------------------------------------
	public List getData() {
		if(CollectionUtils.isEmpty(data)) {
			data = new ArrayList();
		}
	return data;
	}

	public void setData(List data) {
		this.data = data;
	}
	
}