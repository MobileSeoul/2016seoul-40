package ssangyong.dclass.seoulexploration.data;

import java.util.ArrayList;
import java.util.List;

import ssangyong.dclass.seoulexploration.temp.Utils;

import static ssangyong.dclass.seoulexploration.activity.LoadActivity.sysLanguage;


/********************************************************************************
 * * 작성자 : 박보현
 * * 기  능 :
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/
public class XMLDataManager {

    private static List<TourDTO> TOUR_INFO_LIST = new ArrayList<>();
    private static TourPaserThread PARSER_THREAD = new TourPaserThread(sysLanguage);
    public static List<TourDTO> getTourList() {

        if (TOUR_INFO_LIST.isEmpty()) {
            PARSER_THREAD.start();
            try {
                PARSER_THREAD.join();  // 작업이 끝날떄까지 기달린다.
                TOUR_INFO_LIST = PARSER_THREAD.getList();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (TourDTO tour : TOUR_INFO_LIST) {
//                Utils.log(tour.getNumber()+". "+tour.getTheme()+" > "+tour.getName()+" > "+tour.getArea());
            }
        }
        return TOUR_INFO_LIST;
    }

    public static void loadData() {
        getTourList();
    }

    public static List<TourDTO> searchByCategory(String cate, String subCate) {
        List<TourDTO> result = new ArrayList<>();
        List<TourDTO> tourList = getTourList();

        Utils.log("# SEARCH BY CATEGORY: "+cate+" > "+subCate+" in "+tourList.size());
        for (TourDTO tour : tourList) {
            if (cate.equals("구역")) {
                if (tour.getArea().replaceAll("\\s","").equals(subCate.trim().replaceAll("\\s",""))) {
                    result.add(tour);
                }
            } else if (cate.equals("테마")) {
                if (tour.getTheme().equals(subCate)) {
                    result.add(tour);
                }
            }
        }
        Utils.log("# SEARCH RESULT " + result.size()+"개");
        return result;
    }

    public static List<TourDTO> searchByKeyword(String query) {
        List<TourDTO> result = new ArrayList<>();
        List<TourDTO> tourList = getTourList();

        Utils.log("# SEARCH BY KEYWORD: "+query+" in "+tourList.size());
        for (TourDTO tour : tourList) {

            String target = tour.getName().replaceAll("\\s", "");
            query = query.replaceAll("\\s", "");
            if (target.equals(query) || target.contains(query)) {
                result.add(tour);
            }
        }
        Utils.log("# SEARCH RESULT "+result.size()+"개");
        return result;
    }

}
