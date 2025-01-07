package com.bookserver.deamon.controller;

import com.bookserver.deamon.service.BookService;
import com.bookserver.deamon.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
     * 获取分页的书籍列表。
     *
     * @param page 页码，从0开始，默认为0。
     * @param size 每页的书籍数量，默认为10。
     * @return 包含书籍的分页对象的响应实体。
     */
    @GetMapping
    public ResponseEntity<Page<Book>> getBooksPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getBooksPaginated(page, size));
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
        Book createdBook = bookService.addBook(book);
        if (createdBook != null) {
            return ResponseEntity.ok(createdBook);
        }
        return ResponseEntity.badRequest().build();
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
        if (bookService.deleteBook(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 热门书籍推荐
    @GetMapping("/recommend/top")
    public ResponseEntity<List<Book>> getTopBooks(
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getTopBooks(size));
    }

    // 随机推荐书籍
    @GetMapping("/recommend/random")
    public ResponseEntity<List<Book>> getRandomBooks(
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getRandomBooks(size));
    }

    // 基于用户偏好的书籍推荐
    @GetMapping("/recommend/user/{userId}")
    public ResponseEntity<List<Book>> getUserRecommendedBooks(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getUserRecommendedBooks(userId, size));
    }

    // 搜索书籍
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }
}
