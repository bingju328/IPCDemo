package com.ipcdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ipcdemo.entity.Book
import com.ipcdemo.entity.IBookManager
import com.ipcdemo.utils.lock
import com.ipcdemo.utils.rlock
import java.util.*

/**
 * Created by bingju on 2017/6/1.
 * 1,创建一个Service并注册
 * 2,实现AIDL中的Binder的接口(即Stub)
 * 3,开放给客户端一个action以便其绑定
 * 4,通过onBind返回这个Binder的实现给客户端
 */
class KTAIDLService: Service() {
    val TAG = this.javaClass.simpleName
    var mBooks : List<Book> = ArrayList()
    /**
     * 创建⼀个继承⾃IBookManager.Stub类型的匿名类的对象，即实现AIDL中的Binder的接口
     * */
    var mIBookManager = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            return rlock {
                if (mBooks==null) ArrayList<Book>() else mBooks as MutableList<Book>
            }
        }

        override fun addBook(book: Book?) {
            lock {
                if (book != null) {
                    //TODO 此处这个判断无效
                    if (!(mBooks as MutableList<Book>).contains(book)) {
                        (mBooks as MutableList<Book>).add(book)
                    }
                } else {
                    var booknew = Book(111,"百年孤独")
                    (mBooks as MutableList<Book>).add(booknew)
                }
            }
        }

    }
    override fun onCreate() {
        Log.i(TAG, "onCreate()")
        var book = Book(222,"毒木圣经")
        (mBooks as MutableList<Book>).add(book)
        super.onCreate()
    }
    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, String.format("on bind,intent = %s", intent.toString()))
        return mIBookManager
    }

}