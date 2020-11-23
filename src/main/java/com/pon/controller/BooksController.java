/**
 * 
 */
package com.pon.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pon.entity.Book;
import com.pon.repository.BooksRepository;


/**
 * @author Sanjeev
 *
 */
@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksRepository repository;

    @PostMapping("/create")
    public String newBook(@RequestBody Book newBook) {
        repository.save(newBook);
        return "Added";
    }

    @GetMapping("/get/{id}")
    public Book findOne(@PathVariable Long id) {
        return repository.findById(id);
    }

}