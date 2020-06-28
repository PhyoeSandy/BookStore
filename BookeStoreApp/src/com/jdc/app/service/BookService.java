package com.jdc.app.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jdc.app.entity.Author;
import com.jdc.app.entity.Book;
import com.jdc.app.entity.Category;
import com.jdc.app.util.ConnectionManager;

public class BookService {

	private static BookService INSTANCE;

	private BookService() {
	}

	public static BookService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BookService();
		return INSTANCE;
	}

	public void add(Book book) {
		String sql = "INSERT INTO book(name, image, price, released_date, remark, author_id,category_id) VALUES(?,?,?,?,?,?,?)";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, book.getName());
			stmt.setString(2, System.getProperty("user.dir").concat("\\cover.jpeg"));
			stmt.setDouble(3, book.getPrice());
			stmt.setDate(4, Date.valueOf(book.getReleasedDate()));
			stmt.setString(5, book.getRemark());
			stmt.setInt(6, book.getAuthor().getId());
			stmt.setInt(7, book.getCategory().getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Book book) {
		String sql = "DELETE FROM book WHERE id=?";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, book.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Book book) {
		String sql = "UPDATE book SET name=?, price=?, released_date=?, remark=?, author_id=?, category_id=? WHERE id=?";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, book.getName());
			stmt.setDouble(2, book.getPrice());
			stmt.setDate(3, Date.valueOf(book.getReleasedDate()));
			stmt.setString(4, book.getRemark());
			stmt.setInt(5, book.getAuthor().getId());
			stmt.setInt(6, book.getCategory().getId());
			stmt.setInt(7, book.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Book> findAll() {
		return findByParams(null, null, null, null);
	}

	public List<Book> findByParams(Category category, Author author, String bookName, LocalDate releasedDate) {
		List<Book> bList = new ArrayList<>();
		List<Object> params = new ArrayList();
		String sql = "SELECT b.id,b.name book_name,b.price price ,b.released_date released_date, b.remark remark,"
				+ " b.author_id author_id,b.category_id category_id, c.name category_name, a.name author_name"
				+ " FROM book b JOIN category c ON b.category_id = c.id"
				+ " JOIN author a ON b.author_id = a.id  WHERE 1=1";

		StringBuilder sb = new StringBuilder(sql);

		if (category != null) {
			sb.append(" AND c.name=?");
			params.add(category);
		}

		if (author != null) {
			sb.append(" AND a.name=?");
			params.add(author);
		}

		if (bookName != null && !bookName.isEmpty()) {
			sb.append(" AND b.name LIKE ?");
			params.add("%".concat(bookName).concat("%"));
		}

		if (releasedDate != null) {
			sb.append(" AND b.released_date >=?");
			params.add(releasedDate);
		}
		
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {

			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				bList.add(getObject(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bList;
	}

	public void imageUpload(Book book) {
		String sql = "UPDATE book SET image=? WHERE id=?";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, book.getImage());
			stmt.setInt(2, book.getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Book getObject(ResultSet rs) throws SQLException {
		Book b = new Book();
		b.setId(rs.getInt("id"));
		b.setName(rs.getString("book_name"));
		b.setPrice(rs.getInt("price"));
		b.setReleasedDate(rs.getDate("released_date").toLocalDate());
		b.setRemark(rs.getString("remark"));

		Category c = new Category();
		c.setId(rs.getInt("category_id"));
		c.setName(rs.getString("category_name"));

		Author a = new Author();
		a.setId(rs.getInt("author_id"));
		a.setName(rs.getString("author_name"));

		b.setCategory(c);
		b.setAuthor(a);
		return b;
	}

	public String getImage(Book book) {
		String imgPath = "";
		String sql = "SELECT image FROM book WHERE id=?";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, book.getId());
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				imgPath = rs.getString("image");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imgPath;
	}

}
