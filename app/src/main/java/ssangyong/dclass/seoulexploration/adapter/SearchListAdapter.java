package ssangyong.dclass.seoulexploration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.data.TourDTO;

/********************************************************************************
 * * 작성자 : 박보현
 * * 기  능 :
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/

public class SearchListAdapter extends BaseAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    private List<TourDTO> tourList;
    private String type;
    Context context;

    static class ViewHolder {
        TextView title;
        TextView subTitle;

    }

    public SearchListAdapter(Context context, List<TourDTO> tourList, String type) {
        this.tourList = tourList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        final TourDTO tour = getItem(position);
        if (rowView == null) {
            holder = new ViewHolder();

            rowView = inflater.inflate(R.layout.search_list_row, parent, false);

            holder.title = (TextView) rowView.findViewById(R.id.search_item_title);
            holder.subTitle = (TextView) rowView.findViewById(R.id.search_item_sub_title);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.title.setText(tour.getName().trim());
        if (type.equals("AREA")) {
            holder.subTitle.setText(tour.getTheme().trim());
        } else {
            holder.subTitle.setText(tour.getArea().trim());
        }


        return rowView;
    }

    @Override
    public int getCount() {
        return this.tourList.size();
    }

    @Override
    public TourDTO getItem(int position) {
        return tourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
