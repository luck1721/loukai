package com.example.demo.bll.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * @author lk
 * @date 2021/1/19
 */
@NoRepositoryBean
public interface BaseDao<T, I extends Serializable>
		extends PagingAndSortingRepository<T, I>, JpaSpecificationExecutor<T> {

}
