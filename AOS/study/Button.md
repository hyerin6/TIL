# 버튼 만들기   

1. 아이콘   
(1) Google Material Design   
(2) 아이콘 파일 복사   
Mipmap - 해상도와 무관    

2. Buttons 화면 추가   

3. Buttons 화면 Java 코드   
(1) ButtonsAvtivity.java   <br/><br/> 
```View.OnClickListener listener = new View.OnClickListener(){...} ```  
anonymous inner class 문법으로 View.OnClickListener의 자식 클래스 객체를 하나 생성하였다.   
생선된 객체에 대한 참조를 listener 참조 변수에 대입하였다.   
Listener 참조 변수의 타입은 View.OnClickListener 클래스 타입이다.   <br/><br/>
```public void onClick(View view){```  
이 예제에서 onClick 메소드의 파라미터로 전달되는 것은 클릭된 버튼 객체이다.   
onClick 메소드의 파라미터 변수의 타입은 View 클래스이다.   
view 클래스는 Button 클래스와 ImageButton 클래스의 부모 클래스이다.   
부모 클래스 타입의 참조 변수가 자식 클래스 객체를 참조할 수 있다. (up-casting)  

(2) 메뉴 추가   
새 메뉴 항목을 MainActivity의 메뉴 리소스 파일인 res/menu/menu_main.xml 파일을 수정하자.   

```xml 
<item android:id="@+id/action_buttons"
      android:title="@string/action_buttons" app:showAsAction="never" />
```
새 메뉴 항목을 추가했다.   

- XML 파일에서 문자열 리소스 id : @string/action_buttons  
- XML 파일에서 메뉴의 id : @+id/action_buttons  
- java 파일에서 문자열 리소스 id : R.string.action_buttons  
- java 파일에서 메뉴의 id : R.id.action_buttons  <br/><br/>
안드로이드 화면 상단에 메뉴가 표시되는 제목 줄을 액션바(actionbar)라고 부른다.  
액션바에 표시되는 아이콘을 액션바 오버플로우 아이콘이라고 부른다.    
