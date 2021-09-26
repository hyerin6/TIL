# UNIX2   

### The Unix System   
- MultiUser : Server 터미널을 통해 연결   
- SingleUser : 자신의 PC  
- interface : 텍스트 기반, GUI  

### Basic   
- 유닉스는 대소문자를 구분한다.  
- 백스페이스, delete는 시스템마다 다르지만 ^h     
  * ^ = ctrl  
  
### Loggin In   
- userName(Login Id) : 당신을 구별하는 유일한 이름 (계정 = id = 로그인 id = user id) **예)** unix27    
- root 시스템 관리자 : 시스템 관리자가 사용자 이름과 초기 비밀번호(비밀번호도 대소문자 구분!)를 할당합니다.  

### User Account  
- 일반적으로 이름 짓는 방법   
  + John doe = jdoe     
  + Im Hyunmi = him    
- 첫번째 작업 : 비밀번호 변경   
  + 6자리 이상, 문자와 숫자 혼합   
  
### Shell     
- $, %, ... 여러 프롬프트가 있다.  
- A Shell   
  + 당신과 유닉스 운영체제 사이의 중간자    
  + 프로그램 실행, 프로세스의 파이프라인의 구축, 파일에 출력 저장, 한개의 프로그램 동시 실행   
  + 모든 명령어를 실행한다.   
- 가장 인기 있는 Sell   
  + 본셸(sh)      
  + 콘셸(ksh)      
  + c셸(csh)      
  + 바쉬셸(bash)      
- 지금의 Shell이 마음에 들지 않으면, 마음에 드는 Shell 이름을 치고 Enter을 누르면 Shell이 변경된다.   
- ```$ echo$SHELL``` 어떤 Shell을 사용하고 있는지 알려주는 명령어     

### Loggin off   
- ```$ exit```   
- ```$ logout```    
- ^d   
(유닉스 버전마다 다르다.)   

### Turning off a Unix System     
소유자 혹은 root 사용자만 시스템을 종료시킬 수 있다.     
- CTR-ALT-DEL     
- init 5    
- shutdown now      
- halt    
(유닉스 버전마다 다르다.)    

### Using special charaters      
- ^h - 한글자 지워준다.     
- ^c - 강제로 종료시켜준다.   
- ^d - logout   
- ```$ stty -a``` - 이 명령어는 특수한 단축 명령어들을 전부 보여달라는 것이다. (-a는 All을 의미한다.)   

단축 명령어를 변경하고 싶으면 ```$ stty erase ^a``` (한글자 지워주는 ^h를 ^a로 변경) 이런식으로 변경하면 된다.    
하지만 이런 방식은 로그아웃하면 초기화되기 때문에 계속 변경되기를 원하면   
환경설정으로 변경해야 한다.    


### Setting / change password  
- '[]' : 대괄호는 옵션을 나타내기 때문에 있어도되고 없어도되는 옵션을 나타낼때 사용한다.      
- '+' : 최소한 한개 이상이 들어가야 하는 부분을 나타낸다. (하나라도 없으면 오류)  
- ```$ whoami``` - 현재 접속한 사용자가 누군지를 보여주는 명령어, 현재 사용자의 id를 보여준다.   

- root 사용자는 모든 것에 접근할 수 있다.   
- shadow 파일에는 사용자 비밀번호가 암호화되어 저장되어있고, passwd 파일에는 사용자 정보를 갖고 있다.   

### File Structure   
- 파일명  
  + 문자, 숫자 사용이 가능하다.   
  + 특수 문자는 ' _ ' underscore와 ' . 'priod 만 사용할 수 있다.      
    * ' . ' 을 사용하는 경우 파일명의 시작이 '.'이면 hidden 파일이다.    
    ```$ ls``` 명령어로는 확인할 수 없고 ```$ ls -a``` 명령어를 사용해야 확인할 수 있다.     
  + Length - 최대 255자     
  + 대소문자 구분, 특별한 경우가 아니면 대부분 소문자를 사용한다.    
  
 
### Directories   
- 계층구조, 트리모양, 레벨을 갖고있다.   
- / (root)가 시작이다.   
- 이 구조는 MS/DOS(마이크로소프트)와 비슷하다. 마이크로소프트가 유닉스를 빌렸다.    

