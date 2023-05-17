package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

public class SistemaDeMensajeria extends JFrame implements Ivista{

	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JPanel panel_2;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_8;
	private JPanel panel_9;
	private ActionListener actionListener;
	private JPanel panel_10;
	private JPanel panel_11;
	private JPanel panel_13;
	private JPanel panel_3;
	private JPanel panel_12;
	private JPanel panel_7;
	private JPanel panel_15;
	private JPanel panel_14;
	private JPanel panel_18;
	private JPanel panel_17;
	private JPanel panel_19;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SistemaDeMensajeria frame = new SistemaDeMensajeria();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SistemaDeMensajeria() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new GridLayout(1, 2, 0, 0));
		
		this.panel = new JPanel();
		this.panel.setBorder(null);
		this.contentPane.add(this.panel);
		this.panel.setLayout(new GridLayout(3, 3, 0, 0));
		
		this.panel_2 = new JPanel();
		this.panel_2.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_2);
		
		this.panel_5 = new JPanel();
		this.panel_5.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_5);
		
		this.panel_4 = new JPanel();
		this.panel_4.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_4);
		
		this.panel_10 = new JPanel();
		this.panel_10.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_10);
		
		this.btnNewButton = new JButton("Crear SV");
		this.panel.add(this.btnNewButton);
		btnNewButton.setActionCommand("Crear");
		this.btnNewButton.setForeground(Color.BLACK);
		this.btnNewButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.btnNewButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
		this.btnNewButton.setBackground(Color.WHITE);
		
		this.panel_13 = new JPanel();
		this.panel_13.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_13);
		
		this.panel_11 = new JPanel();
		this.panel_11.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_11);
		
		this.panel_3 = new JPanel();
		this.panel_3.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_3);
		
		this.panel_12 = new JPanel();
		this.panel_12.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_12);
		
		this.panel_1 = new JPanel();
		this.panel_1.setBorder(null);
		this.contentPane.add(this.panel_1);
		this.panel_1.setLayout(new GridLayout(3, 3, 0, 0));
		
		this.panel_6 = new JPanel();
		this.panel_6.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_6);
		this.panel_6.setLayout(new GridLayout(3, 3, 0, 0));
		
		this.panel_7 = new JPanel();
		this.panel_7.setBackground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_7);
		
		this.panel_9 = new JPanel();
		this.panel_9.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_9);
		
		this.panel_8 = new JPanel();
		this.panel_8.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_8);
		
		this.panel_15 = new JPanel();
		this.panel_15.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_15);
		
		this.btnNewButton_1 = new JButton("Conectarse");
		this.panel_1.add(this.btnNewButton_1);
		btnNewButton_1.setActionCommand("Unirse");
		this.btnNewButton_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.btnNewButton_1.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
		this.btnNewButton_1.setBackground(Color.WHITE);
		this.btnNewButton_1.setForeground(Color.BLACK);
		
		this.panel_14 = new JPanel();
		this.panel_14.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_14);
		
		this.panel_18 = new JPanel();
		this.panel_18.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_18);
		
		this.panel_17 = new JPanel();
		this.panel_17.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_17);
		
		this.panel_19 = new JPanel();
		this.panel_19.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_19);
	}

	@Override
	public void cerrar() {
		this.dispose();
	}

	@Override
	public void mostrar() {
		this.setVisible(true);
		
	}

	@Override
	public void setActionListener(ActionListener actionListener) {
		this.btnNewButton.addActionListener(actionListener);
		this.btnNewButton_1.addActionListener(actionListener);
		this.actionListener=actionListener;
	}
	
	

}
