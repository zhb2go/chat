package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import user.User;
import user.UserJDBC;

/**
 * @author 16130140342֣�Ʋ�
 * 
 *	������
 */
public class Server {
	private static ServerSocket ss;
	private List<ServerThread> all = new ArrayList<>();
	private List<String> allUsers = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		new Server().start();
	}
	
	public void start() throws IOException{
		
		
		ss = new ServerSocket(8888);
		
		//����һ�������࣬���ڴ�ſͻ���
		
		while(true){
			//ʹ��accept()�����ȴ��ͻ�����,�����������һ��Socket���󣬲�����ִ��
			Socket s = ss.accept();
			ServerThread st = new ServerThread(s);
			//�����������߳�
			new Thread(st).start();
			
		}
		
	}
	
	//���ڲ���ʵ��Ϊÿ���ͻ��˴���һ���߳�
	private class ServerThread implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning = true;
		private String username0;
		//�вι���
		public ServerThread(Socket s){
			try {
				System.out.println("�ɹ�����һ���ͻ���");
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				
			} catch (IOException e) {
				// �����쳣ʱ
				isRunning = false;
				//�Ƴ��û�
				allUsers.remove(username0);
				String allusers = "0";
				for(String u : allUsers){
					allusers = allusers+u+"\r\n";
				}
				sendOthers(allusers);
				all.remove(this);
			}
		}
		
		//��������
		public String receive(){
			String msg = "";
			try {
				msg = dis.readUTF();
				return msg;
			} catch (IOException e) {
				// �����쳣ʱ
				isRunning = false;
				
				allUsers.remove(username0);
				String allusers = "0";
				for(String u : allUsers){
					allusers = allusers+u+"\r\n";
				}
				sendOthers(allusers);
				
				all.remove(this);
			}
			return msg;
		}
		
		//��ͻ��ˣ���ǰ�ͻ��ˣ�������
		public void send(String msg){
			try {
				//����
				dos.writeUTF(msg);
				//ˢ��
				dos.flush();
			} catch (IOException e) {
				// �����쳣ʱ
				isRunning = false;
				
				allUsers.remove(username0);
				String allusers = "0";
				for(String u : allUsers){
					allusers = allusers+u+"\r\n";
				}
				sendOthers(allusers);
				
				all.remove(this);
			}
		}
		
		//�������ͻ��˷�������
		public void sendOthers(String msg){
			//�Ƿ�Ϊ˽��Լ��
			if(msg.startsWith("@")){
				//˽��
				//��ȡname
				String username0 = msg.substring(1, msg.indexOf(":"));
				String content = msg.substring(msg.indexOf(":")+1);
				for(ServerThread other : all){
					if(other.username0.equals(username0)){
						//˽����Ϣͷ��Ϊ0
						other.send("10"+this.username0+":"+content);
					}
				}
			}else{
				for(ServerThread other : all){
					//�����Լ�ʱ����
					if(other==this){
						continue;
					}
					//�����ͻ���ʱ����
					//Ⱥ����Ϣͷ��Ϊ1
					if(msg.startsWith("0")){
						other.send(msg);
					}else if(msg.startsWith(":")){
						other.send("11"+username0+msg);
					}
				}
			}
		}
		
		//�����пͻ��˷������ݣ������Լ�
		public void sendAll(String msg){
			for(ServerThread other : all){
				other.send(msg);
			}
		}
		//�鿴���ݿ⣬��֤��½�û���������
		public boolean equal(User user){
			boolean flag = false;
			UserJDBC uj = new UserJDBC();
			List<User> ls = uj.list();
			for(int i=0; i<ls.size(); i++){
				String username = ls.get(i).getId();
				String password = ls.get(i).getPassword();
				if(user.getId().equals(username)&&user.getPassword().equals(password)){
					return true;
				}
			}
			return flag;
		}
		
		//ע���û�
		public void register(User user){
			UserJDBC uj = new UserJDBC();
			uj.add(user);
		}
		
		@Override
		public void run() {
			//�����û���������
			String username = receive();
			String password = receive();
			
			//����username��ͷ�����ж��ǵ�½����ע��
			if(username.startsWith("0")){
				this.username0 = username.substring(1, username.length());
				User user = new User(this.username0, password);
				//�ж��û����������Ƿ���ȷ
				if(equal(user)){
					send("��½�ɹ�");
					//���뵽��������
					all.add(this);
					//�����ּӵ���������
					allUsers.add(this.username0);
					
					//0��ʾ�б��Ա,1��ʾ��Ϣ
					String allusers = "0";
					for(String u : allUsers){
						allusers = allusers+u+"\r\n";
					}
					sendAll(allusers);
					while(isRunning){
						//��������
						String msg = receive();
						//�������ٷ��ظ��ͻ���
						sendOthers(msg);
					}
				}else{
					send("��½ʧ��");
				}
			}else if(username.startsWith("1")){
				User user = new User(username.substring(1, username.length()), password);
				//��ӵ����ݿ�
				register(user);
				send("ע��ɹ�");
			}
		}
	}
}
