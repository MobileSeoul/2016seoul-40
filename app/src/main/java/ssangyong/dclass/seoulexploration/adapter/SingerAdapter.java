package ssangyong.dclass.seoulexploration.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.view.SingerItemView;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 기본적인 리스트뷰에 장착되는 어댑터이다.
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/

public class SingerAdapter extends BaseAdapter {   //자동으로 호촐되는 4가지 오버라이딩 함수들 //어뎁터는 데이터도 관리하고 뷰도 생성한다는 중요함
    Context context;

    public SingerAdapter(Context context) {
        this.context = context;
    }

    ArrayList<TourDTO> items = new ArrayList<TourDTO>(); //우리가 만든 dto타입을 지정


    //아이템삭제해주는거
    public void clearData() {
        items.clear();
    }

    public void addItem(TourDTO item) {
        items.add(item);
    }


    @Override
    public int getCount() { //데이터가 몇개인지 넣어준다.
        return items.size();
    }

    @Override
    public Object getItem(int position) {   //각각 하나하나씩 아이템값
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //뷰를 여기서 만든다.
        //view가  엄청많으면 특히 이미지 메모리가 문제가됨 그래서 재사용 방법으로
        //convertView를 사용하여 메모리를 효율적사용
        SingerItemView view = null;
        if(convertView == null) {
            view = new SingerItemView(context);
        } else {
            view = (SingerItemView)convertView;
        }

        TourDTO curItem = items.get(position);

        view.setName(curItem.getName());
        view.setAddr(curItem.getTheme());

        return view;
    }
}