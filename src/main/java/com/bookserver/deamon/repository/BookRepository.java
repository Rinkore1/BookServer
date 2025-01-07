package com.bookserver.deamon.repository;

import com.bookserver.deamon.model.Book;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends MongoRepository<Book, String> {
    @Query("{}")
    List<Book> findTopBooks(Pageable pageable); // 按分页获取热门书籍

    List<Book> findByTitleContainingIgnoreCase(String keyword); // 根据关键词搜索书籍

    Page<Book> findByTitleContainingIgnoreCase(String keyword, Pageable pageable); // 根据关键词分页搜索书籍
}
