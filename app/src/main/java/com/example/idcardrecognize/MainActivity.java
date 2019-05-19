package com.example.idcardrecognize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        System.loadLibrary("OpenCV");
    }

    private ImageView mIvIdcard;
    private Button mBtnPre;
    private Button mBtnNext;
    private Button mBtnIdent;
    private TextView mTvText;

    private TessBaseAPI tessBaseAPI;
    private AsyncTask<Void,Void,Boolean> asyncTask;
    private String language = "";

    private int index = 0;
    private int[] ids = {R.drawable.idcard1,R.drawable.idcard2,R.drawable.idcard3};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initTestAPI();
    }

    private void initTestAPI() {
        asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                InputStream in = null;
                FileOutputStream fos = null;
                try {
                    in = getAssets().open(language + ".traineddata");
                    File file = new File("");


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);


            }
        };
        asyncTask.execute();
    }

    private void initData() {
        tessBaseAPI = new TessBaseAPI();
        mIvIdcard.setImageResource(ids[Math.abs(index % ids.length)]);
    }

    private void initView() {
        mIvIdcard = (ImageView) findViewById(R.id.iv_idcard);
        mBtnPre = (Button) findViewById(R.id.btn_pre);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnIdent = (Button) findViewById(R.id.btn_ident);
        mTvText = (TextView) findViewById(R.id.tv_text);

        mBtnPre.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnIdent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pre:
                mIvIdcard.setImageResource(ids[Math.abs(--index % ids.length)]);
                break;
            case R.id.btn_next:
                mIvIdcard.setImageResource(ids[Math.abs(++index % ids.length)]);
                break;
            case R.id.btn_ident:
                // 从原图得到号码区域bitmap
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[index]);
                Bitmap idNumber = findIdNumber(bitmap, Bitmap.Config.ARGB_8888);
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (idNumber == null) {
                    return;
                }
                mIvIdcard.setImageBitmap(idNumber);

                // OCR 文字识别
                tessBaseAPI.setImage(idNumber);
                mTvText.setText(tessBaseAPI.getUTF8Text());
                break;
        }
    }


    private native Bitmap findIdNumber(Bitmap bitmap, Bitmap.Config argb8888);


}
