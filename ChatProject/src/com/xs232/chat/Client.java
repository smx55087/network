package com.xs232.chat;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.xs232.chat.Send;
import com.xs232.chat.Receive;



public class Client extends JFrame implements ActionListener{
    GridBagLayout gbl = new GridBagLayout();  
    GridBagConstraints gbs = new GridBagConstraints(); 
    private JLabel la;
    private JTextField tf;
	private JTextArea ta,ta2;
	private JButton b1,b2;
	private String name;
	private static Socket client;
	private Send send;
	private Receive receive;
	private JScrollPane jp1,jp2;
	public Client ()
	{
		this.setBounds(0,0,600,600);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(gbl); 
	    gbs.fill=GridBagConstraints.BOTH;
	    gbs.insets = new Insets(10, 5, 10, 5); //�ϣ����£���
	    
	    la = new JLabel("�������û���:");
	    setConstraints(gbl,gbs,0,0,1,1,la);
	    tf = new JTextField(10);
	    setConstraints(gbl,gbs,1,0,1,1,tf);
	    b1 = new JButton("ȷ��");
	    b1.addActionListener(this);
	    setConstraints(gbl,gbs,2,0,1,1,b1);
	    ta = new JTextArea(15, 40);
	    ta.setEditable(false);
	    ta.setLineWrap(true);
	    jp1 = new JScrollPane(ta);
	    setConstraints(gbl,gbs,0,1,4,4,jp1);
	    ta2 = new JTextArea(5,40);
	    ta2.setEditable(false);
	    ta2.setLineWrap(true); 
	    jp2 = new JScrollPane(ta2);
	    //jp2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    setConstraints(gbl,gbs,0,5,4,2,jp2);
	    b2 = new JButton("����");
	    b2.addActionListener(this);
	    b2.setEnabled(false);
	    setConstraints(gbl,gbs,3,7,1,1,b2);
	    //������
	    this.add(la);
	    this.add(tf);
	    this.add(b1);
	    this.add(jp1);
	    this.add(jp2);
	    this.add(b2);
	    //��ʾ���
	    this.setVisible(true);
	}
	//�����¼�
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand().equals("����")) { 
			send.setMsg(ta2.getText());
			ta2.setText("");
		}
		if(ev.getActionCommand().equals("ȷ��")) {
			name=tf.getText();
			try {
				client= new Socket("localhost",9999);
		    	send = new Send(client,name);
		    	receive = new Receive(client,ta);
				DataInputStream dis=new DataInputStream(client.getInputStream());
				String msg=dis.readUTF();
				if(msg.equals("false")) {
					JOptionPane.showMessageDialog(null, "�û����Ѵ��ڣ����������룡", "����",JOptionPane.WARNING_MESSAGE); 
					return;
				}
				else {
					new Thread(send).start();
					new Thread(receive).start();
					ta2.setEditable(true);
					b2.setEnabled(true);
					b1.setEnabled(false);
					tf.setEditable(false);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client();
	}
	public void setConstraints(GridBagLayout gbl,GridBagConstraints gbs,int x,int y,int width,int height,Component c) {
		gbs.gridwidth=width;
		gbs.gridheight=height;
		gbs.gridx=x;
		gbs.gridy=y;
		gbl.setConstraints(c, gbs);
	}
}