- Home Directory   
  + 처음 로그인한 곳   
  + 일반적으로 /home/username 이 곳이 당신의 파일 영역이다.   
  + hidden 파일을 포함한다.   
  + 시스템 관리자는 이러한 홈 디렉토리 값을 할당한다.   

- 디렉토리 관련 명령어   
  + ```$ pwd``` - 현재 어떤 디렉토리에서 작업중인지 알려주는 명령어  
  + ```$ mkdir``` - 디렉토리를 생성하는 명령어   
  + ```$ cd``` - 뒤에 입력한 디렉토리로 이동한다.   
  + ```$ rmdir``` - 디렉토리 삭제 (빈 경우에만 가능)    
  

### Changing Directories : CD   
```$ cd [directoryName]```    

- 뒤에 입력한 디렉토리 이름으로 이동한다.   
- '[directoryName]'이 생략된 경우 또는, '~'가 붙는 경우에는 소유자의 홈 디렉토리로 이동한다.   

- ```$ cd .``` - 현재 디렉토리       
- ```$ cd ..``` - 부모 디렉토리로 이동 (up one)  
- ```$ cd ~``` - home 디렉토리로 이동   

### Pathnames    
- Absolute 절대경로     
/(root)로 시작     
**예)**          
```/home/hyerin$ cd /tmp```      
```/tmp$ ```    

- Relative 상대경로     
현재 디렉토리부터 시작      
**예)**      
```/home/hyerin$ cd tmp```     
```/home/hyerin/tmp$ ```    

### 파일과 디렉토리 / 상대경로와 절대경로를 구분해보자.    
* ../ 은 one up directory를 의미한다.     

### Making Directory : mkdir  
```$ mkdir newDirectoryName```     
- 새로운 디렉토리를 만드는 명령어, 절대경로와 상대경로를 사용할 수 있다.         
- 이미 만들어진 디렉토리와 동일한 이름을 생성하려고하면 생성되지 않는다.          

**예)**    
```$ pwd```    
/home/hyerin    
```$ mkdir /home/hyerin/test``` OR ```$ mkdir test```     

### Deleting a Directory : rmdir      
```$ rmdir {directoryName}+```    
- 디렉토리 안에 하나의 파일이나 디렉토리가 있으면 삭제되지 않는다.   
- '-r' 옵션을 주면 그 안에 있는 것들 전부 삭제할 수 있다. ```$ rm -r {directoryName}+``` rmdir이 아닌 rm임을 주의하자.    
- '-p' 옵션은 상위 디렉토리를 삭제하는 옵션이다.      
 
### man : online help   
```$ man word```    
```$ man -k keyword```    

명령어 중에서 모르는게 있으면 man 뒤에 입력하면 옵션이나 설명 파일이 열리는데    
man 페이지에 들어갔다가 나오려면 'q'를 누르면 된다.   


### Displaying a File : cat     
```$ cat -n {fileName}*```     

- 파일의 내용을 확인하거나 입력할 수 있는 명령어이다.     
- '-n' 옵션은 라인 넘버를 보여준다.     
- 유닉스에서 standard output/input을 바꿀 수 있다.   
  + ```$ cat > b.txt``` 화면에서 편집한 내용을 b.txt 파일에 전달한다.        
   input (동일한 파일명이 없으면 파일이 생성됨)                   
  + ```$ cat < b.txt``` OR ```$ cat b.txt``` b.txt 파일 내용이 화면에 출력된다. ouput        

### Displaying a File : more   
```$ more -f [+LineNumber]{fileName}*```        

- 특정 파일의 내용을 확인하는 그 페이지에서 바로 vi로 파일을 열어 편집할 수 있으며,   
텍스트 파일의 내용을 한 페이지씩 차례대로 확인할 수 있다.   
- 기본적으로 라인 1부터 시작한다.     
- 한번에 한 페리지씩 목록을 스크롤한다.   
- +option을 사용해서 시작 line 번호를 지정할 수 있다.    

- 다음 페이지를 표시하려면 스페이스바를 누르세요.    
- 다음 행을 나열하려면 Enter 키를 누르세요.    
- 더 이상 그만두려면 'q' 키를 누르세요.     
- ^b 는 이전페이지를 표시합니다.    
- h 는 도움말 페이지를 표시합니다.     

**예)**      
```$ ls –la /usr/bin > myLongFile```                     
```$ more myLongFile```                 


