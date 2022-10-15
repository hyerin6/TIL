package net.skhu;

import android.content.Context;
import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerView1Adapter extends RecyclerView.Adapter<RecyclerView1Adapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
        }

    }

    LayoutInflater layoutInflater;
    ArrayList<String> arrayList;

    public RecyclerView1Adapter(Context context, ArrayList<String> arrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    ///ViewHolder가 초기화 될 때 혹은 ViewHolder를 초기화 할 때 실행되는 메서드
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item1, viewGroup, false);
        return new ViewHolder(view);
    }

    // RecyclerView의 Row 하나하나를 구현하기위해 Bind(묶이다) 될 때
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.textView.setText(arrayList.get(index));
    }
}

