/**
 * 
 */
package com.pon.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pon.entity.Book;

/**
 * @author Sanjeev
 *
 */
@Component
public class BooksRepository {

    @Autowired
    private RedisTemplate<Long, Book> redisTemplate;

    public void save(Book book) {
        redisTemplate.opsForValue()
            .set(book.getId(), book);
    }

    public Book findById(Long id) {
        return redisTemplate.opsForValue()
            .get(id);
    }

}