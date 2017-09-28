package ssangyong.dclass.seoulexploration.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import ssangyong.dclass.seoulexploration.R;

/**********************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 인터넷 브라우저 연결화면naver_icon.png
 * * 수정내용 : 없음
 * * 버  전 :
 * *********************************************************************/

public class WebActivity extends AppCompatActivity {



    private WebView webview;    //웹뷰 객체


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        String url = "";     //링크주소 담을 변수
       Intent intent = getIntent();
        if(intent != null) {
            url = intent.getStringExtra("url");     //인텐트로 넘어온 링크값 저장
        }
        else
            Toast.makeText(WebActivity.this, "링크값이 넘어오지 않았습니다.", Toast.LENGTH_SHORT).show();

        // 웹뷰 객체 참조
        webview = (WebView) findViewById(R.id.webview);

        // 웹뷰 설정 정보
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); //자바스크립트 허용



        webview.loadUrl(url);   //웹 실행

    }


}
