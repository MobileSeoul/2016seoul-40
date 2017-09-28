package ssangyong.dclass.seoulexploration.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by sist100 on 2016-10-05.
 */

//서버에서 이미지불러오는 부분.
public class ServerGetImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Bitmap bitmap;

    public ServerGetImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            bitmap =mIcon11;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }

    public Bitmap getBitMap() {
        return bitmap;
    }
}