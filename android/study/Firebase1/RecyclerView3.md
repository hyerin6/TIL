# Firebase와 RecyclerView 3   
### 1. RecyclerView 다시 그리기 개선     
#### (1) 개요    
데이터가 변경되었을 때, RecyclerViewAdapter 클래스의 notifyDataSetChanged 메소드를 호출하면,   
RecyclerView가 다시 그려진다.     
그런데 이때 변경된 부분만 다시 그려지는게 아니라 전체가 다시 새로고침이 된다.   
변경된 부분만 그려질 수 있도록 개선하자.      

```java 
recyclerView2Adapter.notifyItemInserted(index);
recyclerView2Adapter.notifyItemChanged(index);
recyclerView2Adapter.notifyItemRemoved(index);
```     
index 항목만 다시 그려진다.    

### 2. startActivityForResult       
#### (1) startActivityForResult 호출하기   
``` activity.startActivityForResult(intent, Item2EditActivity.REQUEST_EDIT); ```    

작업결과를 받기를 기대하면서 Item2EditActivity 액티비티를 호출한다.   
호출된 액티비티는 작업 결과를 리턴해야 한다.   

두번째 파라미터는 상수이다.   
이 값은 어떤 요청인지 자세히 구분하기 위해 사용한다.     


ex
```java
public static final int REQUEST_EDIT = 1;
public static final int REQUEST_CREATE = 2;
```

같은 activity인데 어떤 작업을 하는지 구분하기 위해 두번째 파라미터를 사용한다.   
구분할 필요가 없는 상황에서는 0을 보내면 된다.      


#### (2) 호출 결과 리턴      
호출된 액티비티는(Item2EditActivity) 작업 결과를 리턴해야 한다.    

- 작업 성공    
```java
// 내용이 수정된 item 객체를 intent 객체에 넣어서 리턴한다.  
Intent intent = new Intetn();
intent.putExtra("item", item);
setResult(Activity.RESULT_OK, intent); // RESULT_OK - 작업 완료 코드 
finish(); // 호출된 액티비티를 종료하고 호출한 액티비티로 화면이 돌아간다.   
```

- 작업 실패   
```java
Intent intent = new Intent();
setResult(Activity.RESULT_CANCELED, intent); // RESULT_CANCELED - 작업 실패 코드 
finish();
```


#### (3) 호출 결과 받기     
호출된 액티비티에서 finish() 메소드가 호출되면,   
호출된 액티비티는 종료되고 호출한 액티비티로 화면이 돌아간다.     
그리고 호출한 액티비티의 onActivityResult 메소드가 호출된다.      


```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if(requestCode == Item2EditActivity.REQUEST_EDIT) {
        if (resultCode == RESULT_OK) {
            Item2 item = (Item2)intent.getSerializableExtra("item");
            firebaseDbService.updateServer(item);
        }
    }
}
```

**onActivityResult 메소드의 파라미터**    
- requestCode : startActivityForResult 메소드를 호출할 때, 두번째 파라미터로 전달한 int 상수   
- resultCode : 작업 완료 코드(RESULT_OK, RESULT_CANCELED)    


```Item2 item = (Item2)intent.getSerializableExtra("item");```              
호출된 액티비티가 리턴한 데이터 객체를 intent에서 꺼낸다.            



### 3. 수정 기능 구현        
Item2 객체 내용을 수정할 수 있도록 setTitle, setDataFormatted 메소드를 구현한다.     
액티비티들 사이에 Item2 객체가 전달될 수 있도록 Serializable 인터페이스를 implements 한다.      
get메소드의 리턴 타입과 set메소드의 파라미터 타입이 반드시 일치해야 한다.        

### RecyclerView2Adapter.java 수정        
```java
@Override
public void onClick(View view) { 
	Firebase3Activity activity = (Activity)view.getContext(); // (1)
	Item2 item = arrayList.get(getAdapterPosition()); // (2)
	Intent intent = new Intent(activity, Item2EditActivity.class); // (3)
	intent.putExtra("item", item); // (4)
	activity.startActivityForResult(intent, Item2EditActivity.REQUEST_EDIT); // (5)
}
```

(1) view 클래스의 getContext() 메소드는 뷰 객체가 들어있는 액티비티 객체를 리턴한다.     
	**Q.** 액티비티가 아닌데 어떻게 뷰를 가져올까?    
	**A.** View.getContext()로 view를 가져올 수 있다.         

(2) ViewHolder 클래스의 getAdapterPosition() 메소드는 현재 데이터 항목의 index를 리턴한다.   

(3) Item2EditActiviry 액티비티를 호출하기 위한 Intent 객체 생성   

(4) 액티비티를 호출하면서 파라미터로 전달할 객체를 intent에 넣는다.   
이 객체는 Serializable 인터페이스를 implements 해야 한다.        

(5) 작업 결과를 받기를 기대하면서, Item2EditActivity 액티비티를 호출한다.     
호출된 액티비티는 작업결과를 리턴해야 한다.   



### Firebase3Activity.java         
호출된 액티비티에서 finish() 메소드가 호출되면, 호출된 액티비티는 종료되고 호출한 액티비티로 화면이 돌아간다.              
그리고 호출한 액티비티의 onActivityResult 메소드가 호출된다.            

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if(requestCode == Item2EditActivity.REQUEST_EDIT) {
        if (resultCode == RESULT_OK) {
            Item2 item = (Item2)intent.getSerializableExtra("item");
            firebaseDbService.updateServer(item);
        }
    }
}
```

**onActivityResult 메소드의 파라미터**
- requestCode : startActivityForResult 메소드를 호출할 때, 두 번째 파라미터로 전달한, int 상수
- resultCode : 작업 완료 코드 (RESULT_OK, RESULT_CANCELED)

``` Item2 item = (Item2)intent.getSerializableExtra("item"); ```     
호출된 액티비티가 리턴한 데이터 객체를 intent에서 꺼낸다.    

