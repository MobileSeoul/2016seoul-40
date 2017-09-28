package ssangyong.dclass.seoulexploration.data;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 서버에 있는 xml을 파싱해서 DTO클래스 담는 클래스. Thread로 구현함.
 * * 수정내용 : 영어버전도 완료.
 * * 버  전 : 1.1
 * *******************************************************************************/

public class TourPaserThread extends Thread {

    private String url;

    private ArrayList<String> number, name, theme,area, latitude, longitude, note, time, phone,traffic;
    private ArrayList<TourDTO> list;

    String langage;


    public TourPaserThread(String langage) {
        this.langage = langage;
        if(langage.equals("ko"))
            url = "http://xml.namoolab.com/kor.xml";    //
        else if(langage.equals("en"))
            url = "http://xml.namoolab.com/eng.xml"; //
    }

    @Override
    public void run() {

        number = new ArrayList<String>();
        name = new ArrayList<String>();
        theme = new ArrayList<String>();
        latitude = new ArrayList<String>();
        longitude = new ArrayList<String>();
        area = new ArrayList<String>();
        phone = new ArrayList<String>();
        note = new ArrayList<String>();
        time= new ArrayList<String>();
        traffic= new ArrayList<String>();

        list = new ArrayList<TourDTO>();


        try {

            URL text = new URL(url); // 파싱하고자하는 URL

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();

            XmlPullParser parser = parserCreator.newPullParser(); // XMLPullParser 사용

            parser.setInput(text.openStream(), null);   // 파싱하기위해서 스트림을 열어야한다.

            int parserEvent = parser.getEventType();  // 파싱할 데이터의 타입을 알려준다.
            String tag;


            boolean inTitle1 = false, inTitle2 = false, inTitle3 = false, inTitle4 = false, inTitle5 = false, inTitle6 = false, inTitle7 = false, inTitle8 = false,inTitle9 = false;
            while (parserEvent != XmlPullParser.END_DOCUMENT) { // xml 파일의 문서 끝인가?
                switch (parserEvent) {

                    case XmlPullParser.TEXT:
                        tag = parser.getName();
                        if (inTitle1) {
                            String max = parser.getText();
                            number.add(max);
                        } else if (inTitle2) {
                            String max = parser.getText();
                            name.add(max);
                        } else if (inTitle3) {
                            String max = parser.getText();
                            theme.add(max);
                        } else if (inTitle4) {
                            String max = parser.getText();
                            area.add(max);
                        } else if (inTitle5) {  //xml에서 좌표로 하나로 묶여있음 x,y가
                            String max = parser.getText();
                            StringTokenizer tok = new StringTokenizer(max,",");
                            latitude.add(tok.nextToken());      //x
                            longitude.add(tok.nextToken());     //y
                        } else if (inTitle6) {
                            String max = parser.getText();
                            note.add(max);
                        } else if (inTitle7) {
                            String max = parser.getText();
                            time.add(max);
                        } else if (inTitle8) {
                            String max = parser.getText();
                            phone.add(max);
                        } else if (inTitle9) {
                            String max = parser.getText();
                            traffic.add(max);
                        }

                        break;

                    case XmlPullParser.START_TAG: // 먼저
                        tag = parser.getName();


                        if (tag.compareTo("번호") == 0) {
                            inTitle1 = true;
                        } else if (tag.compareTo("관광지명") == 0) {
                            inTitle2 = true;
                        } else if (tag.compareTo("테마분류") == 0) {
                            inTitle3 = true;
                        } else if (tag.compareTo("구역") == 0) {
                            inTitle4 = true;
                        } else if (tag.compareTo("좌표") == 0) {
                            inTitle5 = true;
                        } else if (tag.compareTo("설명") == 0) {
                            inTitle6 = true;
                        } else if (tag.compareTo("운영시간") == 0) {
                            inTitle7 = true;
                        } else if (tag.compareTo("전화번호") == 0) {
                            inTitle8 = true;
                        } else if (tag.compareTo("주변교통시설") == 0) {
                            inTitle9 = true;
                        }

                        break;

                    case XmlPullParser.END_TAG: // 나중에
                        tag = parser.getName();
                        if (tag.compareTo("번호") == 0) {
                            inTitle1 = false;
                        } else if (tag.compareTo("관광지명") == 0) {
                            inTitle2 = false;
                        } else if (tag.compareTo("테마분류") == 0) {
                            inTitle3 = false;
                        } else if (tag.compareTo("구역") == 0) {
                            inTitle4 = false;
                        } else if (tag.compareTo("좌표") == 0) {
                            inTitle5 = false;
                        } else if (tag.compareTo("설명") == 0) {
                            inTitle6 = false;
                        } else if (tag.compareTo("운영시간") == 0) {
                            inTitle7 = false;
                        } else if (tag.compareTo("전화번호") == 0) {
                            inTitle8 = false;
                        } else if (tag.compareTo("주변교통시설") == 0) {
                            inTitle9 = false;
                        }

                        break;


                }
                parserEvent = parser.next();

            }
        } catch (Exception e) {
            Log.e("tour", "Error in network call", e);
        }

        Log.d("tour", "크기 1: " + number.size());
        Log.d("tour", "크기 2: " + name.size());
        Log.d("tour", "크기 3: " + theme.size());
        Log.d("tour", "크기 4: " + area.size());
        Log.d("tour", "크기 5: " + latitude.size());
        Log.d("tour", "크기 6: " + longitude.size());
        Log.d("tour", "크기 7: " + note.size());
        Log.d("tour", "크기 8: " + time.size());
        Log.d("tour", "크기 9: " + phone.size());
        Log.d("tour", "크기 10: " + traffic.size());
        Log.d("tour", "langage: " + langage);



        try {
            for (int i = 0; i < traffic.size(); i++) {  //사이즈 바꿔져야함!

                list.add(new TourDTO(number.get(i), name.get(i), theme.get(i), area.get(i),latitude.get(i), longitude.get(i), note.get(i),time.get(i), phone.get(i), traffic.get(i)));
            }
          /*  for (int i = 0; i < number.size(); i++) {
                list.get(i).print();
            }*/


        } catch (Exception e) {
            Log.e("tour", "array null "+langage, e);
        }
    }

    public ArrayList getList() {
        if(list == null) {
            Log.d("tour","ArrayList의 값이 null입니다.");
        } else {
            Log.d("tour","파싱완료");
        }
        return list;
    }

}
