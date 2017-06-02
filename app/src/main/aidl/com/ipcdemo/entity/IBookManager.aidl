// IBookManager.aidl
package com.ipcdemo.entity;


import com.ipcdemo.entity.Book;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
