# RecyclerView2     

### 1. RecyclerView2Adapter 화면 만들기     
**(1) 개요**  
RecyclerView1Adapter 에서는 RecyclerView를 이용하여 문자열 목록을 화면에 표시했다.   
즉, 데이터 항목은 문자열 하나이다.   
이번에는 데이터 항목에 문자열과 시각을 표시해보자.   
그러기 위해서는 데이터 항목에 해당하는 클래스를 구현해야 한다. (Item2.java)  

**(2) RecyclerView 라이브러리 등록 확인하기**         
app 폴더 아래의 build.gradle 파일에서 dependencies 항목은             
프로젝트에 필요한 라이브러리 목록이다.     

RecyclerView를 사용하려면 필요한 라이브러리를 여기에 등록해야 한다.     
     
RecyclerView 객체를 드래그 드롭할 때, RecyclerView 라이브러리가 프로젝트 설정 파일에 자동으로 등록된다.   
하지만 레이아웃 리소스 xml 파일의 소스코드에 RecyclerView 항목을 직접 입력하거나 붙여 넣을 때는      
필요한 라이브러리가 자동으로 등록되지 않기 때문에 직접 등록해줘야 한다.       

**(3) item2.xml 파일 생성**    
LinearLayout의 layout_hight 값이 warp_content로 설정되어야 데이터 객체들이 보인다.                    
Match_parent 이면 객체가 한 화면에 하나씩 밖에 안보인다.        

**(4) Item2.java 생성**      
시각을 나타내는 데이터를 담을 DTO class가 필요하다.       
데이터 항목에 해당하는 클래스를 구현하자.         

```java
package net.skhu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item2 {
    final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String title;
    Date date;
    
    ...
    
}
```
date 값으로부터 "2018-11-15 13:20:35" 형태의 문자열을 생성하기 위해        
SimpleDateFormat 클래스를 사용하여 구현하였다.        

**(5) RecyclerView2Adapter.java 생성**      

<details>
<summary>CODE</summary>
<div markdown="1">
     
```java
public class RecyclerView2Adapter extends RecyclerView.Adapter<RecyclerView2Adapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2; // 데이터 항목을 채울 view를 멤버변수로 꺼내놓기 

        public ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }

        public void setData() {
            Item2 item = arrayList.get(getAdapterPosition()); // index를 알 수 있는 ViewHolder의 메소드   
            textView1.setText(item.getTitle());
            textView2.setText(item.getDateFormatted());
        }
    }

    LayoutInflater layoutInflater;
    ArrayList<Item2> arrayList;

    public RecyclerView2Adapter(Context context, ArrayList<Item2> arrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.setData();
    }
}
```

</div>
</details>


**(6) RecyclerView2Activity.java 수정**   
```java
public class RecyclerView2Activity extends AppCompatActivity {

    RecyclerView2Adapter recyclerView2Adapter;
    ArrayList<Item2> arrayList; // * 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view2);

        arrayList = new ArrayList<Item2>(); // * 
        arrayList.add(new Item2("one", new Date()));
        arrayList.add(new Item2("two", new Date())); // * 

        recyclerView2Adapter = new RecyclerView2Adapter(this, arrayList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerView2Adapter);

        Button b = (Button)findViewById(R.id.btnAdd);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText e = (EditText) findViewById(R.id.editText);
                String s = e.getText().toString();
                e.setText("");
                arrayList.add(new Item2(s, new Date())); // * 
                recyclerView2Adapter.notifyDataSetChanged();
            }
        });
    }
}
```
**&#42;** 이 부분은 문자열 목록만 표시할 떄의 액티비티와 다른 코드 부분을 표시해둔 것이다.       


### 3. RecyclerView 항목 클릭 기능 구현   
**(1) RecyclerView2Adapter.java**     

<details>
<summary>CODE</summary>
<div markdown="1">

