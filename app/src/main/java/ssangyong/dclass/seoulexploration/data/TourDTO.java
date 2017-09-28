package ssangyong.dclass.seoulexploration.data;


import android.util.Log;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : DTO(Data Transfer Object)  데이터를 전달및 관리 클래스입니다.
 * * 수정내용 : db컬럼 1개 추가. (추가 및 방문 유무 확인 컬럼)
 * * 버  전 : 1.1
 * *******************************************************************************/

public class TourDTO {
    private String number;    //번호
    private String name;    //관광지명
    private String theme;    //테마
    private String area;    //구역
    private String x;       //위도 (37.xxxxxx)
    private String y;       //경도 (128.xxxxxx)       //xml자료에는 좌표로 x,y 가 묶여있음.
    private String note;    //설명
    private String time;    //운영시간
    private String phone;   //전화번호
    private String traffic; //주변교통시설

    private int poke; //내가 추가한지역
    private boolean isChecked = false; // 체크박스 체크 여부


    public TourDTO() {

    }

    public TourDTO(String number, String name, String theme,String area, String x, String y,String note,String time, String phone, String traffic,int poke) {
        this.number = number;
        this.name = name;
        this.theme = theme;
        this.area = area;
        this.note = note;
        this.phone = phone;
        this.time = time;
        this.traffic = traffic;
        this.x = x;
        this.y = y;
        this.poke = poke;
    }

    public TourDTO(String number, String name, String theme,String area, String x, String y,String note,String time, String phone, String traffic) {
       this(number, name, theme, area, x, y, note, time, phone, traffic,0);
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getPoke() {
        return poke;
    }

    public void setPoke(int poke) {
        this.poke = poke;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }



    public void print() {
        String str =  number+"/"+ name+"/"+ theme+"/"+area+"/"+ x+"/"+ y+"/"+ note+"/"+ phone+"/"+ time+"/"+traffic+"/"+poke;
        Log.d("tour","print() : "+str);
    }
}
