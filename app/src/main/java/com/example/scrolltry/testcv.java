package com.example.scrolltry;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class testcv extends AppCompatActivity implements View.OnClickListener{

    private String CV_TAG = "OpenCV";
    private Button processBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testcv_layout);

        iniLoadOpenCV();//加载OpenCV的本地库
        processBtn = (Button) findViewById(R.id.process_btn);//实例化按钮并添加事件响应
        processBtn.setOnClickListener(this);
    }

    //加载OpenCV的本地库
    private void iniLoadOpenCV(){
        boolean success = OpenCVLoader.initDebug();
        if(success){
            Log.i(CV_TAG,"OpenCV Libraries loaded...");
        }else{
            Toast.makeText(this.getApplicationContext(), "WARNING: Could not load OpenCV Libraries!", Toast.LENGTH_LONG).show();
        }
    }

    //实现事件响应
    @Override
    public void onClick(View v) {
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.bit_logo);
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,bitmap);
        ImageView iv = (ImageView)this.findViewById(R.id.sample_img);
        iv.setImageBitmap(bitmap);
        src.release();
        dst.release();
    }
}