### Displaying a File : head and tail   
```$ head -n {fileName}*```      
 - 파일의 앞 부분을 출력하는 명령어     
 - 디폴트는 1부터 10 line    
 - '-n' 옵션은 파일의 처음부터 n개 출력   
 
 ```$ tail -n {fileName}*```     
 - 파일의 뒷 부분을 출력하는 명령어   
 - '-n' 옵션은 파일의 마지막에서 부터 n개 출력    


### Create a file     
파일을 만드는 세가지 방법    
1.  ```$ cat > test.txt```    
2.  ```$ touch test.txt```   
3.  ```$ vi test.txt```           
 
 
### Listing Contents of a Directory : ls    
```$ ls -adglsFR {fileName}* {directoryName}*```    

- hidden 파일을 제외하고 파일이나 디렉토리의 정보를 알파벳 순으로 나열한다.    
- '-a' 옵션은 숨긴 파일까지 전부 보여준다.   
- '-l' 옵션은 만든사람, 언제 만들었는지, 권한 등을 전부 보여준다.   

**예)**  
 ```$ ls```  
 heart    
 
```$ ls -l heart```    
-rw-r--r-- 1 glass 106 Jan 30 19:46 heart      
(순서대로) 권한, 하드링크 개수, 파일 소유자의 사용자 이름, 파일의 크기(바이트), 파일을 마지막으로 수정한 시간, 파일명      

### Renaming / Moving a File : mv    
```
$ mv -i oldFileName newFileName 
$ mv -i {fileName}* directoryName
$ mv -i oldDirectoryName newDirectoryName
```
- mv 명령어는 두번쨰 이름(newName)이 존재하는지 아닌지에 따라 의미가 달라진다.     
1. 이름 변경    
2. 파일들을 디렉토리로 이동   
3. 새로운 디렉토리로 이동 OR newName으로 이름 변경         

**예)** ```$ mv e.txt dir3/g.txt```     
파일을 dir3으로 옮기고 파일명을 g.txt로 변경하는 명령어이다.      

- 옵션 -i    
interation의 약자로 뭔가 경고할 일이 있으면 명령문을 실행하기 전에 경고해달라는 의미이다.       
만약 -i 옵션이 없는데 같은 이름의 파일을 생성하려고하면 덮어버린다.   

```$ alias cp = 'cp -i'```
alias 설정을 하면, ```$ cp```만 입력해도 -i 옵션이 자동으로 함께 실행된다.      
alias 설정을 무시하고 싶을 때는 ```$ \cp``` 역슬래시를 앞에 넣어주면된다.          


### Copying Files : cp       
```
$ cp -i oldFileName newFileName
$ cp -ir {fileName}* directoryName  
```
- 파일의 내용이나 디렉토리의 파일을 복사해준다.      
- 디렉토리의 내용을 복사하려면 -r 옵션이 필요하다.     

### Deleting files and directories    
```$ rm -fir {fileName}*```    
- 디렉토리 계층에서 파일 제거    
- '-r' 옵션은 하위 디렉토리를 포함한 모든 디렉토리의 내용을 재귀적으로 삭제한다.           
- '-f' 옵션은 모든 오류 메시지와 메시지를 금지한다. 이는 –i 옵션을 재정의한다.(이름에서 오는 옵션도 포함) 이건 위험한 옵션이다.    

### Filtering Files   
```$ grep 'reg' fileName```    
- 입력으로 전달된 파일의 내용에서 특정 문자열을 찾고자할 때 사용하는 명령어     
- 정규 표현식(Regular Expression)에 의한 패턴 매칭(Pattern Matching) 방식을 사용    

**예)**     
```$ cat grepfile```      
👉 필터링할 파일 나열       

```$ grep 'the' grepfile```      
👉 "the"라는 단어를 검색    

### Comparing Files : diff   
```$ diff fileName1 fileName2```     

- fileName1과 fileName2 비교하여 차이점을 찾아낸다. 

**예)**   
```$ cat lady1```    
Lady of the night,       
I hold you close to me,        
And everything you say to me is right.          

```$ cat lady2```    
Lady of the night,     
I hold you close to me,    

```$ diff lady1 lady2```
3d2
< And all those loving words you say are right.           

### Counting Lines, Words and Characters in Files : wc     
```$ wc fileName```     
 - 파일의 행, 단어, 글자 수를 알려주는 명령어       
 - '-l' 옵션은 라인 수만 보여준다.     
 - '-w' 옵션은 단어 수만 보여준다.      
 - 'c' 옵션은 글자 수만 보여준다.        
 
 **예)** 9 43 213 heart.final        
 
