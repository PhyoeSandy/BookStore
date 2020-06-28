package com.jdc.app.service;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdc.app.entity.Category;
import com.jdc.app.util.ConnectionManager;

public class CategoryService {
	
	private static CategoryService INSTANCE;
	
	private CategoryService() {}
	
	public static CategoryService getInstance() {
		if(INSTANCE == null) 
			INSTANCE = new CategoryService();
		return INSTANCE;
	}
	
	public void add(Category c) {
		String sql = "INSERT INTO category(name) VALUES(?)";
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setString(1, c.getName());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			while(rs.next()) {
				c.setId(rs.getInt(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(Category c) {
		String sql = "UPDATE category SET name=? WHERE id=?";
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, c.getName());
			stmt.setInt(2, c.getId());
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Category c) {
		String sql = "DELETE FROM category WHERE id=?";
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setInt(1, c.getId());
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Category> findByName(String name) {
		String sql = "SELECT * FROM category WHERE 1=1";
		List<Category> cList = new ArrayList<Category>();
		boolean isConcat = name != null && !name.isEmpty();
		
		if(isConcat) {
			sql = sql.concat(" AND name LIKE ?");
		}
		
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			if(isConcat) {
				stmt.setString(1, "%".concat(name).concat("%"));
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				cList.add(getObject(rs));
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
 		return cList;
	}
	
	public void upload(File file) {
		
		try {
			Files.readAllLines(file.toPath()).stream()
											 .map(Category::new)
										     .forEach(this::add);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Category getObject(ResultSet rs) throws SQLException {
		Category c = new Category();
		c.setId(rs.getInt("id"));
		c.setName(rs.getString("name"));
		return c;
	}

}
