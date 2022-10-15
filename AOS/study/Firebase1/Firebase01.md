# Firebase 시작하기         

### 1. Google firebase                  
앱을 개발할 때, 로그인ㅇ르 위해 인증 기능, 데이터베이스 기능 등을 담당할 서버가 필요하다.       
google의 firebase 서비스를 이용하면 앱 서버 기능을 쉽게 개발할 수 있다.          


### Firebase1Activity.java         

<details>
<summary>CODE</summary>
<div markdown="1">  

```java
public class Firebase1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase1);

        final DatabaseReference myData01 = FirebaseDatabase.getInstance().getReference("myData01");
        ValueEventListener listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView textView = (TextView)findViewById(R.id.textView);
                textView.setText(value);
                Log.d("내태그", "받은 데이터: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) { 
                Log.e("내태그", "서버 에러: ", error.toException());
            }
        };
        myData01.addValueEventListener(listener1); // 데이터가 변경되면 리스너가 자동으로 호출

        Button button = (Button)findViewById(R.id.btnSaveIntoServer);
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText)findViewById(R.id.editText);
                String s = editText.getText().toString();
                myData01.setValue(s); // String 한 개만 보관, 만약 배열이면 배열이라고 앞에서 알려줘야함
            }
        };
        button.setOnClickListener(listener2);
    }
} 
```

</div>
</details>



 ```final DatabaseReference myData01 = FirebaseDatabase.getInstance().getReference("myData01");```          
firebase 데이터베이스의 "myData01" 데이터 항목을 인터넷을 통해서 읽고 쓰기 위한 객체를 생성한다.            
생성된 객체에 대한 참조를 지역 변수 myData01에 대입한다.       

이 앱을 실행하기 전에 먼저 firebase 데이터베이스 관리화면에서 "myData01" 데이터 항목을 만들어야 한다.     
"myData01"이 없으면 저절로 만들어지긴 한다.       

- outer class 지역변수 사용하기      
inner class의 메소드에서 outer 클래스의 멤버 변수를 사용할 수 있다.             
inner class의 메소드에서 outer 클래스의 지역 변수는 사용할 수 없고, final 지역 변수만 사용할 수 있다.                
그래서 final 키워드를 제거하면, ```myData01.setValue(s);```에서 컴파일 에러가 발생한다.                

```java
ValueEventListener listener1 = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ...
    }

    @Override
    public void onCancelled(DatabaseError error) {
        ...
    }
});
```   
위 코드는, ValueEventListener 인터페이스의 자식 클래스를 생성하고,        
그 자식 클래스에서 onDataChange, onCancelled 메소드를 재정의하고,   
그 자식 클래스의 객체를 하나 생성해서, 그 객체에 대한 참조를 listener1 변수에 대입한다.        

```myData01.addValueEventListener(listener1);```         
ViewEventListener 객체에 대한 참조를 myData01 객체에 등록한다.     
myData01 객체는 Firebase 서버의 "myData01" 데이터 항목을 사용하기 위한 객체이다.       

Firebase 서버의 "myData01" 데이터 항목의 데이터가 변경되면,     
등록한 리스너 객체의 onDataChange 메소드가 즉시 호출된다.       
혹시 Firebase 서버와 통신에 문제가 발생하면, 리스너 객체의 onCancelled 메소드가 즉시 호출된다.           

```myData01.setValue(s);```       
Firebase 서버의 "myData01" 데이터 항목의 데이터를 변경(저장)하는 코드이다.         



### 실행 과정 요약     
EditText에 문자열을 입력하고 "서버에 저장" 버튼을 누르면,          
입력된 문자열이 firebase 데이터베이스의 "myData01" 항목에 저장된다.      

firebase 데이터베이스의 "myData01" 데이터 항목의 값이 변경되자 마자,        
ValueEventListener 객체의 inChangeData 메소드에 그 변경된 데이터가 전달된다.              

onChangeData 메소드는, firebase 데이터베이스로부터 전달받은 데이터를 TextView에 채운다.         




