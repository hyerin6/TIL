package skhu.net;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class B201732017Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b201732017);

        Button b = (Button)findViewById(R.id.button);
        View.OnClickListener listenerObj = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = findViewById(R.id.editText1);
                CharSequence s = e.getText();
                EditText e2 = findViewById(R.id.editText2);
                e2.setText(s);
                e.setText("");
            }
        };
        b.setOnClickListener(listenerObj);

    }

}
