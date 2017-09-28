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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.data.TreasureDAO;
import ssangyong.dclass.seoulexploration.data.TreasureDTO;

/***********************************
 * * 작성자 : 김훈영
 * * 기  능 : 메인화면
 * * 수정내용 : 없음
 * * 버  전 :
 * **********************************/

public class MedalListActivity extends AppCompatActivity {
    ArrayList<TreasureDTO> list;
    int mainMedal;

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
        ab.setTitle("획득한 메달");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));
        setContentView(R.layout.activity_medal_list);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);

        ImageView imageView = (ImageView) findViewById(R.id.main_medalimg);
        TextView textView=(TextView) findViewById(R.id.main_medalExp);

        TreasureDAO dao = new TreasureDAO(this);
        list=dao.selectGain();
        mainMedal=list.size();


        if(mainMedal<2){
            imageView.setImageResource(R.drawable.medal_tree);
            textView.setText("나무등급입니다.");

        }
        else if(mainMedal>=2 && mainMedal<4){
            imageView.setImageResource(R.drawable.medal_stone);
            textView.setText("돌등급입니다.");

        }
        else if(mainMedal>=4 && mainMedal<6){
            imageView.setImageResource(R.drawable.medal_bronze);
            textView.setText("브론즈등급입니다.");

        }
        else if(mainMedal>=6 && mainMedal<8){
            imageView.setImageResource(R.drawable.medal_silver);
            textView.setText("실버등급입니다.");

        }
        else if(mainMedal>=8){
            imageView.setImageResource(R.drawable.medal_gold);
            textView.setText("골드등급입니다.");

        }

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