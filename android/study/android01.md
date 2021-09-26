# 첫 예제   
#### 1. 안드로이드 업데이트  
Help - Check for Updates  

#### 2. 안드로이드 소개  
안드로이드는 Google이 만든 스마트폰용 운영체제이다.
- 리눅스 기반  
- 커널과 시스템 주요 모듈은 c언어  
- 주로 앱 개발은 Java로 하지만, 다른 언어로도 개발 가능  

| 안드로이드 시스템의 계층 구조 | 
|:--------:|
| 안드로이드 앱 | 
| 안드로이드 API 클래스(Java Library) | 
| Java Virtual Machine(Dalvik) | 
| 프로그래밍 라이브러리나 엔진 | 
| 운영체제 커널 | 
| 디바이스 드라이버 | 
| 하드웨어 |    



- API(Application Programming Interface)  
안드로이드 앱 개발을 공부하는 것은 안드로이드 API를 공부하는 것이라고도 한다.  
API는 말 그대로 애플리케이션을 프로그래밍 하는데 필요한 인터페이스의 약자이다.  

- 안드로이드 애플리케이션의 주요 구성 요소  
(1) Activity   
activity는 화면 하나에 해당하는 객체이다. 언제나 하나의 activity가 차지한다.  </br>  
(2) Service  
화면에 보이지 않고 백그라운드에서 실행되는 서비스 객체이다.  </br>  
(3) context  
Activity 클래스와 Service 클래스의 부모 클래스이다.  
정확하게 말하면 부모에 해당하는 interface 이다.   </br>  

**Q.** 안드로이드에 어떤 항목들이 포함되어 있나요?  
**A.** 운영체제, 그래픽 사용자 인터페이스, 표준 앱  


#### 3. 첫번째 예제 (E01Button)   
1. 액티비티   
MainActivity.java 소스코드 파일  
activity_main.xml 화면 형태를 지정하는 파일   

2. activity_main.xml 수정  
뷰(vuew), 레이아웃(layout)   
텍스트 박스, 체크 박스, 버튼 등 화면의 구성요소를 뷰(view)객체 라고 부른다.   
이 뷰 객체를 화면에 배치하는 방식을 레이아웃이라고 부른다.   

- activity_main.xml 내용   
앱의 첫 화면에 표현될 뷰 객체의 목록  
크기, 폰트, 배경색 등 뷰 객체들의 속성  
뷰 객체들의 배치 레이아웃   

- constraint layout 수정  
TextView를 마우스로 드래그해서 위치를 옮겨보자.   
이런 방식으로 위치를 결정하는 것을 ConstraintLayout이라고 부른다.   

- TextAppearance (Text Size)  
텍스트 크기를 수정해보자.   
안드로이드 폰트 크기 단위는 sp이다.   
(sp는 시스템 설정으로 일괄 조정이 가능하다.)    


