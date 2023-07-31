package com.exam_reflection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    runHandler runHandler = new runHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runThread thread = new runThread();
                thread.start();
            }
        });
    }

    class runThread extends Thread {
        public void run() {
            for (int i = 1; i < 4 ; i++) {
                try {
                    /* key point - start */
                    String className = "com.exam_reflection.test.TestName0" + i;
                    Object object = Class.forName(className).newInstance();
                    Method m = object.getClass().getMethod("runTest", null);
                    Object retObj = m.invoke(object);
                    String returnString = (String) retObj;
                    /* key point - end */

                    Message message = runHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("textView", returnString);
                    message.setData(bundle);
                    runHandler.sendMessage(message);

                    Thread.sleep(1000);
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class runHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String str = bundle.getString("textView");
            textView.setText(str);
        }
    }
}