```java
public class RecyclerView2Adapter extends RecyclerView.Adapter<RecyclerView2Adapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView1, textView2;

        public ViewHolder(View view) { 
            super(view);
            textView1 = view.findViewById(R.id.textView1); // 예제에서 view : 리니어 레이아웃이 넘어옴
            textView2 = view.findViewById(R.id.textView2);
            view.setOnClickListener(this);
        }

        public void setData() {
            Item2 item = arrayList.get(getAdapterPosition());
            textView1.setText(item.getTitle());
            textView2.setText(item.getDateFormatted());
        }

        @Override
        public void onClick(View view) {
            Item2 item = arrayList.get(super.getAdapterPosition());
            String s = String.format("index: %d,  title: %s", super.getAdapterPosition(), item.getTitle());
            Toast.makeText(view.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    LayoutInflater layoutInflater;
    ArrayList<Item2> arrayList;

    public RecyclerView2Adapter(Context context, ArrayList<Item2> arrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item2, viewGroup, false); // *
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.setData();
    }
}
```

</div>
</details>

```View view = layoutInflater.inflate(R.layout.item2, viewGroup, false);```       
여기서 view는 레이아웃 인플레이션의 결과임. 뭔지 알고싶으면 item2.xml을 열어보면 된다. (예제에서는 뷰 3개임)     

- ViewHolder 클래스에 View.OnClickListener 인터페이스를 구현한 이유   
지금까지 anonymous inner class 문법을 사용하여 리스너 클래스를 구현했는데,   
반드시 그래야 하는 것은 아니다.   
리스너 클래스는 그냥 평범한 클래스로 구현해도 된다.   
그리고 기존의 클래스들 중 하나가 리스너 클래스의 역할도 같이 담당해도 된다.   

RecyclerView에서 데이터 항목 리스너 객체는 데이터 항목 뷰 객체마다 하나씩 달려있어야 한다.     
즉, RecyclerView에서 데이터 항목 리스너 객체 수는 데이터 항목 뷰 객체의 수와 일치해야 한다.   

데이터 항목 뷰 객체란,          
RecyclerView2Adapter 클래스의 onCreateViewHolder 메소드에서 layout inflation으로 생성된 뷰 객체이다.      
```View view = layoutInflater.inflate(R.layout.item2, viewGroup, false);```   

이 데이터 항목 뷰 객체는 ViewHolder 객체에 들어있다.       
```return new ViewHolder(view);```    

즉, 데이터 항목 뷰 객체의 수와 ViewHolder 객체의 수가 일치한다.   

**ViewHolder 객체의 수 = 데이터 항목 뷰 객체의 수 = 데이터 항목 리스너 객체의 수**    
위 세 객체의 수는 동일해야 한다.   

따라서 리스너 클래스를 새로 구현해서 리스너 객체를 생성하는 것 보다   
이미 생성된 ViewHolder 객체들이 리스너 객체의 역할도 같이 담당하는 것이 효율적이다.    


- 기존 클래스에 View.OnClickListener 인터페이스 구현하기       
1. implements View.OnClickListener 이 필요     
2. onClick 메소드 재정의    
3. 데이터 항목 뷰 객체에 리스너 객체를 등록해야 한다.            

- viewHolder의 역할   
매번 findViewById로 찾는 것은 비효율적이다.     
viewHolder에 만들어 놓고 가져다쓰면 효율적이다.     
각종 동작을 처리하기 위한 메소드도 ViewHolder에 정의한다.      


### 4. 삭제 기능 구현   
**(1) item2.xml**    
item2.xml 레이아웃 파일에서 루트 항목은 LinearLayout이다.        
LinearLayout 객체가 루트 항목일 경우에 자식 뷰 객체들을 일려로만 배치할 수 있다.   
그래서 TextView 옆에 Checkbox를 추가하기 어렵다.    

**(2) item2a.xml**                        
constraintLayout으로 item2a.xml 생성      


**(3) Item2.java 생성**    
boolean 타입의 get 메소드 이름은 get으로 시작해도 되지만, is로 시작하는 것이 더 바람직하다.      
즉, getCheckd/setChecked 도 가능하지만, isChecked/setChecked 가 더 바람직하다.    

