package tw.katy.com.entity;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;

public class SqlEntity {
	
	private String sql;
	
	private Map<Integer,Object> args = Maps.newHashMap();
	
	public SqlEntity() {
	}

	public SqlEntity(String sql, Map<Integer, Object> args) {
		super();
		this.sql = sql;
		this.args = args;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<Integer, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<Integer, Object> args) {
		this.args = args;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
