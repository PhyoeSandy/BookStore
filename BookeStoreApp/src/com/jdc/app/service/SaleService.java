package com.jdc.app.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jdc.app.entity.Author;
import com.jdc.app.entity.Book;
import com.jdc.app.entity.Category;
import com.jdc.app.entity.Sale;
import com.jdc.app.entity.SaleDetails;
import com.jdc.app.util.ConnectionManager;

public class SaleService {

	private static SaleService INSTANCE;

	private SaleService() {
	}

	public static SaleService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SaleService();
		return INSTANCE;
	}

	public SaleDetails getDetails(int saleId, int bookId) {
		SaleDetails details = new SaleDetails();
		String sql = "SELECT sd.id id,sd.quantity quantity,sd.unit_price unit_price,s.tax tax"
				+ " FROM sale_details sd JOIN sale s ON sd.sale_id = s.id WHERE sale_id=? AND book_id=?";

		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, saleId);
			stmt.setInt(2, bookId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				details.setId(rs.getInt("id"));
				details.setQuantity(rs.getInt("quantity"));
				details.setUnitPrice(rs.getInt("unit_price"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;
	}

	public int addSale(Sale sale) {
		String sql = "INSERT INTO sale(sale_date, sale_time, tax) VALUES(?,?,?)";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement saleStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			saleStmt.setDate(1, Date.valueOf(sale.getSaleDate()));
			saleStmt.setTime(2, Time.valueOf(sale.getSaleTime()));
			saleStmt.setDouble(3, sale.getTax());
			saleStmt.executeUpdate();

			ResultSet rs = saleStmt.getGeneratedKeys();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public void addDetails(SaleDetails sd) {
		String sql = "INSERT INTO sale_details(quantity, unit_price, book_id, sale_id, author_id, category_id)"
				+ " VALUES(?,?,?,?,?,?)";

		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, sd.getQuantity());
			stmt.setDouble(2, sd.getUnitPrice());
			stmt.setInt(3, sd.getBook().getId());
			stmt.setInt(4, sd.getSale().getId());
			stmt.setInt(5, sd.getAuthor().getId());
			stmt.setInt(6, sd.getCategory().getId());
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(int saleDetailsId, int quantity) {
		String sql = "UPDATE sale_details SET quantity=? WHERE id=?";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, quantity);
			stmt.setInt(2, saleDetailsId);
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Sale> findSale(LocalDate dateFrom, LocalDate dateTo) {
		List<Sale> sList = new ArrayList<Sale>();
		List<Object> params = new LinkedList<Object>();
		String sql = "SELECT * FROM sale WHERE 1=1";
		StringBuilder sb = new StringBuilder(sql);

		if (dateFrom != null && dateTo != null && dateFrom.isBefore(dateTo)) {
			sb.append(" AND sale_date BETWEEN ? AND ?");
			params.add(Date.valueOf(dateFrom));
			params.add(Date.valueOf(dateTo));
		} else {
			if (dateFrom != null) {
				sb.append(" AND sale_date >=?");
				params.add(Date.valueOf(dateFrom));
			}

			if (dateTo != null) {
				sb.append(" AND sale_date <=?");
				params.add(Date.valueOf(dateTo));
			}
		}

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {

			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				sList.add(getSaleObject(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sList;
	}

	public List<SaleDetails> findSaleDetails(Category category, String bookName, LocalDate dateFrom, LocalDate dateTo,
			int sale_id) {

		String sql = "SELECT c.name category_name, b.name book_name, a.name author_name, b.price price, sd.sale_id sale_id, sd.book_id book_id,"
				+ " sd.id id, sd.author_id author_id, sd.category_id category_id, sd.quantity, sd.unit_price unit_price, s.tax tax"
				+ " FROM sale_details sd JOIN sale s ON sd.sale_id = s.id" + " JOIN book b ON sd.book_id = b.id"
				+ " JOIN author a ON sd.author_id = a.id" + " JOIN category c ON sd.category_id = c.id WHERE 1=1";
		List<SaleDetails> sdList = new ArrayList<SaleDetails>();
		StringBuilder sb = new StringBuilder(sql);
		List<Object> params = new LinkedList<Object>();

		if (category != null) {
			sb.append(" AND c.name=?");
			params.add(category.getName());
		}

		if (bookName != null && !bookName.isEmpty()) {
			sb.append(" AND b.name LIKE ?");
			params.add("%".concat(bookName).concat("%"));
		}

		if (dateFrom != null && dateTo != null && dateFrom.isBefore(dateTo)) {
			sb.append(" AND s.sale_date BETWEEN ? AND ?");
			params.add(Date.valueOf(dateFrom));
			params.add(Date.valueOf(dateTo));
		} else {
			if (dateFrom != null) {
				sb.append(" AND s.sale_date >=?");
				params.add(Date.valueOf(dateFrom));
			}

			if (dateTo != null) {
				sb.append(" AND s.sale_date <=?");
				params.add(Date.valueOf(dateTo));
			}
		}

		if (sale_id > 0) {
			sb.append(" AND sd.sale_id=?");
			params.add(sale_id);
		}

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {

			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				sdList.add(getDetailsObject(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdList;
	}

	private Sale getSaleObject(ResultSet rs) throws SQLException {
		Sale s = new Sale();
		s.setId(rs.getInt("id"));
		s.setSaleDate(rs.getDate("sale_date").toLocalDate());
		s.setSaleTime(rs.getTime("sale_time").toLocalTime());
		s.setTax(rs.getInt("tax"));
		return s;
	}

	private SaleDetails getDetailsObject(ResultSet rs) throws SQLException {
		SaleDetails sd = new SaleDetails();
		sd.setId(rs.getInt("id"));
		sd.setQuantity(rs.getInt("quantity"));
		sd.setUnitPrice(rs.getInt("unit_price"));

		Sale s = new Sale();
		s.setId(rs.getInt("sale_id"));
		s.setTax(rs.getInt("tax"));

		Book b = new Book();
		b.setId(rs.getInt("book_id"));
		b.setName(rs.getString("book_name"));

		Author a = new Author();
		a.setId(rs.getInt("author_id"));
		a.setName(rs.getString("author_name"));

		Category c = new Category();
		c.setId(rs.getInt("category_id"));
		c.setName(rs.getString("category_name"));

		sd.setAuthor(a);
		sd.setBook(b);
		sd.setCategory(c);
		sd.setSale(s);
		return sd;
	}

	public void delete(List<SaleDetails> sdList) {
		String sql = "DELETE FROM sale_details WHERE id IN (";
		StringBuilder sb = new StringBuilder(sql);
		List<Integer> params = new ArrayList<Integer>();

		for (int i = 0; i < sdList.size() - 1; i++) {
			sb.append("?,");
			params.add(sdList.get(i).getId());
		}
		sb.append("?)");
		params.add(sdList.get(sdList.size() - 1).getId());

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {

			for (int i = 0; i < params.size(); i++) {
				stmt.setInt(i + 1, params.get(i));
			}
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
