package cf.awidiyadew.drawerexpandablelistview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import cf.awidiyadew.drawerexpandablelistview.R;
import cf.awidiyadew.drawerexpandablelistview.item.TeacherAttendCheckItem;
import cf.awidiyadew.drawerexpandablelistview.item.TeacherAttendCheckItemView;

public class TeacherAttendCheckAdapter extends BaseAdapter {

    Context context;
    ArrayList<TeacherAttendCheckItem> items = new ArrayList<>();

    public TeacherAttendCheckAdapter(Context context) {
        this.context = context;
    }

    public void addItem(TeacherAttendCheckItem item){
        items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TeacherAttendCheckItemView view = null;
        if (convertView == null)
        {
            view = new TeacherAttendCheckItemView(context);
        } else {
            view = (TeacherAttendCheckItemView) convertView;
        }

        TeacherAttendCheckItem item = items.get(position);
        view.setStudentName(item.getStudentName());
        view.setStudentMac(item.getStudentMac());

        view.setAttendCheck(item.getAttendCheck());

        return view;
    }


    public void checkImg(int position){
        TeacherAttendCheckItem item = items.get(position);
        item.setAttendCheck( R.drawable.check );
        items.set(position, item);
    }


}
