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
 * @author 16130140342郑浩波
 * 
 *	服务器
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
		
		//创建一个集合类，用于存放客户端
		
		while(true){
			//使用accept()阻塞等待客户请求,请求到来则产生一个Socket对象，并继续执行
			Socket s = ss.accept();
			ServerThread st = new ServerThread(s);
			//创建并启动线程
			new Thread(st).start();
			
		}
		
	}
	
	//用内部类实现为每个客户端创建一个线程
	private class ServerThread implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning = true;
		private String username0;
		//有参构造
		public ServerThread(Socket s){
			try {
				System.out.println("成功连接一个客户端");
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				
			} catch (IOException e) {
				// 发生异常时
				isRunning = false;
				//移除用户
				allUsers.remove(username0);
				String allusers = "0";
				for(String u : allUsers){
					allusers = allusers+u+"\r\n";
				}
				sendOthers(allusers);
				all.remove(this);
			}
		}
		
		//接收数据
		public String receive(){
			String msg = "";
			try {
				msg = dis.readUTF();
				return msg;
			} catch (IOException e) {
				// 发生异常时
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
		
		//向客户端（当前客户端）发数据
		public void send(String msg){
			try {
				//发送
				dos.writeUTF(msg);
				//刷新
				dos.flush();
			} catch (IOException e) {
				// 发生异常时
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
		
		//向其他客户端发送数据
		public void sendOthers(String msg){
			//是否为私聊约定
			if(msg.startsWith("@")){
				//私聊
				//获取name
				String username0 = msg.substring(1, msg.indexOf(":"));
				String content = msg.substring(msg.indexOf(":")+1);
				for(ServerThread other : all){
					if(other.username0.equals(username0)){
						//私聊信息头部为0
						other.send("10"+this.username0+":"+content);
					}
				}
			}else{
				for(ServerThread other : all){
					//等于自己时跳过
					if(other==this){
						continue;
					}
					//其他客户端时则发送
					//群聊信息头部为1
					if(msg.startsWith("0")){
						other.send(msg);
					}else if(msg.startsWith(":")){
						other.send("11"+username0+msg);
					}
				}
			}
		}
		
		//向所有客户端发送数据，包括自己
		public void sendAll(String msg){
			for(ServerThread other : all){
				other.send(msg);
			}
		}
		//查看数据库，验证登陆用户名和密码
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
		
		//注册用户
		public void register(User user){
			UserJDBC uj = new UserJDBC();
			uj.add(user);
		}
		
		@Override
		public void run() {
			//接收用户名和密码
			String username = receive();
			String password = receive();
			
			//根据username的头部来判断是登陆还是注册
			if(username.startsWith("0")){
				this.username0 = username.substring(1, username.length());
				User user = new User(this.username0, password);
				//判断用户名和密码是否正确
				if(equal(user)){
					send("登陆成功");
					//加入到集合类中
					all.add(this);
					//将名字加到集合类中
					allUsers.add(this.username0);
					
					//0表示列表成员,1表示消息
					String allusers = "0";
					for(String u : allUsers){
						allusers = allusers+u+"\r\n";
					}
					sendAll(allusers);
					while(isRunning){
						//接收数据
						String msg = receive();
						//将数据再返回给客户端
						sendOthers(msg);
					}
				}else{
					send("登陆失败");
				}
			}else if(username.startsWith("1")){
				User user = new User(username.substring(1, username.length()), password);
				//添加到数据库
				register(user);
				send("注册成功");
			}
		}
	}
}
