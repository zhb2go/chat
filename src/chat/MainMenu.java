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
		final JFrame f = new JFrame("������");
		//���õ�¼�����С
		f.setSize(600, 500);
		//���õ�¼����λ��,����
		f.setLocationRelativeTo(null);
		//ʹ�����С���ɸ���
		f.setResizable(false);
		//���ò���
		f.setLayout(null);
		
		//����һ����ߵ����
		final JPanel pleft = new JPanel();
		//���ò���
		pleft.setLayout(new FlowLayout());
		//����һ���ұߵ����
		JPanel pright = new JPanel();
		//���ò���
		pright.setLayout(new FlowLayout());
		//����һ��ˮƽJSplitPane�������pleft,�ұ���pnewOrientationright
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pleft, pright);
		
		/**
		 * �ұߵ����
		 */
		//������Ա�б��ǩ
		JLabel luser = new JLabel("��:"+username.substring(1, username.length()));
		//���������ʽ
		luser.setFont(new Font("����", Font.BOLD, 25));
		//������ɫ
		luser.setForeground(Color.RED);
		//���뵽�ұߵ������
		pright.add(luser);
		JLabel lusers = new JLabel("�����û�");
		//���������ʽ
		lusers.setFont(new Font("����", Font.BOLD, 30));
		//���뵽�ұߵ������
		pright.add(lusers);
		
		//�����ı����������ó�Ա
		final JTextArea ta = new JTextArea();
		//��������
		ta.setFont(new Font("����", Font.BOLD, 28));
		//����Ϊ���ɱ༭
		ta.setEditable(false);
		//ʹ�ı����������
		JScrollPane scp = new JScrollPane(ta);
		scp.setPreferredSize(new Dimension(160, 380));
		pright.add(scp);
		
		/**
		 * ��ߵ����
		 */
		//����˽�����
		JPanel personalChat = new JPanel();
		//���ò���
		personalChat.setLayout(new FlowLayout());
		//�����˺źű�ǩ
		JLabel lnum = new JLabel("�˺�:");
		//��ǩ��С��λ��
		lnum.setPreferredSize(new Dimension(60, 25));
		//���������ʽ
		lnum.setFont(new Font("����", Font.BOLD, 20));
		personalChat.add(lnum);
		//�����ı���
		final JTextField tfnum = new JTextField("");
		//�ı����С��λ��
		tfnum.setPreferredSize(new Dimension(130, 25));
		tfnum.setFont(new Font("����", Font.BOLD, 20));
		personalChat.add(tfnum);
		//������Ϣ�ı���
		final JTextField tfmsg = new JTextField("");
		//�ı����С��λ��
		tfmsg.setPreferredSize(new Dimension(250, 25));
		tfmsg.setFont(new Font("����", Font.BOLD, 20));
		personalChat.add(tfmsg);
		//�������Ͱ�ť
		JButton b = new JButton("����");
		b.setPreferredSize(new Dimension(60, 25));
		personalChat.add(b);
		
		//�����ı���
		final JTextArea tapersonalChat = new JTextArea();
		//����Ϊ�Զ�����
		tapersonalChat.setLineWrap(true);
		//����Ϊ���ɱ༭
		tapersonalChat.setEditable(false);
		//��������
		tapersonalChat.setFont(new Font("����", Font.BOLD, 20));
		JScrollPane scp0 = new JScrollPane(tapersonalChat);
		scp0.setPreferredSize(new Dimension(300, 350));
		personalChat.add(scp0);
		
		
		
		
		//����Ⱥ�����
		JPanel allChat = new JPanel();
		//������Ϣ�ı���
		final JTextField tfmsg1 = new JTextField("");
		//�ı����С��λ��
		tfmsg1.setPreferredSize(new Dimension(250, 25));
		tfmsg1.setFont(new Font("����", Font.BOLD, 20));
		allChat.add(tfmsg1);
		//�������Ͱ�ť
		JButton b1 = new JButton("����");
		b1.setPreferredSize(new Dimension(60, 25));
		allChat.add(b1);
		//�����ı���
		final JTextArea taallChat = new JTextArea();
		//�����Զ�����
		taallChat.setLineWrap(true);
		//����Ϊ���ɱ༭
		taallChat.setEditable(false);
		//��������
		taallChat.setFont(new Font("����", Font.BOLD, 20));
		JScrollPane scpall = new JScrollPane(taallChat);
		scpall.setPreferredSize(new Dimension(300, 380));
		allChat.add(scpall);
		
		
		
		//����������Ϣ�߳�
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
		//���������߳�
		receiveThread.start();
		
		//˽�ķ��Ͱ�ť������
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//��ȡ�ı����е��˺�
				String name = tfnum.getText();
				//�ж������Ƿ�Ϊ��
				if(name.equals("")){
					JOptionPane.showMessageDialog(pleft, "��������ϵ���˺�");
				}else if(ta.getText().contains(name+"\r\n")){
					try{
						DataOutputStream dos = new DataOutputStream(s.getOutputStream());
						//��˽���ı����л�ȡ��Ϣ
						String str = tfmsg.getText();
						//����Ϣ��ӵ��ı�����
						tapersonalChat.append("��:"+str+"\r\n");
						dos.writeUTF("@"+name+":"+str);
					}catch(IOException e1){
						e1.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(pleft, "�˺Ų�����");
				}
			}
		});
		//Ⱥ�ķ��Ͱ�ť������
		b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					String str = tfmsg1.getText();
					taallChat.append("��:"+str+"\r\n");
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
		
		
		
		//���ñ���
		tp.setTitleAt(0, "˽��");
		tp.setTitleAt(1, "Ⱥ��");
		tp.setFont(new Font("����", Font.BOLD, 25));
		pleft.add(tp);
		
		
		
		
		
		//���÷ָ�����λ��
		sp.setDividerLocation(400);
		//�������뵽��������
		f.setContentPane(sp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
