package skhu.net;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Exam2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2);
        alert();
    }

    public void buttonClick(View view){
        alert();
    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.test);
        builder.setMessage(R.string.data);

        builder.setPositiveButton(R.string.one, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                EditText editText = findViewById(R.id.editText2);
                editText.setText(R.string.one);
            }

        });

        builder.setNegativeButton(R.string.two, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                EditText editText = findViewById(R.id.editText2);
                editText.setText(R.string.two);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
