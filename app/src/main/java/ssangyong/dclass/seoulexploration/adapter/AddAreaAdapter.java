package ssangyong.dclass.seoulexploration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.handler.AsyncCallBack;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 기본적인 리스트뷰에 장착되는 어댑터이다.
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/

public class AddAreaAdapter extends BaseAdapter {   //자동으로 호촐되는 4가지 오버라이딩 함수들 //어뎁터는 데이터도 관리하고 뷰도 생성한다는 중요함
    Context context;
    private LayoutInflater inflater;
    private ViewHolder holder;
    AsyncCallBack<String> callBack;

    public AddAreaAdapter(Context context) {
        this.context = context;
    }

    List<TourDTO> tourList = new ArrayList<TourDTO>(); //우리가 만든 dto타입을 지정
    Boolean IS_EDIT_MODE = false;


    //아이템삭제해주는거
    public void clearData() {
        tourList.clear();
    }

    public void addItem(TourDTO item) {
        tourList.add(item);
    }


    @Override
    public int getCount() { //데이터가 몇개인지 넣어준다.
        return tourList.size();
    }

    @Override
    public TourDTO getItem(int position) {   //각각 하나하나씩 아이템값
        return tourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void checkAll(boolean isCheckAll) {
        if (isCheckAll) {
            for (TourDTO tour : tourList) {
                tour.setIsChecked(true);
            }
        } else {
            for (TourDTO tour : tourList) {
                tour.setIsChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<TourDTO> list) {
        this.tourList = list;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView nameTextView,addrTextView;
        ImageView image;
        CheckBox checkBox;
    }

    public AddAreaAdapter(Context context, List<TourDTO> tourList, AsyncCallBack<String> callBack) {
        this.tourList = tourList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.callBack = callBack;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //뷰를 여기서 만든다.

        final TourDTO tour = getItem(position);

        View rowView = convertView;
        if (rowView == null) {
            holder = new ViewHolder();

            rowView = inflater.inflate(R.layout.add_area_list_row, parent, false);

            holder.checkBox = (CheckBox) rowView.findViewById(R.id.delCheckBox);
            holder.nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
            holder.addrTextView = (TextView) rowView.findViewById(R.id.addrTextView);
            holder.image = (ImageView) rowView.findViewById(R.id.add_area_image);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.nameTextView.setText(tour.getName());
        holder.addrTextView.setText(tour.getTheme());

        holder.checkBox.setTag(tour);
        holder.checkBox.setFocusable(false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TourDTO tour = (TourDTO) buttonView.getTag();
                if (isChecked) {
                    tour.setIsChecked(true);
                } else {
                    tour.setIsChecked(false);
                }

                callBack.call("CHECK");
                notifyDataSetChanged();
            }
        });

        if (tour.isChecked()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        if (IS_EDIT_MODE) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        return rowView;
    }

    public void toggleEditMode(boolean isEditMode) {
        IS_EDIT_MODE = isEditMode;

        notifyDataSetChanged();
    }
}