# Spinners  
## 1. Spinners 화면 만들기   
### (1) 새 액티비티 만들기   
### (2) String Array 리소스 만들기   
문자열 리소스 파일에 문자열 배열 리소스(String Array Resource)도 추가할 수 있다.   
문자열 배열 리소스는 문자열 목록을 배열 하나에 모아놓은 것이다.   

```xml
<string-array name="phoneTypes">
    <item>집전화</item>
    <item>사무실전화</item>
    <item>휴대폰</item>
    <item>기타</item>
</string-array>
```
stirngs.xml 파일에서 <string>, <string-array> 태그들의 순서는 중요하지 않다.   

| 속성 | 값 |  
|:--------:|:--------:| 
| entries | @array/phoneTypes |

### (3) activity_spinners.xml 파일 수정   
Palette에서 Spinner 항목을 화면에 추가하자.   
entries 속성은 Spinner에 표시할 문자열 배열 리소스 id이다. 

<br/>

## 2. SpinnerActivity.java 수정   
### (1) Spinner에 표시할 문자열 배열 채우는 방법 

**방법 1**  
위와 같은 방법으로 문자열 배열을 문자열 배열 리소스로 저장한다.   
이 문자열 배열 리소스의 아이디를 Spinner의 entries 속성값으로 지정한다.   
- 장점   
편하고 다국어 버전을 만들 수 있다.  
- 단점   
문자열 리소스리기 때문에 채우는 항목이 고정된다.   

**방법 2**   
Spinner의 entries 속성값을 지정하지 않는다.   
Java에서 다음과 같이 구현한다. 

```java
String[] stringArray = { "집주소", "직장주소", "기타" };
ArrayAdapter<String> adapter = 
                        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringArray); // (1)
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // (2)
Spinner spinner = (Spinner) findViewById(R.id.spinner_addressType);
spinner.setAdapter(adapter);
```
Spinner에 표시할 문자열 배열이 변하지 않는 값이라면 방법1 방식으로 구현하면 되고    
Spinner에 표시할 문자열 배열을 DB에 조회해야 한다면, 방법2 방식으로 구현하면 된다.   
(실제로는 방법2를 더 많이 사용하고 다국어 버전은 서버에서 데이터를 넘겨줄 때 다국어로 넘겨주면 된다.)  

(1), (2) 에서 리소스 id는 android로 시작하는데, 안드로이드에 속한 리소스라서 앞에 android가 붙는다.   

setDropDownViewResource() 메소드는 dropdwon 되었을 때, 보여지는 view에서 사용될 layout을 별도로 설정하는 경우에 사용한다.      

**Q.** 왜 adapter에 두개의 레이아웃 리소스를 줄까?   
**A.**      

| ID | 설명 |     
|:--------:|:--------:|     
| android.R.layout.simple_spinner_item | StringArray와 함께 전달한다. 생성자 파라미터로 전달 즉, 필수이다. |     
| android.R.layout.simple_spinner_dropdown_item | 메소드 파라미터로 전달 즉, 필수가 아니다. |     


spinner와 데이터 목록 사이에는 adapter가 있어야하고, adapter는 (데이터 하나하나에 대한) 뷰를 만드는 일을 한다.   

### (2) Spinner에서 선택된 항목 확인하는 방법   
- 선택된 항목의 인덱스(index) 값을 얻는 코드   
```java
int index = spinner.getSelectedItemPosition();
```
- 선택된 항목의 텍스트 문자열 값을 얻는 코드   
```java 
String text = spinner.getSelectedItem().toString();  
```  

## 3. 메인 메뉴 수정   
새로 생성한 SpinnersActivity 화면으로 넘어가기 위한 메뉴를   
MainActivity 메뉴에 추가하자.  

```xml
<item android:id="@+id/action_spinners"
    android:title="@string/action_spinners" app:showAsAction="never" />
```
이 메뉴의 id는 다음과 같다.  
XML 파일에서  "@+id/action_spinners"  
Java 파일에서  R.id.action_spinners  


**마무리**  
**Adater의 역할은 데이터 항목 하나하나 뷰를 생성하는 것 !**  
**layout inflation - layout 리소스 xml 파일이 필요하다.**  


