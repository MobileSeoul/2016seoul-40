package ssangyong.dclass.seoulexploration.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.adapter.SingerAdapter;
import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;

import static ssangyong.dclass.seoulexploration.activity.LoadActivity.sysLanguage;

public class VisitAreaActivity extends AppCompatActivity {
    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;

    Button backBtn;

    ListView listView;
    SingerAdapter adapter;
    ArrayList<TourDTO> list;

    public void print(String str) {
        Log.d("tour [AddArea]",str);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("탐험한 관광지");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));
        setContentView(R.layout.activity_add_area);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);

        try {
            listView = (ListView) findViewById(R.id.addArea_ListView);   //리스트뷰는 껍데기
            adapter = new SingerAdapter(getApplicationContext()); //어댑터 생성

            list = new TourDAO(this,sysLanguage).selectPoke(TourDAO.TOUR_VISIT);       //db에 추가한 지역리스트만 받음.


            if (list.size() == 0) {
                Toast.makeText(this, "현재 방문완료한 지역이 아무것도 없습니다.", Toast.LENGTH_SHORT).show();

            }
            for (int i = 0; i < list.size(); i++) {
                TourDTO dto = list.get(i);
                adapter.addItem(dto);   //어댑터에 list에 있는값들 모두 장착
            }


            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new ListView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                    TourDTO dto = (TourDTO) parent.getAdapter().getItem(position);
                    String name = dto.getName();

                    intent.putExtra("name", name);

                    startActivityForResult(intent, 2001);
                }


            });
        } catch (Exception e) {
            Log.e("tour","addAara",e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //홈버튼을 선택하면
        if(id == android.R.id.home){

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
