//-----------------------------------헤더 파일 모음---------------------------------------
#include <Servo.h> 
#include <SPI.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
//-------------------------------------------------------------------------------------
//-----------------------------------서브모터 셋팅---------------------------------------
/*  *servo 변수 선언
    *motor을 입출력 7번 핀에 연결
    *초기 각도값 설정*/
Servo servo; 
int motor = 5; 
int angle = 90; 
//-------------------------------------------------------------------------------------
//-----------------------------------WiFi 및 AWS 주소 설정-------------------------------
/* 
    *AWS ip 설정 (13.125.190.107)
*/
const char* ssid = "PASCUCCI_3F_001_2G";
const char* password = "p0226028497";
const char* mqtt_server = "13.125.190.107";
//-------------------------------------------------------------------------------------
//-----------------------------------MQTT 객체 및 sub,pub 메세지 변수---------------------
WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;
//-------------------------------------------------------------------------------------
//-----------------------------------초기 와이파이 연결 함수-------------------------------
void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
//-------------------------------------------------------------------------------------
//-----------------------------------콜백 함수 정의--------------------------------------
/*  *콜백함수는 Mqtt Sub 시 동작하는 함수로 Sub 시 필요한 로직을 정희한다
    *본 콜백함수에서는 센서 임계값을 Sub로 받은 후 if문으로 모터와 부저를 동작시킨다. */
void callback(char* topic, byte* payload, unsigned int length) {
   Serial.print("Message arrived [");
   Serial.print(topic);
   Serial.print("] ");
   String value = "";
   for (int i=0;i<length;i++) {
     value += (char)payload[i];
     Serial.print((char)payload[i]);
   }
   int realValue=value.toInt();
   if(realValue==1){
     for(int i = 0; i <30; i++)
       {
         angle = angle + 1;
         if(angle >=180) 
         angle = 180; 
         servo.write(angle);
         delay(10);
       }
   } else if(realValue==0){
     for(int i = 0; i <30; i++)
       {
         angle = angle -1;
         if(angle <= 0)
         angle = 0; 
         servo.write(angle);
         delay(10);
       }
   }
   Serial.println();
}
//------------------------------------------------------------------------------------------
//-----------------------------------reconnect 함수 정의--------------------------------------
/* 
   *맨처음 연결할 때와 연결이 끊겼을 때 재연결하기 위한 함수
*/
void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe("nodemcutest");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
//------------------------------------------------------------------------------------------
//-----------------------------------setUP--------------------------------------------------
/*  *Loop를 돌기 전 딱 한번만 실행되는 함수 */
void setup() {
  servo.attach(motor);  
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}
//------------------------------------------------------------------------------------------
//-----------------------------------loop--------------------------------------------------
/*   *무한 반복 */
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}
