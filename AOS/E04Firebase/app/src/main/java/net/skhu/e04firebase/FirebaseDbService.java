package net.skhu.e04firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        databaseReference.child(key).setValue(item);
    }

    public void updateServer(Item2 item) {
        // 서버 데이터를 update 한다.
        String key = item.getKey();
        databaseReference.child(key).setValue(item);
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
        int index = itemList.size() - 1;
        recyclerView2Adapter.notifyItemInserted(index); // RecyclerView를 다시 그린다.
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
        if (index >= 0) {
            itemList.set(index, item); // 수정된 데이터를 대입한다 (overwrite)
            recyclerView2Adapter.notifyItemChanged(index); // RecyclerView를 다시 그린다.
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        // DB의 어떤 데이터 항목이 삭제 되었을 때, 이 메소드가 자동으로 호출된다.
        // dataSnapshot은 서버에서 삭제된 데이터 항목이다.
        String key = dataSnapshot.getKey(); // 삭제된 데이터 항목의 키 값을 꺼낸다.

        // key 값의 데이터 항목을 찾아서 삭제한다
        int index = findKey(key);
        itemList.remove(index); // itemList에서 그 데이터 항목을 삭제한다.
        recyclerView2Adapter.notifyItemRemoved(index); // RecyclerView를 다시 그린다.
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
