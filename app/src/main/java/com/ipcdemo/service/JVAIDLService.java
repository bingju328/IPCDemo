package com.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ipcdemo.entity.Book;
import com.ipcdemo.entity.IBookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingj on 2017/5/12.
 */

public class JVAIDLService extends Service{
    public final String TAG = this.getClass().getSimpleName();
    private List<Book> mBooks = new ArrayList<>();

    //由AIDL文件生成的BookManager
    private final IBookManager.Stub iBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.i(TAG, "invoking getBookList() method , now the list is : " + mBooks.toString());
            synchronized (this) {
                if (mBooks != null) {
                    return mBooks;
                }
            }
            return new ArrayList<>();
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized(this){
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if (book==null) {
                    Log.i(TAG , "Book is null in In");
                    book = new Book(111,"百年孤独");
                }
                //此判断无效 因为对象传递到远程服务端，在服务端又创建了一个对象
                //即：此book是服务端新建的一个值一样的Book对象
//                if (!mBooks.contains(book)) {
//                    mBooks.add(book);
//                }
                if (!isContain(mBooks,book)) mBooks.add(book);
                //打印mBooks列表，观察客户端传过来的值
                Log.i(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
            }
        }
    };

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        Book book = new Book(222,"毒木圣经");
        mBooks.add(book);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, String.format("on bind,intent = %s", intent.toString()));
        return iBookManager;
    }
    private boolean isContain(List<Book> arrayList,Book book){
        for (Book b:
             arrayList) {
            if (b.getBookId() == book.getBookId()) {
                return true;
            }
        }
        return false;
    }
}
