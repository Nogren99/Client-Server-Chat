package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;


import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;

public class SalaDeEsperaCliente extends JFrame implements Ivista { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnAlta;
	private JPanel panelAlta;
	private JPanel panelBotones;
	private JScrollPane scrollPane;
	private JList<Object> list;
	private DefaultListModel<Object> modeloLista;
	private JPanel panelNorte;
	private JPanel panelSur;
	private JPanel panelOeste;
	private JPanel panelEste;
	private ActionListener actionListener;


	public SalaDeEsperaCliente() {
		setTitle("Sala de Conexiones");
		this.modeloLista = new DefaultListModel<Object>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		this.panel = new JPanel();
		this.contentPane.add(this.panel, BorderLayout.CENTER);
		this.panel.setLayout(new BorderLayout(0, 0));
		
		this.scrollPane = new JScrollPane();
		this.panel.add(this.scrollPane, BorderLayout.CENTER);
		
		this.list = new JList();
		this.list.setModel(modeloLista);
		this.scrollPane.setViewportView(this.list);
		
		this.panelNorte = new JPanel();
		this.panelNorte.setBackground(SystemColor.inactiveCaption);
		this.panel.add(this.panelNorte, BorderLayout.NORTH);
		
		this.panelSur = new JPanel();
		this.panelSur.setBackground(SystemColor.inactiveCaption);
		this.panel.add(this.panelSur, BorderLayout.SOUTH);
		
		this.panelOeste = new JPanel();
		this.panelOeste.setBackground(SystemColor.inactiveCaption);
		this.panel.add(this.panelOeste, BorderLayout.WEST);
		
		this.panelEste = new JPanel();
		this.panelEste.setBackground(SystemColor.inactiveCaption);
		this.panel.add(this.panelEste, BorderLayout.EAST);
		
		panelBotones = new JPanel();
		this.panelBotones.setBackground(SystemColor.inactiveCaption);
		this.contentPane.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.setLayout(new GridLayout(1, 3, 0, 0));
		
		this.panelAlta = new JPanel();
		this.panelAlta.setBackground(SystemColor.inactiveCaption);
		panelBotones.add(this.panelAlta);
		
		this.btnAlta = new JButton("Conectar");
		btnAlta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		this.btnAlta.setBackground(SystemColor.textHighlight);
		this.btnAlta.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 12));
		this.panelAlta.add(this.btnAlta);
	}

	@Override
	public void cerrar() {
		this.dispose();
	}

	@Override
	public void mostrar() {
		this.setVisible(true);
	}

	
	
	public JList<Object> getList() {
		return list;
	}

	public DefaultListModel<Object> getModeloLista() {
		return modeloLista;
	}

	@Override
	public void setActionListener(ActionListener actionListener) {
		this.btnAlta.addActionListener(actionListener);
		this.actionListener=actionListener;
	}

	public JButton getBtnAlta() {
		return btnAlta;
	}

	

}
