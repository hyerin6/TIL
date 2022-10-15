package net.skhu.e04firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button_clicked(View view) {
        Class activityClass = null;
        switch (view.getId()) {
            case R.id.button1:
                activityClass = Firebase1Activity.class;
                break;
            case R.id.button2:
                activityClass = Firebase2Activity.class;
                break;
            case R.id.button3: activityClass = Firebase3Activity.class; break;
        }
        startActivity(new Intent(this, activityClass));
    }
}

