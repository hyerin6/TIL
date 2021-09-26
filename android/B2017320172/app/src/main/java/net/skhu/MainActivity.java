package net.skhu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View button){
        Intent intent = new Intent(this, Exam1Activity.class);
        startActivity(intent);
    }

    public void click2(View button){
        Intent intent = new Intent(this, Exam2Activity.class);
        startActivityForResult(intent, Exam2Activity.REQUEST_SAVE);
        // startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == Exam2Activity.REQUEST_SAVE) {
            if (resultCode == RESULT_OK) {
                Data data = (Data)intent.getSerializableExtra("data");

                t1 = (TextView)findViewById(R.id.textView1);
                t2 = (TextView)findViewById(R.id.textView2);

                t2.setText(data.getTitle());
                t1.setText(data.getId());
            }
        }
    }

}
