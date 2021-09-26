package net.skhu.e04firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerView2Adapter extends RecyclerView.Adapter<RecyclerView2Adapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        TextView textView1, textView2;
        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            checkBox = view.findViewById(R.id.checkBox);
            view.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        public void setData() {
            Item2 item = arrayList.get(getAdapterPosition());
            textView1.setText(item.getTitle());
            textView2.setText(item.getDateFormatted());
            checkBox.setChecked(item.isChecked());
        }

        @Override
        public void onClick(View view) {
            Firebase3Activity activity = (Firebase3Activity)view.getContext();
            Item2 item = arrayList.get(getAdapterPosition());
            Intent intent = new Intent(activity, Item2EditActivity.class);
            intent.putExtra("item", item);
            activity.startActivityForResult(intent, Item2EditActivity.REQUEST_EDIT);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Item2 item = arrayList.get(super.getAdapterPosition());
            item.setChecked(isChecked);
            boolean invalidateMenu = false;
            if (isChecked) invalidateMenu = (++checkedItemCount == 1);
            else invalidateMenu = (--checkedItemCount == 0);
            if (invalidateMenu)
                ((Activity) textView1.getContext()).invalidateOptionsMenu();
        }
    }

    LayoutInflater layoutInflater;
    ArrayList<Item2> arrayList;
    int checkedItemCount = 0;

    public RecyclerView2Adapter(Context context, ArrayList<Item2> arrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.setData();
    }
}
