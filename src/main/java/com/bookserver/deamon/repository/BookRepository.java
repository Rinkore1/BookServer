package com.bookserver.deamon.repository;

import com.bookserver.deamon.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
