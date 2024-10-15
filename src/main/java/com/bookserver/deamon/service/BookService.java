package com.bookserver.deamon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookserver.deamon.model.Book;
import com.bookserver.deamon.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(String id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id); // 保证更新的是相同 ID 的书籍
            return bookRepository.save(book);
        }
        return null; // 若找不到 ID 则返回 null，表示更新失败
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
