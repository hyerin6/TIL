# AlertDialog 대화상자 만들기   
### 1. onClick 메소드 구현 방법   
Button이 클릭되었을 때 실행되는 메소드를 구현하는 방법은 2가지이다.    
#### (1) 방법 1 - 리스너 구현하기   

```java 
View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SpinnersActivity.this, "버튼 클릭됨", Toast.LENGTH_SHORT).show();
            }
        };
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(listener1);
```

anonymous inner class 문법으로 View.OnClickListener를 구현하고 버튼에 리스너 객체를 등록한다.   
클릭되는지 지켜보다가 클릭되면 실행되는데 이는 관찰자 패턴과 매우 유사하다.    

#### (2) 방법2 - 버튼의 onClick 속성   
Button의 onClick 속성에 메소드 이름을 입력한다.   

```java 
public void button1_clicked(View button) {...}
```  

액티비티 클래스에 그 메소드를 구현한다. 메소드 이름이 일치해야 한다.     
이 방법은 버튼만 가능한 방법이다.   


### 2. Alerts 화면 만들기   
#### (1) 새 액티비티 생성   
AlertsActivity.java    
activity_alerts.xml    


#### (2) activity_alerts.xml 수정    
버튼의 text 속성에 언제나 영어만 표시할 것이라면 궅이 문자열 리소스를 사용할 필요는 없다.   
그런데 버튼의 text 속성 값은 "AlertsDialog1"인데 표시되는 것은 "ALERTSDIALOG1"이다.   
버튼의 text를 전부 대문자로 표시하는 것이 기본 스타일이기 때문이다. 기본 스타일을 변경해주면 된다.   

**res/values/styles.xml 수정** - 모든 액티비티에 적용되는 스타일을 정의하는 파일이다.   
```xml 
<resources>

    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar"> //(1)
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="textAllCaps">false</item> // (2)
    </style>

</resources>
```
(1) Theme.AppCompat.Light.DarkActionBar 스타일에 적용된 것들을 상속 받아서     
    AppTheme 스타일을 생성하였다. AppTheme 스타일이 모든 액티비티에 적용된다.     
    Theme.AppCompat.Light.DarkActionBar 스타일에 textAllCaps 값이 true 로 지정되어 있다.    
    이 값이 AppTheme 스타일에 상속되었다. 이 값을 바꿔야 한다.   

(2) textAllCaps 값을 false 로 바꾼다.      


### 3. AlertsActivity.java 수정   
<details>  
<summary>All code</summary>  
<div markdown="1">    
  
```java 
 public class AlertsActivity extends AppCompatActivity {
 
     int selectedAnimalIndex = 0; 
 
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_alerts);
     }
 
     public void button1_clicked(View button) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this); // (줄 23)
         builder.setMessage(R.string.saveSuccess);
         builder.setNeutralButton(R.string.close, null);
         AlertDialog dialog = builder.create();
         dialog.show();
     }

     public void button2_clicked(View button) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle(R.string.confirm);
         builder.setMessage(R.string.doYouWantToDelete);
         builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int index) {
                 // 삭제 작업 실행
                 Toast.makeText(AlertsActivity.this, "삭제성공", Toast.LENGTH_SHORT).show();
             }
         });
         builder.setNegativeButton(R.string.no, null);
         AlertDialog dialog = builder.create();
         dialog.show();
     }

    public void button3_clicked(View button) { // (줄 46)
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle(R.string.selectAnimal);
         builder.setSingleChoiceItems(R.array.animals, selectedAnimalIndex, null); // 목록 중 하나만 선택
         builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int index) {
                 // (미리)선택된 항목에 대한 작업 실행
                 ListView listView = ((AlertDialog)dialog).getListView();
                 selectedAnimalIndex = listView.getCheckedItemPosition();
                 String msg = selectedAnimalIndex + " 번째 항목이 선택되었습니다.";
                 Toast.makeText(AlertsActivity.this, msg, Toast.LENGTH_SHORT).show();

                 int mipmapId = 0;
                 switch (selectedAnimalIndex) {
                     case 0: mipmapId = R.mipmap.animal_cat_large; break;
                     case 1: mipmapId = R.mipmap.animal_dog_large; break;
                     case 2: mipmapId = R.mipmap.animal_owl_large; break;
                 }
                 ImageView imageView1 = (ImageView) findViewById(R.id.imageView1); 
                 imageView1.setImageResource(mipmapId);
             }
         });
         builder.setNegativeButton(R.string.cancel, null);
         AlertDialog dialog = builder.create();
         dialog.show();
     }
 }
```
</div>
</details> 

