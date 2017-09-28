package ssangyong.dclass.seoulexploration.temp;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 두좌표사이에 거리를 계산해서 반환해주는 함수를 가진 클래스
 * * 수정내용 :
 * * 버  전 : 1.0
 * *******************************************************************************/

public class VectorCalc {

    //거리계산함수
    //반환값은 거리
    public  static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "k") {
            dist = dist * 1.609344;
        } else if (unit == "m") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
