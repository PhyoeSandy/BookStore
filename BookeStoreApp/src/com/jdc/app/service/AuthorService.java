package com.jdc.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jdc.app.entity.Author;
import com.jdc.app.util.ConnectionManager;

public class AuthorService {
	
	private static AuthorService INSTANCE;
	
	private AuthorService() { }
	
	public static AuthorService getInstance() {
		if(INSTANCE == null)
			INSTANCE = new AuthorService();
		return INSTANCE;
	}
	
	public void add(Author a) {
		String sql = "INSERT INTO author(name, age, country) VALUES (?,?,?)";
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setString(1, a.getName());
			stmt.setInt(2, a.getAge());
			stmt.setString(3, a.getCountry());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			while(rs.next()) {
				a.setId(rs.getInt(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(Author a) {
		String sql = "UPDATE author SET name=?,age=?,country=? WHERE id=?";
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, a.getName());
			stmt.setInt(2, a.getAge());
			stmt.setString(3, a.getCountry());
			stmt.setInt(4, a.getId());
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Author a) {
		String sql = "DELETE FROM author WHERE id=?";
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, a.getId());
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Author> findByParams(String name, int age, String country) {
		String sql = "SELECT * FROM author WHERE 1=1";
		List<Author> aList = new ArrayList<Author>();
		List<Object> paramsList = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder(sql);
		
		if(name != null && !name.isEmpty()) {
			sb.append(" AND name LIKE ?");
			paramsList.add("%".concat(name).concat("%"));
		}
		
		if(age >0) {
			sb.append(" AND age >=?");
			paramsList.add(age);
		}
		
		if(country != null && !country.isEmpty()) {
			sb.append(" AND country LIKE ?");
			paramsList.add("%".concat(country).concat("%"));
		}
		
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {
			
			for(int i=0; i< paramsList.size(); i++) {
				stmt.setObject(i+1, paramsList.get(i));
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				aList.add(getObject(rs));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return aList;
	}
	
	public List<Author> findAll() {
		return findByParams(null, 0, null);
	}
	
	private Author getObject(ResultSet rs) throws SQLException {
		Author a = new Author();
		a.setId(rs.getInt("id"));
		a.setName(rs.getString("name"));
		a.setAge(rs.getInt("age"));
		a.setCountry(rs.getString("country"));
		return a;
	}
	

}
