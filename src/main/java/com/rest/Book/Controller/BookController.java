package com.rest.Book.Controller;

import com.rest.Book.Entities.Book;
import com.rest.Book.DataAccess.BookRepository;
import com.rest.Book.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookApi")
public class BookController {
    private final BookRepository bookRepository;
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @GetMapping("/books")
    public List<Book> getAllBooks(){
        List<Book> listBooks = bookRepository.findAll();
        return listBooks;
    }

    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable(value = "id") Long id){
        return bookRepository.findById(id).get();
    }

    @PostMapping("/books")
    public void createBook(@RequestBody Book book){
        bookRepository.save(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Long id, @RequestBody Book bookDeatils) throws ResourceNotFoundException {
        Book updatedBook = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not exist with id: " + id));

        updatedBook.setId(bookDeatils.getId());
        updatedBook.setName(bookDeatils.getName());
        updatedBook.setAuthor(bookDeatils.getAuthor());
        bookRepository.save(updatedBook);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public Map<String, Boolean> deleteBook(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + id));

        bookRepository.delete(book);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
