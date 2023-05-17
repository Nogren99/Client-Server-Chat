package vista;

import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;

public class ConexionDenegada extends JFrame implements Ivista {

	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JButton btnNewButton;
	private JPanel panel_7;
	private ActionListener actionListener;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConexionDenegada frame = new ConexionDenegada();
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
	public ConexionDenegada() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		this.contentPane.setLayout(new GridLayout(0, 3, 0, 0));
		
		this.panel = new JPanel();
		this.panel.setBackground(new Color(195, 222, 214));
		this.contentPane.add(this.panel);
		
		this.panel_1 = new JPanel();
		this.contentPane.add(this.panel_1);
		this.panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.panel_3 = new JPanel();
		this.panel_3.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_3);
		this.panel_3.setLayout(new BorderLayout(0, 0));
		
		this.panel_7 = new JPanel();
		this.panel_7.setBackground(new Color(195, 222, 214));
		this.panel_7.setBorder(null);
		this.panel_3.add(this.panel_7, BorderLayout.CENTER);
		this.panel_7.setLayout(new BorderLayout(0, 0));
		
		this.lblNewLabel = new JLabel("Conexión denegada");
		this.panel_7.add(this.lblNewLabel, BorderLayout.CENTER);
		this.lblNewLabel.setFont(new Font("Segoe UI Historic", Font.PLAIN, 16));
		
		this.panel_4 = new JPanel();
		this.panel_4.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_4);
		
		this.lblNewLabel_1 = new JLabel("");
		this.panel_4.add(this.lblNewLabel_1);
		
		this.panel_5 = new JPanel();
		this.panel_5.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_5);
		
		this.lblNewLabel_2 = new JLabel("No se pudo conectar al usuario, revise IP y puerto ");
		this.lblNewLabel_2.setFont(new Font("Segoe UI Historic", Font.PLAIN, 11));
		this.panel_5.add(this.lblNewLabel_2);
		
		this.panel_6 = new JPanel();
		this.panel_6.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_6);
		
		this.btnNewButton = new JButton("Volver a intentar");
		this.btnNewButton.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 12));
		this.panel_6.add(this.btnNewButton);
		
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

	

	public void setActionListener(ActionListener actionListener) {
		this.btnNewButton.addActionListener(actionListener);
		this.actionListener=actionListener;
	}

}
