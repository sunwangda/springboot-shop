package com.shop.user.client.controller;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JLabel;


public class Chat extends Frame {

	MyTextField myTextField = new MyTextField();

	public Chat(String title) {
		super(title);
		Panel disp = new Panel();
		disp.setLayout(new FlowLayout()); //设置布局类型
		disp.add(myTextField);
		new Thread(myTextField).start(); //开启输入检测线程
		add("West", disp);
		
		JLabel info = new JLabel(""); //添加提示标签控件
		add("Center", info);
		
		JButton control = new JButton("发送"); //添加按钮控件
		add("South", control);
		
		pack(); //调整窗口的大小
		setVisible(true); //设置可见性
		
		while (true) { //循环检测输入并提示
			if (myTextField.isInputing)
				info.setText("正在输入...");
			else
				info.setText("");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Chat("模拟QQ检测输入程序");
	}
}

class MyTextField extends TextField implements Runnable {

	boolean isInputing = false; // 是否正在输入

	public MyTextField() {
		super(""); // 文本框默认显示值
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			int strLengthBefore = this.getText().length(); // 获取休眠前输入字符串长度
			try {
				Thread.sleep(500); // 调节检测的周期，即灵敏度
			} catch (InterruptedException e) {
			}
			int strLengthAfter = this.getText().length(); // 获取休眠后输入字符串长度
			if (strLengthAfter != strLengthBefore)
				isInputing = true;
			else
				isInputing = false;
			// System.out.println("当前字符长度： " + strLengthAfter); //后台输出
			if (isInputing)
				System.out.println("正在输入...");
		}
	}
}
