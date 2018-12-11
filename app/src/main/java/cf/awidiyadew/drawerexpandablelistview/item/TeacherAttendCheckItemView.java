package cf.awidiyadew.drawerexpandablelistview.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cf.awidiyadew.drawerexpandablelistview.R;

public class TeacherAttendCheckItemView extends LinearLayout {

    TextView studentName;
    TextView studentMac;
    ImageView attendCheck;

    public TeacherAttendCheckItemView(Context context) {
        super(context);

        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate( R.layout.teacher_attend_check_item_view, this, true);

        studentName = findViewById(R.id.studentName);
        studentMac = findViewById(R.id.studentMac);
        attendCheck = findViewById(R.id.attendCheck);
    }

    public void setStudentName(String name) {
        studentName.setText(name);
    }

    public void setStudentMac(String mac) {
        studentMac.setText(mac);
    }

    public void setAttendCheck(int imgNum) { attendCheck.setImageResource(imgNum); }

}
