package user;

import java.util.List;

public interface DaoUser {
	//�鿴�û�����
	public List<User> list();
	//�����û�����
	public void add(User user);
	//ɾ���û�����
	public void delete(User user);
	//�޸��û�����
	public void update(User user);
}
