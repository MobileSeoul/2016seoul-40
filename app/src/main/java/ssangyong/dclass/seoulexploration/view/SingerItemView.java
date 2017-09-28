package ssangyong.dclass.seoulexploration.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ssangyong.dclass.seoulexploration.R;

/********************************************************************************
 * * 작성자 : 정태훈
 * * 기  능 : 리스트뷰에 뿌려지는 아이템들이다. (기본 리스트뷰 아이템 틀)
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/
public class SingerItemView extends LinearLayout {  //리니어레이아웃 상속
    TextView nameTextView,addrTextView;
    ImageView image;

    public SingerItemView(Context context) {
        super(context);

        init(context);
    }

    public SingerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //인플레이터는 시스텀서비스에서 제공해주기 때문에
        inflater.inflate(R.layout.add_area_list_row,this,true);   //상위레이아웃에 아이디값 할필요없는 이유가 R.layout으로 관리하기 떄문에  ★

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        addrTextView = (TextView) findViewById(R.id.addrTextView);
        image = (ImageView)findViewById(R.id.add_area_image);
    }


    public void setName(String name) {
        nameTextView.setText(name);
    }
    public void setAddr(String addr) {
        addrTextView.setText(addr);
    }
    public void setImage(int imageNumber){
        image.setImageResource(imageNumber);
    }
}
