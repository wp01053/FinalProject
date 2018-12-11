package cf.awidiyadew.drawerexpandablelistview;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cf.awidiyadew.drawerexpandablelistview.task.ClassStudentListTask;

public class SecondFragment extends Fragment {
    View view;

    public SecondFragment(){
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);

        TextView tvAcademyName = (TextView) view.findViewById( R.id.academyName );
        TextView tvClassName = (TextView) view.findViewById( R.id.className );
        ListView chatList = (ListView) view.findViewById( R.id.chatList );

        SharedPreferences setting = getActivity().getSharedPreferences( "setting", Context.MODE_PRIVATE );
        String academyName = setting.getString("ACADEMY_NAME", "" );
        String className = setting.getString("CLASS_NAME", "" );

        tvAcademyName.setText( academyName );
        tvClassName.setText( className );

        Map<String, String> map = new HashMap<>();

        map.put("ACADEMY_NAME", academyName );
        map.put("CLASS_NAME", className );

        String result = null;
        ClassStudentListTask task = new ClassStudentListTask();
        try {
            result = task.execute( map ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(result.equals( "serverFail" )){
            Toast.makeText( getContext(), "서버 접속에 실패 하였습니다", Toast.LENGTH_SHORT ).show();
            return view;
        }

        Gson gson = new Gson();
        ArrayList<String> userList = gson.fromJson(result, new TypeToken<ArrayList<String>>(){}.getType());

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, userList);

        chatList.setAdapter( adapter );

        return view;
    }
}
