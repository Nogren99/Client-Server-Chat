package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import controlador.ControladorCliente;

public class Chat extends JFrame implements Ivista, WindowListener {

	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JTextField textField;
	private JButton btnNewButton;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_8;
	private JScrollPane scrollPane;
	private JPanel panel_9;
	private JPanel panel_10;
	private JPanel panel_11;
	private JPanel panel_12;
	private ActionListener actionListener;
	private DefaultListModel modeloListaEnviados;
	private DefaultListModel modeloListaRecibidos;
	private JPanel panel_18;
	private JPanel panel_19;
	private JTextArea textArea;
	private JPanel panel_7;
	private JButton btnNewButton_1;
	private JPanel panel_13;
	private JLabel lblChatCon;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
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
	public Chat() {
		this.addWindowListener(this);
		this.modeloListaEnviados=new DefaultListModel();
		this.modeloListaRecibidos=new DefaultListModel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		this.panel = new JPanel();
		this.panel.setBackground(new Color(102, 205, 170));
		this.contentPane.add(this.panel, BorderLayout.CENTER);
		this.panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		this.panel_6 = new JPanel();
		this.panel_6.setBackground(new Color(195, 222, 214));
		this.panel.add(this.panel_6);
		this.panel_6.setLayout(new BorderLayout(0, 0));
		
		this.panel_8 = new JPanel();
		this.panel_8.setBackground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_8, BorderLayout.CENTER);
		this.panel_8.setLayout(new BorderLayout(0, 0));
		
		this.scrollPane = new JScrollPane();
		this.panel_8.add(this.scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		this.panel_7 = new JPanel();
		this.panel_8.add(this.panel_7, BorderLayout.NORTH);
		this.panel_7.setLayout(new BorderLayout(0, 0));
		
		this.btnNewButton_1 = new JButton("Cerrar");
		this.panel_7.add(this.btnNewButton_1, BorderLayout.WEST);
		
		this.panel_13 = new JPanel();
		this.panel_13.setBackground(new Color(195, 222, 214));
		this.panel_7.add(this.panel_13, BorderLayout.CENTER);
		
		this.lblChatCon = new JLabel("Chat con:");
		this.panel_13.add(this.lblChatCon);
		
		this.panel_9 = new JPanel();
		this.panel_9.setBackground(new Color(195, 222, 214));
		this.panel_9.setForeground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_9, BorderLayout.NORTH);
		
		this.panel_10 = new JPanel();
		this.panel_10.setBackground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_10, BorderLayout.SOUTH);
		
		this.panel_11 = new JPanel();
		this.panel_11.setBackground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_11, BorderLayout.WEST);
		
		this.panel_12 = new JPanel();
		this.panel_12.setBackground(new Color(195, 222, 214));
		this.panel_6.add(this.panel_12, BorderLayout.EAST);
		
		this.panel_1 = new JPanel();
		this.contentPane.add(this.panel_1, BorderLayout.SOUTH);
		this.panel_1.setLayout(new BorderLayout(0, 0));
		
		this.panel_2 = new JPanel();
		this.panel_1.add(this.panel_2, BorderLayout.CENTER);
		this.panel_2.setLayout(new BorderLayout(0, 0));
		
		this.textField = new JTextField();
		this.panel_2.add(this.textField);
		this.textField.setColumns(10);
		
		this.panel_4 = new JPanel();
		this.panel_4.setBackground(new Color(195, 222, 214));
		this.panel_2.add(this.panel_4, BorderLayout.NORTH);
		
		this.panel_5 = new JPanel();
		this.panel_5.setBackground(new Color(195, 222, 214));
		this.panel_2.add(this.panel_5, BorderLayout.SOUTH);
		
		this.panel_18 = new JPanel();
		this.panel_18.setBackground(new Color(195, 222, 214));
		this.panel_2.add(this.panel_18, BorderLayout.WEST);
		
		this.panel_19 = new JPanel();
		this.panel_19.setBackground(new Color(195, 222, 214));
		this.panel_2.add(this.panel_19, BorderLayout.EAST);
		
		this.panel_3 = new JPanel();
		this.panel_3.setBackground(new Color(195, 222, 214));
		this.panel_1.add(this.panel_3, BorderLayout.EAST);
		
		this.btnNewButton = new JButton("Enviar ");
		btnNewButton.setActionCommand("Enviar");
		this.btnNewButton.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
		this.panel_3.add(this.btnNewButton);
	}

	@Override
	public void cerrar() {
		this.dispose();
	}

	@Override
	public void mostrar() {
		this.setVisible(true);
	}
	
	
	
	public JLabel getLblChatCon() {
		return lblChatCon;
	}

	public void setLblChatCon(JLabel lblChatCon) {
		this.lblChatCon = lblChatCon;
	}

	@Override
	public void setActionListener(ActionListener actionListener) {
		this.btnNewButton.addActionListener(actionListener);
		this.btnNewButton_1.addActionListener(actionListener);
		this.actionListener = actionListener;
	}
	
	

	public DefaultListModel getModeloListaEnviados() {
		return modeloListaEnviados;
	}

	public DefaultListModel getModeloListaRecibidos() {
		return modeloListaRecibidos;
	}

	public JTextField getTextField() {
		return textField;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		ControladorCliente.getInstancia().cerrarVentana();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	
}
