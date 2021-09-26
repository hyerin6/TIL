package net.skhu.e04firebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class Firebase2Activity extends AppCompatActivity {

    RecyclerView2Adapter recyclerView2Adapter;
    ArrayList<Item2> arrayList;
    DatabaseReference myData02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase2);

        arrayList = new ArrayList<>();

        recyclerView2Adapter = new RecyclerView2Adapter(this, arrayList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerView2Adapter);

        Button b = (Button)findViewById(R.id.btnAdd);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText e = (EditText) findViewById(R.id.editText);
                String s = e.getText().toString();
                e.setText("");
                arrayList.add(new Item2(s, new Date()));
                myData02.setValue(arrayList);
                recyclerView2Adapter.notifyDataSetChanged();
            }
        });

        myData02 = FirebaseDatabase.getInstance().getReference("myData02");
        ValueEventListener listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Item2>> typeIndicator = new GenericTypeIndicator<List<Item2>>() {};
                List<Item2> temp = dataSnapshot.getValue(typeIndicator);
                if (temp != null) {
                    arrayList.clear();

                    // 줄 73, 74는 아래 두 줄로 구현해도 된다.
                    // arrayList.addAll(temp);
                    // recyclerView2Adapter.notifyDataSetChanged();
                    recyclerView2Adapter.arrayList = (ArrayList<Item2>) temp;
                    arrayList = (ArrayList<Item2>) temp;
                    Log.e("내태그", arrayList.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("내태그", "서버 에러: ", error.toException());
            }
        };
        myData02.addValueEventListener(listener1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_firebase2, menu);
        MenuItem menuItem = menu.findItem(R.id.action_remove);
        menuItem.setVisible(recyclerView2Adapter.checkedItemCount > 0);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_remove) {
            deleteItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteItems() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.doYouWantToDelete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                ListIterator<Item2> iterator = arrayList.listIterator();
                while (iterator.hasNext())
                    if (iterator.next().isChecked())
                        iterator.remove();
                myData02.setValue(arrayList);
                recyclerView2Adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