**(줄23)** 빌더를 이용해서 AlertDialog 만들기   
생서자를 한번에 호출해서 객체를 만들기 힘든 경우 (set 해야할게 너무 많을 때) 빌더 패턴을 사용해서 객체를 생성한다.   
 
화면에 간단한 메시지를 보여줘야 할 때 AlertDialog 대화상자를 사용하며,   
AlertDialog 대화상자 객체를 생성할 때, new AlertDialog(); 형태로 구현하지 않는다.   

AlertDialo.Builder 클래스 객체를 먼저 만들고   
AlertDialog.Builder 객체의 메소드를 사용해서 AlertDialog 객체를 생성해야 한다.    

**AlertDialog 객체 생성 과정**
1. ```AlertDialog.Builder builder = new AlertDialog.Builder(this);```    
AlertDialog 대화상자를 만들려면 먼저 AlertDialog.Builder 클래스 객체를 생성해야 한다.    

2. ```builder.setMessage(R.string.saveSuccess);```   
빌더 객체의 serMessage() 메소드를 호출하여 대화 상자에 표시되는 텍스트 메시지를 설정한다.   

3. ```builder.setNeutralButton(R.string.close, null);```     
버튼을 클릭하면, AlertDialog 대화상자가 저절로 닫히게 해주는 메소드    
두번째 파라미터가 null이면 아무일 없이 대화상자를 닫는다는 의미이다.   

4. ```AlertDialog dialog = builder.create();```  
위에서 설정한 형태로 AlertDialog 객체를 생성한다.   

5. ```dialog.show();```  
AlertDialog 대화상자 객체를 화면에 보이게 한다.    

**(줄34)** AlertDialog 대화상자에, 긍정(positive) 버튼을 추가  
```java
builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int index) {
        // 삭제 작업 실행
        Toast.makeText(AlertsActivity.this, "삭제성공", Toast.LENGTH_SHORT).show();
    }
});
```
setPositiveButtond의 첫번째 파라미터는 표시될 문자열 리소스 id이고   
두번째 파라미터는 이 버튼이 클릭되었을 때 실행될 리스너 객체이다.  
이 onClick 메소드가 실행될 때는 이미 메소드는 return된 상태이다.   
그렇기 때문에(outer this 사용 불가) Toast에서 AlertsActivitys.this를 넘겨줘야 하는 것이다.   
 
**(줄46)** radioButton(or checkBox)이 추가된 AlertDialog      
AlertDialog.Builder 클래스 객체를 생성 이후,    

1. ```builder.setSingleChoiceItems(R.array.animals, selectedAnimalIndex, null)```   
대화상자에 보여줄 문자열 배열을 지정한다.   
문자열 중 하나를 선택하고 확인버튼을 누른다.   
- 첫번째 파라미터 : 문자열 리소스 Id  
- 두번째 파라미터 : 대화상자가 뜰 때, 처음으로 선택될 항목 index  
- 마지막 파라미터 : Listener   


**요약**  
dialog 에서  
setTitle 은 알림창 제목  
setMessage 는 알림창 내용   
setPositiveButton 은 확인 버튼과 버튼을 눌렀을 경우 처리   
setNeutralButton 은 취소 버튼과 버튼을 눌렀을 경우의 처리   
setNegativeButton 은 아니요 버튼과 버튼을 눌렀을 경우의 처리   

