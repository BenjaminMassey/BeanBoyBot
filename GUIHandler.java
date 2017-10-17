package bbb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIHandler extends JFrame {
	
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	private static final long serialVersionUID = 1L;
	
	public static JLabel cost;
	
	public static void createWindow(String name, String icon) {
		//Create and set up the window.
        GUIHandler frame = new GUIHandler(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display the window.
        ImageIcon ico = new ImageIcon(icon);
        frame.setIconImage(ico.getImage());
        frame.pack();
        frame.setVisible(true);
	}
	
	public GUIHandler(String name) {
        super(name);
        //setResizable(false);
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(9,1));
        //jp.setSize(1000, 1000);
        jp.add(new JLabel("                BeanBoyBot Twitch Bot                ", SwingConstants.CENTER));
        
        JTextField chatChannel = new JTextField(20);
        try {
        	chatChannel.setText(AccountsManager.getChatChannel().substring(1));
        }catch(Exception e) {}
        jp.add(chatChannel);
        JButton channelButton = new JButton("Set Chat Channel");
        jp.add(channelButton);
        channelButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setChatChannel(chatChannel.getText());
        	}
        });
        JTextField botName = new JTextField(20);
        try {
        	botName.setText(AccountsManager.getBotName());
        }catch(Exception e) {}
        jp.add(botName);
        JButton botNameButton = new JButton("Set Bot Name");
        jp.add(botNameButton);
        botNameButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setBotName(botName.getText());
        	}
        });
        JTextField botOauth = new JTextField(20);
        try {
        	AccountsManager.getBotOauth();
        	botOauth.setText("****************");
        }catch(Exception e) {}
        jp.add(botOauth);
        JButton botOauthButton = new JButton("Set Bot Oauth");
        jp.add(botOauthButton);
        botOauthButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setBotOauth(botOauth.getText());
        		botOauth.setText("****************");
        	}
        });
        
        cost = new JLabel("", SwingConstants.CENTER);
        jp.add(cost);
        JButton startButton = new JButton("Start");
        jp.add(startButton);
        startButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		if(TwitchChat.connected) {
        			try{
        				TwitchChat.deactivate();
        			}catch(Exception e) {
        				System.err.println("Oops: " + e);
        			}
        			startButton.setText("Start");
	        		pack();
        		}
        		else {
        			try{
        				AccountsManager.updateAll();
        				LiveSplitHandler.initialize();
        				SplitGame.start();
        				TwitchChat.initialize();
        			}catch(Exception e) {
        				System.err.println("Oops: " + e);
        			}
        			startButton.setText("Stop");
        			pack();
        		}
        	}
        });
        add(jp);
        addWindowListener(new WindowListener() {
        	public void windowClosing(WindowEvent we) {
        		System.exit(1);
        		if(TwitchChat.connected) {
        			try{
        				TwitchChat.deactivate();
        			}catch(Exception e) {
        				System.err.println("Oops: " + e);
        			}
        		}
        	}
        	public void windowIconified(WindowEvent we) {}
			public void windowActivated(WindowEvent we) {}
			public void windowClosed(WindowEvent we) {}
			public void windowDeactivated(WindowEvent we) {}
			public void windowDeiconified(WindowEvent we) {}
			public void windowOpened(WindowEvent we) {}
        });
    }
	
	
	
}
