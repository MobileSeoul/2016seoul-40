package ssangyong.dclass.seoulexploration.data;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : DAO( Data Access Object)  DB에 접속해서 작업하는 클래스임.
 * * 수정내용 : print()함수 추가 . 데이터값 로그로 출력.
 * * 버  전 : 1.0
 *******************************************************************************/

public class TreasureDAO {

    SQLiteDatabase database;
    String databaseName;
    String tableName;

    public TreasureDAO(Activity activity) {

        databaseName = "seoul";
        tableName = "Treasure";

        try {
            database = activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            println("데이터베이스를 열었습니다. : " + databaseName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 데이터베이스가 안열림 ", e);
        }
    }

    public void createTable() {      //테이블만들기
        try {
            if (database != null) {
                database.execSQL("CREATE TABLE if not exists " + tableName + "("    //if not exists 은 이미 있으면 만들지 않는다.
                        + "number integer PRIMARY KEY autoincrement,"
                        + "name text, "
                        + "note text, "
                        + "name_en text, "
                        + "note_en text, "
                        + "gain integer"
                        + ")");
                println("테이블을 만들었습니다. : " + tableName);
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 테이블생성이 안됨 ", e);
        }
    }

    public void setData(ArrayList<TreasureDTO> list) {  //파싱한거 최초1회 실행
        try {
            if (database != null) {

                for (int i = 0; i < list.size(); i++) {

                    TreasureDTO dto = list.get(i);

                    database.execSQL("INSERT INTO " + tableName + "(number,name,note,name_en,note_en,gain) VALUES "
                            + "("
                            +  Integer.parseInt(dto.getNumber()) + ","
                            + "'" + dto.getName() + "',"
                            + "'" + dto.getNote() + "',"
                            + "'" + dto.getName_en() + "',"
                            + "'" + dto.getNote_en() + "', "
                            + dto.getGain()
                            + ")");
                }
                println("데이터를 추가했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 초기값이 안들어가짐 ", e);
        }
    }

    public void updateGain(int number,int key) {

        try {
            if (database != null) {
                if(key==1)
                    database.execSQL("UPDATE " + tableName + " SET gain = '1' where number='"+number+"'");
                else if(key==0)
                    database.execSQL("UPDATE " + tableName + " SET gain = '0' where number='"+number+"'");

                println("Gain 업데이트를 했습니다.(updateGain)");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 업데이트 안됨", e);
        }
    }

   public ArrayList<TreasureDTO> selectGain() {  //조회하기
        ArrayList<TreasureDTO> list = new ArrayList<TreasureDTO>();
        try {

            if (database != null) {
                Cursor cursor = database.rawQuery("SELECT * FROM " + tableName+" where gain=1", null);

                int count = cursor.getCount();
                println("결과 레코드의 갯수 : " + count);

                for (int i = 0; i < count; i++) {
                    cursor.moveToNext();
                    int number = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String note = cursor.getString(2);
                    int Gain = cursor.getInt(5);

                    println("레코드 #" + i + " : " + number + ", " + name + "," + Gain);

                    TreasureDTO dto = new TreasureDTO(number+"",name,note);
                    list.add(dto);
                }

                cursor.close();  //커서어댑터를 사용해서 리스트뷰에 보여질려면 클로즈를 닫아주어야함.

                println("데이터를 조회했습니다. (selectGain)");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour","[TourDAO] ",e);
        }

        return list;
    }


    public ArrayList<TreasureDTO> selectAll() {  //전부조회하기
        ArrayList<TreasureDTO> list = new ArrayList<TreasureDTO>();
        try {

            if (database != null) {
                Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);

                int count = cursor.getCount();
                println("결과 레코드의 갯수 : " + count);

                for (int i = 0; i < count; i++) {
                    cursor.moveToNext();
                    int number = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String note = cursor.getString(2);
                    int Gain = cursor.getInt(5);

                    println("레코드 #" + i + " : " + number + ", " + name + "," + Gain);

                    TreasureDTO dto = new TreasureDTO(number+"",name,note);
                    list.add(dto);
                }

                cursor.close();  //커서어댑터를 사용해서 리스트뷰에 보여질려면 클로즈를 닫아주어야함.

                println("데이터를 조회했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour","[TourDAO] ",e);
        }


        return list;
    }


    public void delete() {

        try {
            if (database != null) {
                database.execSQL("delete from " + tableName);

                println("데이터를 모두삭제 했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 삭제 안됨. ", e);
        }
    }



    private void println(String data) {
        Log.d("tour", "[dao db] " + data);
    }


}
