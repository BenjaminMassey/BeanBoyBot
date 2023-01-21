package bbb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIHandler extends JFrame {
	
	// A part of BeanBoyBot
	// Copyright 2020 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	private static final long serialVersionUID = 1L;
	
	private static GUIHandler frame;
	private static JPanel configPanel;
	private static JPanel nonConfigPanel;
	private static JPanel main;
	private static JButton startButtonConfig;
	private static JButton startButtonNonConfig;
	
	public static boolean imageApproval = false;
	public static boolean messageApproval = false;

	public static void createWindow(String name, String icon) {
		// Create and set up the window
        frame = new GUIHandler(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Display the window
        ImageIcon ico = new ImageIcon(icon);
        frame.setIconImage(ico.getImage());
        frame.pack();
        frame.setVisible(true);
        frame.setSize(250,275);
	}
	
	public GUIHandler(String name) {
        super(name);
        
        ConfigValues.getValues();
        
        nonConfigPanel = generateNonConfigPanel();
        configPanel = generateConfigPanel();
        
        main = new JPanel();
        main.setLayout(new CardLayout());
        
        main.add(nonConfigPanel);
        main.add(configPanel);
        
        add(main);
        
        
        // Handle exit
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
	
	private static JButton generateStartButton() {
		JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		// Stop the bot
        		if(TwitchChat.connected) {
        			try{
        				TwitchChat.deactivate();
        				PlayersHandler.saveAll();
        				startButtonConfig.setText("Start");
            			startButtonNonConfig.setText("Start");
            			ServerUpdate.finish();
        			}catch(Exception e) {
        				System.err.println("Oops: " + e);
        			}
        		}
        		// Start the bot
        		else {
        			try{
        				if (ConfigValues.stocksOn)
        					LiveSplitHandler.initialize();
        				try{
            				AccountsManager.updateAll();
            				SplitGame.start();
            				TwitchChat.initialize();
            				startButtonConfig.setText("Stop");
                			startButtonNonConfig.setText("Stop");
            			}catch(Exception e) {
            				System.err.println("Error: " + e);
            				JOptionPane.showMessageDialog(null,"Failed to initialize...perhaps no internet?");
            			}
        			}catch(Exception e) {
        				System.err.println("Error: " + e);
        				JOptionPane.showMessageDialog(null,"Could not connect to LiveSplit - make sure you started the server!");
        			}
        		}
        	}
        });
        return startButton;
	}
	
	private static JPanel generateConfigPanel() {
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(25,1));
        
        // Put on a title label
        jp.add(new JLabel("                BeanBoyBot Twitch Bot                ", SwingConstants.CENTER));
        
        // Button to toggle configuration
        JButton configButton = new JButton("Hide config");
        jp.add(configButton);
        configButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		CardLayout cl = (CardLayout) main.getLayout();
        		cl.next(main);
        		frame.setSize(250,275);
        	}
        });
		
		// Entry for what channel the bot should be in
        JTextField chatChannel = new JTextField(20);
        if(!AccountsManager.getChatChannel().substring(1).equals("ailed D:"))
        	chatChannel.setText(AccountsManager.getChatChannel().substring(1));
        jp.add(chatChannel);
        // Button to confirm channel
        JButton channelButton = new JButton("Set Chat Channel");
        jp.add(channelButton);
        channelButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setChatChannel(chatChannel.getText());
        	}
        });
        
        // Entry for the bot's name
        JTextField botName = new JTextField(20);
        if(!AccountsManager.getBotName().equals("Failed D:"))
        	botName.setText(AccountsManager.getBotName());
        jp.add(botName);
        // Button to confirm bot's name
        JButton botNameButton = new JButton("Set Bot Name");
        jp.add(botNameButton);
        botNameButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setBotName(botName.getText());
        	}
        });
        
        // Entry for bot oauth code (will be obscured very simply)
        JTextField botOauth = new JTextField(20);
        if(!AccountsManager.getBotOauth().equals("Failed D:"))
        	botOauth.setText("****************");
        jp.add(botOauth);
        // Button to confirm bot's oauth code
        JButton botOauthButton = new JButton("Set Bot Oauth");
        jp.add(botOauthButton);
        botOauthButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setBotOauth(botOauth.getText());
        		botOauth.setText("****************");
        	}
        });
        
        // Blank space for spacing
        JLabel blank = new JLabel("");
        jp.add(blank);

        // Checkbox for toggling stock game
        JCheckBox stocks = new JCheckBox("Stock Game On");
        stocks.setSelected(ConfigValues.stocksOn);
        jp.add(stocks);
        stocks.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				ConfigValues.stocksOn = e.getStateChange() == ItemEvent.SELECTED;
				ConfigValues.writeValues();
			}
        });
        
     	// Checkbox for toggling cheeky emotes
        JCheckBox cemotes = new JCheckBox("Cheeky Emotes On");
        cemotes.setSelected(ConfigValues.cheekyEmotes);
        jp.add(cemotes);
        cemotes.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				ConfigValues.cheekyEmotes = e.getStateChange() == ItemEvent.SELECTED;
				ConfigValues.writeValues();
			}
        });

		// Checkbox for toggling stock game
		JCheckBox shorts = new JCheckBox("Shorting Enabled");
		shorts.setSelected(ConfigValues.shortingOn);
		jp.add(shorts);
		shorts.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				ConfigValues.shortingOn = e.getStateChange() == ItemEvent.SELECTED;
				ConfigValues.writeValues();
			}
		});
        
        // Entry to set the score multiplier
        JTextField scoreMultiplier = new JTextField(20);
        scoreMultiplier.setText(Double.toString(ConfigValues.scoreMultiplier));
        jp.add(scoreMultiplier);
        // Button to confirm score multiplier
        JButton scoreMultiplierConfirm = new JButton("Set Score Multiplier");
        jp.add(scoreMultiplierConfirm);
        scoreMultiplierConfirm.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		ConfigValues.scoreMultiplier = Double.parseDouble(scoreMultiplier.getText());
        		ConfigValues.writeValues();
        	}
        });
        
        // Entry to set the dividend multiplier
        JTextField diviMultiplier = new JTextField(20);
        diviMultiplier.setText(Double.toString(ConfigValues.dividendRate));
        jp.add(diviMultiplier);
        // Button to confirm dividend multiplier
        JButton diviMultiplierConfirm = new JButton("Set Dividend Multiplier");
        jp.add(diviMultiplierConfirm);
        diviMultiplierConfirm.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		ConfigValues.dividendRate = Double.parseDouble(diviMultiplier.getText());
        		ConfigValues.writeValues();
        	}
        });

		// Entry to set the choke multiplier
		JTextField chokeMultiplier = new JTextField(20);
		chokeMultiplier.setText(Double.toString(ConfigValues.chokeRate));
		jp.add(chokeMultiplier);
		// Button to confirm dividend multiplier
		JButton chokeMultiplierConfirm = new JButton("Set Choke Multiplier");
		jp.add(chokeMultiplierConfirm);
		chokeMultiplierConfirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				ConfigValues.chokeRate = Double.parseDouble(chokeMultiplier.getText());
				ConfigValues.writeValues();
			}
		});
        
        // Blank space for spacing
        blank = new JLabel("");
        jp.add(blank);

		JButton denyImageButton = new JButton("Deny quequed image");
		jp.add(denyImageButton);
		denyImageButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (StreamImage.images.size() > 0)
					StreamImage.images.remove(0);
			}
		});

		JButton imageApprovalButton = new JButton("Approve quequed image");
		jp.add(imageApprovalButton);
		imageApprovalButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (StreamImage.images.size() > 0)
					imageApproval = true;
			}
		});

		JButton denyMessageButton = new JButton("Deny quequed message");
		jp.add(denyMessageButton);
		denyMessageButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (StreamMessage.messages.size() > 0)
					StreamMessage.messages.remove(0);
			}
		});

		JButton messageApprovalButton = new JButton("Approve quequed message");
		jp.add(messageApprovalButton);
		messageApprovalButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (StreamMessage.messages.size() > 0)
					messageApproval = true;
			}
		});
        
        // Blank space for spacing
        blank = new JLabel("");
        jp.add(blank);
        
        // Button that toggles the bot on and off
        startButtonConfig = generateStartButton();
        jp.add(startButtonConfig);
        
        return jp;
        
	}
	
	private static JPanel generateNonConfigPanel() {
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(9,1));
        
        // Put on a title label
        jp.add(new JLabel("                BeanBoyBot Twitch Bot                ", SwingConstants.CENTER));
        
        // Button to toggle configuration
        JButton configButton = new JButton("Unhide config");
        jp.add(configButton);
        configButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		CardLayout cl = (CardLayout) main.getLayout();
        		cl.next(main);
        		frame.setSize(250,675);
        	}
        });

        // Blank space for spacing
        JLabel blank = new JLabel("");
        jp.add(blank);

        JButton denyImageButton = new JButton("Deny quequed image");
        jp.add(denyImageButton);
		denyImageButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		if (StreamImage.images.size() > 0)
        			StreamImage.images.remove(0);
        	}
        });
        
        JButton imageApprovalButton = new JButton("Approve quequed image");
        jp.add(imageApprovalButton);
		imageApprovalButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		if (StreamImage.images.size() > 0)
        			imageApproval = true;
        	}
        });

		JButton denyMessageButton = new JButton("Deny quequed message");
		jp.add(denyMessageButton);
		denyMessageButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				if (StreamMessage.messages.size() > 0)
					StreamMessage.messages.remove(0);
			}
		});

        JButton messageApprovalButton = new JButton("Approve quequed message");
        jp.add(messageApprovalButton);
		messageApprovalButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		if (StreamMessage.messages.size() > 0)
        			messageApproval = true;
        	}
        });
        
        // Blank space for spacing
        blank = new JLabel("");
        jp.add(blank);
        
        // Button that toggles the bot on and off
        startButtonNonConfig = generateStartButton();
        jp.add(startButtonNonConfig);
        
        return jp;
        
	}
	
}
