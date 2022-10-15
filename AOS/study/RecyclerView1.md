# RecyclerView1   

### 1. RecyclerView 구현 방법     
**(1) 문자열 목록을 표시하기 위해 필요한 클래스**     
- RecyclerView 클래스   
RecyclerView 클래스 객체는 화면에 문자열 목록을 표시하는 뷰(View) 객체이다.   
좀 더 정확히 말하자면, RecyclerView 클래스는 viewGroup 클래스의 자식 클래스이다.   
RecyclerView 객체는 단지 레이아웃 객체이다. 우리 눈에 보이는 것은 RecyclerView에 채워진 뷰(View) 객체들이다.   
ListView에 비해 코드는 길어졌지만, 메모리 부분에서 성능이 더 좋다.   

- RecyclerViewAdapter 클래스   
이 클래스는 ArrayList<String> 객체와 RecyclerView 객체를 연결해주는 중간 객체이다.   
좀 더 자세히 말하자면, RecyclerViewAdapter 객체는 ArrayList<String> 객체에서 데이터를 한 개씩 가져다가   
이 데이터를 화면에 표시하기 위한 뷰 객체를 생성해서 이것을 RecyclerView 객체에 전달한다.   
뷰를 만든다. = layout inflation   
Adapter은 layout inflation 해야한다.   

- ArrayList<String> 클래스   
  
**(2) layout resource xml 파일을 구현해야 한다.**      
RecyclerViewAdapter가 뷰 객체들을 생성할 때, layout inflation 기능을 활용한다.   
layout inflation 기능은,   
layout resource xml 파일의 내용대로 뷰 객체들을 자동으로 생성해 주는 기능이다.   
데이터 항목 한 개를 화면에 표시하기 위한 뷰 객체와 그 속성을 layout resource 파일에 정의해야 한다.   
즉, 데이터 항목 한 개에 해당하는 layout resource 파일을 만들어야 한다.   

&#42; 레이아웃 클래스    
지금까지 우리가 화면 배치(layout)를 편집한 것은,   

ConstrainLayout 클래스의 기능을 사용하여 편집한 것이다.     
안드로이드에서 여러 뷰 객체들을 자식으로거느리면서, 자식 뷰 객체들을 화면 배치를 관리하는 클래스를 레이아웃 클래스라고 부른다.   

ConstraintLayout 클래스는 가장 대표적인 레이아웃 클래스이다.   
ConstraintLayout 클래스는 상하좌우 constraint 연결선으로 배치를 결정한다.   

**(3) item1.xml**   
화면에 표시할 데이터 항목 한 개를 화면에 표시하기 위해서, 뷰 객체를 생성해야 한다.   
Menu : New - Layout Resource File     
Root element 항목에 LinearLayout 클래스를 선택    

&#42; LinearLayout 클래스   
LinearLayout 클래스는 자식 뷰 객체들을 단순하게 일렬로 배치한다.         

&#42; TextView 추가        
왼쪽 아래 Component Tree 창에, LinearLayout 객체의 자식 목록에     
TextView 객체가 추가되었다.   

이 화면에 TextView 하나만 있는걸까?     
눈에 보이는 뷰는 TextView 하나지만 하나 더 있다.   
디폴트로는 화면을 하나 만들면 root에 ConstraintLayout(Component Tree)가 있다.   
하지만 이 예제에서는 LinearLayout을 선택했기 때문에 ConstraintLayout이 아니라 LinearLayout이다.   
그렇기 때문에 위의 질문에서의 답은 '2개' 이다.     

**(4) RecyclerView Adapter 구현**   

