package chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @author 16130140342郑浩波
 *	登陆界面
 */
public class Login {
	private static Socket s;

	public static void main(String[] args) throws UnknownHostException, IOException {
		final JFrame f = new JFrame("聊天室");
		//设置登录界面大小
		f.setSize(400, 300);
		//设置登录界面位置,居中
		f.setLocationRelativeTo(null);
		//使窗体大小不可更改
		f.setResizable(false);
		//设置布局
		f.setLayout(null);
		
		//创建账号标签
		JLabel l1 = new JLabel("用户名:");
		//标签大小及位置
		l1.setBounds(60, 30, 150, 30);
		//设置字体格式
		l1.setFont(new Font("宋体", Font.BOLD, 28));
		f.add(l1);
		//创建文本框
		final JTextField tf1 = new JTextField("");
		//文本框大小及位置
		tf1.setBounds(170, 30, 150, 30);
		tf1.setFont(new Font("宋体", Font.BOLD, 28));
		f.add(tf1);
		
		//创建密码标签
		JLabel l2 = new JLabel("密  码:");
		//标签大小及位置
		l2.setBounds(60, 80, 150, 30);
		//设置字体格式
		l2.setFont(new Font("宋体", Font.BOLD, 28));
		f.add(l2);
		//创建密码框
		final JPasswordField pf = new JPasswordField("");
		//设置大小及位置
		pf.setBounds(170, 80, 150, 30);
		pf.setEchoChar('*');
		//字体格式
		pf.setFont(new Font("宋体", Font.BOLD, 25));
		f.add(pf);
		
		//设置登录按钮组件
		JButton b = new JButton("登录");
		//位置及大小
		b.setBounds(80, 150, 100, 40);
		//颜色
		b.setForeground(Color.DARK_GRAY);
		//字体格式
		b.setFont(new Font("宋体", Font.BOLD, 25));
		f.add(b);
		//设置注册按钮组件
		JButton b1 = new JButton("注册");
		b1.setBounds(210, 150, 100, 40);
		b1.setForeground(Color.darkGray);
		b1.setFont(new Font("宋体", Font.BOLD, 25));
		f.add(b1);
		
		
		
		
		
		//登录按钮监听
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//用户名
				String username = "0"+tf1.getText();
				//密码
				char[] p = pf.getPassword();
				String password = String.valueOf(p);
				// 将用户名和密码发给服务端
				try {
					//创建Socket对象
					s = new Socket("127.0.0.1", 8888);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					DataInputStream dis = new DataInputStream(s.getInputStream());
					
					dos.writeUTF(username);
					dos.writeUTF(password);
					//获取返回的信息
					String flag = dis.readUTF();
					//根据返回的信息判断是否能登陆成功
					if(flag.equals("登陆成功")){
						f.setVisible(false);
						MainMenu.menu(s,username);
					}else if(flag.equals("登陆失败")){
						JOptionPane.showMessageDialog(f, "输入有误");
						//关闭此客户端
						s.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
			}
		});
		
		//注册按钮监听
		b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 点击注册时，弹出注册界面
				final JDialog d = new JDialog(f, "注册", true);//设置为模态
				d.setSize(350, 250);//大小
				d.setLocationRelativeTo(f);//位置
				d.setLayout(new FlowLayout());//设置布局
				
				//创建用户名标签
				JLabel l1 = new JLabel("用户名:");
				l1.setFont(new Font("宋体", Font.BOLD, 28));//设置字体格式
				d.add(l1);
				//创建文本框
				final JTextField tf1 = new JTextField("");
				tf1.setPreferredSize(new Dimension(120, 30));
				tf1.setFont(new Font("宋体", Font.BOLD, 28));
				d.add(tf1);
				//创建密码标签
				JLabel l2 = new JLabel("密  码:");
				l2.setFont(new Font("宋体", Font.BOLD, 28));//设置字体格式
				d.add(l2);
				//创建密码框
				final JPasswordField pf = new JPasswordField("");
				pf.setPreferredSize(new Dimension(120, 30));
				pf.setFont(new Font("宋体", Font.BOLD, 25));
				d.add(pf);
				//创建确认标签
				JLabel l3 = new JLabel("确  认:");
				l3.setFont(new Font("宋体", Font.BOLD, 28));//设置字体格式
				d.add(l3);
				//创建密码框
				final JPasswordField pf1 = new JPasswordField("");
				pf1.setPreferredSize(new Dimension(120, 30));
				pf1.setFont(new Font("宋体", Font.BOLD, 25));
				d.add(pf1);
				//创建提交按钮
				JButton button = new JButton("提交");
				button.setForeground(Color.BLUE);
				button.setPreferredSize(new Dimension(120, 50));
				button.setFont(new Font("宋体", Font.BOLD, 25));
				d.add(button);
				
				//提交按钮监听器
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						//用户名
						String username = "1"+tf1.getText();
						//密码
						char[] p = pf.getPassword();
						String password = String.valueOf(p);
						//密码确认
						char[] p1 = pf1.getPassword();
						String password1 = String.valueOf(p1);
						//为空判断
						if(username.equals("")||password.equals("")||password1.equals("")){
							JOptionPane.showMessageDialog(d, "信息不完整");
						}else{
							//判断密码是否一致
							if(password.equals(password1)){
								// 将用户名和密码发给服务端
								try {
									//创建Socket对象
									s = new Socket("127.0.0.1", 8888);
									DataOutputStream dos = new DataOutputStream(s.getOutputStream());
									DataInputStream dis = new DataInputStream(s.getInputStream());
									//发送用户名和密码
									dos.writeUTF(username);
									dos.writeUTF(password);
									String msg = dis.readUTF();
									JOptionPane.showMessageDialog(d, msg);
									s.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}else{
								JOptionPane.showMessageDialog(d, "密码不一致");
							}
						}
					}
				});
				
				//设置为可见
				d.setVisible(true);
			}
		});
		
		//设置窗体可见
		f.setVisible(true);
	}
}