### clear     
화면을 깨끗하게 정리해준다.      

### ```$ date [mmddhhmm[[CC]YY][.ss]]```                   
- 날짜와 시간 출력 및 변경          
- ```$ rdate -s time.bora.net```  bora.net 이라는 서버에서 주는 date에 맞춰준다.       

### Archives: tar, gzip, bzip2       
```$ tar {ctxfvuz} {archiveName} {fileOrDirectoryName}*```             
- 파일과 디랙토리를 묶어 하나의 아카이브 파일을 생성한다.          

### Tape Archiving: tar      
```$ tar {ctxfvuz} {archiveName} {fileOrDirectoryName}*```          
- 파일과 디랙토리를 묶어 하나의 아카이브 파일을 생성한다.   
         
- c: 새로운 tar 형식 파일 생성한다.       
- f: 파일 이름을 지정            
- v: 처리하고 있는 파일의 정보를 출력한다.          
- t: tar 파일의 내용을 출력한다.           
- x: tar 압축을 푼다.             
- z: gzip으로 압축해주는 옵션 (압축할때는 .tar.gz)          
- C: 대상 디렉토리를 지정함        
- tar은 예외적으로 옵션을 줄 때 '-'를 붙이지 않아도 된다.             
- 거의 항상 v와 f 옵션을 같이 사용한다.          
**예)**              
```$ tar -cvf aaa.tar abc```             
abc라는 폴더를 aaa.tar로 압축          
 
```$ tar -xvf aaa.tar```        
aaa.tar라는 tar파일 압축을 푼다.       

```$ tar -zcvf aaa.tar.gz abc```     
abc라는 폴더를 aaa.tar.gz로 압축      

```$ tar -zxvf aaa.tar.gz```         
aaa.tar.gz라는 tar.gz파일 압축을 푼다.      

```$ tar -cvf unix00.tar c.txt h.txt```            



### Archiving: gzip, bzip2      
```$ gzip {fileName}*```        
```$ gunzip {compresedFileName}*```         
```$ bzip2 {fileName}*```        
```$ bunzip2 {compresedFileName}*```             
- gzip, bzip2 둘 다 tar 명령어로 압축 가능        
- gzip: -z,  bzip2: -j       


### FIle Attribute          
- ```$ls -l``` 명령어로 확인 가능하고 1부터 7까지 순서대로 나온다.   
1. 권한          
2. 하드링크 개수                
3. 현재 이 파일의 소유자 (id로 나타냄, 초기에 소유자는 만든 사람인데 소유자는 바뀔 수 있다.           
파일을 만든 사람의 그룹이 소유자 그룹이 된다. 그러나 이것도 나중에 바뀔 수 있다.)        
4. 파일 그룹 이름                           
5. 파일 크기 (바이트 단위)           
6. 마지막으로 변경된 날짜         
7. 파일의 이름          

- Filenames    
  + 파일이름은 최대 255자 까지이고 ‘.’ 이나 ‘..’은 사용할 수 없다. (현재 작업 디렉토리 및 상위 디렉토리 전부)      
  + 파일 그룹도 마찬가지로 id로 출력된다.    

- File Owner     
파일 소유자    

- File Group     
파일 그룹도 마찬가지로 id로 출력된다.             
 
- File Types       
```$ ls -l```     
-rw-r--r-- 1 glass cs 213 Jan 31 00:12 heart.final   
 
파일의 유형 및 사용 권한 설정을 설명한다.    
맨 앞의 '-'가 파일 유형을 나타낸다.       

- '-' - regular file     
- 'd' - 디렉토리      
- 'b' - 버퍼링된 특수 파일(예: 디스크 드라이브)     
- 'c' - c 버퍼링되지 않은 특수 파일(예: 터미널)      
- 'l' - 기호 링크       
- 'p' - 파이프         
- 's' - 소켓         

### File Permissions (Security)      
- 사용자, 그룹, 소유자도 아니고 그룹도 아닌 나머지 이렇게 총 3 클러스터로 관리한다.          
**예)** -rw-r—r—            
첫 글자는 어떤 것인지(파일인지 폴더인지 등) 나타내는 문자이고, 이후 3글자 씩 순서대로 권한을 설명하는 것이다. 예시를 풀어보면    
rw- : user (소유자)     
r— : 그룹     
r— : 그 외 나머지      

