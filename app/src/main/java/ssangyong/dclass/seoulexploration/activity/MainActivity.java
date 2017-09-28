package ssangyong.dclass.seoulexploration.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ssangyong.dclass.seoulexploration.ProximityService;
import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.view.SlidingView;

/***********************************
 * * 작성자 : 정태훈
 * * 기  능 : 메인화면
 * * 수정내용 : 없음
 * * 버  전 :
 **********************************/

public class MainActivity extends AppCompatActivity {

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;
    Animation imgAlph;


    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;
    LinearLayout slidingMenuPage1;

    Button backBtn;
    Button backBtn1;
    ImageView imgAlpha1;
    ImageView imgAlpha2;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("권한이 필요합니다.")
                        .setMessage("이 기능을 사용하기 위해서는 단말기의 \"위치기반\" 권한이 필요합니다. 계속하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                                }

                            }

                        })

                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
        }



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("권한이 필요합니다.")
                        .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                }

                            }

                        })

                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("권한이 필요합니다.")
                        .setMessage("이 기능을 사용하기 위해서는 단말기의 \"데이터 저장\" 권한이 필요합니다. 계속하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                                }

                            }

                        })

                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).show();


        }




        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));

        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.activity_main, null);
        View v2 = View.inflate(this, R.layout.activity_main1, null);
        sv.addView(v1);
        sv.addView(v2);
        setContentView(sv);

        imgAlpha1 = (ImageView) findViewById(R.id.imgAlpha1);
        imgAlpha2 = (ImageView) findViewById(R.id.imgAlpha2);


        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);
        imgAlph = AnimationUtils.loadAnimation(this, R.anim.img_alpha);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);
        //슬라이드메뉴페이지
        slidingMenuPage1 = (LinearLayout) findViewById(R.id.slidingMenuPage1);

        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn1 = (Button) findViewById(R.id.backBtn1);

        //서비스 시작
        Intent sIntent = new Intent(this, ProximityService.class);
        startService(sIntent);

        activity = this;

        imgAlpha1.startAnimation(imgAlph);
        imgAlpha1.setVisibility(View.INVISIBLE);
        imgAlpha2.startAnimation(imgAlph);
        imgAlpha2.setVisibility(View.INVISIBLE);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //홈버튼을 선택하면
        if (id == android.R.id.home) {

            if (isPageOpen) { //페이지가 열려있으면
                isPageOpen = false;
                slidingMenuPage.startAnimation(translateLeftAnim);
                slidingMenuPage.setVisibility(View.INVISIBLE);
                slidingMenuPage1.startAnimation(translateLeftAnim);
                slidingMenuPage1.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                backBtn1.setVisibility(View.INVISIBLE);
            } else { //페이지가 닫혀있으면
                isPageOpen = true;
                slidingMenuPage.startAnimation(translateRightAnim);
                slidingMenuPage.setVisibility(View.VISIBLE);
                slidingMenuPage1.startAnimation(translateRightAnim);
                slidingMenuPage1.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                backBtn1.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick1(View v) {    //관광지 고르기 버튼
        Intent intent = new Intent(getApplicationContext(), TourPickActivity.class);

        startActivity(intent);
    }

    public void onButtonClick2(View v) {    //마이페이지
        Intent intent = new Intent(getApplicationContext(), MypageActivity.class);

        startActivity(intent);
    }


    public void onClickTourInfoBtn(View v) {    //관광 안내소 버튼
        if (isPageOpen) { //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.setVisibility(View.INVISIBLE);
            slidingMenuPage1.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(getApplicationContext(), TourInfoActivity.class);
        startActivity(intent);
    }

    public void onClickInfoBtn(View v) {    //출처 안내 버튼
        if (isPageOpen) { //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.setVisibility(View.INVISIBLE);
            slidingMenuPage1.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View v) {   //다른 곳 눌러도 닫히게
        if (isPageOpen) { //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.startAnimation(translateLeftAnim);
            slidingMenuPage.setVisibility(View.INVISIBLE);
            slidingMenuPage1.startAnimation(translateLeftAnim);
            slidingMenuPage1.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
            backBtn1.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickBack1(View v) {   //다른 곳 눌러도 닫히게
        if (isPageOpen) { //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.startAnimation(translateLeftAnim);
            slidingMenuPage.setVisibility(View.INVISIBLE);
            slidingMenuPage1.startAnimation(translateLeftAnim);
            slidingMenuPage1.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
            backBtn1.setVisibility(View.INVISIBLE);
        }
    }

}
