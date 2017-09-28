package ssangyong.dclass.seoulexploration.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import ssangyong.dclass.seoulexploration.R;

/***********************************
 * * 작성자 : 정태훈
 * * 기  능 : 메인화면
 * * 수정내용 : 없음
 * * 버  전 :
 * **********************************/

public class MypageActivity extends AppCompatActivity {

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;

    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("마이페이지");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));
        setContentView(R.layout.activity_mypage);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);

    }


    public void onButtonClick1(View v) {    //관광지 리스트
        Intent intent = new Intent(getApplicationContext(),TourListActivity.class);

        startActivity(intent);
    }

    public void onButtonClick2(View v) {    //유물 리스트
        Intent intent = new Intent(getApplicationContext(),TreasureListActivity.class);

        startActivity(intent);
    }
    public void onButtonClick3(View v) {    //감사패 리스트
        Intent intent = new Intent(getApplicationContext(),MedalListActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //홈버튼을 선택하면
        if(id == android.R.id.home){

            if(isPageOpen){ //페이지가 열려있으면
                isPageOpen = false;
                slidingMenuPage.startAnimation(translateLeftAnim);
                slidingMenuPage.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
            }
            else{ //페이지가 닫혀있으면
                isPageOpen = true;
                slidingMenuPage.startAnimation(translateRightAnim);
                slidingMenuPage.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClickTourInfoBtn(View v) {    //관광 안내소 버튼
        if(isPageOpen){ //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(getApplicationContext(),TourInfoActivity.class);
        startActivity(intent);
    }

    public void onClickInfoBtn(View v) {    //출처 안내 버튼
        if(isPageOpen){ //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View v){   //다른 곳 눌러도 닫히게
        if(isPageOpen){ //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.startAnimation(translateLeftAnim);
            slidingMenuPage.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
    }
}
