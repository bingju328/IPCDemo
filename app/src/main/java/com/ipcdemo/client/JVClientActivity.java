package com.ipcdemo.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ipcdemo.R;
import com.ipcdemo.entity.Book;
import com.ipcdemo.entity.IBookManager;

import java.util.List;

public class JVClientActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    //由AIDL生成的类
    private IBookManager mIBookManager = null;
    //标志当前与服务端连接状况的布尔值，false 为未连接，true
    private boolean mBound = false;
    //包含Book对象的List
    private List<Book> mBooks;

    private TextView java_addbook,java_getbooklist,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jvclient);
        initView();
        initEvent();
    }

    private void initView() {
        java_addbook = (TextView) findViewById(R.id.java_addbook);
        java_getbooklist = (TextView) findViewById(R.id.java_getbooklist);
        content = (TextView) findViewById(R.id.content);
    }

    private void initEvent() {
        java_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {
                    attemptToBindService();
                    Toast.makeText(JVClientActivity.this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mIBookManager == null) {return;}
                Book book = new Book(333,"天黑以后");
                try {
                    mIBookManager.addBook(book);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        java_getbooklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {
                    attemptToBindService();
                    Toast.makeText(JVClientActivity.this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mIBookManager == null) {return;}
                try {
                    mBooks = mIBookManager.getBookList();
                    Log.i(TAG,"getBookList()++"+mBooks.toString());
                    StringBuffer sb = new StringBuffer();
                    for (Book book:
                         mBooks) {
                        sb.append("{"+book.getBookId()+"},"+"{"+book.getBookName()+"}\n");
                    }
                    content.setText(sb);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.ipcdemo.service.JVAIDLService");
        intent.setPackage("com.ipcdemo");
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "service connected");

            mIBookManager = IBookManager.Stub.asInterface(service);
            mBound = true;
            if (mIBookManager != null) {
                try{
                    mBooks = mIBookManager.getBookList();
                    Log.i(TAG, mBooks.toString());
                }catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "service disconnected");
            mBound = false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

}
