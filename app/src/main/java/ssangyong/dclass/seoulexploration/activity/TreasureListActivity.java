package ssangyong.dclass.seoulexploration.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.data.ServerGetImage;
import ssangyong.dclass.seoulexploration.data.TreasureDAO;
import ssangyong.dclass.seoulexploration.data.TreasureDTO;
import ssangyong.dclass.seoulexploration.temp.IconBadge;


/***********************************
 * * 작성자 : 김훈영
 * * 기  능 : 보물리스트
 * * 수정내용 : 카카오톡연동추가헸습니다. -보현-
 * * 버  전 :
 * **********************************/


public class TreasureListActivity extends AppCompatActivity implements View.OnClickListener {

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;

    Button backBtn;

    TextView kakaoShareBtn;

    ArrayList<TreasureDTO> list;
    GridView gridView;
    String note,name,num;
    Context context;
    String medalUrl = "";
    int mainMedal = 0;
    Integer[] ImageID={R.drawable.g1j,	R.drawable.g2j,	R.drawable.g3j,	R.drawable.g4j,	R.drawable.g5j,	R.drawable.g6j,	R.drawable.g7j,	R.drawable.g8j,	R.drawable.g9j,	R.drawable.g10j,
            R.drawable.g11j,	R.drawable.g12j,	R.drawable.g13j,	R.drawable.g14j,	R.drawable.g15j,	R.drawable.g16j,	R.drawable.g17j,	R.drawable.g18j,	R.drawable.g19j,	R.drawable.g20j,
            R.drawable.g21j,	R.drawable.g22j,	R.drawable.g23j,	R.drawable.g24j,	R.drawable.g25j,	R.drawable.g26j,	R.drawable.g27j,	R.drawable.g28j,	R.drawable.g29j,	R.drawable.g30j,
            R.drawable.g31j,	R.drawable.g32j,	R.drawable.g33j,	R.drawable.g34j,	R.drawable.g35j,	R.drawable.g36j,	R.drawable.g37j,	R.drawable.g38j,	R.drawable.g39j,	R.drawable.g40j,
            R.drawable.g41j,	R.drawable.g42j,	R.drawable.g43j,	R.drawable.g44j,	R.drawable.g45j,	R.drawable.g46j,	R.drawable.g47j,	R.drawable.g48j,	R.drawable.g49j,	R.drawable.g50j,
            R.drawable.g51j,	R.drawable.g52j,	R.drawable.g53j,	R.drawable.g54j,	R.drawable.g55j,	R.drawable.g56j,	R.drawable.g57j,	R.drawable.g58j,	R.drawable.g59j,	R.drawable.g60j,
            R.drawable.g61j,	R.drawable.g62j,	R.drawable.g63j,	R.drawable.g64j,	R.drawable.g65j,	R.drawable.g66j,	R.drawable.g67j,	R.drawable.g68j,	R.drawable.g69j,	R.drawable.g70j,
            R.drawable.g71j,	R.drawable.g72j,	R.drawable.g73j,	R.drawable.g74j,	R.drawable.g75j,	R.drawable.g76j,	R.drawable.g77j,	R.drawable.g78j,	R.drawable.g79j,	R.drawable.g80j,
            R.drawable.g81j,	R.drawable.g82j,	R.drawable.g83j,	R.drawable.g84j,	R.drawable.g85j,	R.drawable.g86j,	R.drawable.g87j,	R.drawable.g88j,	R.drawable.g89j,	R.drawable.g90j,
            R.drawable.g91j,	R.drawable.g92j,	R.drawable.g93j,	R.drawable.g94j,	R.drawable.g95j,	R.drawable.g96j,	R.drawable.g97j,	R.drawable.g98j,	R.drawable.g99j,	R.drawable.g100j,
            R.drawable.g101j,	R.drawable.g102j,	R.drawable.g103j,	R.drawable.g104j,	R.drawable.g105j,	R.drawable.g106j,	R.drawable.g107j,	R.drawable.g108j,	R.drawable.g109j,	R.drawable.g110j,
            R.drawable.g111j,	R.drawable.g112j,	R.drawable.g113j,	R.drawable.g114j,	R.drawable.g115j};



