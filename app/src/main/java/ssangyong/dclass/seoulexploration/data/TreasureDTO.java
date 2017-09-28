package ssangyong.dclass.seoulexploration.data;


import android.util.Log;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 유물관련 데이터 DTO 클래스
 * * 수정내용 :
 * * 버  전 : 1.0
 * *******************************************************************************/

public class TreasureDTO {
    private String number;    //번호
    private String name;    //명칭
    private String note;    //설명

    private String name_en;    //명칭
    private String note_en;    //설명
    private int gain;       //얻었는지 판별 boolean할려고 했는데 일단

    public TreasureDTO() {

    }

    public TreasureDTO(String number, String name, String note) {
        this.name = name;
        this.note = note;
        this.number = number;

        this.gain = 0;

        this.note_en = "no English";
        this.name_en = "no English";

    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }
    public String getNote_en() {
        return note_en;
    }
    public void setNote_en(String note_en) {
        this.note_en = note_en;
    }
    public int getGain() {
        return gain;
    }
    public void setGain(int gain) {
        this.gain = gain;
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

    public void print() {
        String str =  number+"/"+ name+"/"+ note+"/"+gain;
        Log.d("tour","print() : "+str);
    }
}
