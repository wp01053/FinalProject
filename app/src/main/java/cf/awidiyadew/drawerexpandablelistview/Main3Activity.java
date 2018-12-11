package cf.awidiyadew.drawerexpandablelistview;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {
    FragmentTransaction tran;
    public static Context context;

    private TextView mTextMessage;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getFragmentManager();
            tran = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    fm.beginTransaction().replace(R.id.container1, new FirstFragment()).commit();
                    tran.commit();
                    break;
                case R.id.navigation_people:
                    fm.beginTransaction().replace(R.id.container1, new SecondFragment()).commit();
                    tran.commit();
                    break;
                case R.id.navigation_profile:
                    fm.beginTransaction().replace(R.id.container1, new ThirdFragment()).commit();
                    tran.commit();
                    break;
            }


            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        context = this;

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.container1, new FirstFragment()).commit();

    }

}