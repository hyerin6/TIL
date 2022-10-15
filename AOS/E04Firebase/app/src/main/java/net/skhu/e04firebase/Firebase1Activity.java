package net.skhu.e04firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase1);

        FirebaseApp.initializeApp(this);

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
        myData01.addValueEventListener(listener1);

        // editText를 final 변수로 outer class에 꺼내놓으면 반복적인 findViewById를 줄일 수 있다.
        final EditText editText = (EditText)findViewById(R.id.editText);

        // 불필요한 변수를 제거하고 lambda expression으로 구현해보기
        Button button = (Button)findViewById(R.id.btnSaveIntoServer);
        button.setOnClickListener( (view) -> myData01.setValue(editText.getText().toString()) );
    }
}
