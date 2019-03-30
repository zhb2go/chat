package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserJDBC implements DaoUser{
	//在构造方法中进行驱动初始化
	public UserJDBC(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	//返回一个连接对象
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection
				("jdbc:mysql://localhost/db_book?characterEncoding=UTF-8","root","123456");
	}
	//查看用户
	@Override
	public List<User> list() {
		List<User> ls = null;
		try(Connection conn = getConnection();
				Statement s = conn.createStatement()){
			String sql = "select * from user";
			ResultSet rs = s.executeQuery(sql);
			ls = new ArrayList<>();
			while(rs.next()){
				User user = new User(rs.getString(1), rs.getString(2));
				ls.add(user);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return ls;
	}
	//添加用户
	@Override
	public void add(User user) {
		String sql = "insert into user values(?,?)";
		try(Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, user.getId());
			ps.setString(2, user.getPassword());
			//执行
			ps.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	//删除用户
	@Override
	public void delete(User user) {
		try(Connection conn = getConnection();
				Statement s = conn.createStatement()){
			String sql = "delete from user where id ='"+user.getId()+"'";
			s.execute(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	//修改密码
	@Override
	public void update(User user) {
		String sql = "update user set password=? where id ='"+user.getId()+"'";
		try(Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, user.getPassword());
			ps.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
}
