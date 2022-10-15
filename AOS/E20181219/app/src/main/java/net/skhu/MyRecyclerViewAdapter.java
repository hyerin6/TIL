package net.skhu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView textView1, textView2;

        public ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            view.setOnClickListener(this);
        }

        public void setData() {
            Student student = arrayList.get(getAdapterPosition());
            textView1.setText(student.getStudentName());
            textView2.setText(student.getStudentId());
        }

        @Override
        public void onClick(View view) {
            arrayList.remove(super.getAdapterPosition()); // 클릭된 학생 삭제
            notifyDataSetChanged(); // 리사이클러뷰 다시 그리기
        }
    }
    LayoutInflater layoutInflater;
    ArrayList<Student> arrayList;

    public MyRecyclerViewAdapter(Context context, ArrayList<Student> arrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.student, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.setData();
    }
}
