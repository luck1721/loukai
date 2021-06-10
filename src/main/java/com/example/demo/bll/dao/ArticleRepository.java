package com.example.demo.bll.dao;

import com.example.demo.bll.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/*@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Article findByIdForUpdate(Integer id);
}*/
