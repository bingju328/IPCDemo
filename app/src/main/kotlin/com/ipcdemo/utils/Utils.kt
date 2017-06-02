package com.ipcdemo.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by bingju on 2017/6/1.
 */
/**
 * 给Any添加锁同步的扩展函数
 * */
fun Any.lock(body: ()->Unit) {
    synchronized(this) {
        body()
    }
}
fun <T>Any.rlock(body: ()-> T): T{
    synchronized(this) {
        return body()
    }
}
/**
 * 给Context添加toast方法
 * */
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,message,duration).show()
}