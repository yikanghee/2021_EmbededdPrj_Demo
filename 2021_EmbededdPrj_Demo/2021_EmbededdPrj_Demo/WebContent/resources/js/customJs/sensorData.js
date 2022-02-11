// @Auth 최별규
// @Version 1.2
// 사용자 정의 js, 몽고에서 데이터를 가져와서 모달을 처리 하기 위한 동적처리를 정의해 놓은 js
// __________________________________________________________________________________________________________________________
// |   시기    |   작성일     |   작성자    |                                           내용                                 |
// |-------------------------------------------------------------------------------------------------------------------------
// | 최초 작성 | 2021.09.14   |  최별규     |  초안 작성
// | 중간 수정 | 2021.11.11   |  최별규     |  최종 완성(반드시!!프로미스나 async로 업데이트하기!!)
// --------------------------------------------------------------------------------------------------------------------------
// 동작 순서 => modal.jsp가 임포트 되고 파서에 의해 다운로드 될 때 실행 된다.
'use strict' // => 선언되지 않은 변수는 사용할 수 없다 ES6

   //--------------------------------------------전역 변수 영역(차 후 객체로 변환 예정)---------------------------------------

	const realTimeSetingFromMongo = 3000; // => 몽고에서 데이터 가져올 시간 설정(밀리세컨즈 1000 == 1초)
	//const limit = 280; // => 임계값 설정하는 변수
	
   //---------------------------------------------몽고 데이터 불러오기(메인 컨트롤러)------------------------------------------
	$(document).ready(()=>
	{	// parameter 정보 : p1 : 몽고에서 센서 정보 가져오기, p2 : fan 동작 컨트롤
	    realTimeGetSensorData(initSensorData, changeFan);
	})
	//-----------------------------재귀호출을 하여 정한 시간마다 센서 데이터 가져오는 함수-------------------------------------
	function realTimeGetSensorData(initSensorData, changeFan)
	{
		setTimeout(()=>{
			//console.log("실행!")
			initSensorData(changeFan);
			return realTimeGetSensorData(initSensorData, changeFan);
		}, realTimeSetingFromMongo);
	}
	//-------------------------------------------몽고에서 비동기로 Sensor데이터 가져오는 함수-----------------------------------
	function initSensorData(changeFan)
	{
		let resFan = 0;
	    $.ajax({
	        url : '/sensor/getAsyncSensorData.do', 
	        type : 'GET', 
	        success : function (data) { // 데이터 형식 : {"1" : data, "2" : data, "3" : data}
	        	//console.log("data : " + data);
	        	localStorage.clear(); //=> 메모리를 모두 비운 후 
	        	localStorage.setItem("sensorOBJ", JSON.stringify(data)); // => JS 단에서 데이터 통신을 위한 저장
	        	let limit = data["limitValue"]; // => 임계값 가져오기
	        	//console.log("limit : " + limit); // => 임계값 조회
	            resFan = initModal(data, limit); // => 모달을 띄우기 위한 함수 실행, fan 이미지컨트롤
	            changeFan(resFan); // => 1이면 동작 나머지는 멈춤
	        }
	    });
	    return resFan;
	}
    //-----------------------------------------JSON 파싱해서 모달 띄워주는 펑션-----------------------------------------------
    function initModal(data, limit)
    {
		let successNum = 0;
		let sensorArr = new Array;
		sensorArr.push(data["sensorNum1"]); // => 1번 센서
		sensorArr.push(data["sensorNum2"]); // => 2번 센서
		sensorArr.push(data["sensorNum3"]); // => 3번 센서
			    
		sensorArr.forEach(sensorData => {
			   if(sensorData <= limit){ // => 임계값 이하일시 모달을 띄움
			     	$('#myModal').show();
			   } else {
				//console.log( sensorData + " 번 이상없음, 가스 정상")
			}
	    });
	   if(sensorArr[0] <= limit || sensorArr[1] <= limit || sensorArr[3] <= limit){ //=> fan 이미지 동작 
	   		//console.log("data : " + sensorArr[0] + sensorArr[1] + sensorArr[2]);
			successNum = 1;
		} else successNum = 0;
		return successNum;
	}
    //---------------------------------------------------모달 팝업 Close 기능-----------------------------------------------
    function close_pop(flag) 
    {
        $('#myModal').hide();
    }  
	//-------------------------------------------------- fan 동작 컨트롤 ----------------------------------------------------
	function changeFan(num) 
	{ 
		console.log("Para : " + num);
    	if(num === 1) { // => 동작
    		$("#fanImg").attr("src", "/resources/img/spinning-fan.gif");
    	} else { // => 멈춤
    		$("#fanImg").attr("src", "/resources/img/stopped-fan.png");
    	}
    }