# Firebase 와 RecyclerView           

### Firebase2Activity 화면 만들기     
Item2 객체 목록을 Firebase 데이터베이스에 저장하자.      

## Firebase2Activity.java       
### 실행순서   
**App 실행되면,**   
(1) 버튼에 리스너 객체 등록   
(2) DataReference 객체에 리스너 객체를 등록한다.   

**사용자가 EditText에 문자열을 입력하고 버튼을 클릭하면,**      
(1) 버튼에 등록된 리스너 객체의 onClick 메소드가 호출된다.   
(2) EditText에 입력된 문자열을 꺼낸다.   
(3) arrayList 멤버변수 객체에 Item 객체를 추가한다.   
(4) myData02.setValue(s); 를 실행하여     
arrayList 멤버변수 객체를 Firebase 서버의 "myData02" 항목에 저장한다.   

**Firebase 서버의 데이터에 새 데이터가 저장되면,**             
(1) DatabaseReference 객체에 등록된 리스너 객체의 onDataChange 메소드가 호출된다.   
(2) 데이터 목록 전체 (ArrayList<Item2> 객체)를 받는다.   
이 데이터 목록 객체는 arrayList 멤버 변수 객체와 별도의 새 객체이다.   
(3) 이 새 객체의 값을 arrayList 멤버 변수 객체에 채운다.   
(4) 데이터 값이 변경되었으니 RecyclerView 가 다시 그려지도록 강제한다.   

**사용자가 메뉴를 클릭하면,**       
(1) 체크된 항목들을 arrayList 멤버변수 객체에서 제거한다.   
(2) myData02.setValue(s); 실행하여,   
arrayList 멤버변수 객체를 Firebase 서버의 "myData02" 항목에 저장한다.   


**code**    
```java
myData02 = FirebaseDatabase.getInstance().getReference("myData02");
ValueEventListener listener1 = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<Item2>> typeIndicator = new GenericTypeIndicator<List<Item2>>() {};
        List<Item2> temp = dataSnapshot.getValue(typeIndicator);
        if (temp != null) {
            arrayList.clear();

            // 줄 73, 74는 아래 두 줄로 구현해도 된다.
            // arrayList.addAll(temp);
            // recyclerView2Adapter.notifyDataSetChanged();
            recyclerView2Adapter.arrayList = (ArrayList<Item2>) temp;
            arrayList = (ArrayList<Item2>) temp;
            Log.e("내태그", arrayList.toString());
        }
    }
    @Override
    public void onCancelled(DatabaseError error) {
        Log.e("내태그", "서버 에러: ", error.toException());
    }
};
```