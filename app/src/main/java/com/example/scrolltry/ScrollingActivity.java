package com.example.scrolltry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scrolltry.databinding.ActivityScrollingBinding;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "尚未开放", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        String[] S_set = new String[]{"丹枫A"};
//        String[] T_set = new String[]{"丹枫B"};
        Intent intent = getIntent();
        String S = intent.getStringExtra("start");
        String T = intent.getStringExtra("end");

        //TODO: 字符串切割集合处理
        String[] S_set = new String[]{S};
        String[] T_set = new String[]{T};
        myVisualizer myVis_ = new myVisualizer(this);
        Solver_class solver_ = new Solver_class(this);
        myPath opt_path = solver_.solve(S_set, T_set);
        TextView tv_ans = (TextView) findViewById(R.id.answer_text);
        tv_ans.setText(opt_path.getAns());

        //可视化
        Bitmap ans_bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.north);
        myVis_.setBase_img(BitmapFactory.decodeResource(this.getResources(), R.mipmap.north));
        Mat path_map = myVis_.print_point_on_baseImg(opt_path.getRx(), opt_path.getRy());
//        Bitmap ans_bitmap = Bitmap.createBitmap(path_map.width(),path_map.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(path_map,ans_bitmap);
        // 11.23 先做一个静态的添加吧 TODO:动态添加fragment
        ImageView iv = (ImageView) this.findViewById(R.id.first_ans_img);
        iv.setImageBitmap(ans_bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //openCV4Android 需要加载用到
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
//                    mOpenCvCameraView.enableView();
//                    mOpenCvCameraView.setOnTouchListener(ColorBlobDetectionActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

}