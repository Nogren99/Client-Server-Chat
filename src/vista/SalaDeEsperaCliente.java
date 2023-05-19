package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Color;

public class SalaDeEsperaCliente extends JFrame implements Ivista {

	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPanel panel_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SalaDeEsperaCliente frame = new SalaDeEsperaCliente();
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
	public SalaDeEsperaCliente() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new GridLayout(0, 3, 0, 0));
		
		this.panel = new JPanel();
		this.panel.setBackground(new Color(195, 222, 214));
		this.contentPane.add(this.panel);
		
		this.panel_1 = new JPanel();
		this.contentPane.add(this.panel_1);
		this.panel_1.setLayout(new GridLayout(3, 1, 0, 0));
		
		this.panel_3 = new JPanel();
		this.panel_3.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_3);
		
		this.panel_6 = new JPanel();
		this.panel_6.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_6);
		
		this.panel_4 = new JPanel();
		this.panel_4.setBackground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_4);
		this.panel_4.setLayout(new BorderLayout(0, 0));
		
		this.lblNewLabel = new JLabel("Servidor");
		this.lblNewLabel.setBackground(Color.GRAY);
		this.lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		this.panel_4.add(this.lblNewLabel, BorderLayout.NORTH);
		
		this.lblNewLabel_1 = new JLabel("Abrido");
		this.lblNewLabel_1.setBackground(new Color(195, 222, 214));
		this.lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 26));
		this.panel_4.add(this.lblNewLabel_1);
		
		this.panel_5 = new JPanel();
		this.panel_5.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_5);
		
		this.panel_2 = new JPanel();
		this.panel_2.setBackground(new Color(195, 222, 214));
		this.contentPane.add(this.panel_2);
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
		// TODO Auto-generated method stub
		
	}

}
