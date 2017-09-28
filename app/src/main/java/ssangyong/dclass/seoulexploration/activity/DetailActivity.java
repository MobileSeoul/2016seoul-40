package ssangyong.dclass.seoulexploration.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.data.ServerGetImage;
import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.data.TreasureDAO;

import static ssangyong.dclass.seoulexploration.activity.LoadActivity.sysLanguage;


/***********************************
 * * 작성자 : 정태훈
 * * 기  능 : 하나 자세히 보기
 * * 수정내용 : 없음
 * * 버  전 :
 * **********************************/

public class DetailActivity extends AppCompatActivity {

    TextView nameTextView,themeTextView,areaTextView,noteTextView,timeTextView,phoneTextView,trafficTextView;

    private GoogleMap map;
    LocationManager manger;
    MyLocationListener listener;
    ImageView imageView;

    ArrayList<TourDTO> list;
    String name,traffic;
    TourDTO add_dto;
    double x,y;

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
        ab.setTitle("관광지 상세보기");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));

        setContentView(R.layout.activity_detail);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);

        nameTextView= (TextView)findViewById(R.id.textName);
        themeTextView= (TextView)findViewById(R.id.textTheme);
        areaTextView= (TextView)findViewById(R.id.textArea);
        noteTextView= (TextView)findViewById(R.id.textNote);
        timeTextView= (TextView)findViewById(R.id.textTime);
        phoneTextView= (TextView)findViewById(R.id.textPhone);
        trafficTextView= (TextView)findViewById(R.id.textTraffic);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = fragment.getMap();
        manger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        nameTextView.setText(name); //받아온 이름값을 등록한다.

        list = LoadActivity.list;


        for(int i=0;i<list.size();i++) {
            TourDTO dto = list.get(i);
            if(name.equals(dto.getName())) {
                add_dto = dto;
                themeTextView.setText(dto.getTheme());
                areaTextView.setText(dto.getArea());
                noteTextView.setText(dto.getNote());

                if(dto.getTime().equals(" ")) {
                    TextView timeiconTextView= (TextView)findViewById(R.id.timeicon);
                    timeiconTextView.setVisibility(View.GONE);
                    timeTextView.setVisibility(View.GONE);
                } else {
                    timeTextView.setText(dto.getTime());
                }
                if(dto.getPhone().equals(" ")) {
                    TextView phoneiconTextView= (TextView)findViewById(R.id.phoneicon);
                    phoneiconTextView.setVisibility(View.GONE);
                    phoneTextView.setVisibility(View.GONE);
                } else {
                    phoneTextView.setText(dto.getPhone());
                }
                if(dto.getTraffic().equals(" ")) {
                    TextView trafficeiconTextView= (TextView)findViewById(R.id.trafficeicon);
                    trafficeiconTextView.setVisibility(View.GONE);
                    trafficTextView.setVisibility(View.GONE);
                } else {
                    traffic = dto.getTraffic();
                    trafficTextView.setText(traffic);
                }


                x = Double.parseDouble(dto.getX());
                y = Double.parseDouble(dto.getY());

                int num = Integer.parseInt(dto.getNumber());
                String imageUrl = "http://xml.namoolab.com/image/tour/t"+num+".png";


                new ServerGetImage((ImageView) findViewById(R.id.detail_imageview)).execute(imageUrl);
               // ImageView imageView = (ImageView)findViewById(R.id.detail_imageview);
                //imageView.setMaxHeight();

                break;
            }
        }
    }

    public void onButton_Call(View v) {         //Call버튼

        String str = phoneTextView.getText().toString();

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+str));
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();

        map.setMyLocationEnabled(false);

        if (manger != null) {
            manger.removeUpdates(listener);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        map.setMyLocationEnabled(true); //내위치 깜빡깜빡

        requestMyLocation();
    }

    public void requestMyLocation() {

        long minTime = 10000; //10초마다 업데이트
        float minDistance = 0; //항상 업데이트(최소거리 0)


        manger.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);

        manger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener);

        Location lastLocation = manger.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (lastLocation != null) {
            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();
           // Log.d("MainActivity", "가장 최근의 내위치 : " + latitude + "/" + longitude);

        }
    }

    private void showCurrentMap(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(x, y);   //지도상의 하나의 위치값
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15)); //현재 위치를 지도의 중심으로 표시
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);  //일반 지도 유형

        MarkerOptions marker = new MarkerOptions();                         //다른곳의 위치
        marker.position(new LatLng(x,y));
        marker.title(name);
        marker.snippet(traffic);
        marker.draggable(true);
        //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));

        map.addMarker(marker);

    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

          //  Log.d("MainActivity", "내위치 : " + latitude + "/" + longitude);

            showCurrentMap(latitude, longitude);

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    public void onAddButtonClick(View view) {   //추가

        Toast.makeText(this, "추가한 관광지에 추가되었습니다.", Toast.LENGTH_SHORT).show();
        TourDAO dao = new TourDAO(this,sysLanguage);
        dao.updatePoke(Integer.parseInt(add_dto.getNumber()),TourDAO.TOUR_ADD);
    }

    public void onAddButtonClick2(View view) {  //방문완료

        Toast.makeText(this, "관광지 방문 완료됨.", Toast.LENGTH_SHORT).show();
        TourDAO dao = new TourDAO(this,sysLanguage);
        dao.updatePoke(Integer.parseInt(add_dto.getNumber()),TourDAO.TOUR_VISIT);

        TreasureDAO dao2 = new TreasureDAO(this);
        dao2.updateGain(Integer.parseInt(add_dto.getNumber()),1);
    }

    public void onAddButtonClick3(View view) {  //삭제

        Toast.makeText(this, "마이리스트에서 삭제함.", Toast.LENGTH_SHORT).show();
        TourDAO dao = new TourDAO(this,sysLanguage);
        dao.updatePoke(Integer.parseInt(add_dto.getNumber()),TourDAO.TOUR_DEL);

        TreasureDAO dao2 = new TreasureDAO(this);
        dao2.updateGain(Integer.parseInt(add_dto.getNumber()),0);
    }

    public void onButton_naver(View v) {        //네이버 검색

        Intent intent = new Intent(getApplicationContext(),WebActivity.class);
        String url = "https://m.search.naver.com/search.naver?query="+nameTextView.getText();
        intent.putExtra("url",url);
        startActivityForResult(intent,1500);


    }

   /* public void onClickPlus(View v) {    //추가 버튼
        Toast.makeText(this, "관광지가 추가되었습니다. 마이리스트로 이동하시겠습니까?", Toast.LENGTH_SHORT).show();
        TourDAO dao = new TourDAO(this,sysLanguage);
        dao.updatePoke(Integer.parseInt(add_dto.getNumber()),TourDAO.TOUR_ADD);
    }*/

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
