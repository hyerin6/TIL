package net.skhu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    MyRecyclerViewAdapter myRecyclerViewAdapter;
    ArrayList<Student> arrayList; // 학생 목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<Student>();

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, arrayList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myRecyclerViewAdapter);

        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText e1 = (EditText) findViewById(R.id.editText1);
                EditText e2 = (EditText) findViewById(R.id.editText2);
                String studentNo = e1.getText().toString();
                String name = e2.getText().toString();
                e1.setText("");
                e2.setText("");
                arrayList.add(new Student(studentNo, name));
                myRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recycler_view, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_remove) {
            arrayList.clear(); //  전체 학생 삭제
            myRecyclerViewAdapter.notifyDataSetChanged(); // 리사이클러뷰 새로 그리기
            return true;
        } else if (id == R.id.action_add) {
            alert(); // 학생 생성 대화상자 보이기
        }
        return super.onOptionsItemSelected(item);
    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("학생 등록"); // 대화 상자의 제목 설정하기

        // 대화 상자에 표시될 뷰 객체들을 자동으로 생성함.
        final View rootView = this.getLayoutInflater().inflate(R.layout.student_edit, null);

        final EditText editText_studentNo = (EditText) rootView.findViewById(R.id.editText_studentNo);
        final EditText editText_name = (EditText) rootView.findViewById(R.id.editText_name);

        // 자동으로 생성된 뷰 객체들을 대화상자에 추가한다
        builder.setView(rootView);

        // 대화상자에 '저장' 버튼 추가하는 코드의 시작
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            // 대화상자의 '저장' 버튼이 클릭되면 실행되는 메소드
            public void onClick(DialogInterface dialog, int which) {
                // 대화 상자의 EditText에 입력된 내용을 꺼낸다.
                // editText_studentNo, editText_name outer 메소드의 final 지역 변수이다.
                // outer 메소드의 final 지역 변수는 inner 메소드에서 사용할 수 있다.
                String studentNo = editText_studentNo.getText().toString();
                String name = editText_name.getText().toString();

                // 학생 목록에 학생 추가
                Student student = new Student(studentNo, name);
                arrayList.add(student);

                // 리사이클러뷰 다시 그리기
                myRecyclerViewAdapter.notifyDataSetChanged();
            } // onClick 메소드의 끝
        });
        // 대화상자에 '저장' 버튼을 추가하는 코드의 끝

        builder.setNegativeButton("취소", null); // 대화상자에 '취소' 버튼을 추가하기
        AlertDialog dialog = builder.create(); // 대화상자 객체 생성하기
        dialog.show();
    }

}
