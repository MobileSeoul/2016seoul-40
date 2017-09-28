package ssangyong.dclass.seoulexploration.view;

import java.util.ArrayList;
import java.util.List;

/********************************************************************************
 * * 작성자 : 박보현
 * * 기  능 :
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/

public class CategoryManager {

    private static List<String> themeList = new ArrayList<String>();
    private static List<String> areaList = new ArrayList<String>();
    private static List<String> cateList = new ArrayList<String>();

    private static List<String> enThemeList = new ArrayList<String>();
    private static List<String> enAreaList = new ArrayList<String>();
    private static List<String> enCateList = new ArrayList<String>();


    static {
        themeList.add("문화유산");
        themeList.add("랜드마크");
        themeList.add("전통문화 체험");
        themeList.add("쇼핑");
        themeList.add("박물관과 갤러리");
        themeList.add("엔터테인먼트");
        themeList.add("공원");

        cateList.add("구역");
        cateList.add("테마");

        areaList.add("강남역");
        areaList.add("광화문");
        areaList.add("남대문시장·남산");
        areaList.add("대학로");
        areaList.add("동대문시장");
        areaList.add("명동·을지로");
        areaList.add("삼성역");
        areaList.add("삼청동·북촌");
        areaList.add("서초");
        areaList.add("송파");
        areaList.add("광화문");
        areaList.add("시청");
        areaList.add("신사동 가로수길");
        areaList.add("신촌·이대");
        areaList.add("압구정동·청담동");
        areaList.add("여의도");
        areaList.add("이태원");
        areaList.add("인사동");
        areaList.add("잠실");
        areaList.add("청계천·종로");
        areaList.add("홍대·상수역");


        enCateList.add("area");
        enCateList.add("theme");

        enThemeList.add("Cultural Heritage");
        enThemeList.add("Landmarks");
        enThemeList.add("Experience Traditional Culture");
        enThemeList.add("Shopping");
        enThemeList.add("Museums and Galleries");
        enThemeList.add("Entertainment");
        enThemeList.add("Parks");


        enAreaList.add("Around Gangnam Station");
        enAreaList.add("Gwanghwamun");
        enAreaList.add("Namdaemun Market · Namsan Mountain");
        enAreaList.add("Daehangno");
        enAreaList.add("Dongdaemun Market");
        enAreaList.add("Myeong-dong · Euljiro");
        enAreaList.add("Around Samseong Station");
        enAreaList.add("Samcheong-dong · Bukchon");
        enAreaList.add("Seoul Forest");
        enAreaList.add("Seocho");
        enAreaList.add("Songpa");
        enAreaList.add("City Hall Area");
        enAreaList.add("Around Sinsa-dong Garosu-gil");
        enAreaList.add("Around Sinchon ·Ewha Womans Univ. Stations");
        enAreaList.add("Apgujeong-dong · Cheongdam-dong");
        enAreaList.add("Yeouido");
        enAreaList.add("Itaewon");
        enAreaList.add("Insa-dong");
        enAreaList.add("Jamsil");
        enAreaList.add("Cheonggyecheon Stream · Jongno");
        enAreaList.add("Around Hongik Univ. · Sangsu Stations");
    }

    public static List<String> getCategory(String sysLanguage) {
        if (sysLanguage.equals("ko")) {
            return cateList;
        } else {
            return enCateList;
        }
    }

    public static List<String> getThemeCategory(String sysLanguage) {
        if (sysLanguage.equals("ko")) {
            return themeList;
        } else {
            return enThemeList;
        }
    }

    public static List<String> getAreaCategory(String sysLanguage) {
        if (sysLanguage.equals("ko")) {
            return areaList;
        } else {
            return enAreaList;
        }
    }

}