#### 4. lambda expression 으로 자동 수정 기능 끄기   
Java 언어에 anonymous inner class 문법이 있다. android 앱을 구현할 때 이 문법을 자주 사용한다.  
구현할 메소드가 하나인 anonymous inner class인 경우에 lambda expression 문법으로 구현하면 훨씬 간결하다.   
android studio는 anonymous inner class 문법으로 구현된 소스코드를 자동으로 lambda expression 문법으로 수정해준다.   
[lambda, inner class Java 코드 보러가기](https://github.com/hyerin6/Java2/tree/master/InnerClassExam/src/net/skhu/inner)   

#### 5. MainActivity.java 소스 관찰  
- ```public class MainActivity extends AppCompatActivity {...}```    
안드로이드에서 액티비티는 Activity 클래스로 생성된다.  
AppCompatActivity는 Activity 클래스의 자식 클래스이다.  
(즉, MainActivity도 액티비티가 된다.) 액티비티는 안드로이드에서 애플리케이션을 구성하는 컴포넌트 중 하나이다.  
사용자가 화면을 통하여 어떤 작업을 할 수 있도록 하는 컴포넌트가 바로 액티비티이다.   </br>    
애플리케이션은 여러개의 액티비티를 가질 수 있지만,    
하나의 순간에 사용자는 오직 하나의 액티비티 하고만 상호작용하고 액티비티는 다른 액티비티로 넘어갈 수 있다.   

- ```public void onCreate(Bundle savedInstanceState) {...}```  
onCreate() 메소드는 액티비티가 생성되는 순간 자동으로 딱 한번 호출된다.   
따라서 모든 초기화와 사용자 인터페이스 설정이 여기서 이루어져야 한다.  
매개변수인 savedInstanceState는 애플리케이션이 이전에 실행되었던 상태를 전달해준다.   

- ```super.onCreate(savedInstanceState);```
부모 클래스인 AppCompatActivity 클래스의 onCreate()를 호출하는 문장이다.  
onCreate 메소드를 구현할 때, 언제나 첫 줄이 이 문장이어야 한다.   

- ```setContentView(R.layout.activity_main);```  
setContentView()는 액티비티의 화면을 설정하는 함수이다. 여기서는 R.layout.activity_main으로 액티비티의 화면을 설정한다.   
**Q.** R.layout.activity_main은 무엇일까?  
**A.** 리소스 파일에 대한 id입니다.    
Java에서 점 연산자(.)는 클래스의 멤버 변수를 참조할 때 사용하는 연산이다.     
R/layout/activity_main >> 이렇게 예상해 볼 수 있습니다.   
res/layout/activity_main.xml >> 이 파일이 현재 보이는 콘텐트를 XML로 작성한 것입니다.    
리소스 파일의 내용대로 뷰(view) 객체가 자동으로 생성되어 화면에 채워집니다.   

**[참고 그림]**    
<img width="1411" alt="스크린샷 2019-09-24 오후 4 19 33" src="https://user-images.githubusercontent.com/33855307/65489962-33d63a00-dee7-11e9-8500-c81610d1a05b.png">

#### 뷰 (View)    
view 클래스는 모든 뷰들의 부모 클래스이다. 따라서 view 클래스가 가지고 있는 필드나 메소드는 모든 뷰에서 공통적으로 사용 가능하다.   
- id   
모든 뷰는 정수로된 id(식별자)를 가진다. XML 파일에서는 다음과 같은 형식으로 뷰에 id를 부여한다.  
```android:id="@+id/my_button"```   

- 뷰의 위치와 크기   
match_parent : 부모의 크기를 꽉 채운다.(fill_parent도 같은 의미)  
wrap_content : 뷰가 나타내는 내용물의 크기에 맞춘다.   

- 버튼   
버튼 클래스도 TextView 클래스를 상속받아 작성되었으므로 TextView의 모든 속성을 사용할 수 있다.   

- 액티비티의 화면에서 특정한 뷰를 찾는 메소드는 findViewById()입니다.  


#### 이벤트 처리      
1. 입력 위젯    
입력 위젯은 사용자 인터페이스에서 사용자와의 상호작용을 제공하는 컴포넌트이다.   
즉, 사용자의 입력을 받을 수 있는 위젯이다.    
아래 표는 몇 가지 위젯을 설명한다.   


| 위젯 | 설명 | 관련 클래스 | 
|:--------:|:--------:|:--------:|
| Button | 어떤 동작을 수행하기 위해 사용자가 누를 수 있는 푸시 버튼 | Button | 
| Text filed | 편집이 가능한 텍스트 필드 | EditText, AutoCompleteTextView (자동완성기능) | 
| Checkbox | on/off 스위치, 사용자가 그룹에서 여러가지 옵션을 동시에 선택할 수 있게 하려면 체크 박스를 사용한다. | CheckBox | 
| Radio button | 체크박스와 비슷한 그룹에서 하나의 옵션만 선택 가능 | RadioGroup, RadioButton | 
| Spinner | 사용자가 여러 값 중에서 하나를 선택할 수 있는 드롭 다운 리스트 | Spinner |   



2. 버튼 
- 텍스트 버튼 (android:text="")    
- 이미지 버튼 (android:src="@drawable/button_icon")     
- 텍스트 + 이미지 버튼 (android:drawableLeft="@drawable/button_icon")     

버튼의 이벤트 처리 - onClick() 메소드 구현   
자세한건 이벤트 처리에서 설명한다.   
일단 세가지 구현 규칙만 알아두자.   
- public 
- void 반환형   
- View를 메소드의 인수로 가진다. 클릭된 view 객체가 전달된다.    


3. 이벤트 처리 객체를 이용하여 이벤트 처리하기   
이벤트를 처리하는 메소드들이 정의된 인터페이스를 이벤트 리스너(event listener)라고 부른다.   
이벤트 리스너를 구현하는 클래스를 정의하고 이 클래스의 객체를 생성하여 위젯에 등록한다.  

```java
class Myclass{
  class Listener implements View.OnClickListener{ 
    public void onClick(View view){
        ...
    }
  } // 여기까지 리스너 인터페이스를 구현할 클래스 정의 
  Listener lis = new Listener(); // 이벤트 리스너 객체 생성  
  button.setOnClickListener(lis); // 버튼에 이벤트 리스너 객체 등록
}
```

**예)** 버튼이 클릭되는 경우에 발생하는 클릭 이벤트를 처리 하려면,  

View.OnclickListener 객체를 생성하고, SetOnClickListener(View.OnclickListener)를 호출하여 버튼에 설정하면 된다.  
```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // 버튼이 클릭되면 여기서 어떤 작업을 한다.  
            }
        };
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(listener);
    }
```

