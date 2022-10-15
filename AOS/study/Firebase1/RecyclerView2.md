# Firebase와 RecyclerView2       

### 1. 효율개선   
#### (1) 변경된 데이터만 전송   
지금까지 구현한 Firebase 예제들은 앱의 데이터가 수정되면, 데이터 전체를 서버에 전송한다.   
그리고 서버의 데이터가 변경되면 데이터 전체를 앱에 전송한다.   
언제나 데이터 전체를 전송하기 때문에 비효율적이다. 변경된 데이터만 전송되게 구현하자.   
 
#### (2) String key   
Firebase 서버 데이터베이스의 데이터 항목에는 String 기본키가 자동으로 부여된다.     
변경된 데이터만 전송할 때, 이 키를 사용해서 데이터 항목을 식별한다.      

이 키의 역할은 관계형 데이터베이스의 primary key의 역할과 같다.   
서버의 데이터를 삭제하거나 수정할 때, 이 키가 필요하다.       

#### (3) ChildEventListener 구현하기   
FirebaseDbService.java 생성       
작업이 커지면 service class로 빼줘야 한다.        
```java

public class FirebaseDbService implements ChildEventListener {

    RecyclerView2Adapter recyclerView2Adapter;
    ArrayList<Item2> itemList; // RecyclerView에 표시할 데이터 목록
    DatabaseReference databaseReference;

    public FirebaseDbService(Context context, RecyclerView2Adapter recyclerView2Adapter, ArrayList<Item2> itemList) {
        this.recyclerView2Adapter = recyclerView2Adapter;
        this.itemList = itemList; // RecyclerView에 표시할 데이터 목록
        databaseReference = FirebaseDatabase.getInstance().getReference("myData03");
        databaseReference.addChildEventListener(this);
    }

    public void addIntoServer(Item2 item) {
        // 새 기본 키(primary key)를 생성한다.
        String key = databaseReference.push().getKey();
        item.setKey(key); // item 객체에 key를 채운다

        // 새 기본 키로 데이터를 등록한다.
        // 서버에서 key 값으로 dataItem 값이 새로 등록된다.
        databaseReference.child(key).setValue(item); // 특정 child만 set, child가 없으면 통째로 바뀜
    }

    public void removeFromServer(int index) {
        // 서버에서 데이터를 delete 한다.
        // 서버에서 key 값으로 등록된 데이터가 제거된다.
        String key = itemList.get(index).getKey();
        databaseReference.child(key).removeValue();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        // DB에 새 데이터 항목이 등록되었을 때, 이 메소드가 자동으로 호출된다.
        // dataSnapshot은 서버에 등록된 새 데이터 항목이다.
        String key = dataSnapshot.getKey(); // 새 데이터 항목의 키 값을 꺼낸다.
        Item2 item = dataSnapshot.getValue(Item2.class);  // 새 데이터 항목을 꺼낸다.
        if (key.equals(item.getKey()) == false)
            Log.e("로그", "key값 불일치 오류");

        itemList.add(item); // 새 데이터를 itemList에 등록한다.
        recyclerView2Adapter.notifyDataSetChanged(); // RecyclerView를 다시 그린다.
    }

    // itemList에서 key 값을 찾아서 index를 리턴한다.
    private int findKey(String key) {
        for (int i = 0; i < itemList.size(); ++i)
            if (key.equals(itemList.get(i).getKey()))
                return i;
        return -1;
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        // DB의 어떤 데이터 항목이 수정되었을 때, 이 메소드가 자동으로 호출된다.
        // dataSnapshot은 서버에서 수정된 데이터 항목이다.
        String key = dataSnapshot.getKey();  // 수정된 데이터 항목의 키 값을 꺼낸다.
        Item2 item = dataSnapshot.getValue(Item2.class); // 수정된 데이터 항목을 꺼낸다.
        if (key.equals(item.getKey()) == false)
            Log.e("로그", "key값 불일치 오류");

        // key 값의 데이터 항목을 찾아서 수정한다.
        int index = findKey(key);
        if (index >= 0) // 못찾으면 -1 
            itemList.set(index, item); // 수정된 데이터를 대입한다 (overwrite)
        recyclerView2Adapter.notifyDataSetChanged(); // RecyclerView를 다시 그린다.
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        // DB의 어떤 데이터 항목이 삭제 되었을 때, 이 메소드가 자동으로 호출된다.
        // dataSnapshot은 서버에서 삭제된 데이터 항목이다.
        String key = dataSnapshot.getKey(); // 삭제된 데이터 항목의 키 값을 꺼낸다.

        // key 값의 데이터 항목을 찾아서 삭제한다.
        int index = findKey(key);
        if (index >= 0) // 못찾으면 -1 
            itemList.remove(index); // itemList에서 그 데이터 항목을 삭제한다.
        recyclerView2Adapter.notifyDataSetChanged(); // RecyclerView를 다시 그린다.
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        // DB의 어떤 데이터 항목의 위치가 변경되었을 때, 이 메소드가 자동으로 호출된다.
        // 데이터 이동 기능을 구현하지 않을 것이기 때문에, 이 메소드를 구현하지 않는다.
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Firebase DB에서 에러가 발생했을 때, 이 메소드가 자동으로 호출된다.
        Log.e("Firebase Error", databaseError.getMessage());
    }
}

```

### 2. Firebase3Activity 생성      
- 저장 버튼 클릭    

(1) OK 버튼이 클릭되면, 입력된 문자열로 Item 객체를 생성하고  
(2) 생성된 Item 객체를 서버에 저장한다.   
``` firebaseDbService.addIntoServer(item); ```    
(3) 거버에 데이터가 추가되면, FirebaseDbService 클래스의 onChildAdded 메소드가 자동 호출되고,  
서버에 추가된 Item2 객체가 app에 전달된다.        
```java
String key = dataSnapshot.getKey();
Item2 item = dataSnapshot.getValue(Item2.class);
``` 
(4) 서버에 추가된 데이터 항목을 이 앱의 itemList 목록에도 추가한다.      
``` itemList.add(item); ```  
(5) 리사이클러 뷰가 다시 그려지도록 강제한다.   
``` recyclerView2Adapter.notifyDataSetChanged(); ```   

- 삭제 메뉴 클릭   

(1) 삭제 메뉴가 클릭되면, 체크 박스가 체크된 항목들을 서버에 삭제한다.    
(2) 서버의 데이터가 삭제되면,   
FirebaseDbService 클래스의 onChildDeleted 메소드가 자동 호출되고  
서버에서 삭제된 항목의 키가 전달된다.   
(3) 서버에서 삭제된 항목을 이 app의 itemList 목록에서도 삭제한다.    
```java
int index = findKey(key);
if(index >= 0)
	itemList.set(index, item);
```
(4) 리사이클러 뷰가 다시 그려지도록 강제한다.       

