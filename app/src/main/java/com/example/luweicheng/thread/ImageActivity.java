package com.example.luweicheng.thread;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by luweicheng on 2016/4/21.
 */
public class ImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private Button button1,button2;
    private static String url="http://p2.so.qhimg.com/bdr/_240_/t01e767afd2dbff19ac.jpg";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        textView= (TextView) findViewById(R.id.text);
        imageView= (ImageView) findViewById(R.id.imageView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.downImage).setOnClickListener(this);
        findViewById(R.id.startProgress).setOnClickListener(this);
        findViewById(R.id.startActivity).setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.startProgress:
                AsyncTask asyncTask=new AsyncTask();
                asyncTask.execute();
                break;
            case R.id.downImage:
                ImageAsyncTask imageAsyncTask=new ImageAsyncTask();
                //通过调用execute执行异步处理任务，相当执行一个线程的start方法
                imageAsyncTask.execute(url);
                break;
            case R.id.startActivity:
                startActivity(new Intent(ImageActivity.this,Refersh.class));
                break;
        }
    }

    class AsyncTask extends android.os.AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //运行在UI线程
            Log.d(Thread.currentThread().getName(),"准备工作");
        }

        @Override
        protected String doInBackground(String... params) {
            //独自开辟一个线程AsyncTask
            int i=0;
            String str="下载成功";
            while(i<100){
                i++;
                publishProgress(i);//将在AsyncTask线程中的数据通过该
                              // 方法将数据传送到onProgressUpdate(Integer... values)方法中。
                Log.d(Thread.currentThread().getName(),i+"");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            //运行在主线程。当AsyncTask结束时返回一个结果给主线程
            super.onPostExecute(s);
            Log.d(Thread.currentThread().getName(),"下载完成");
            textView.setText("下载完成");


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //运行在UI线程，随时获取该ASyncTask线程传过来的数据存入数组中
            Log.d(Thread.currentThread().getName(),"下载了"+values[0]);
            super.onProgressUpdate(values);
            int i=values[0];
            textView.setText("正在下载%"+i);
            progressBar.setProgress(i);


        }
    }
    class ImageAsyncTask extends android.os.AsyncTask<String,Integer,Bitmap>{
        //异步处理前的操作onPreExecute
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);//设置进度条可见
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url=params[0];//获取url参数
            Bitmap image=null;
            URLConnection connection=null;
            InputStream is;
            try {
                connection=new URL(url).openConnection();
                is=connection.getInputStream();//连接到输入流
                BufferedInputStream bis=new BufferedInputStream(is);
                image=BitmapFactory.decodeStream(bis);
                Thread.sleep(3000);
                bis.close();
                is.close();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            textView.setText("正在下载");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
        }
    }
}
