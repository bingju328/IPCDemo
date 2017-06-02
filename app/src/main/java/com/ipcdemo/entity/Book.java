package com.ipcdemo.entity;

/**
 * Created by bingju on 2017/6/1.
 */

//public class Book implements Parcelable{
//
//    public int bookId;
//    public String bookName;
//
//    public Book(int bookId, String bookName) {
//        this.bookId = bookId;
//        this.bookName = bookName;
//    }
//
//    public int getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(int bookId) {
//        this.bookId = bookId;
//    }
//
//    public String getBookName() {
//        return bookName;
//    }
//
//    public void setBookName(String bookName) {
//        this.bookName = bookName;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(bookId);
//        dest.writeString(bookName);
//    }
//    public static final Creator<Book> CREATOR = new Creator<Book>(){
//
//        @Override
//        public Book createFromParcel(Parcel source) {
//            return new Book(source);
//        }
//
//        @Override
//        public Book[] newArray(int size) {
//            return new Book[size];
//        }
//    };
//    private Book(Parcel in){
//        bookId = in.readInt();
//        bookName = in.readString();
//    }
//
//}
