# ListView 문자열 목록을 화면에 표시하기  
### 1. 자료 목록 화면 구현   
#### (1) 문자열 목록을 표시하기 위해 필요한 클래스   

ListView, ArrayAdapter<String>, ArrayList<String>  
화면에 문자열 목록을 표시하기 위해, 위의 세 클래스가 필요하다.   


- ListView 클래스  
ListView 클래스 객체는 화면에 문자열 목록을 표시하는 뷰(View) 객체이다.  
좀 더 정확히 말하면, ListView 클래스는 ViewGroup 클래스의 자식 클래스이다.  
ListView 객체는 단지 레이아웃 객체이다. 우리 눈에 보이는 것은, ListView에 채워진 뷰(View) 객체들이다.  

- ArrayList<String> 클래스    
ArrayList<String> 클래스 객체는 화면에 표시할 문자열 목록을 보관하기 위한, 문자열 목록 클래스이다.   
배열 형태의 자료구조 클래스이다.    

- ArrayAdapter<String> 클래스  
ArrayListAdapter<String> 클래스 객체는 ArrayList<String>에 들어있는   
  데이터 목록을 ListView를 통해서 화면에 출력하기 위해 필요한 중간 객체이다.   
ArrayList에 저장할 데이터가 String이므로, ArrayList<String>, ArrayAdapter<String> 타입으로 선언해야한다.  
ArrayAdapter<String> 클래스 객체는, 데이터를 하나씩 가져다가, 뷰(View)객체를 하나씩 생성하여, ListView에 채운다.  

#### (2) Layout Resource XML 파일   
화면에 표시할 뷰(view) 객체들의 배치와 형태를 지정하는 XML 파일을,  
layout resource XML 파일이라고 부른다.  

res/layout/activity_main.xml 파일이 대표적인 layout resource XML 파일이다.    
이 파일의 id는 R.layout.activity_main 이다.  

안드로이드 시스템 내에 포함되어 있는 layout resource XML 파일들도 있다.  
이 파일들의 id는 android.R.layout.XXXXXX 형태이다.  

화면에 표시할 뷰 객체들을 자동으로 생성해야 할 때, layout resource XML 파일이 필요하다.  
  
예들 들어, android.R.layout.simple_list_item_1 는 앞으로 자주 사용하게 될 layout resource XML 파일의 id 이다.    
이 파일은 안드로이드 시스템 내에 포함되어 있다.    
이 파일의 내용에는 문자열 하나를 화면에 표시하기 위한 TextView 한 개가 들어있다.    
  
앞으로 이것을 android.R.layout.simple_list_item_1 리소스 라고 부르자.    

***********

### ListViewActivity.java    
``` ArrayList<String> arrayList; ```    
Java에서 객체의 목록을 저장하는 자료구조로 ArrayList 클래스를 자주 사용한다.   
ArrayList<String> 타입은 저장할 객체의 타입이 String 이라는 뜻이다.   
arrayList 는 멤버변수일 뿐이고 아직 객체는 생성되지 않았다.   
new ArrayList<String>(); 코드를 실행해야 객체가 생성된다.   
  
``` ArrayAdapter<String> adapter; ```  
ArrayAdapter<String> 클래스는 ArrayList<String> 에 들어있는 데이터 목록을 ListView를 통해서 화면에   
출력하기 위해 필요한 중간 객체이다. adapter은 멤버변수일 뿐이고, 아직 객체는 생성되지 않았다.   
  
``` setContentView(R.layout.activity_list_view); ```  
R.layout.activity_list_view 레이아웃 리소스의 내용대로 액티비티 화면을 만들라는 메소드 호출이다.   
이 파일의 내용대로 EditText, Button, ListView 객체가 자동으로 만들어진다.   

``` arrayList = new ArrayList<String>(); ```  
ArrayList<String> 객체를 생성하고 그 객체에 대한 참조를 멤버변수 arrayList 에 대입한다.   
arrayList 변수에 대입된 것은 객체가 아니고 객체에 대한 참조 값이다.   

``` adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item1, arrayList); ```     
ArrayAdapter<String> 객체를 생성하고 그 객체에 대한 참조를 멤버 변수 adapter에 대입한다.       
ArrayAdapter 생성자의 첫번째 파라미터 this는 액티비티의(LIstViewActivity) 객체이다.       
두번째 파라미터는 데이터 항목 하나를 화면에 어떤 형태로 표시할지를 지정하기 위한 layout resource id 이다.     
(화면에 문자열 하나를 표시해야 할 때 이 리소스를 사용한다.)       
세번째 파라미터는 화면에 표시할 문자열 목록에 해당하는 ArrayList<String> 객체이다.     
ArrayAdapter<String> 객체와 ArrayList<String> 객체가 연결된다.       
  
``` ListView listView = (ListView)findViewById(R.id.listView); ```    
이 listView 객체는 setContentView 메소드 호출에 의해서 생성되었다.       

``` listView.setAdapter(adapter); ```      
ListView 객체에 ArrayAdapter<String> 객체를 연결한다.   
ArrayAdapter<String> 객체도 ArrayList<String> 객체와 연결되었으므로   
ListView, ArrayAdapter<String>, ArrayList<String> 3 객체가 연결되었다.   
이제 ArrayList<String> 에 들어있는 데이터 목록이 화면에 표시될 수 있다.     


```java   
b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View arg0) {
        EditText e = (EditText) findViewById(R.id.editText);
        CharSequence s = e.getText();
        e.setText("");
        arrayList.add(s.toString()); // (1)
        adapter.notifyDataSetChanged(); // (2)
    }
});
```
온클릭 리스너(View.OnClickListener) 객체를 하나 생성하여, 버튼 객체에 연결한다.     
이제 버튼이 클릭되면 연결된 온클릭리스너 객체의 onClick 메소드가 즉시 호출될 것이다.      
여기서는 온클릭 리스너를 버튼에 연결만 할 뿐이지, 아직 onClick 메소드는 호출되지 않는다.      

(1) ```arrayList.add(s.toString());```    
EditText에 입력되었던 문자열을 ArrayList<String> 객체에 추가한다.       
이 ArrayList<String> 객체는 ListView와 연결되어 있다.      
ArrayList<String> 객체에 들어있는 문자열 목록은 ListView에 의해 화면에 표시된다.      
따라서 ArrayList<String> 객체에 문자열을 추가하면, 추가된 내용도 화면에 표시된다.      
그런데 ArrayList<String> 객체에 문자열이 추가되거나 삭제된 사실을,      
ListView가 즉시 알지는 못하므로 추가된 내용이 화면에 표시되려면 시간이 좀 걸린다.       
  
(2) ```adapter.notifyDataSetChanged();```           
ArrayList<String> 객체에 들어있는 데이터가 변경되었으니, 즉시 화면을 다시 그리라고       
ListView에게 통보하기 위한 메소드 호출이다.       
ArrayAdapter<String> 클래스의 notifyDataSetChanged 메소드를 호출하면       
데이터 변경 사실이 ListView에게 통보되어 즉시 화면이 다시 그려진다.            
