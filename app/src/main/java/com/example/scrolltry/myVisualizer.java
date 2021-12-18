package com.example.scrolltry;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class myVisualizer {

    private Context context;
    private Mat base_img = new Mat();// 基础图片 TODO:需要从资源管理器中读取id？？
    static double lam = 0.4;

    public myVisualizer(Context context) {
        this.context = context;
        iniLoadOpenCV();
    }

    //加载OpenCV的本地库
    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if (success) {
            Log.i("opencv:", "OpenCV Libraries loaded...");
        } else {
            Log.i("opencv:", "OpenCV Libraries load FAILED!!");
        }
    }

    public Mat print_point_on_baseImg(ArrayList<Integer> rx, ArrayList<Integer> ry) {
        if (base_img == null) return null;
        Mat dst = new Mat();
        float scale=0.5f;
        float width=base_img.width();
        float height=base_img.height();
//        Imgproc.resize(this.base_img, dst, new Size(width*scale , height*scale), lam, lam, Imgproc.INTER_CUBIC);
        Imgproc.resize(this.base_img, dst, new Size(443,850));
        Imgproc.cvtColor(dst,dst,Imgproc.COLOR_BGRA2BGR);
        Toast.makeText(context, "图像大小："+dst.width()+" * "+dst.height() + " * "+dst.channels(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < rx.size(); i++) {
            Imgproc.circle(dst, new Point(rx.get(i), ry.get(i)), 3, new Scalar(0, 0, 220), -1);
        }
        Imgproc.cvtColor(dst,dst,Imgproc.COLOR_BGR2BGRA);
        Imgproc.resize(dst, dst, base_img.size());

        return dst;
    }

    public Mat getBase_img() {
        return base_img;
    }

//    public void setBase_img(Mat base_img) {
//        this.base_img = base_img;
//    }

    public void setBase_img(Resources res, int img_id) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, img_id);
        Utils.bitmapToMat(bitmap, this.base_img);
    }
    public void setBase_img(Bitmap bitmap) {
        Utils.bitmapToMat(bitmap, this.base_img);
    }
}
