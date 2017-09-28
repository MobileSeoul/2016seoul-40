package ssangyong.dclass.seoulexploration.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.adapter.AddAreaAdapter;
import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.handler.AsyncCallBack;

import static ssangyong.dclass.seoulexploration.activity.LoadActivity.sysLanguage;


/**********************************************************************
 * * 작성자 : 정태훈
 * * 기  능 :  관광지 추가한 리스트 보는 화면.
 * * 수정내용 : 삭제하는거추가했습니다. -보현-
 * * 버  전 :
 * *********************************************************************/

public class AddAreaActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    AddAreaAdapter adapter;
    ArrayList<TourDTO> list;

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;

    Button backBtn;




    TextView selectedItemCntTxt;
    LinearLayout editBottomBar;
    ImageView deleteBtn;
    TextView delCancelBtn;
    TextView delOkBtn;

    public void print(String str) {
        Log.d("tour [AddArea]",str);
    }

    @Override
    public void onClick(View v) {
        if (v == deleteBtn) {
            showEditMode();

        } else if (v == delOkBtn) {
            hideEditMode();

            // 체크박스에서 체크된 항목은 리스트뷰에서 제거한다.
            for (TourDTO tour : list) {
                if (tour.isChecked()) {
                    TourDAO dao = new TourDAO(this,sysLanguage);
                    dao.updatePoke(Integer.parseInt(tour.getNumber()),TourDAO.TOUR_DEL);
                }
            }
            list = new TourDAO(this,sysLanguage).selectPoke(TourDAO.TOUR_ADD);
            adapter.updateList(list);

        } else if (v == delCancelBtn) {
            hideEditMode();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("추가한 관광지");
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



        editBottomBar = (LinearLayout) findViewById(R.id.edit_bottom_bar);



        delCancelBtn = (TextView) findViewById(R.id.del_cancel_btn);
        delOkBtn = (TextView) findViewById(R.id.del_ok_btn);
        delCancelBtn.setOnClickListener(this);
        delOkBtn.setOnClickListener(this);

        try {



            listView = (ListView) findViewById(R.id.addArea_ListView);   //리스트뷰는 껍데기
            list = new TourDAO(this,sysLanguage).selectPoke(TourDAO.TOUR_ADD);       //db에 추가한 지역리스트만 받음.

            if (list.size() == 0) {
                print("현재 추가한 지역이 아무것도 없습니다.");
                Toast.makeText(this, "현재 추가한 지역이 아무것도 없습니다.", Toast.LENGTH_SHORT).show();

            }

            adapter = new AddAreaAdapter(getApplicationContext(), list, new AsyncCallBack<String>() {
                @Override
                public void call(String event) {
                    // 체크박스를 체크 또는 해제 할때마다 이벤트를 받는다
                    if (event.equals("CHECK")) {

                        int checkedItemCnt = 0;
                        for (TourDTO tour : list) {
                            if (tour.isChecked()) {
                                checkedItemCnt++;
                            }
                        }
                        updateSelectedItemText(checkedItemCnt);
                    }
                }
            });
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

    private void updateSelectedItemText(int checkedItemCnt) {
        selectedItemCntTxt.setText(checkedItemCnt + "개 선택됨");
    }



    private void showEditMode() {
        editBottomBar.setVisibility(View.VISIBLE);
        selectedItemCntTxt.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.GONE);
        adapter.toggleEditMode(true);

    }
    private void hideEditMode() {
        editBottomBar.setVisibility(View.GONE);
        selectedItemCntTxt.setVisibility(View.GONE);
        deleteBtn.setVisibility(View.VISIBLE);
        adapter.toggleEditMode(false);

    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.miActionButton);
        View v = MenuItemCompat.getActionView(actionViewItem);

        deleteBtn = (ImageView) v.findViewById(R.id.add_area_del_btn);
        deleteBtn.setOnClickListener(this);

        selectedItemCntTxt = (TextView) v.findViewById(R.id.seleted_item_cnt_txt);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_area, menu);


        return true;
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
