# Tomcat 설치


1. tomcat을 다운 받는다.  
http://tomcat.apache.org
  
2. 다운 받은 파일 압축 해제    
  
3. 터미널 접속 후 명령어 입력   
  
``` sudo mkdir -p /usr/local ```  
경로가 존재하지 않을 때 사용  
-p: 상위 폴더가 없으면 상위 폴더도 만들어 준다.   
  
``` sudo mv ~/Downloads/apache-tomcat-8.5.37 /usr/local ```    
다운받은 폴더를 local 폴더로 이동 (버전 확인하기)     

```   sudo rm -f /Library/Tomcat ```  
예전에 톰캣 설치를 했었다면 Tomcat 폴더 삭제   
-f: 사용자에게 물어보지 않고 삭제    
  
```   sudo ln -s /usr/local/apache-tomcat-8.5.37 /Library/Tomcat ```    
apache-tomcat-8.5.37 폴더와 Tomcat 폴더, 심볼릭 링크를 설정한다.  
-s : 심볼릭링크를 생성한다.  

**[주의!]**   
apache-tomcat-8.5.20 폴더 안에 apache-tomcat-8.5.20가 있을 경우 하위에 있는 폴더로 지정해야한다.  
그래야 이후에 사용할 bin 폴더를 사용할 수 있다.  
Tomcat 폴더는 따로 만들어주지 않아도 되며, 위처럼 직접 타이핑하면 만들어진다.  


```   sudo chown -R 아이디 /Library/Tomcat ```    
Tomcat 폴더에 소유권 지정 ('아이디' 부분에 사용자의 아이디를 적는다.)  
 -R : '아이디'로 user ID, group ID를 변경한다.  
  
```   sudo chmod +x /Library/Tomcat/bin/*.sh ```    
/bin/*.sh 에 권한수정  
+x : 실행권한(x) 추가  

### Tomcat 시작  
```   /Library/Tomcat/bin/startup.sh ```  
  
### Tomcat 종료  
``` /Library/Tomcat/bin/shutdown.sh ```  
 

이후 Tomcat을 시작하고  http://localhost:8080/ 에 방문했을 때, 
아래와 같은 창이 나왔다면 설치 완료~   


<img width="1440" alt="2019-01-02 5 55 19" src="https://user-images.githubusercontent.com/33855307/50585524-c990eb80-0eb8-11e9-9d85-05551416ef8b.png">