    @Override
    public void onClick(View v) {

        //카카오톡 연동
        if (v == kakaoShareBtn) {
            try {

                final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
                final KakaoTalkLinkMessageBuilder builder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                builder.addAppButton("같이 탐험하러 가기");
                builder.addText("[서울 유적 탐험가]\n\n나 우리나라 보물 "+ list.size()+"개 있다~!!\n부럽지??!! 너도 할 수 있는데 같이 할래?!");
                builder.addImage(medalUrl, 300, 330);
                kakaoLink.sendMessage(builder, this);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IconBadge.updateIconBadge(this, 0);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);



        TreasureDAO dao = new TreasureDAO(this);
        list = dao.selectGain();

        if(list.size()==0) {
            Toast.makeText(this, "획득한 보물이 없습니다. ", Toast.LENGTH_SHORT).show();
        }


        setContentView(R.layout.activity_treasure_list);
        gridView = (GridView) findViewById((R.id.gridView1));
        TreasueAdapter gAdapter = new TreasueAdapter(this);
        for(int i=0;i<list.size();i++) {
            gAdapter.addItem(list.get(i));
        }

        gridView.setAdapter(gAdapter);



        // 감사패 가져오기
        TreasureDAO treasureDAO = new TreasureDAO(this);
        list=treasureDAO.selectGain();
        mainMedal=list.size();



        //감사패 조건
        if(mainMedal<2){
            // 브론즈
            medalUrl = "http://xml.namoolab.com/image/class/medal_tree.png";
        }
        else if(mainMedal>=2 && mainMedal<4){
            // 실버
            medalUrl = "http://xml.namoolab.com/image/class/medal_stone.png";

        }
        else if(mainMedal>=4 && mainMedal<6){
            // 골드
            medalUrl = "http://xml.namoolab.com/image/class/medal_bronze.png";
        }
        else if(mainMedal>=6 && mainMedal<8){
            // 다이아몬드
            medalUrl = "http://xml.namoolab.com/image/class/medal_silver.png";

        }
        else if(mainMedal>=8){
            // 마스터
            medalUrl = "http://xml.namoolab.com/image/class/medal_gold.png";

        }

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionViewItem = menu.findItem(R.id.talkShareActionBtn);
        View v = MenuItemCompat.getActionView(actionViewItem);

        kakaoShareBtn = (TextView) v.findViewById(R.id.kakaoShareBtn);
        kakaoShareBtn.setOnClickListener(this);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.treasure_list, menu);
        return true;
    }


    public class TreasueAdapter extends BaseAdapter {

        Context context;

        public TreasueAdapter(Context context) {
            this.context = context;
        }
        ArrayList<TreasureDTO> items = new ArrayList<>();

        public void addItem(TreasureDTO item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(400,400));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageResource(ImageID[position]);



            //그리드뷰에 띄울 이미지
            final View dialogView = View.inflate(TreasureListActivity.this, R.layout.activity_treasure_list_dialog, null);
            final ImageView ivPoster = (ImageView) dialogView.findViewById(R.id.ivPoster);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TreasureDTO curItem = items.get(position);
                    note = curItem.getNote();
                    name = curItem.getName();
                    num= curItem.getNumber();

                    View dialogView = View.inflate(TreasureListActivity.this, R.layout.activity_treasure_list_dialog, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(TreasureListActivity.this);
                    ImageView ivPoster = (ImageView) dialogView.findViewById(R.id.ivPoster);
                    ivPoster.setImageResource(ImageID[position]);

                    TextView textView=(TextView) dialogView.findViewById(R.id.textView);
                    textView.setText(note);

                    dlg.setTitle(name);
                    dlg.setIcon(R.drawable.logo);
                    dlg.setView(dialogView);
                    dlg.setNegativeButton("닫기", null);
                    dlg.show();
                }
            });

            return imageView;
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