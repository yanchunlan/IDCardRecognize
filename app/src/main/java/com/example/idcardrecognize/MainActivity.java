package com.example.idcardrecognize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        System.loadLibrary("OpenCV");
    }

    private ImageView mIvIdcard;
    private Button mBtnInitTest;
    private Button mBtnPre;
    private Button mBtnNext;
    private Button mBtnIdent;
    private TextView mTvText;


    private TessBaseAPI tessBaseAPI;
    private AsyncTask<Void, Void, Boolean> asyncTask;
    private String language = "ck";


    private int index = 0;
    private int[] ids = {R.drawable.id_card0, R.drawable.id_card1,
            R.drawable.id_card2, R.drawable.id_card3,
            R.drawable.id_card4, R.drawable.id_card5,
            R.drawable.id_card6,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initTestAPI() {
        asyncTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = getAssets().open(language + ".traineddata");
                    File file = new File(getExternalCacheDir().getAbsolutePath()+"/tessdata/"
                            + language + ".traineddata");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        fos = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                    is.close();
                    return tessBaseAPI.init(getExternalCacheDir().getAbsolutePath(), language);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != is)
                            is.close();
                        if (null != fos)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                Toast.makeText(MainActivity.this,
                        b ? "初始化OCR成功" : "初始化OCR失败",
                        Toast.LENGTH_SHORT).show();
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
        mBtnInitTest = (Button) findViewById(R.id.btn_init_test);
        mBtnPre = (Button) findViewById(R.id.btn_pre);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnIdent = (Button) findViewById(R.id.btn_ident);
        mTvText = (TextView) findViewById(R.id.tv_text);

        mBtnInitTest.setOnClickListener(this);
        mBtnPre.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnIdent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_init_test:
                initTestAPI();
                break;
            case R.id.btn_pre:
                mIvIdcard.setImageResource(ids[Math.abs(--index % ids.length)]);
                break;
            case R.id.btn_next:
                mIvIdcard.setImageResource(ids[Math.abs(++index % ids.length)]);
                break;
            case R.id.btn_ident:
                // 从原图得到号码区域bitmap
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[Math.abs(index % ids.length)]);
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
