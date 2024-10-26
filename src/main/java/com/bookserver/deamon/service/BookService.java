package com.bookserver.deamon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.bookserver.deamon.model.Book;
import com.bookserver.deamon.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    /**
     * 获取所有书籍。
     * 使用 Resilience4j 的 CircuitBreaker 进行断路保护。
     *
     * @return 书籍列表
     */
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "getAllBooksFallback")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * 获取分页的书籍列表。
     *
     * @param page 页码，从0开始。
     * @param size 每页的书籍数量。
     * @return 包含书籍的分页对象。
     */
    public Page<Book> getBooksPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    /**
     * 根据 ID 获取书籍。
     * 使用 Resilience4j 的 CircuitBreaker 进行断路保护。
     *
     * @param id 书籍 ID
     * @return 包含书籍的 Optional 对象
     */
    @CircuitBreaker(name = "bookServiceCircuitBreaker", fallbackMethod = "getBookByIdFallback")
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    /**
     * 添加新书籍。
     *
     * @param book 要添加的书籍
     * @return 保存后的书籍
     */
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * 更新书籍信息。
     *
     * @param id   书籍 ID
     * @param book 更新后的书籍信息
     * @return 更新后的书籍，如果 ID 不存在则返回 null
     */
    public Book updateBook(String id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id); // 保证更新的是相同 ID 的书籍
            return bookRepository.save(book);
        }
        return null; // 若找不到 ID 则返回 null，表示更新失败
    }

    /**
     * 根据 ID 删除书籍。
     *
     * @param id 书籍 ID
     */
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    /**
     * 获取所有书籍的回退方法。
     *
     * @param t 触发回退的异常
     * @return 空列表
     */
    public List<Book> getAllBooksFallback(Throwable t) {
        return Collections.emptyList(); // 返回空列表
    }

    /**
     * 根据 ID 获取书籍的回退方法。
     *
     * @param id 书籍 ID
     * @param t  触发回退的异常
     * @return 空的 Optional 对象
     */
    public Optional<Book> getBookByIdFallback(String id, Throwable t) {
        return Optional.empty(); // 返回空结果
    }
}