![carbon](https://user-images.githubusercontent.com/33855307/68952808-e3eb6380-0803-11ea-96d0-8a6ff31654a9.png)

**Q.** (1) 에서 왜 두번째 파라미터로 ViewGroup이 들어갈까?    
**A.** 여기서 자식 view를 하나 만들고 있는데 만드는 과정에서 ViewGroup은 자식을 거느리는 담당이다.   
(배치나 크기를 결정하는 역할, 레이아웃 부모가 결정)   
레이아웃을 결정하는 부모 즉, ViewGroup이 필요하다.   
부모 레이아웃 = ViewGroup   

**Q.** View를 만드는 것과 데이터를 채우는게 왜 따로 구현되어 있을까?           
**A.** RecyclerView = 재활용 뷰   
ListView 보다 메모리 성능이 좋다고 하는데 무엇을 재활용하길래 더 좋다고 하는걸까? View를 재활용한다.   
데이터 항목이 1000개라고 할 때, 뷰도 1000개가 필요할까? 아니다.   
스크롤할 때 뷰가 필요없어지기 때문에 필요없어진 뷰를 재활용하면 된다.   

- ViewHolder 클래스         
ViewHolder 객체는 내부에 뷰 객체를 보유하고 있어야 한다.             
이 뷰 객체는 데이터 항목 항개를 화면에 보여주기 위한 뷰 객체이다.          
이 데이터 항목 뷰 객체에 대한 작업을 구현해야 한다면, ViewHolder 클래스의 메소드로 구현한다. (ex. 클릭)             
ViewHolder 클래스는 보통 Adapter 클래스의 inner class 이므로 outer 클래스인 MyRecyclerViewAdapter 클래스의         
멤버변수인 arrayList 변수를 사용할 수 있다.     </br>            
inner class를 만드는 대부분의 목적은 outer class의 this(멤버변수나 메소드 사용)를 사용하기 위함이다.         
그런데 이 예제에서는 static inner class로 구현되었다. static inner class는 outer class의 this를 사용할 수 없다. </br>             
**Q.** 그럼 왜 innerclass로 만들었는가?         
**A.** 어떤 폴더 안에 서브 폴더를 만드는 의미와 비슷하다.                     
폴더 안에 폴더 생성은 과제안의 백업 폴더 플젝안의 백업 폴더 이렇게 가능 하지만              
Innerclass가 아닌 경우, ‘과제백업폴더’, ‘플젝백업폴더’ 이런식으로 이름이 중복안되게 만들어 줘야 한다.       
즉, static innerclass의 용도는 이 정도..           
안에서만 사용하니까 헷갈리지 않게 innerclass로 만들어주자는 의미이다.                 

- ViewHolder 클래스의 getAdapterPosition() 메소드        
viewHolder 클래스의 getAdapterPosition() 메소드는 viewHolder에 채워진 데이터 항목의 index 값을 리턴한다.   

- onCreateViewHolder 메소드    
데이터 항목 뷰 객체를 생성하고 ViewHolder 객체를 생성해서 리턴한다.   

- onBindViewHolder 메소드    
ViewHolder 객체 내부의 데이터 항목 객체에 데이터를 채운다.   

- findViewById() 메소드     
여태까지 봤던 findViewById 메소드의 클래스는 액티비티였다.    
하지만 이 예제에서는 view의 메소드이다. 액티비티는 뷰를 화면 전체에서 찾지만     
View의 findViewById는 화면 전체가 아니라 그 뷰의 자식들에서만 찾는다.      

- template method 패턴   
getItemCount, onCreateViewHolder, onBindViewHolder 메소드들은 간단한 작업을 구현한 단위 작업 메소드들이다.   
이 메소드들을 호출하는 소스코드는 라이브러리의 RecyclerView에 구현되어 있다. 이 메소드 호출은 다형성 호출이라서,    
프로그래머가 구현한 RecyclerView.Adapter 클래스의 자식 클래스의 메소드가 호출된다. (template method 패턴 구조)      
언제나 동일한 작업 절차의 지휘 통제 메소드는 부모 클래스에 구현되어 있고,      
매번 달라지는 단위 작업 메소드는 자식 클래스에서 재정의한다.        
부모 클래스의 지휘 통제 메소드가 단위 작업 메소드를 호출하는 것은 다형성 호출이다.   
GUI는 template method 패턴을 광범위하게 사용한다.   
GUI 앱을 구현할 때 부모 클래스를 상속해서 몇몇 메소드를 재정의한다.   
재정의된 메소드를 호출하는 소스코드는 라이브러리에 들어있다.     

**(5) RecyclerView1Activity.java**  

```java
arrayList = new ArrayList<String // 문자열 목록 
arrayList.add("one");
arrayList.add("two");

recyclerView1Adapter = new RecyclerView1Adapter(this, arrayList); // RecyclerView1Adapter은 전역 변수로 선언 
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // 데이터 항목 구분하는 회색줄
recyclerView.setLayoutManager(new LinearLayoutManager(this)); // LayoutManager를 통해서 어떤 형식으로 띄울것인지 결정
recyclerView.setItemAnimator(new DefaultItemAnimator()); // 아이템 항목이 추가, 제거되거나 정렬될때 애니메이션 처리
recyclerView.setAdapter(recyclerView1Adapter);

Button b = (Button)findViewById(R.id.btnAdd);
b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View arg0) {
        EditText e = (EditText) findViewById(R.id.editText);
        String s = e.getText().toString();
        e.setText("");
        arrayList.add(s);
        recyclerView1Adapter.notifyDataSetChanged();
   }
});
```

``` recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); ```  
데이터 항목 하나 하나 나눌 때, 사이에 회색 구분선이 생긴다.   

``` recyclerView.setLayoutManager(new LinearLayoutManager(this)); ```    
RecyclerView 생성 시, 반드시 있어야 한다.   
이를 통해 모든 아이템의 뷰의 레이아웃을 관리한다.     
LinearLayoutManager - 수평/수직의 스크롤 리스트    

``` recyclerView.setItemAnimator(new DefaultItemAnimator()); ```    
View 애니메이션 처리       

