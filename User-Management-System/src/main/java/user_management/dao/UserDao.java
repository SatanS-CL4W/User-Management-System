package user_management.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import user_management.model.user;

// This DAO(Data-Access-Object) class provides CRUD database operations for the table users in the database 
public class UserDao {
	private static String jdbcURL = "jdbc:mysql://localhost:3306/user_management";
	private static String username = "root";
	private static String password = "root";

	private static final String INSERT = "INSERT into users" +  " (name, email, country) values " 
	+ " (?,?,?); "; 
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_Users_SQL = "delete from users where id = ?";
	private static final String UPDATE_USERS_SQL = "update users set name = ?, email = ?, country = ? where id = ?";

	protected static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(jdbcURL, username, password);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			// TODO: handle exception
			e.printStackTrace();
		}
		return con;
	
	}
	//Create or Insert User
	public static void insertUser(user u) throws SQLException {
		try(Connection connection = getConnection();
				PreparedStatement st = connection.prepareStatement(INSERT);){
				st.setString(1, u.getName());
				st.setString(2, u.getEmail());
				st.setString(3, u.getCountry());
				
				st.executeUpdate();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//Update User
	public static boolean updateUser(user u) throws SQLException {
		boolean rowUpdated;
		
		try(Connection connection = getConnection();
				PreparedStatement st = connection.prepareStatement(UPDATE_USERS_SQL);){
				st.setString(1, u.getName());
				st.setString(2, u.getEmail());
				st.setString(3, u.getCountry());
				st.setInt(4, u.getId());
				
				rowUpdated = st.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	//Select User By id
	public static user selectUser(int id) throws SQLException {
		user u = null;
		
		try(Connection connection = getConnection();
				PreparedStatement st = connection.prepareStatement(SELECT_USER_BY_ID);){
				st.setInt(1, id);
				System.out.println(st);
				
				ResultSet rs = st.executeQuery();
				
				
				while(rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");				
					u = new user(id, name, email, country);					
				}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return u;
	}
	
	//Select All Users
		public static List<user> selectAllUsers() throws SQLException {
			List<user> users = new ArrayList<>();
			
			try(Connection connection = getConnection();
					PreparedStatement st = connection.prepareStatement(SELECT_ALL_USERS);){
					System.out.println(st);
					
					ResultSet rs = st.executeQuery();
					
					
					while(rs.next()) {
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String email = rs.getString("email");
						String country = rs.getString("country");				
						users.add(new user(id, name, email, country));
					}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return users;
		}

	//Delete USer
		public static boolean deleteUser(int id) throws SQLException {
			boolean rowDeleted;
			
			try(Connection connection = getConnection();
					PreparedStatement st = connection.prepareStatement(DELETE_Users_SQL);){
					st.setInt(1, id);
					
					rowDeleted = st.executeUpdate() > 0;
			}
			return rowDeleted;
		}

}
