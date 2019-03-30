package user;

import java.util.List;

public interface DaoUser {
	//查看用户类型
	public List<User> list();
	//增加用户类型
	public void add(User user);
	//删除用户类型
	public void delete(User user);
	//修改用户类型
	public void update(User user);
}
