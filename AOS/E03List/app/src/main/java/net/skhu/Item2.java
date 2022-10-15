package net.skhu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item2 {
    final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String title;
    Date date;
    boolean checked;

    public Item2(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDateFormatted() {
        return format.format(date);
    }

    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
}