**(4) RecyclerView2Adapter.java 생성**   
- 체크박스 리스너 구현    
체크박스를 클릭하여 체크 상태가 변경된 경우에, Item 데이터 항목 객체의 checked 속성에 이 값을 저장해야 한다.   
그러려면 체크박스에 OnCheckedChangedListener 객체를 등록해야 한다.   

체크박스의 수와 리스너 객체의 수가 정확하게 일치해야 바람직 하므로 ViewHolder 객체가 리스너 객체의 
역할도 동시에 담당하는 것이 바람직하다. 그래서 ViewHolder 클래스에 OnCheckedChangedListener 인터페이스를 구현하였다.  

화면에 보이는 체크박스와 Item2 객체의 멤버변수(DTO 객체의 값)의 체크 상태가 같아야 하는데,   
화면에 보이는 체크박스의 값이 변경되었다고 해서 멤버변수의 값이 자동으로 변경되는 것이 아니다.   
하지만, 이 둘의 값은 같아야 한다.    

- ViewHolder가 할 일    
1. 참조 미리 꺼내놓기        
2. 데이터 항목에 대한 리스너 구현       
3. 데이터 채우는 메소드 구현                     

- Adapter가 할 일          
1. 데이터 목록 하나를 보여주기 위한 뷰 객체 생성 -> layout inflation      
2. 그 데이터 값 채우기        
3. ViewHolder 만들기       

**(5) RecyclerView2Adapter.java 수정**         
선택된 항목 삭제하기 구현   
       
```java 
ListIterator<Item2> iterator = arrayList.listIterator();
while (iterator.hasNext())
    if (iterator.next().isChecked())
        iterator.remove(); // 삭제 
recyclerView2Adapter.notifyDataSetChanged(); // 화면이 다시 그려진다. 
``` 


### 5. 메뉴 상태 변경       
체크가 되어 있을 떄만, 삭제 메뉴가 보이도록 한다.       

```java
boolean invalidateMenu = false;
if (isChecked) invalidateMenu = (++checkedItemCount == 1);
else invalidateMenu = (--checkedItemCount == 0);
if (invalidateMenu)
    ((Activity) textView1.getContext()).invalidateOptionsMenu();
```

체크박스의 체크상태가 변경될 때마다 이 코드가 실행된다.      
체크가 한개 켜지면, checkedItemCount++ 한개 꺼지면, checkedItemCount-- 한다.    

checkedItemCount 값이 0에서 1로 혹은 1에서 0으로 변경될 때 invalidatMenu 변수 값이 true가 된다.     
invalidatMenu 변수 값이 true이면, 액티비티 메뉴가 다시 생성되도록 구현했다. (invalidatMenu 메소드)     
액티비티의 onCreateOptionsMwnu 메소드가 자동으로 호출되어, 메뉴를 보일지 말지 결정한다.       


**(2) RecyclerView2Activity.java 수정**        

```java
MenuItem menuItem = menu.findItem(R.id.action_remove);
menuItem.setVisible(recyclerView2Adapter.checkedItemCount > 0);
```

체크박스가 체크된 항목이 1개 이상인 경우에만 삭제 메뉴가 보이게 한다.       

### 6. 삭제 확인 대화상자      
**(1) RecyclerView2Activity.java 수정**         

```java
private void deleteItems() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.doYouWantToDelete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // '예'
            @Override
            public void onClick(DialogInterface dialog, int index) {
                ListIterator<Item2> iterator = arrayList.listIterator();
                while (iterator.hasNext())
                    if (iterator.next().isChecked())
                        iterator.remove(); // 삭제
                recyclerView2Adapter.notifyDataSetChanged(); // 화면 다시 그리기
            }
        }); 
        builder.setNegativeButton(R.string.no, null); // '아니오'
        AlertDialog dialog = builder.create();
        dialog.show();
    }
```

