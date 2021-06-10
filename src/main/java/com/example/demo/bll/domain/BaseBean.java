package com.example.demo.bll.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Bean基础类
 * 
 * @created 2016-5-19
 * @author  huanglj
 */
public abstract class BaseBean implements Bean {
	private static final long serialVersionUID = -3707046914855595598L;

	/**
	 * @see Object#hashCode()
	 * @created 2016年5月28日
	 * @author  huanglj
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * @see Object#equals(Object)
	 * @created 2016年5月28日
	 * @author  huanglj
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * @see Object#toString()
	 * @created 2016-5-19
	 * @author  huanglj
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 浅层复制(如果属性为引用类型则只复制属性的引用值)当前对象
	 * 
	 * @param <T>
	 * @return
	 * @created 2016-5-19
	 * @author  huanglj
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseBean> T shallowClone() {
		try {
			return (T)clone();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 深层复制当前对象
	 * 
	 * @param <T>
	 * @return
	 * @created 2016-5-19
	 * @author  huanglj
	 */
	/*@SuppressWarnings("unchecked")
	public <T extends BaseBean> T deepClone() {
		try {
			return (T) CloneUtils.recursiveClone(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

}
