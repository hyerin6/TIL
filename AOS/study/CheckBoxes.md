# 체크 박스 만들기   

## 1. CheckBoxes 화면 만들기     
(1) activity_checkboxes.xml 파일 수정 
##### Buttons에 있는   
- checkBox 추가        
- switch 추가          
- RadioGroup 추가     </br>
뷰 그룹 객체는 뷰 객체들의 그룹을 넣어두기 위해 사용한다.   
RadioGroup 클래스는 ViewGroup 클래스의 자식 클래스이다.  
RadioButton들을 그룹으로 묶기 위해 RadioGroup이 필요하다.   
- RadioButton 추가    
- RadioGroup 추가     </br>
RadioGroup의 orientation 속성값을 horizontal으로 수정하자.  

(2) Checkboxes.java 수정   
```java
1 public class CheckboxesActivity extends AppCompatActivity {
2 
3    @Override
4    protected void onCreate(Bundle savedInstanceState) { // (8)
5        super.onCreate(savedInstanceState);
6        setContentView(R.layout.activity_checkboxes); // layout inflation 
7        // (1) [줄 9~15]
8        // 리스너 객체 만들어서 버튼이나 체크박스나 즉, 뷰에 등록하기 
9        CompoundButton.OnCheckedChangeListener listener1 = new CompoundButton.OnCheckedChangeListener() {
10            @Override
11            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
12                String s = String.format("%s : %b", buttonView.getText(), isChecked);
13                Toast.makeText(CheckboxesActivity.this, s, Toast.LENGTH_SHORT).show();
14            }
15        }; 
16        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1); // (2) [줄 16~19]
17        final Switch switch1 = (Switch) findViewById(R.id.switch1);
18        checkBox1.setOnCheckedChangeListener(listener1);
19        switch1.setOnCheckedChangeListener(listener1);
20        // (3) [줄 21~28]
21        RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
22            @Override
23            public void onCheckedChanged(RadioGroup group, int checkedId) {
24                RadioButton radioButton = (RadioButton) findViewById(checkedId);
25                String s = radioButton.getText().toString();
26                Toast.makeText(CheckboxesActivity.this, s, Toast.LENGTH_SHORT).show();
27            }
28        };
29        final RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1); // (4) [줄 29~30]
30        radioGroup1.setOnCheckedChangeListener(listener2);
31        // (5) [줄 32~46]
32        RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {
33            @Override
34            public void onCheckedChanged(RadioGroup group, int checkedId) {
35                int mipmapId = 0;
36                switch (checkedId) {
37                    case R.id.radioCat: mipmapId = R.mipmap.animal_cat_large; break;
38                    case R.id.radioDog: mipmapId = R.mipmap.animal_dog_large; break;
39                    case R.id.radioOwl: mipmapId = R.mipmap.animal_owl_large; break;
40                }
41                ImageView imageView1 = (ImageView) findViewById(R.id.imageView); // (6) [줄 41~42]
42                imageView1.setImageResource(mipmapId);
43            }
44        };
45        final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2); // (7) [줄 45], [줄 55]
46        radioGroup2.setOnCheckedChangeListener(listener3);
47
48        Button button = (Button)findViewById(R.id.button1); 
49        button.setOnClickListener(new View.OnClickListener() { // error 발생
50            @Override
51            public void onClick(View v) { // (8)
52                String s = "checkBox1=" + checkBox1.isChecked() + " ";
53                s += "switch1=" + switch1.isChecked() + " ";
54                s += "radioGroup1=";
55                switch (radioGroup1.getCheckedRadioButtonId()) {
56                    case R.id.radioRed: s += "red "; break;
57                    case R.id.radioYellow: s += "yellow "; break;
58                    case R.id.radioBlue: s += "blue "; break;
59                }
60                switch (radioGroup2.getCheckedRadioButtonId()) {
61                    case R.id.radioCat: s += "cat"; break;
62                    case R.id.radioDog: s += "dog"; break;
63                    case R.id.radioOwl: s += "owl"; break;
64                }
65                Toast.makeText(CheckboxesActivity.this, s, Toast.LENGTH_SHORT).show();
66            }
67        }); // 이 부분이 파라미터이다.
68    }
69
70}
```

#### (1) (줄 9~15)    
anonymous inner class 문법으로 CompoundButton.OnCheckedChangeListener 객체를 하나 생성하는 코드이다.  
CompoundButton.OnCheckedChangeListener 객체를 CheckBox 객체에 등록해 주면,    
체크박스의 체크 값이 변경될 때마다 onCheckChanged 메소드가 자동으로 즉시 호출된다.   
onCheckChanged 메소드의 파라미터 buttonView는 체크박스 객체이고, isChecked는 체크 상태 값이다.

**CompoundButton** - 체크 박스, 스위치는 모양은 다르지만 기능은 같다. 체크박스와 스위치의 부모클래스이다.   

#### (2) (줄 16~19)  
CheckBox 객체나 Switch 객체의 체크 상태가 변경되면 CompoundButton.OnCheckedChangeListener 객체의   
onCheckedChanged 메소드가 즉시 호출될 수 있도록  
CompoundButton.OnCheckedChangeListener 객체를 CheckBox 객체와 Switch 객체에 등록하는 코드이다.   

