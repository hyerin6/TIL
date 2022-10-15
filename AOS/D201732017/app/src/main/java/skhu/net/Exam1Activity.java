package skhu.net;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Exam1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam1);

        // 클릭된 라디오 버튼의 문자열이 editText에 채워진다.
        RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String s = radioButton.getText().toString();
                EditText editText1 = findViewById(R.id.editText1);
                editText1.setText(s);
            }
        };
        final RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(listener2);

        // spinner에 선택된 문자열이 editText에 채워진다.
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner1 = findViewById(R.id.spinner_userType);
                String text1 = spinner1.getSelectedItem().toString();

                EditText editText1 = findViewById(R.id.editText1);
                editText1.setText(text1);
            }
        };

       Button button = findViewById(R.id.btnOk);
       button.setOnClickListener(listener1);
    }


}