4. 리스너 객체를 생성하는 방법    
**anonymous inner class 문법으로 구현하기**  
(1) OnClickListener 인터페이스를 상속받아 자식 클래스를 생성한다.   
(2) 그 자식 클래스에 OnClickListener의 onClick 메소드를 재정의(Override)한다.  
(3) 그 자식 클래스의 객체를 하나 생성한다.   

```java
View.OnClickListener listenerObj = new View.OnClickListener(){ // (1), (2)
  @Override
  public void onClick(View v){ // (3)
          ...
  }
};
```  

- inner class 장점  
내부 클래스는 자신이 속해있는 클래스의 멤버 변수들에 자유롭게 접근할 수 있다.  
즉, inner class는 outter class의 this가 사용가능하다.

- anonymous class 장점  
한 곳에 이벤트 처리와 관련된 모든 코드가 작성된다.   


5. 이벤트 핸들러 (event handler)  
어떤 특정한 상황이나 사건이 벌어지자마자 자동으로 호출되어야 하는 메소드를 이벤트 핸들러라고 부른다.    
이벤트 핸들러의 이름은 on으로 시작하는 것이 관례이다.   
예 - onCreate, onClick  </br> </br> 
액티비티 클래스의 onCreate 이벤트 핸들러 메소드는 액티비티가 생성되자마자 자동으로 호출된다.  
액티비티가 생성되자마자 해야할 작업을 구현해야 한다면, 액티비티 클래스에서 onCreate 메소드를 재정의(Override)해서 여기에 구현하면 된다.   </br> </br> 
안드로이드 앱을 만들때에는 화면에 해당하는 액티비티 클래스를 구현해야 한다.   
안드로이드 SDK 라이브러리에 들어있는 Activity 클래스나 AppCompatActivity 클래스를 상속받아서   
액티비티 클래스를 구현해야 한다. 그 클래스에서 이벤트 핸들러 메소드를 재정의해야 한다.   

6. 리스너(Listener)  
액티비티 클래스의 이벤트 핸들러는 액티비티의 자식 클래스에 구현하지만   
뷰 클래스의 이벤트 핸들러는 리스너 클래스를 만들어서 리스너 클래스에 구현해야 한다.   </br> </br> 
오직 이벤트 핸드러를 구현하기 위한 목적의 클래스를 리스너 클래스라고 부른다. (event listener class)    
Java애서 이벤트 핸들러의 메소드 이름은 on으로 시작, 리스너 클래스 이름은 On으로 시작하는 것이 관례이다.   </br> </br> 
버튼이 클릳될 때, 일어나는 일을 구현하려면 OnClickListener 인터페이스를 상속하여    
자식 리스너 클래스를 만들고 여기에 onClick 이벤트 핸들러 메소드를 구현해야 한다.     
그리고 그렇게 구현된 자식 리스너 클래스의 객체를 하나 생성하여, 버튼 객체에 등록해줘야 한다.     
리스너 객체를 버튼에 등록할 때는 setOnClickListener 메소드를 호출한다.      


#### 6. 소스코드 또 관찰   
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button)findViewById(R.id.button);
        View.OnClickListener listenerObj = new View.OnClickListener() {
            @Override
            public void onClick(View v) { // (1)
                EditText e = (EditText)findViewById(R.id.editText); // (2)
                CharSequence s = e.getText(); // (3)
                TextView t = (TextView)findViewById(R.id.textView); // (4)
                t.setText(s); //(5)
            }
        };
        b.setOnClickListener(listenerObj);
    }
}
```  

(1) 위 예제에서 onClick은 리스너 객체의 메소드이다.  
위 코드에서 리스너 객체를 생성해서 버튼에 등록만 했을 뿐이지, 리스너 객체의 onClick을 호출한 것은 아니다.   
즉, 위 코드를 실행시킨다고 onClick 메소드가 호출되지는 않는다.   

(2) 이 EditText 객체는 setContentView(R.layout.activity_main); 에서 생성되었다.    

(3) EditText는 문자열을 입력받기 위한 텍스트 박스이다. 텍스트 박스에 입력된 문자열을 얻으려면 getText() 메소드를 사용 해야한다.   
EditText클래스의 getText 메소드는 **CharSequence** 타입의 문자열을 리턴한다.    
CharSequence - String, StringBuilder, StringBuffer 의 공통 부모 인터페이스    

(4) 이 TextView 객체는 setContentView(R.layout.activity_main); 에서 생성되었다.   

(5) TextVIew에 표시된 문자열을 바꿀때에는 setText 메소드를 사용한다.   

onCreate의 지역변수와 파라미터는 onClick이 실행될 때는 없는 상태이다.   
이 예제에서 onClick의 view 파라미터는 buttton이다.   


