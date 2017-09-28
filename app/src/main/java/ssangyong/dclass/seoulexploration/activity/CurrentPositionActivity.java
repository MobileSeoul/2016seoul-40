package ssangyong.dclass.seoulexploration.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.adapter.SingerAdapter;
import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.temp.VectorCalc;

import static android.R.attr.layout_marginBottom;
import static ssangyong.dclass.seoulexploration.activity.LoadActivity.sysLanguage;
import static ssangyong.dclass.seoulexploration.data.TourDAO.TOUR_DEL;

/**********************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 내위치에서 일정범위내에  위치 보기
 * * 수정내용 : 없음
 * * 버  전 :
 * *********************************************************************/

public class CurrentPositionActivity extends AppCompatActivity {

    String[] limits = {"5KM","10KM","20KM"};
    double limit;
    double latitude,longitude;

    private GoogleMap map;
    LocationManager manger;
    MyLocationListener listener;

    ListView listView;
    SingerAdapter adapter;
    ArrayList<TourDTO> list;

    boolean isShow;

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;

    Button backBtn;

    //int[] imageArray = {R.drawable.marker_a, R.drawable.marker_b, R.drawable.marker_c, R.drawable.marker_d, R.drawable.marker_e, R.drawable.marker_f, R.drawable.marker_g, R.drawable.marker_h, R.drawable.marker_i, R.drawable.marker_j};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("현재위치에서 선택");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));

        setContentView(R.layout.activity_current_position);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);

        isShow = true;


        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = fragment.getMap();

        manger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        listView = (ListView) findViewById(R.id.searchListView);   //리스트뷰는 껍데기
        adapter = new SingerAdapter(getApplicationContext()); //어댑터 생성


        //스피너 부분
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                limits
        );
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(CurrentPositionActivity.this, "선택된 아이템 :"+limits[position], Toast.LENGTH_SHORT).show();
                if(position==0) {
                    limit = 5.0;
                } else if(position==1) {
                    limit = 10.0;

                } else if(position==2) {
                    limit = 20.0;
                }
                clearList();
                addList(latitude, longitude);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(CurrentPositionActivity.this, "이거 언제 호출되니?", Toast.LENGTH_SHORT).show();
            }
        });




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
        //long minTime = 10000; //10초마다 업데이트
        float minDistance = 0; //항상 업데이트(최소거리 0)


        manger.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);

        manger.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener);

        Location lastLocation = manger.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (lastLocation != null) {
            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();

          //  Toast.makeText(CurrentPositionActivity.this, "가장 최근의 내위치 : " + latitude + "/" + longitude, Toast.LENGTH_SHORT).show();

        }
    }

    private void showCurrentMap(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);   //지도상의 하나의 위치값

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);  //일반 지도 유형

       /* String[] addrName = { "노량진2동 주민센터", "KBS별관", "공덕역", "신촌역", "경복궁" };
        String[] addrXY = { "37.5082030,126.9373670", "37.5188158,126.9292650", "37.5435594,126.9519426",
                "37.5552192,126.9368460", "37.5788408,126.9770162" };

        StringTokenizer token;*/
        if (isShow) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 13)); //현재 위치를 지도의 중심으로 표시  (13이면 약 5KM 반경)
            addList(latitude, longitude);
        }
        isShow = false;

        Log.d("hun", "내위치 : " + latitude + "/" + longitude);


    }




    class MyLocationListener implements LocationListener {  //
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();


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


    public void clearList() {
        map.clear();
        adapter.clearData();
    /*    map.addMarker(marker);
        adapter.addItem(dto);
        listView.setAdapter(adapter);*/
    }


    public void addList(double latitude, double longitude) {        //거리내에 있는 리스트를 맵과 리스트뷰에 각각 장착
        this.latitude = latitude;
        this.longitude = longitude;



        //list = LoadActivity.list;   //데이터집합을 받음.
        list = new TourDAO(this,sysLanguage).selectPoke(TOUR_DEL);  //db에 저장된것들



        Log.d("hun", "search 크기 : " + list.size());

        for (int i = 0, j = 0; i < list.size(); i++) {
            TourDTO dto = list.get(i);
            double x = Double.parseDouble(dto.getX());
            double y = Double.parseDouble(dto.getY());


            double vector = VectorCalc.distance(latitude, longitude, x, y, "k");
            Log.d("hun", dto.getName() + "거리 : " + vector);
            if (limit >= vector) {

                MarkerOptions marker = new MarkerOptions();                         //다른곳의 위치
                marker.position(new LatLng(x, y));
                //37.7958188,127.7760805
                marker.title(dto.getName());    //이름
                marker.snippet(dto.getPhone()); //부가
                marker.draggable(true);
                //marker.icon(BitmapDescriptorFactory.fromResource(imageArray[j++]));

                map.addMarker(marker);
                adapter.addItem(dto);

            }
        }


        listView.setAdapter(adapter);


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {        //marker 이벤트처리
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                String name = marker.getTitle();
                intent.putExtra("name", name);
                startActivityForResult(intent, 2002);
            }
        });


        listView.setOnItemClickListener(new ListView.OnItemClickListener() {    //리스트뷰 이벤트처리
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListActivity.this, "눌렀따", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                TourDTO dto = (TourDTO) parent.getAdapter().getItem(position);
                String name = dto.getName();

                intent.putExtra("name", name);

                startActivityForResult(intent, 2001);
            }


        });


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

