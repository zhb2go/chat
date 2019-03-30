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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainMenu {
	public static void menu(final Socket s,final String username) throws IOException{
		final JFrame f = new JFrame("聊天室");
		//设置登录界面大小
		f.setSize(600, 500);
		//设置登录界面位置,居中
		f.setLocationRelativeTo(null);
		//使窗体大小不可更改
		f.setResizable(false);
		//设置布局
		f.setLayout(null);
		
		//创建一个左边的面板
		final JPanel pleft = new JPanel();
		//设置布局
		pleft.setLayout(new FlowLayout());
		//创建一个右边的面板
		JPanel pright = new JPanel();
		//设置布局
		pright.setLayout(new FlowLayout());
		//创建一个水平JSplitPane，左边是pleft,右边是pnewOrientationright
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pleft, pright);
		
		/**
		 * 右边的面板
		 */
		//创建成员列表标签
		JLabel luser = new JLabel("我:"+username.substring(1, username.length()));
		//设置字体格式
		luser.setFont(new Font("宋体", Font.BOLD, 25));
		//设置颜色
		luser.setForeground(Color.RED);
		//加入到右边的面板中
		pright.add(luser);
		JLabel lusers = new JLabel("在线用户");
		//设置字体格式
		lusers.setFont(new Font("宋体", Font.BOLD, 30));
		//加入到右边的面板中
		pright.add(lusers);
		
		//创建文本域用来放置成员
		final JTextArea ta = new JTextArea();
		//设置字体
		ta.setFont(new Font("宋体", Font.BOLD, 28));
		//设置为不可编辑
		ta.setEditable(false);
		//使文本域带滚动条
		JScrollPane scp = new JScrollPane(ta);
		scp.setPreferredSize(new Dimension(160, 380));
		pright.add(scp);
		
		/**
		 * 左边的面板
		 */
		//创建私聊面板
		JPanel personalChat = new JPanel();
		//设置布局
		personalChat.setLayout(new FlowLayout());
		//创建账号号标签
		JLabel lnum = new JLabel("账号:");
		//标签大小及位置
		lnum.setPreferredSize(new Dimension(60, 25));
		//设置字体格式
		lnum.setFont(new Font("宋体", Font.BOLD, 20));
		personalChat.add(lnum);
		//创建文本框
		final JTextField tfnum = new JTextField("");
		//文本框大小及位置
		tfnum.setPreferredSize(new Dimension(130, 25));
		tfnum.setFont(new Font("宋体", Font.BOLD, 20));
		personalChat.add(tfnum);
		//创建消息文本框
		final JTextField tfmsg = new JTextField("");
		//文本框大小及位置
		tfmsg.setPreferredSize(new Dimension(250, 25));
		tfmsg.setFont(new Font("宋体", Font.BOLD, 20));
		personalChat.add(tfmsg);
		//创建发送按钮
		JButton b = new JButton("发送");
		b.setPreferredSize(new Dimension(60, 25));
		personalChat.add(b);
		
		//创建文本域
		final JTextArea tapersonalChat = new JTextArea();
		//设置为自动换行
		tapersonalChat.setLineWrap(true);
		//设置为不可编辑
		tapersonalChat.setEditable(false);
		//设置字体
		tapersonalChat.setFont(new Font("宋体", Font.BOLD, 20));
		JScrollPane scp0 = new JScrollPane(tapersonalChat);
		scp0.setPreferredSize(new Dimension(300, 350));
		personalChat.add(scp0);
		
		
		
		
		//创建群聊面板
		JPanel allChat = new JPanel();
		//创建消息文本框
		final JTextField tfmsg1 = new JTextField("");
		//文本框大小及位置
		tfmsg1.setPreferredSize(new Dimension(250, 25));
		tfmsg1.setFont(new Font("宋体", Font.BOLD, 20));
		allChat.add(tfmsg1);
		//创建发送按钮
		JButton b1 = new JButton("发送");
		b1.setPreferredSize(new Dimension(60, 25));
		allChat.add(b1);
		//创建文本域
		final JTextArea taallChat = new JTextArea();
		//设置自动换行
		taallChat.setLineWrap(true);
		//设置为不可编辑
		taallChat.setEditable(false);
		//设置字体
		taallChat.setFont(new Font("宋体", Font.BOLD, 20));
		JScrollPane scpall = new JScrollPane(taallChat);
		scpall.setPreferredSize(new Dimension(300, 380));
		allChat.add(scpall);
		
		
		
		//创建接收信息线程
		Thread receiveThread  = new Thread(){
			public void run(){
				try{
					DataInputStream dis = new DataInputStream(s.getInputStream());
					while(true){
						String str = dis.readUTF();
						if(str.startsWith("1")){
							if(str.substring(1, 2).equals("1")){
								taallChat.append(str.substring(2, str.length())+"\r\n");
							}else{
								tapersonalChat.append(str.substring(2, str.length())+"\r\n");
							}
						}else{
							ta.setText(str.substring(1, str.length()));
						}
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		//启动接收线程
		receiveThread.start();
		
		//私聊发送按钮监听器
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//获取文本框中的账号
				String name = tfnum.getText();
				//判断输入是否为空
				if(name.equals("")){
					JOptionPane.showMessageDialog(pleft, "请输入联系人账号");
				}else if(ta.getText().contains(name+"\r\n")){
					try{
						DataOutputStream dos = new DataOutputStream(s.getOutputStream());
						//从私聊文本框中获取信息
						String str = tfmsg.getText();
						//将信息添加到文本域中
						tapersonalChat.append("我:"+str+"\r\n");
						dos.writeUTF("@"+name+":"+str);
					}catch(IOException e1){
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(pleft, "账号不存在");
				}
			}
		});
		//群聊发送按钮监听器
		b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					String str = tfmsg1.getText();
					taallChat.append("我:"+str+"\r\n");
					dos.writeUTF(":"+str);
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		});
		
		
		
		
		
		JTabbedPane tp = new JTabbedPane();
		tp.setPreferredSize(new Dimension(395, 500));
		tp.add(personalChat);
		tp.add(allChat);
		
		
		
		//设置标题
		tp.setTitleAt(0, "私聊");
		tp.setTitleAt(1, "群聊");
		tp.setFont(new Font("宋体", Font.BOLD, 25));
		pleft.add(tp);
		
		
		
		
		
		//设置分割条的位置
		sp.setDividerLocation(400);
		//将面板加入到主框体中
		f.setContentPane(sp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
