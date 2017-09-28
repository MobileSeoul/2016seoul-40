package ssangyong.dclass.seoulexploration.data;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 서버에 있는 xml을 파싱해서 DTO클래스 담는 클래스. Thread로 구현함.
 * * 수정내용 :
 * * 버  전 : 1.1
 * *******************************************************************************/

public class TreasurePaserThread extends Thread {

    private String url;

    private ArrayList<String> number,name,note;
    private ArrayList<TreasureDTO> list;
    String langage;

    public TreasurePaserThread(String langage) {
        this.langage = langage;
        if(langage.equals("ko"))
            url = "http://xml.namoolab.com/treasure.xml";
        else if(langage.equals("en"))
            url = "http://xml.namoolab.com/treasure.xml";
    }

    @Override
    public void run() {

        number = new ArrayList<String>();
        name = new ArrayList<String>();
        note = new ArrayList<String>();


        list = new ArrayList<TreasureDTO>();


        try {

            URL text = new URL(url); // 파싱하고자하는 URL

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();

            XmlPullParser parser = parserCreator.newPullParser(); // XMLPullParser 사용

            parser.setInput(text.openStream(), null);   // 파싱하기위해서 스트림을 열어야한다.

            int parserEvent = parser.getEventType();  // 파싱할 데이터의 타입을 알려준다.
            String tag;


            boolean inTitle1 = false, inTitle2 = false, inTitle3 = false;
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
                            note.add(max);
                        }

                        break;

                    case XmlPullParser.START_TAG: // 먼저
                        tag = parser.getName();


                        if (tag.compareTo("번호") == 0) {
                            inTitle1 = true;
                        } else if (tag.compareTo("한글명칭") == 0) {
                            inTitle2 = true;
                        } else if (tag.compareTo("설명") == 0) {
                            inTitle3 = true;
                        }

                        break;

                    case XmlPullParser.END_TAG: // 나중에
                        tag = parser.getName();
                        if (tag.compareTo("번호") == 0) {
                            inTitle1 = false;
                        } else if (tag.compareTo("한글명칭") == 0) {
                            inTitle2 = false;
                        } else if (tag.compareTo("설명") == 0) {
                            inTitle3 = false;
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
        Log.d("tour", "크기 3: " + note.size());

        try {
            for (int i = 0; i < number.size(); i++) {  //사이즈 바꿔져야함!

                list.add(new TreasureDTO(number.get(i), name.get(i), note.get(i)));
            }
          /*  for (int i = 0; i < number.size(); i++) {
                list.get(i).print();
            }*/


        } catch (Exception e) {
            Log.e("tour", "array null", e);
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
