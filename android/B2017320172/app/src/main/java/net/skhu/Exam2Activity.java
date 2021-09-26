package net.skhu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Exam2Activity extends AppCompatActivity {

    public static final int REQUEST_SAVE = 1;
    public static final int REQUEST_CANSEL = 2;

    EditText e1;
    EditText e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2);

        // 저장
        Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                e1 = (EditText) findViewById(R.id.editText1);
                e2 = (EditText) findViewById(R.id.editText2);

                Data data = new Data(e1.getText().toString(), e2.getText().toString());

                Intent intent = new Intent();
                intent.putExtra("data", data);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
