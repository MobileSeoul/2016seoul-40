package ssangyong.dclass.seoulexploration;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import ssangyong.dclass.seoulexploration.activity.LoadActivity;
import ssangyong.dclass.seoulexploration.activity.MainActivity;
import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.data.TreasureDAO;
import ssangyong.dclass.seoulexploration.data.TreasureDTO;
import ssangyong.dclass.seoulexploration.temp.IconBadge;
import ssangyong.dclass.seoulexploration.temp.VectorCalc;

/********************************************************************************
 * * 작성자 : 이조은
 * * 기  능 : 서비스와 브로드캐스트부분
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/

public class ProximityService extends Service implements Runnable {


    public static final String TAG = "tour";
    public static double dist_test = 100.0;
    public static double dist;

    //전체 관광지 리스트
    TourDAO dao;
    //전체 유물 리스트
    TreasureDAO rdao;

    //"추가"된 관광 리스트
    ArrayList<TourDTO> tList;
    //유물 리스트
    ArrayList<TreasureDTO> rList;

    //알람에 넣을 관광지 이름
    String tName;
    //알람에 넣을 유물 이름
    String rName;

    //current position 현재 위치
    private double cx;
    private double cy;

    Handler handler;
    NotificationManager Notifi_M;
    Notification Notifi;

    Random rand;


    public void onCreate() {
        super.onCreate();

        rand = new Random();
//        count = 0;

        //관광지리스트 전체db가져오기
//        dao = new TourDAO(MainActivity.activity, "ko");

        rdao = new TreasureDAO(MainActivity.activity);

        tList = new ArrayList<>();
        rList = new ArrayList<>();

        //유물리스트에 값넣기
        rList = rdao.selectAll();


        //위치 확인
        startLocationService();
        //권한 확인
        checkDangerousPermissions();

        Thread sThread = new Thread(this);
        sThread.start();
    }

    //서비스가 실행중인동안에 할 것들 적는 곳
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //관광지리스트 전체db가져오기
        dao = new TourDAO(MainActivity.activity, "ko");

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        this.handler = handler;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    //스레드의 실행 부분
    public void run() {
        while (true) {
            try {

                // 내가 추가한 관광지 리스트 목록 채우기(계속 채우기)
                tList = dao.selectPoke(TourDAO.TOUR_ADD);

                Log.d(TAG, "Service 크기 : " + tList.size());

                for (int i = 0; i < tList.size(); i++) {


                    TourDTO sdto = tList.get(i);
                    double x = Double.parseDouble(sdto.getX());
                    double y = Double.parseDouble(sdto.getY());

                    //목적지 지점이랑 거리 비교
                    dist = VectorCalc.distance(x, y, cx, cy, "m");

                    Log.d(TAG, sdto.getName() + "의 거리 차이 : " + dist);
                    //   Toast.makeText(MainActivity.activity,sdto.getName() + "의 거리 차이 : " + dist, Toast.LENGTH_SHORT).show();


                    //2m이내면 관광지 도착으로 처리
                    //if (0.0 <= dist && dist <= 2.0) {
                    if (0.0 <= dist && dist <= dist_test) {

                        Log.d(TAG, sdto.getName() + "관광지 도착!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  " + "  거리 차이 : " + dist);
                        //  Toast.makeText(MainActivity.activity, "sdto.getName() + \"관광지 도착!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  \" + \"  거리 차이 : \" + dist", Toast.LENGTH_SHORT).show();


                        //알람에 넣을 관광지명 넣기
                        tName = sdto.getName();


                        //관광지 갔다 온 걸로 표시되기
                        TourDAO dao = new TourDAO(MainActivity.activity, "ko");
                        dao.updatePoke(Integer.parseInt(sdto.getNumber()), TourDAO.TOUR_VISIT);

                        //유물 랜덤으로 주기
//                        String number = sdto.getNumber();
                        int ran = rand.nextInt(40) + 1;

                        //rName에 넣기
//                        rName = "유물명";
                        rName = rList.get(ran+1).getName();

                        Log.d("유물명 나오는지 확인 : ", rName);

                        String number = String.valueOf(ran);
                        Log.d("랜덤값 확인 : ", number);

                        Log.d(TAG, "number 값 : " + number);
                        addTreasure(number);

                        handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
                        IconBadge.addNotiCnt();
                        IconBadge.updateIconBadge(ProximityService.this, IconBadge.getNotiCnt());

                        break;
                    }else if (0.0 <= dist && dist <= 500) {
                        Toast.makeText(getApplicationContext(), sdto.getName() + "에서 반경 500m 이내에 있습니다." , Toast.LENGTH_SHORT).show();
                    }else if (0.0 <= dist && dist <= 250) {
                        Toast.makeText(getApplicationContext(), sdto.getName() + "에서 반경 250m 이내에 있습니다." , Toast.LENGTH_SHORT).show();
                    }

                }

                Thread.sleep(10000);

            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "권한 있음");
        } else {
            Log.i(TAG, "권한 없음");

        }
    }


    private void addTreasure(String number) {

        TreasureDAO dao = new TreasureDAO(MainActivity.activity);
        dao.updateGain(Integer.parseInt(number),1);

    }


    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                Log.d(TAG, "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude);
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

    }

    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            cx = location.getLatitude();
            cy = location.getLongitude();

            String msg = "현재 위치\nx : " + cx + "\ny : " + cy;
            Log.i("GPSListener", msg);

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

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(ProximityService.this, LoadActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(ProximityService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notifi = new Notification.Builder(getApplicationContext())
                    .setContentTitle(tName + " 도착")
                    .setContentText(rName + "을 얻었습니다.")
                    .setSmallIcon(R.drawable.logo)
                    .setTicker(tName + "에 도착")
                    .setContentIntent(pendingIntent)
//            진동의 설정은 홀수 배열은 진동의 시간, 짝수 배열은 진동이 멈춰있는 시간이다.
//            다음은 1000ms(1초)진동 후 1000ms진동이 멈추고, 이후 1000ms간 진동이 울리고, 1000ms동안 진동이 멈추게 된다.
//            FLAG_INSISTENT플래그를 설정할 경우 자용자가 알림을 제거할때까지 반복 재생된다.
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .build();

//            FLAG_INSISTENT플래그를 설정할 경우 자용자가 알림을 제거할때까지 반복 재생된다.
            //소리추가
            Notifi.defaults = Notification.DEFAULT_SOUND;

            //알림 소리를 한번만 내도록
            Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

            //notify.sound = Uri.parse("file:/system/media/audio/ringtones/sample.ogg");


            //확인하면 자동으로 알림이 제거 되도록
            Notifi.flags = Notification.FLAG_AUTO_CANCEL;


            //(id, notification) 존재하는 알림의 id를 사용하면 알림을 update할 수 있다.
            Notifi_M.notify((int)System.currentTimeMillis(), Notifi);

        }
    }

}