각각의 클러스터가 3문자씩 권한을 나타내고 있는데             
맨 앞에 있는 문자는 read권한(r) 즉, 읽을 수 있는 권한이고,         
두번째는 write(w) 즉, 쓰고 수정할 수 있는 권한,       
세번째는(x) 실행할 수 있는 권한이다.         

디렉토리에 읽기 권한이 있다는 것은 디렉토리 안에 있는 파일 정보를 읽을 수 있다.            
디렉토리에 권한이 없으면 파일에 권한이 있더라도 파일을 읽을 수 없다.           

- Hard-Link Count     
계층에서 동일한 실제 파일을 가리키는 레이블 수를 표시한다.      


### Listing File Group: groups      
```$ groups [ userId ]```      
(유닉스가 실제로 인식하는 사용자 아이디는 숫자, 여기서는 문자 id나 숫자 id 전부 사용할 수 있다.)          
본인 혹은 userId가 속한 그룹이 나온다.     

### Changing File Group : chgrp   
```$ chgrp -R groupname {fileName}*```   
- 파일(혹은 디렉토리, 옵션이 없으면 디렉토리 안의 권한은 달라지지 않는다.)의 그룹을 바꿔줄 때 사용하는 명령어   
- root 사용자가 변경할 수 있다.    
- '-R' 옵션은 디렉토리 안의 파일까지 전부 권한을 바꿔주고 싶을 때 사용한다.        
'-R'은 다른 권한을 바꿔주는 명령에서 동일하게 사용된다.         

### chown     
```$ chown -R newUserId {fileName}+ ```     
파일의 소유자를 바꿔주는 명령어       
마찬가지로 숫자 id를 사용할 수 있다.       
디렉토리 안의 소유자까지 전부 바꿔주려면 -R 옵션을 추가해준다.      

### chmod    
```$ chmod -R change{, change}* {fileName}+```        
- 권한의 상태를 바꿔주는 명령어이다.           
- 두가지 사용 방법이 있다. (차이점은 없고 그냥 사용 방법만 다름)              
1. symbolic 심볼릭           
특정 문자를 쓴다.                  
+ : 권한 추가        
- : 권한 제거      
= : 권한과 상관없이 적어주는대로 부여       

**예)** chmod 뒤에 붙는 옵션 ~         
- g+w - 그룹에 쓰기 권한을 추가         
- u-rw - user에 읽고 쓰는 권한 제거        
- g=r - 기존 설정은 관여하지 않고 여기 적혀진 것이 r이기 때문에 그룹에 읽는 기능만 준다.         

2. absolute 절대방식 (주로 이 방법을 자주 쓴다.)           
플래그 개념으로 권한을 한번에 전부 설정한다.       
이진수로 변환 - 1은 권한 있음, 0은 권한 없음.      

**예)** ```$ chmod 755 myprog```    
rwxr-xr-x    

### umask   
file을 처음에 만들 때 주어지는 기본 권한을 설정해 주는 것이라고 생각하면된다.             
로그아웃했다가 다시 들어오면 이 설정이 저장되어있지 않다.      
그냥 프로세스 위에서만 설정되는 것이다.    


### Links  
윈도우에서의 바로가기(shortcuts)랑 비슷하다.      
바로가기랑 똑같은 것은 아니다.            
윈도우 에서는 바로가기 파일을 하나 만든다. 그 파일이 하는 일은 바로가기가 가르키는 파일로 가게끔 하는 것이다.          
두가지 방법이 있다.  

1. Hard Links      
윈도우의 바로가기와 똑같은 방법이다. 새로운 바로가기 파일이 만들어진다.          
c언어로 예를 들면, pointer 이름은 다를지라도 같은 것을 가르킨다.       

2. Symbolic Links          
메모리 공간에 새로 만들어지는 것이 없다.       
윈도우 처럼 바로가기 파일이 만들어지는 것이 아니라, meta data가 생긴다.          
c언어로 예를 들면, pointer 이름은 다를지라도 같은 것을 가르킨다.          


### Hard LInks : In    
이 명령어는 hard link를 만들어 주는 명령어이다.       
**예)**    
```$ ln hold.3 hold``` - 생성             
 ```$ rm hold``` - 삭제   
 
 ### Soft Links: In    
 ```$ ln -s```       
 soft link를 생성하는 명령어    
 
