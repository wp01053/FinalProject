package cf.awidiyadew.drawerexpandablelistview;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;

import cf.awidiyadew.drawerexpandablelistview.dto.StudentDto;
import cf.awidiyadew.drawerexpandablelistview.task.CalenderCheckTask;
import cf.awidiyadew.drawerexpandablelistview.task.LoginDataTask;
import cf.awidiyadew.drawerexpandablelistview.decorators.EventDecorator;
import cf.awidiyadew.drawerexpandablelistview.decorators.OneDayDecorator;
import cf.awidiyadew.drawerexpandablelistview.decorators.SaturdayDecorator;
import cf.awidiyadew.drawerexpandablelistview.decorators.SundayDecorator;
import cf.awidiyadew.drawerexpandablelistview.task.CalenderCheckTask;

public class CalendarActivity extends AppCompatActivity {

    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    TextView textView;
    TextView textView2;
    MaterialCalendarView materialCalendarView;
    private static final String TAG = "lecture";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);



        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2018,11,26","2018,11,27","2018,11,28","2018,11,29"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");
                String shot_Day = String.valueOf(Year) + String.valueOf(Month) + String.valueOf(Day);
                textView.setText(shot_Day);
                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();
                Map<String, String> params = new HashMap<String, String>();


                SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
                // 출석 조회를 위한 데이터
                params.put("ACADEMY_NAME", setting.getString("ACADEMY_NAME", ""));
                params.put("CLASS_NAME", setting.getString("CLASS_NAME", ""));
                params.put("USER_NAME", setting.getString("USER_NAME", ""));
                params.put("ATTEND_DATE",shot_Day);

                String result = null;
                String result2 = "";


                try {

                    CalenderCheckTask calenderCheckTask = new CalenderCheckTask();
                    result = calenderCheckTask.execute(params).get();

                    Log.d(TAG, result);
                    StringTokenizer token = new StringTokenizer(result,"@");

                    while(token.hasMoreTokens()){

                        result2 += token.nextToken().trim() +"\n";
                    }
                    Log.d(TAG,"123123123"+result);
                    textView2.setText(result2);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), shot_Day , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,CalendarActivity.this));
        }
    }
}