package cf.awidiyadew.drawerexpandablelistview;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThirdFragment extends Fragment {
    View view;

    public ThirdFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);

        TextView tvProfileAcademyName = (TextView) view.findViewById( R.id.profileAcademyName );
        TextView tvProfileClassName = (TextView) view.findViewById( R.id.profileClassName );
        TextView tvProfileStudentName = (TextView) view.findViewById( R.id.profileStudentName );
        TextView tvProfileBirthGender = (TextView) view.findViewById( R.id.profileBirthGender );
        TextView tvProfilePhone = (TextView) view.findViewById( R.id.profilePhone );
        TextView tvProfileMAC = (TextView) view.findViewById( R.id.profileMAC );
        TextView tvProfileGuardianName = (TextView) view.findViewById( R.id.profileGuardianName );
        TextView tvProfileGuardianPhone = (TextView) view.findViewById( R.id.profileGuardianPhone );

        SharedPreferences setting = getActivity().getSharedPreferences( "setting", Context.MODE_PRIVATE );
        String academyName = setting.getString("ACADEMY_NAME", "" );
        String className = setting.getString("CLASS_NAME", "" );
        String studentName = setting.getString( "USER_NAME", "" );

        String studentBirth = setting.getString( "USER_BIRTH", "" );
        String studentGender = setting.getString( "USER_GENDER", "" );
        String studentBirthGendr = studentBirth + " - " + studentGender;

        String studentPhone = setting.getString( "USER_TEL", "" );
        String studentMAC = setting.getString( "USER_MAC", "" );
        String guardianName = setting.getString( "GUARDIAN_NAME", "" );
        String guardianPhone = setting.getString( "GUARDIAN_TEL", "" );

        tvProfileAcademyName.setText( academyName );
        tvProfileClassName.setText( className );
        tvProfileStudentName.setText( studentName );
        tvProfileBirthGender.setText( studentBirthGendr );
        tvProfilePhone.setText( studentPhone );
        tvProfileMAC.setText( studentMAC );
        tvProfileGuardianName.setText( guardianName );
        tvProfileGuardianPhone.setText( guardianPhone );

        return view;
    }
}
