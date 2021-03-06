## 쿠버네티스란?
쿠버네티스(kubernetes)는 컨테이너화된 애플리케이션을 효율적으로 배포하고 운영하기 위해 설계된 오픈소스 플랫폼이다.

일상에서 애플리케이션이 수시로 사용되고, 경쟁하게 되면서    
지속적 통합(CI)과 지속적 배포(CD)가 중요해졌다.

오픈 소스 사용하여 짧은 시간에 고품질 애플리케이션 개발    
➡️  오픈 소스의 경우 버전이 계속 바뀐다.   
➡️  개발자들 간에 개발 환경의 차이가 발생   
➡️  개발 생산성과 안정성이 떨어진다.

이런 상황에서 컨테이너 기술이 필요하다.   
컨테이너 기술은 애플리케이션 실행에 필요한 라이브러리나 운영체제 패키지 등을 모두 담아서 불변의 실행 환경을 만든다.     
이렇게 하면 개발자들 간에, 테스트와 운영 환경 간의 차이를 없앨 수 있어     
**개발 생산성을 높이고, 애플리케이션 정식 서비스를 안정적으로 배포**할 수 있게 된다.

<br />      
<br />   

## 쿠버네티스가 제공하는 기능
* 배포 계획에 맞춰 애플리케이션을 신속하게 배포할 수 있다.
* 가동 중인 애플리케이션을 스케일 업/다운 가능
* 새로운 버전의 애플리케이션 무정지 업그레이드 가능
* 하드웨어 가동률을 높여 자원 낭비를 줄인다.

<br />      
<br />

## 쿠버네티스 특징
쿠버네티스는 서비스 운영에서 발생하는 다양한 부담을 줄이는 것을 목표로 하여 다음과 같은 특징을 가진다.

* 다양한 환경에서 쿠버네티스 사용 가능
* 계속되는 변화를 전제로 설계된 높은 유연성과 실행환경
* 고가용성과 성능 관리

<br />    
<br />   

## 쿠버네티스가 해결하는 과제

#### (1) 애플리케이션의 빈번한 출시
정식 운영 중인 서비스의 애플리케이션 컨테이너를 무정지로 교체할 수 있다.     
또한 교체 중에 발생하는 성능 저하와 프로그램 충돌로 인한 서비스 정지를 막기 위해       
컨테이너 교체 정책(policy)을 설정할 수 있다.

<br />   

#### (2) 무정지 서비스
쿠버네티스의 자기 회복 기능은 무정지 서비스 운영을 도와준다.      
응답이 없어진 컨테이너를 재기동하며, 쿠버네티스 클러스터(K8s 클러스터) 내에 지정한 수만큼 컨테이너가 돌도록 관리해준다.

<br />   

#### (3) 초기 비용을 낮추고 비즈니스 상황에 맞게 규모를 조정
컨테이너 기술은 애플리케이션과 실행 환경을 하나로 묶어서 배포할 수 있게 해준다.   
쿠버네티스는 복수의 노드 위에서 컨테이너가 조화롭게 돌아갈 수 있도록 해준다.   
이때 K8s 클러스터의 각 노드들이 똑같은 스펙일 필요는 없다.

<br />   

#### (4) 쿠버네티스와 외부 서비스와의 연동
애플리케이션 서버와 달리 데이터베이스에 대한 컨테이너화는 신중하게 접근할 필요가 있다.   
컨테이너는 상태를 포함하지 않는 것을 전제로 하기 때문이다.

`예) 클라우드의 DBaaS(DB as a Service)나 온프레미스에서 관리하는 데이터베이스와 연동하는 것`

이러한 연동을 위해 쿠버네티스는 외부 서비스를 내부 DNS에 등록하는 기능을 제공한다.

<br />  

#### (5) 개발 환경과 운영 환경의 분리
운영 환경에서 사용하는 엔드포인트나 인증 정보는 테스트 환경과 다르기 마련이다.    
쿠버네티스에서는 클러스터를 여러 개의 가상 환경으로 분할하는 것이 가능하다.   
그리고 각각의 가상 환경에 설정 파일, 보안이 필요한 인증서나 비밀번호를 저장할 수 있다.

