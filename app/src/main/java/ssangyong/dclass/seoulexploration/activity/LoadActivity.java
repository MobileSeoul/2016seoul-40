package ssangyong.dclass.seoulexploration.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.data.TourPaserThread;
import ssangyong.dclass.seoulexploration.data.TreasureDAO;
import ssangyong.dclass.seoulexploration.data.TreasureDTO;
import ssangyong.dclass.seoulexploration.data.TreasurePaserThread;
import ssangyong.dclass.seoulexploration.temp.IconBadge;

/***********************************
 * * 작성자 : 정태훈
 * * 기  능 : 앱 실행시 로딩화면 / xml파싱/db저장 및 쿼리을 여기서 함.
 * * 수정내용 : 없음
 * * 버  전 : 2016-10-31
 * **********************************/


public class LoadActivity extends AppCompatActivity {

    public static ArrayList<TourDTO> list = null;
    public static ArrayList<TreasureDTO> list_treasure = null;
    public static String sysLanguage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);








        IconBadge.updateIconBadge(this, 0);

        //시스템 언어정보
        Locale systemLocale = getResources().getConfiguration().locale;
        sysLanguage = systemLocale.getLanguage();

        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("tour", "[first] 첫 실행...");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();

            /**********앱 최초 실행시 하고 싶은 작업***********/
            TourPaserThread thread_ko = new TourPaserThread("ko");
           // TourPaserThread thread_en = new TourPaserThread("en");

            TreasurePaserThread thread2 = new TreasurePaserThread("ko");


            try {
                thread_ko.start();
                thread_ko.join();
            //    thread_en.start();
           //     thread_en.join();  // 작업이 끝날떄까지 기달린다.
                thread2.start();
                thread2.join();

                /*Log.d("tour","onCreate list :"+list);

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).print();
                }*/

                TourDAO dao_ko = new TourDAO(this,"ko");
             //   TourDAO dao_en = new TourDAO(this,"en");
                TreasureDAO dao = new TreasureDAO(this);
                dao_ko.createTable();
              //  dao_en.createTable();
                dao.createTable();

                //dao_en.delete();
                //dao_ko.delete();

                list = thread_ko.getList();
                dao_ko.setData(list);
             //   list = thread_en.getList();
           //     dao_en.setData(list); //최초 1회 실행시 값을 집어넣음.

                list_treasure = thread2.getList();
                dao.setData(list_treasure);

                TourDAO tourdao = new TourDAO(this,sysLanguage);
                tourdao.createTable();
                list =  tourdao.selectAll();

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).print();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }else{
            Log.d("tour", "첫번째 실행아님!");
            TourDAO dao = new TourDAO(this,sysLanguage);
            dao.createTable();
            list =  dao.selectAll();

            for (int i = 0; i < list.size(); i++) {
                list.get(i).print();
            }

            Log.d("tour","두번째 실행부터 과연 영어와 한글 db가 다들어가 있니?");
        }





      /*  for (int i = 0; i < list_treasure.size(); i++) {
            list_treasure.get(i).print();
        }*/


        try {
            Thread.sleep(2000); //1초 딜레이
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class)); //메인화면 실행시키고
        finish();      //종료함

    }
}
