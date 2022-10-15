package net.skhu.e04firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Item2EditActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT = 1;
    public static final int REQUEST_CREATE = 2;

    EditText editText_title; // 제목을 수정할 EditText
    EditText editText_date;  // 날짜를 수정할 EditText
    Item2 item; // 수정할 EditText 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item2_edit);

        editText_title = findViewById(R.id.editText_title);
        editText_date = findViewById(R.id.editText_date);

        // 수정할 Item2 객체를 Intent에서 꺼낸다.
        item = (Item2) getIntent().getSerializableExtra("item");

        // 수정할 내용을 EditText에 채운다.
        editText_title.setText(item.getTitle());
        editText_date.setText(item.getDateFormatted());
    }

    // 저장 버튼 클릭
    public void btnSave_clicked(View view) {
        // EditText에 입력된 내용을 item 객체에 채운다.
        item.setTitle(editText_title.getText().toString());
        item.setDateFormatted(editText_date.getText().toString());

        // 내용이 수정된 item 객체를, intent 객체에 넣어서 리턴한다.
        Intent intent = new Intent();
        intent.putExtra("item", item);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    // 취소 버튼 클릭
    public void btnCancel_clicked(View view) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
