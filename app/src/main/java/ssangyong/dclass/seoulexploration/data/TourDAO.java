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

public class TourDAO {

    SQLiteDatabase database;
    String databaseName;
    String tableName;
    String language;

    public static final int TOUR_ADD = 1;   //추가한지역
    public static final int TOUR_DEL = 0;   //추가하지 않은것 or 삭제
    public static final int TOUR_VISIT = 10;    // 다녀온것.

    public TourDAO(Activity activity,String language) {
        this.language = language;
        databaseName = "seoul";
        tableName = "tourTable_"+language;   // 테이블 이름은 tourTable_ko | tourTable_en

        try {
            database = activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            println("데이터베이스를 열었습니다. : " + databaseName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 데이터베이스가 안열림 "+language, e);
        }
    }

    public void createTable() {      //테이블만들기
        try {
            if (database != null) {
                database.execSQL("CREATE TABLE if not exists " + tableName + "("    //if not exists 은 이미 있으면 만들지 않는다.
                        + "number integer PRIMARY KEY autoincrement,"
                        + "name text, "
                        + "theme text, "
                        + "area text, "
                        + "x text, "
                        + "y text, "
                        + "note text, "
                        + "time text, "
                        + "phone text, "
                        + "traffic text, "
                        + "poke integer"
                        + ")");
                println("테이블을 만들었습니다. : " + tableName);
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 테이블생성이 안됨 "+language, e);
        }
    }

    public void setData(ArrayList<TourDTO> list) {  //파싱한거 최초1회 실행
        try {
            if (database != null) {

                for (int i = 0; i < list.size(); i++) {

                    TourDTO dto = list.get(i);

                    database.execSQL("INSERT INTO " + tableName + "(number,name,theme,area,x,y,note,time,phone,traffic,poke) VALUES "
                            + "("
                            +  Integer.parseInt(dto.getNumber()) + ","
                            + "'" + dto.getName() + "',"
                            + "'" + dto.getTheme() + "',"
                            + "'" + dto.getArea() + "',"
                            + "'" + dto.getX() + "',"
                            + "'" + dto.getY() + "',"
                            + "'" + dto.getNote() + "',"
                            + "'" + dto.getTime() + "',"
                            + "'" + dto.getPhone() + "',"
                            + "'" + dto.getTraffic() + "',"
                            + dto.getPoke()
                            + ")");
                }
                println("데이터를 추가했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 초기값이 안들어가짐 "+language, e);
        }
    }

    public void updatePoke(int number,int key) {

        try {
            if (database != null) {

                //database.execSQL("UPDATE " + tableName + " SET poke ='1' where number='"+number+"'");
                database.execSQL("UPDATE " + tableName + " SET poke ='"+key+"' where number='"+number+"'");

                println("poke 업데이트를했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 업데이트 안됨"+language, e);
        }
    }

    public ArrayList<TourDTO> selectPoke(int key) {  //조회하기
        ArrayList<TourDTO> list = new ArrayList<TourDTO>();
        try {

            if (database != null) {
                Cursor cursor = database.rawQuery("SELECT * FROM " + tableName+" where poke='"+key+"'", null);

                int count = cursor.getCount();
                println("결과 레코드의 갯수 : " + count);

                for (int i = 0; i < count; i++) {
                    cursor.moveToNext();
                    int number = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String theme = cursor.getString(2);
                    String area = cursor.getString(3);
                    String x = cursor.getString(4);
                    String y = cursor.getString(5);
                    String note = cursor.getString(6);
                    String time = cursor.getString(7);
                    String phone = cursor.getString(8);
                    String traffic = cursor.getString(9);
                    int poke = cursor.getInt(10);

//                    println("레코드 #" + i + " : " + number + ", " + name + ", " + theme + ", " +","+x+","+y+","+note+ ","+area+","+add_area+","+visit);
                    println("레코드 #" + i + " : " + number + ", " + name + "," + poke);

                    TourDTO dto = new TourDTO(number+"",name,theme,area,x,y,note,time,phone,traffic);
                    list.add(dto);
                }

                cursor.close();  //커서어댑터를 사용해서 리스트뷰에 보여질려면 클로즈를 닫아주어야함.

                println("데이터를 조회했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour","[TourDAO] "+language,e);
        }

        return list;
    }

    public ArrayList<TourDTO> selectAll() {  //전부조회하기
        ArrayList<TourDTO> list = new ArrayList<TourDTO>();
        try {

            if (database != null) {
                Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);

                int count = cursor.getCount();
                println("결과 레코드의 갯수 : " + count);

                /*startManagingCursor(cursor);
                String[] columns = new String[] {"_id", "age", "mobile"};
                int[] to = new int[] {R.id.editText3, R.id.editText4, R.id.editText5};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.customer_item, cursor, columns, to);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged(); //리스트뷰가 업데이트 되는거.*/


                for (int i = 0; i < count; i++) {
                    cursor.moveToNext();
                    int number = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String theme = cursor.getString(2);
                    String area = cursor.getString(3);
                    String x = cursor.getString(4);
                    String y = cursor.getString(5);
                    String note = cursor.getString(6);
                    String time = cursor.getString(7);
                    String phone = cursor.getString(8);
                    String traffic = cursor.getString(9);
                    int poke = cursor.getInt(10);

                    println("레코드 #" + i + " : " + number + ", " + name + "," + poke);

                    TourDTO dto = new TourDTO(number+"",name,theme,area,x,y,note,time,phone,traffic);
                    list.add(dto);
                }

                cursor.close();  //커서어댑터를 사용해서 리스트뷰에 보여질려면 클로즈를 닫아주어야함.

                println("데이터를 조회했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour","[TourDAO] "+language,e);
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
            Log.e("tour", "[dao db] : 삭제 안됨. "+language, e);
        }
    }

    public void deleteTest() {  //테스트용 나중에 삭제

        try {
            if (database != null) {
                database.execSQL("delete from " + tableName+" where number > '115'", null);

                println("데이터를 모두삭제 했습니다.");
            } else {
                println("데이터베이스를 먼저 열어야 합니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tour", "[dao db] : 삭제 안됨. "+language, e);
        }
    }

    private void println(String data) {
        Log.d("tour", "[dao db] " + data);
    }


}
