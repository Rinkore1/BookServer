package main.java.com.bookserver.repository;

import main.java.com.bookserver.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
