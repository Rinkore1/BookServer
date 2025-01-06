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
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

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
        try {
            return bookRepository.save(book);
        } catch (Exception e) {
            // 可以记录日志或处理异常
            return null;
        }
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
     * @return 如果成功删除返回 true，否则返回 false
     */
    public boolean deleteBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
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

    // 热门书籍推荐（按受欢迎度排序）
    public List<Book> getTopBooks(int size) {
        // 创建分页请求，按 popularity 降序排序
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "popularity"));
        return bookRepository.findTopBooks(pageable);
    }

    // 随机推荐书籍
    public List<Book> getRandomBooks(int size) {
        List<Book> allBooks = bookRepository.findAll();
        Random random = new Random();
        return allBooks.stream()
                .sorted((o1, o2) -> random.nextInt(2) - 1) // 随机排序
                .limit(size)
                .collect(Collectors.toList());
    }

    // 基于用户偏好的书籍推荐
    public List<Book> getUserRecommendedBooks(String userId, int size) {
        // 模拟用户偏好数据：在实际系统中，这应该从用户行为记录中获取
        // TODO: 从用户行为记录中获取用户偏好数据
        Map<String, List<String>> userPreferences = getUserPreferences();

        // 获取用户偏好的作者或关键词
        List<String> preferences = userPreferences.getOrDefault(userId, List.of());

        // 从数据库中查找符合偏好条件的书籍
        List<Book> preferredBooks = preferences.stream()
                .flatMap(preference -> bookRepository.findByTitleContainingIgnoreCase(preference).stream())
                .collect(Collectors.toList());

        // 如果偏好书籍不足，补充随机书籍
        if (preferredBooks.size() < size) {
            List<Book> randomBooks = getRandomBooks(size - preferredBooks.size());
            preferredBooks.addAll(randomBooks);
        }

        // 返回限定数量的书籍
        return preferredBooks.stream().limit(size).collect(Collectors.toList());
    }

    // 模拟用户偏好数据
    // TODO: 从用户行为记录中获取用户偏好数据
    private Map<String, List<String>> getUserPreferences() {
        Map<String, List<String>> preferences = new HashMap<>();
        preferences.put("user1", Arrays.asList("Java", "Spring"));
        preferences.put("user2", Arrays.asList("Python", "Machine Learning"));
        return preferences;
    }
}
