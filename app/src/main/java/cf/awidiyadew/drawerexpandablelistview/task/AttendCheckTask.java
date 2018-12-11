package cf.awidiyadew.drawerexpandablelistview.task;

import android.os.AsyncTask;

import java.util.Map;

public class AttendCheckTask extends AsyncTask<Map<String, String>, Integer, String> {

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override
    protected String doInBackground(Map<String, String>... maps) { // 내가 전송하고 싶은 파라미터

        // Http 요청 준비 작업
        HttpClient.Builder http = new HttpClient.Builder
                // ("POST", "http://" + ip + ":80/spring_mybatis/vision"); //포트번호,서블릿주소
                ("POST", "http://" + TaskServer.ip + ":8081/spring/attendCheck"); //포트번호,서블릿주소

        // Parameter 를 전송한다.
        http.addAllParameters(maps[0]);

        //Http 요청 전송
        HttpClient post = http.create();
        post.request();

        // 응답 상태코드 가져오기
        int statusCode = post.getHttpStatusCode();

        if(statusCode == -10){
            return "serverFail";
        }

        // 응답 본문 가져오기
        String body = post.getBody();

        return body;
    }

    @Override
    protected void onPostExecute(String result) { //서블릿으로부터 값을 받을 함수

//        Gson gson = new Gson();
//        StudentDto userData = gson.fromJson(result, StudentDto.class);
//
//        LoginActivity login = new LoginActivity();
//
//        login.loginCheck(userData);

    }
}