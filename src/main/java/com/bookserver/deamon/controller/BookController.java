package com.bookserver.deamon.controller;

import com.bookserver.deamon.service.BookService;
import com.bookserver.deamon.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 控制器类，用于处理与书籍相关的 HTTP 请求。
 */
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    /**
     * 获取所有书籍。
     *
     * @return 包含所有书籍的响应实体。
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * 根据 ID 获取书籍。
     *
     * @param id 书籍的 ID。
     * @return 包含书籍的响应实体，如果书籍不存在则返回 404 状态。
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 添加新书籍。
     *
     * @param book 要添加的书籍。
     * @return 包含添加的书籍的响应实体。
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    /**
     * 更新书籍信息。
     *
     * @param id   要更新的书籍的 ID。
     * @param book 更新后的书籍信息。
     * @return 包含更新后书籍的响应实体，如果书籍不存在则返回 404 状态。
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 删除书籍。
     *
     * @param id 要删除的书籍的 ID。
     * @return 空的响应实体。
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}