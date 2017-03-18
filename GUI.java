package com.cheesecaketornado;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	//USED FOR THE JOPTIONPANE IN A ACTIONLISTENER
	private GUI gui;
	private JPanel panel;
	
	public GUI () {
		this.gui = this;
		setTitle("OnionMan");
		//CREATE ONIONMAN INSTANCE FOR GUI
		OnionMan om = new OnionMan();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(250, 120));
		setPreferredSize(new Dimension(250, 120));
		setLocationRelativeTo(null);
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel targetLabel = new JLabel("Target Word:");
		targetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(targetLabel);
		
		JTextField targetField = new JTextField();
		targetField.setAlignmentX(Component.CENTER_ALIGNMENT);
		targetField.setMaximumSize(new Dimension(150, 20));
		panel.add(targetField);
		
		JButton goBtn = new JButton("GO");
		goBtn.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  String address = om.generateAddress(targetField.getText());
				  try {
					FileOutputStream fos = new FileOutputStream(address + ".privKey");
					fos.write(om.getPrivateKey().getBytes());
					fos.close();
					//I AM AWARE THIS IS AN AWFUL PRACTICE
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				  	JOptionPane.showMessageDialog(gui, "ONION ADDRESS: \n" + 
				  			address + ".onion\n" +
				  			"PRIVATE GENERATED IN : \n" + address + ".privKey");
				  } 
				} );
		goBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(goBtn);
		
			
		add(panel);
		pack();
		setVisible(true);
		
		
		

		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	om.killAllThreads();
		    }
		});
		
	}
	
	

}
