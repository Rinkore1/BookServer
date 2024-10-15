package main.java.com.bookserver.controller;

import main.java.com.bookserver.repository.BookRepository;
import main.java.com.bookserver.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book book) {
        book.setId(id);
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(id);
    }
}