#### (3) (줄 21~28)    
anonymous inner class 문법으로 RadioGroup.OnCheckedChangeListener 객체를 하나 생성하는 코드이다.  
RadioGroup.OnCheckedChangeListener 객체를 RaioGroup 객체에 등록해 주면  
RadioGroup 내부의 RadioButton들 중에서 체크된 것이 바뀔 때마다   
onCheckChanged 메소드가 자동으로 즉시 호출된다.   
onCheckChanged 메소드의 파라미터 group은 RadioGroup 객체이고,  
checkedId는 새로 체크된 RadioButton 객체의 id 값이다.   

##### (4) (줄 29~30)    
RadioGroup 내부의 RadioButton들 중에서 체크된 것이 바뀔 때마다,   
RadioGroup.OnCheckedChangeListener 객체의 onCheckedChange 메소드가 즉시 호출될 수 있도록   
RadioGroup.OnCheckedChangeListener 객체를 RadioGroup 객체에 등록하는 코드이다.   

#### (5) (줄 32~46)  
위 png 파일들의 id는 각각 다음과 같다.  
R.mipmap.animal_cat_large  
R.mipmap.animal_dog_large  
R.mipmap.animal_owl_large  

#### (6) (줄 41~42)  
ImageView에 표시될 이미지를 교체하는 코드이다.     
(줄 41)은 onCheckedChange 메소드가 실행될 떄마다 실행된다.    
그렇기 때문에 (줄 41)은 매번 찾아지는데 비효율적이기 때문에 변수에 넣어두고 사용하는 것이 좋다.     

#### (7) 줄 45  
Q. 줄 45은 몇번, 언제 실행되고 그 이유를 적으세요.			  </br> 
A. Activity가 만들어질때 한번 실행된다.  </br>

Q. 줄 55은 몇번, 언제 실행되고 그 이유를 적으세요.			</br>
A. 줄 55은 버튼이 클릭될때마다 한번씩 실행된다.  </br>

#### (8) (줄 47~67)
checkBox1, switch1, radioGroup1, radioGroup2 변수는,  
줄51의 onClick 메소드의 지역 변수가 아니고 (inner class의 메소드),  
줄4의 onCreate 메소드의 지역 변수이다. (outer class의 메소드)  

outer class의 멤버 변수와 메소드만 inner class에서 사용할 수 있고,  
outer class의 지역 변수를 inner class에서 사용할 수 없다.  
그런데 outer class의 final 지역 변수는 inner class에서 사용할 수 있다.  

CheckBox 클래스와 Swtich 클래스의 isChecked() 메소드는 boolean 값을 리턴한다.    
체크된 상태면 true, 그렇지 않으면 false를 리턴한다.   

RadioGroup 클래스의 getCheckedRadioButtonId() 메소드는 int 값을 리턴한다.  
체크된 RadioButton의 id 값을 리턴한다.  

## 2. 에러 분석 및 해결   
#### (1) 에러 메시지   
앱을 실행하면 다음과 같은 에러가 발생한다.   
'E02Views이(가) 중지됨'   
'앱 다시 열기'    

net.skhu.e02views 패키지의 CheckboxesActivity 클래스의 onCreate 메소드에서 에러가 발생했다.  
소스 코드에서 위치는 CheckboxesActivity.java의 49줄이다.   
에러 메시지는 NullPointerException이다.   
null pointer exception 에러는 어떤 객체의 메소드를 호출했는데, 그 객체가 null인 경우에 발생한다.   

```java
48 Button button = (Button)findViewById(R.id.button1); 
49 button.setOnClickListener(new View.OnClickListener() { // error 발생
```
줄 49는 지역변수 button이 참조하는 객체의 setOnClickListener 메소드를 호출하는 문장이므로   
button 지역 변수 값이 null이어서 발생한 에러이다.   
즉, R.id.button1 아이디가 잘못된 것임을 짐작할 수 있다.   

```java
Button button = (Button)findViewById(R.id.btnSave);
```
R.id.btnSave로 변경하면 해결 ! 

## 3. 요약  
#### (1) CheckBox와 Switch의 사용법  
CheckBox와 Switch는 true/false 값을 입력 받을 때 사용한다.  
CheckBox와 Switch는 모양만 다르고 사용법은 같다.   

체크 상태가 변하자마자 즉시 무엇을 해야 한다면,  
CompoundButton.OnCheckedChangeListener 인터페이스의 onCheckedChanged 메소드를 구현해야 한다.  
그리고 CompoundButton.OnCheckedChangeListener 객체를 CheckBox나 Switch 객체에 등록해야 한다.  

나중에 데이터를 저장할 때, 체크박스의 체크 상태를 알고 싶다면,   
CheckBox, Switch 클래스의 isChecked 메소드를 호출하면 된다.   

#### (2) RadioButton과 RadioGroup의 사용법  
객관식 항목들 중의 하나만 선택해야 할 때 RadioButton을 사용한다.  
RadioButton 한 개가 객관식 항목 하나에 해당한다.  
RadioButton들은 RadioGroup 내부에 들어있어야 한다.  

선택된 항목이 바뀌자마자 무엇을 해야 한다면,    
RadioGroup.OnCheckedChangeListener 인터페이스의 onCheckedChanged 메소드를 구현해야 한다.    
그리고 RadioGroup.OnCheckedChangeListener 객체를 RadioGroup 객체에 등록해야 한다.    

나중에 데이터를 저장할 때, 선택된 RadioButton 객체의 id를 알고 싶다면,    
RadioGroup 클래스의 getCheckedRadioButtonId 메소드를 호출하면 된다.    

