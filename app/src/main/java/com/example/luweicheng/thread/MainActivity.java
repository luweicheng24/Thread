package com.example.luweicheng.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView timeView;
    private MyHandler myHandler;
    private Thread1 myThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = (TextView) findViewById(R.id.time);
        myThread=new Thread1();
        myThread.start();
        findViewById(R.id.sendFromMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg=new Message();
                       msg=myThread
                        .getHandler()
                        .obtainMessage();
                Bundle b=new Bundle();
                b.putString("data","你好。子线程");
                msg.setData(b);
                myThread.getHandler().sendMessage(msg);
            }
        });
        myHandler = new MyHandler(MainActivity.this.getMainLooper());
        findViewById(R.id.startNewThread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyThread myThread = new MyThread();
                new Thread(myThread).start();

            }
        });
        findViewById(R.id.startAnotherActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ImageActivity.class));
            }
        });
        findViewById(R.id.postButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (true) {
                            myHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String time = String
                                            .format(new Date()
                                                            .toLocaleString(),
                                                    "yyyy-MM-dd hh:mm:ss.SSS");
                                    timeView.setText(time);
                                }
                            }, 1000);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }.start();
            }
        });

    }

    class MyThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                Message message = myHandler.obtainMessage();
                Bundle b = new Bundle();

                b.putString("time", String.format(new Date().toLocaleString(), "yyyy-MM-dd hh:mm:ss.SSS"));
                message.setData(b);
                myHandler.sendMessage(message);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyHandler extends Handler {
        public MyHandler() {

        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundl = msg.getData();
            String string = (String) bundl.get("time");
            timeView.setText(string);
        }
    }

class Thread1 extends Thread{
    Handler handler=null;
    /*Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String data=(String)bundle.get("data");
            timeView.setText(data);
        }
    };*/
   public Handler getHandler() {
       return handler;
   }
    @Override
    public void run() {
        Looper.prepare();
        super.run();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
               Bundle bundle= msg.getData();
                String string=(String)bundle.get("data");
                Log.d("子线程",string);
            }
        };
        Looper.loop();

    }
}
}
