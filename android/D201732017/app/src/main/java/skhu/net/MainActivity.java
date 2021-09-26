package skhu.net;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // exam1
    public void btn_clicked2(View button) {
        EditText editText1 = findViewById(R.id.editText1);
        editText1.setText("");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.doYouWantToDelete);
        builder.setNeutralButton(R.string.cancel, null); // 취소 버튼
        // 확인 버튼
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                EditText editText1 = findViewById(R.id.editText1);
                editText1.setText(R.string.delete);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // exam
    public void btn_clicked1(View button){
        Intent intent = new Intent(this, Exam1Activity.class);
        startActivity(intent);
    }

}