컨테이너에서는 이 저장된 정보에 접근할 수 있어
이를 활용하여 테스트 환경에서 운영 환경으로 옮길 때 이미지를 다시 만들 필요가 없다.    
즉 테스트가 완료된 컨테이너의 이미지를 그대로 정식 운영 환경에 배포할 수 있는 것이다.

<br />  

#### (6) 온프레미스와 클라우드 위에 구축
대형 클라우드 업체들은 쿠버네티스 서비스를 제공한다.    
쿠버네티스는 인프라의 복잡성을 감추며, 일관된 인터페이스로 다룰 수 있도록 설계되었다.

<br />  

#### (7) 애플리케이션 중심의 오케스트레이션
YAML 파일을 기술하여 쿠버네티스에 제출하면 로드밸런서, 저장소, 네트워크, 런타임 등의 환경이 구성된다.

<br />    

#### (8) 특정 기업에 종속되지 않는 표준 기술
처음엔 구글에서 시작되었지만 오픈 소스 프로젝트로 운영되고 있다.

<br />    

#### (9) 서버들의 가동률 높이기
쿠버네티스에서 사용되는 컨테이너 기술은 애플리케이션이 정해진 서버에서 돌지 않아도 된다는 자유를 제공한다.    
또한 CPU 사용 시간이나 메모리 요구량도 간단히 제어할 수 있다.

이 기술 덕분에 쿠버네티스는 가동률이 적은 서버의 애플리케이션을 한곳에 모을 수 있다.    
서버의 CPU 가동률을 높게 유지하면서도 안정적으로 서비스를 제공한다는 상반되는 요구사항을 충족시킬 수 있다.

<br />   
<br />      

## 쿠버네티스 아키텍처
<img width="1279" alt="스크린샷 2021-07-28 오후 11 53 42" src="https://user-images.githubusercontent.com/33855307/127344562-7377ed1f-24bd-4da1-a890-13fa62175436.png">   


<br />  

* 마스터: 클러스터 관리 담당
* 노드: 컨테이너화된 애플리케이션을 실제로 실행

<br />  

마스터는 kubect1과 같은 API 클라이언트로부터 요청을 받아서 애플리케이션의 배포, 스케일업/다운, 컨테이너의 버전 업 등의 요구를 처리한다.    
마스터는 K8s 클러스터의 단일 장애점이 되지 않도록 다중화할 수 있다.


유저의 요청이 늘어나 처리 능력을 늘려야 할 때는 기본적으로 컨테이너의 수를 늘리면 되는데 이때 노드 수를 늘려야 할 때도 있다.


K8s 클러스터의 외부에는 레지스트리가 있다.   
이는 도커의 레지스트리와 동일하다.   
각 노드에서 이미지를 다운로드할 수 있도록 네트워크상 접근 가능한 곳에 있어야 한다.

<br />    
<br />    

## 레지스트리와 쿠버네티스의 관계     
쿠버네티스에서도 레지스트리에서 이미지를 다운로드받아 컨테이너를 실행한다.    
1. `docker build`로 이미지를 빌드  
2. `docker push`로 이미지를 레지스트리에 등록    
3. kubectl 커맨드로 매니페스트에 기재한 오브젝트의 생성을 요청   
4. 매니페스트에 기재된 레포지토리로부터 컨테이너 이미지 다운    
5. 컨테이너를 파드 위에서 기동   
<br />   

<img width="1023" alt="스크린샷 2021-07-29 오후 4 16 54" src="https://user-images.githubusercontent.com/33855307/127448601-0c56a7c9-fca3-46de-84f1-ccb783e2d4e2.png">

<br />     
<br />     
 
## 도커와 쿠버네티스 연동     
쿠버네티스는 도커를 컨테이너의 런타임 환경으로 사용한다.      
이는 쿠버네티스 설치 시 제일 먼저 도커를 설치하는 이유이다.   

