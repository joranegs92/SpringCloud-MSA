kafak있는 폴더에서 cmd 열기 

# 주키퍼 실행
1. bin\windows\zookeeper-server-start.bat config\zookeeper.properties 
# 카프카 실행
2. bin\windows\kafka-server-start.bat config\server.properties
# 토픽생성 & 리스트 확인 
3. bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic dev-topic
4. bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
# producer 
5. bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
# consumer
6. bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic quickstart-events

produser 쪽에서 메세지를 보내면 consumer가 받는다 
