package skhu.net;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Exam1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam1);
    }


    public void buttonClick(View view){
        EditText editText = findViewById(R.id.editText);
        String s = editText.getText().toString();
        TextView textView = findViewById(R.id.textView);
        String s2 = textView.getText().toString();
        editText.setText(s2);
        textView.setText(s);
    }
}
