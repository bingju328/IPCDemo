package com.ipcdemo.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ipcdemo.R
import com.ipcdemo.entity.Book
import com.ipcdemo.entity.IBookManager
import com.ipcdemo.utils.toast
import kotlinx.android.synthetic.main.activity_ktclient.*
/**
 * 1,绑定远程服务。
 * 2,获取服务端实现的Binder实例
 * 3,通过这个实例调用服务端远程方法
 * */
class KTClientActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    //由AIDL生成的类
    private lateinit var mIBookManager: IBookManager
    //标志当前与服务端连接状况的布尔值，false 为未连接，true
    private var mBound: Boolean = false
    //包含Book对象的List
    private lateinit var mBooks: List<Book>

    private val book = Book(333,"天黑以后")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ktclient)
        initEvent()
    }

    private fun initEvent() {
        kotlin_addbook.setOnClickListener {
            if (!mBound) {
                attemptToBindService()
                toast("当前与服务端处于未连接状态，正在尝试重连，请稍后再试")
                return@setOnClickListener
            }
            mIBookManager?.addBook(book)
        }
        kotlin_getbooklist.setOnClickListener {
            if (!mBound) {
                attemptToBindService()
                toast("当前与服务端处于未连接状态，正在尝试重连，请稍后再试")
                return@setOnClickListener
            }
            mBooks = mIBookManager?.bookList ?: mBooks
            var sb = StringBuffer()
            for (book in mBooks) {
                sb.append("{"+book.bookId+"},"+"{"+book.bookName+"}\n")
            }
            content.text = sb.toString()
        }
    }
    fun attemptToBindService() {
        val intent = Intent()
        intent.action = "com.ipcdemo.service.KTAIDLService"
        intent.`package`="com.ipcdemo"
        //1,绑定一个远程的服务
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE)
    }
    var mServiceConnection: ServiceConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "service disconnected")
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "service connected")
            //2,将服务返回的Binder对象换成AIDL接口
            mIBookManager = IBookManager.Stub.asInterface(service)
            mBound = true
            mBooks = mIBookManager?.bookList
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mBound) {
            attemptToBindService();
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            //解绑远程服务
            unbindService(mServiceConnection);
            mBound = false;
        }
    }
}
