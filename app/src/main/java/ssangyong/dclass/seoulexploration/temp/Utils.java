package ssangyong.dclass.seoulexploration.temp;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ssangyong.dclass.seoulexploration.R;

/********************************************************************************
 * * 작성자 : 박보현
 * * 기  능 :
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/

public class Utils {

    private static int TICK_INTERVAL = 500;
    private static int TOAST_DURATION = 1500;
    public static void log(String msg, Throwable e) {
        Log.i("TEST", msg, e);
    }

    public static void log(String msg) {
        Log.i("TEST", msg);
    }

    public static void toast(Context context, String msg) {

        try {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.toast, null);

            TextView toastText = (TextView) view.findViewById(R.id.toast_text);
            toastText.setText(msg.trim());

//			Utils.log(" - " + msg.length());
            toastText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

//			ImageView toastImg = (ImageView) view.findViewById(R.id.toast_img);
//			int emoticonId = emoticonList[random.nextInt(emoticonList.length)];
//			toastImg.setBackgroundResource(emoticonId);

            final Toast toast = new Toast(context);
            toast.setView(view);
            toast.setGravity(Gravity.BOTTOM, 0, 350);
            toast.show();



            CountDownTimer toastCountDown = new CountDownTimer(TOAST_DURATION, TICK_INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    toast.show();
                }
                public void onFinish() {
                    toast.cancel();
                }
            };

            toast.show();
            toastCountDown.start();


        } catch (Exception e) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
