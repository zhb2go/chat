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
 * @author 16130140342֣�Ʋ�
 *	��½����
 */
public class Login {
	private static Socket s;

	public static void main(String[] args) throws UnknownHostException, IOException {
		final JFrame f = new JFrame("������");
		//���õ�¼�����С
		f.setSize(400, 300);
		//���õ�¼����λ��,����
		f.setLocationRelativeTo(null);
		//ʹ�����С���ɸ���
		f.setResizable(false);
		//���ò���
		f.setLayout(null);
		
		//�����˺ű�ǩ
		JLabel l1 = new JLabel("�û���:");
		//��ǩ��С��λ��
		l1.setBounds(60, 30, 150, 30);
		//���������ʽ
		l1.setFont(new Font("����", Font.BOLD, 28));
		f.add(l1);
		//�����ı���
		final JTextField tf1 = new JTextField("");
		//�ı����С��λ��
		tf1.setBounds(170, 30, 150, 30);
		tf1.setFont(new Font("����", Font.BOLD, 28));
		f.add(tf1);
		
		//���������ǩ
		JLabel l2 = new JLabel("��  ��:");
		//��ǩ��С��λ��
		l2.setBounds(60, 80, 150, 30);
		//���������ʽ
		l2.setFont(new Font("����", Font.BOLD, 28));
		f.add(l2);
		//���������
		final JPasswordField pf = new JPasswordField("");
		//���ô�С��λ��
		pf.setBounds(170, 80, 150, 30);
		pf.setEchoChar('*');
		//�����ʽ
		pf.setFont(new Font("����", Font.BOLD, 25));
		f.add(pf);
		
		//���õ�¼��ť���
		JButton b = new JButton("��¼");
		//λ�ü���С
		b.setBounds(80, 150, 100, 40);
		//��ɫ
		b.setForeground(Color.DARK_GRAY);
		//�����ʽ
		b.setFont(new Font("����", Font.BOLD, 25));
		f.add(b);
		//����ע�ᰴť���
		JButton b1 = new JButton("ע��");
		b1.setBounds(210, 150, 100, 40);
		b1.setForeground(Color.darkGray);
		b1.setFont(new Font("����", Font.BOLD, 25));
		f.add(b1);
		
		
		
		
		
		//��¼��ť����
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//�û���
				String username = "0"+tf1.getText();
				//����
				char[] p = pf.getPassword();
				String password = String.valueOf(p);
				// ���û��������뷢�������
				try {
					//����Socket����
					s = new Socket("127.0.0.1", 8888);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					DataInputStream dis = new DataInputStream(s.getInputStream());
					
					dos.writeUTF(username);
					dos.writeUTF(password);
					//��ȡ���ص���Ϣ
					String flag = dis.readUTF();
					//���ݷ��ص���Ϣ�ж��Ƿ��ܵ�½�ɹ�
					if(flag.equals("��½�ɹ�")){
						f.setVisible(false);
						MainMenu.menu(s,username);
					}else if(flag.equals("��½ʧ��")){
						JOptionPane.showMessageDialog(f, "��������");
						//�رմ˿ͻ���
						s.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
			}
		});
		
		//ע�ᰴť����
		b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// ���ע��ʱ������ע�����
				final JDialog d = new JDialog(f, "ע��", true);//����Ϊģ̬
				d.setSize(350, 250);//��С
				d.setLocationRelativeTo(f);//λ��
				d.setLayout(new FlowLayout());//���ò���
				
				//�����û�����ǩ
				JLabel l1 = new JLabel("�û���:");
				l1.setFont(new Font("����", Font.BOLD, 28));//���������ʽ
				d.add(l1);
				//�����ı���
				final JTextField tf1 = new JTextField("");
				tf1.setPreferredSize(new Dimension(120, 30));
				tf1.setFont(new Font("����", Font.BOLD, 28));
				d.add(tf1);
				//���������ǩ
				JLabel l2 = new JLabel("��  ��:");
				l2.setFont(new Font("����", Font.BOLD, 28));//���������ʽ
				d.add(l2);
				//���������
				final JPasswordField pf = new JPasswordField("");
				pf.setPreferredSize(new Dimension(120, 30));
				pf.setFont(new Font("����", Font.BOLD, 25));
				d.add(pf);
				//����ȷ�ϱ�ǩ
				JLabel l3 = new JLabel("ȷ  ��:");
				l3.setFont(new Font("����", Font.BOLD, 28));//���������ʽ
				d.add(l3);
				//���������
				final JPasswordField pf1 = new JPasswordField("");
				pf1.setPreferredSize(new Dimension(120, 30));
				pf1.setFont(new Font("����", Font.BOLD, 25));
				d.add(pf1);
				//�����ύ��ť
				JButton button = new JButton("�ύ");
				button.setForeground(Color.BLUE);
				button.setPreferredSize(new Dimension(120, 50));
				button.setFont(new Font("����", Font.BOLD, 25));
				d.add(button);
				
				//�ύ��ť������
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						//�û���
						String username = "1"+tf1.getText();
						//����
						char[] p = pf.getPassword();
						String password = String.valueOf(p);
						//����ȷ��
						char[] p1 = pf1.getPassword();
						String password1 = String.valueOf(p1);
						//Ϊ���ж�
						if(username.equals("")||password.equals("")||password1.equals("")){
							JOptionPane.showMessageDialog(d, "��Ϣ������");
						}else{
							//�ж������Ƿ�һ��
							if(password.equals(password1)){
								// ���û��������뷢�������
								try {
									//����Socket����
									s = new Socket("127.0.0.1", 8888);
									DataOutputStream dos = new DataOutputStream(s.getOutputStream());
									DataInputStream dis = new DataInputStream(s.getInputStream());
									//�����û���������
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
								JOptionPane.showMessageDialog(d, "���벻һ��");
							}
						}
					}
				});
				
				//����Ϊ�ɼ�
				d.setVisible(true);
			}
		});
		
		//���ô���ɼ�
		f.setVisible(true);
	}
}
