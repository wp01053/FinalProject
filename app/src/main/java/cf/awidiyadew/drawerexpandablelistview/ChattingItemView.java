package cf.awidiyadew.drawerexpandablelistview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import cf.awidiyadew.drawerexpandablelistview.dto.ChatDTO;

import static android.content.Context.MODE_PRIVATE;

public class ChattingItemView extends LinearLayout {
    private static final String TAG = "lecture";
    private SharedPreferences setting;
    TextView myname;
    TextView mymsg;




    public ChattingItemView(final Context context, String name) {
        super(context);
//        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);

        setting = context.getSharedPreferences("setting", Context.MODE_MULTI_PROCESS);
        final String studentName = setting.getString("USER_NAME", "");

        Log.d("123123",studentName+"이름123123 ");
        Log.d("123123","이름 : "+name+"");
        if(name.equals(studentName)){
            LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.mychatting_item_view,this,true);
        }else {
            LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.chatting_item_view,this,true);
        }

        myname = findViewById(R.id.myname);
        mymsg = findViewById(R.id.mymsg);
    }

    public void setMyName(String name){
        myname.setText(name);
    }
    public void setMymsg(String msg) { mymsg.setText(msg); }

}
