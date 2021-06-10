package com.example.demo.bll.handle;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @author lk
 * @date 2020/5/17
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

	@Nullable
	@Override
	protected Object determineCurrentLookupKey() {
		return DBContextHolder.get();
	}
}
