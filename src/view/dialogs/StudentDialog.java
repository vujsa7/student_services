package view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.StudentController;
import main.MainFrame;
import view.dialogs.components.ButtonHolderPanel;
import view.dialogs.components.CustomComboBox;
import view.dialogs.components.CustomTextField;
import view.dialogs.components.DateComboBox;
import view.dialogs.components.DialogConfirmButton;
import view.dialogs.components.ErrorPanel;
import view.dialogs.components.FieldName;
import view.listeners.StudentListener;

public class StudentDialog extends JDialog{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7352043213856771747L;
	
	private static StudentDialog instance = null;
	
	public static StudentDialog getInstance() {	
		if(instance == null) {
			try {
				instance = new StudentDialog(MainFrame.getInstance());
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return instance;
	}
	
	public static ArrayList<ErrorPanel> errorPanelList = new ArrayList<ErrorPanel>();
	public static ArrayList<CustomComboBox> customComboBoxes = new ArrayList<CustomComboBox>();
	public static ArrayList<DateComboBox> dateComboBoxes = new ArrayList<DateComboBox>();
	public static ArrayList<JTextField> textFieldList = new ArrayList<JTextField>();
	private static DialogConfirmButton dialogConfirmButton;
	
	
	public static final String[] fieldText = {"Ime", "Prezime", "Datum rođenja", "Adresa stanovanja", "Broj telefona", "E-mail adresa",
			"Broj indeksa", "Godina upisa", "Trenutna godija studija", "Način finansiranja"};
	public String[] textFieldName = {"0","1","2","3","4","5","6"};
	
	public String[] regex = {
			"[A-Za-zšđžčć]{1,20}",
			"[A-Za-zšđžčć]{1,20}",
			".+",
			"[0-9]{3}\\/?[0-9]{3,4}\\-?[0-9]{3,4}",
			"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
			"[A-Z]{2}[0-9]{1,3}\\/[0-9]{4}",
			"\\d{4}"
	};
	public String[] errorText = {
			"Pogrešan format imena",
			"Pogrešan format prezimena",
			"Unesite grad, ulicu i broj",
			"Broj telefona mora da sadrži 9 ili 10 cifara",
			"Pogrešan format e-mail adrese",
			"Pogrešan format broja indeksa (SMERBROJ/GODINA UPISA)",
			"Pogrešan format godine upisa (XXXX)"
	};
	@SuppressWarnings("serial")
	public ArrayList<String> godina_studija = new ArrayList<String>() {{
		   add("I(prva)");
		   add("II(druga)");
		   add("III(treća)");
		   add("IV(četvrta)");
		   add("V(master studije)");
		   add("VI(doktorske studije)");
		   add("VII(doktorske studije)");
		   add("VIII(doktorske studije)");
		}};
		
	@SuppressWarnings("serial")
	public ArrayList<String> status = new ArrayList<String>() {{
		add("Budžet");
		add("Samofinansiranje");
		
	}};
	@SuppressWarnings("serial")
	public ArrayList<ArrayList<String>> studentLista = new ArrayList<ArrayList<String>>() {{
		   add(godina_studija);
		   add(status);
	}};
	@SuppressWarnings("serial")
	public ArrayList<Integer> days = new ArrayList<Integer>() {{
		for(int i = 1; i <= 31; i++)
			add(i);
	}};
	@SuppressWarnings("serial")
	public ArrayList<Integer> months = new ArrayList<Integer>() {{
		for(int i = 1; i <= 12; i++)
			add(i);
	}};
	@SuppressWarnings("serial")
	public ArrayList<Integer> years = new ArrayList<Integer>() {{
		for(int i = 1955; i <= 2001; i++)
			add(i);
	}};
	
	private StudentDialog(JFrame parent) {
		super(parent, "Dodavanje studenta", true);		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(507, 750));
		setResizable(false);
		pack();
		setLocationRelativeTo(parent);
		
		dialogConfirmButton = new DialogConfirmButton();
		dialogConfirmButton.addMouseListener(new MyMouseListener());
		
		JPanel basePanel = new JPanel();
		BoxLayout box = new BoxLayout(basePanel, BoxLayout.Y_AXIS);
		basePanel.setLayout(box);
		basePanel.setBackground(new Color(249,249,249));
		basePanel.add(Box.createVerticalStrut(18));
		basePanel.setPreferredSize(new Dimension(507,692));
		int regexCounter = 0;
		int textFieldCounter = 0;
		int errorTextCounter = 0;
		
		for(int i = 1; i <= 10; i++) {
			JPanel holderPanel = new JPanel();
			holderPanel.setPreferredSize(new Dimension(507, 49));
			holderPanel.setOpaque(false);
			
			BoxLayout boxHolder = new BoxLayout(holderPanel, BoxLayout.X_AXIS);
			holderPanel.setLayout(boxHolder);
						
			FieldName fieldName = new FieldName(fieldText[i-1]);
			
			holderPanel.add(fieldName);
			holderPanel.add(Box.createHorizontalStrut(39));
			
			
			
			if(i == 9 || i == 10) {
				CustomComboBox customComboBox = new CustomComboBox(studentLista.get(i-9));
				customComboBoxes.add(customComboBox);
				holderPanel.add(customComboBox);
			} else if (i == 3) {
				DateComboBox yearsComboBox = new DateComboBox(years, new Dimension(80, 36), "years");
				dateComboBoxes.add(yearsComboBox);
				holderPanel.add(yearsComboBox);
				holderPanel.add(Box.createHorizontalStrut(8));
				DateComboBox monthsComboBox = new DateComboBox(months, new Dimension(58, 36), "months");
				dateComboBoxes.add(monthsComboBox);
				holderPanel.add(monthsComboBox);
				holderPanel.add(Box.createHorizontalStrut(8));
				DateComboBox daysComboBox = new DateComboBox(days, new Dimension(58, 36), "days");
				dateComboBoxes.add(daysComboBox);
				holderPanel.add(daysComboBox);
				daysComboBox.setDateComboBoxes(dateComboBoxes);
				monthsComboBox.setDateComboBoxes(dateComboBoxes);
				yearsComboBox.setDateComboBoxes(dateComboBoxes);
			} else {
				JPanel textAndErrorPanel = new JPanel();
				textAndErrorPanel.setPreferredSize(new Dimension(214, 49));
				textAndErrorPanel.setMaximumSize(new Dimension(214, 49));
				textAndErrorPanel.setMinimumSize(new Dimension(214, 49));
				textAndErrorPanel.setOpaque(false);
				BoxLayout boxTextAndError = new BoxLayout(textAndErrorPanel, BoxLayout.Y_AXIS);
				textAndErrorPanel.setLayout(boxTextAndError);
				
				JPanel textPanel = new JPanel();
				textPanel.setPreferredSize(new Dimension(214, 36));
				textPanel.setMinimumSize(new Dimension(214, 36));
				textPanel.setMaximumSize(new Dimension(214, 36));
				textPanel.setLayout(new GridLayout(0,1));
				textPanel.setOpaque(false);
				JTextField textField = new JTextField();
				textField.getDocument().addDocumentListener(listener);
				textFieldList.add(textField);
				
				textField.addFocusListener(new StudentListener(regex[regexCounter++]));
				CustomTextField customTextField = new CustomTextField(textField, textFieldName[textFieldCounter++]);
				textPanel.add(customTextField);
					
				ErrorPanel errorPanel = new ErrorPanel(errorText[errorTextCounter++]);		
				errorPanelList.add(errorPanel);
				
				textAndErrorPanel.add(textPanel);
				textAndErrorPanel.add(errorPanel);
				holderPanel.add(textAndErrorPanel);
			}
			
			holderPanel.add(Box.createHorizontalStrut(54));
			
			basePanel.add(holderPanel);
			if(i%3 == 0 || i%8 == 0) {
				if(i%9==0) {
					basePanel.add(Box.createVerticalStrut(5));
				} else {
					basePanel.add(Box.createVerticalStrut(45));
				}
			} else {
				basePanel.add(Box.createVerticalStrut(5));
			}
			
		}
		
		basePanel.add(Box.createVerticalStrut(13));
		ButtonHolderPanel buttonHolderPanel = new ButtonHolderPanel(dialogConfirmButton, this);
		
		basePanel.add(buttonHolderPanel);
		basePanel.add(Box.createVerticalStrut(16));
		
		
		add(basePanel);
	}
	
	DocumentListener listener = new DocumentListener() {
	    @Override
	    public void removeUpdate(DocumentEvent e) {
	    	changedUpdate(e); 
	    }
	    @Override
	    public void insertUpdate(DocumentEvent e) { 
	    	changedUpdate(e); 
	    }
	    @Override
	    public void changedUpdate(DocumentEvent e) {
	        boolean enableButton = true;
	        for (JTextField textField : textFieldList) {
	            if (textField.getText().isEmpty()) {
	            	enableButton = false;
	            }
	        }
	        dialogConfirmButton.setEnabled(enableButton);
	    }
	};
	
	public static void showErrorPanel(int index) {
		int i = 0;
		for(JPanel errorPanel : errorPanelList) {
			if(i == index) {
				errorPanel.setVisible(true);
			}
			i++;
		}
	}
	
	public static void hideErrorPanel(int index) {
		int i = 0;
		for(JPanel errorPanel : errorPanelList) {
			if(i == index) {
				errorPanel.setVisible(false);
			}
			i++;
		}
	}
	
	public static void checkIfCanBeValidated() {
		dialogConfirmButton.validated = true;
		for(JPanel errorPanel : StudentDialog.errorPanelList) {
			if(errorPanel.isVisible()) {
				dialogConfirmButton.validated = false;
				break;
			}
		}
	}
	
	public void setDefaultValues() {
		for(JPanel errorPanel : errorPanelList) {
			errorPanel.setVisible(false);
		}
		for(JTextField textField : textFieldList) {
			textField.setText("");
		}
		for(DateComboBox dateComboBox : dateComboBoxes) {
			dateComboBox.setDefaultDate();
		}
		for(CustomComboBox customComboBox : customComboBoxes) {
			customComboBox.setDefaultValue();
		}
	}
	
	public static void showIndexErrorPanel() {
		ErrorPanel errorPanel = errorPanelList.get(5);  //jer mi je na petom mestu broj indeksa, tj na sestom ali indeksiranje ide od nule
		errorPanel.setLabelText("Student sa tim brojem indeksa je vec unet u tabelu!");
		errorPanel.setVisible(true);
	}
	
	public static void hideIndexErrorPanel() {
		ErrorPanel errorPanel = errorPanelList.get(5);
		errorPanel.setLabelText("Pogrešan format broja indeksa (SMERBROJ/GODINA UPISA)");
		errorPanel.setVisible(false);
	}
	
	private class MyMouseListener extends MouseAdapter{
		public void mouseEntered(MouseEvent mouseEvent) {
			JButton thisButton = (JButton) mouseEvent.getComponent();
			if(thisButton.isEnabled()) {
				thisButton.setIcon(dialogConfirmButton.hoveredConfirmIcon);
				new Thread(
						new Runnable() {
							public void run() {
								for (float i = 0.75f; i <= 1f; i += 0.02f){
									dialogConfirmButton.setAlpha(i);
									try {
										Thread.sleep(10);
									} catch (Exception e) {
										
									}
								}
							}
						}).start();
			}

		}
		
		public void mouseExited(MouseEvent mouseEvent) {
			JButton thisButton = (JButton) mouseEvent.getComponent();
			if(thisButton.isEnabled()) {
				new Thread(
						new Runnable() {
							public void run() {
								for (float i = 1f; i >= 0.8f; i -= 0.07f){
									dialogConfirmButton.setAlpha(i);
									try {
										Thread.sleep(10);
									} catch (Exception e) {
										
									}
								}
								thisButton.setIcon(dialogConfirmButton.confirmIcon);
								dialogConfirmButton.setAlpha(1f);
							}
						}).start();
			}
		}
		
		public void mousePressed(MouseEvent mouseEvent) {
			JButton thisButton = (JButton) mouseEvent.getComponent();
			if(thisButton.isEnabled()) {
				new Thread(
						new Runnable() {
							public void run() {
								for (float i = 1f; i >= 0.7f; i -= 0.1f) {
									dialogConfirmButton.setAlpha(i);
									try {
										Thread.sleep(1);
									} catch (Exception e) {
										
									}
								}
							}
						}).start();
				ArrayList<String> comboAnswers = new ArrayList<String>();
				for(CustomComboBox customComboBox : StudentDialog.customComboBoxes) {
					comboAnswers.add(customComboBox.getField());
				}
				String date = DateComboBox.dateString;
				LocalDate localDate;
				if(date.contentEquals("yyyy-MM-dd")) {
					localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				} else if(date.contentEquals("yyyy-M-dd")){
					localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-dd"));
				} else if(date.contentEquals("yyyy-MM-d")){
					localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-d"));
				} else {
					localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
				}
				
				int j = 0;
				for(JTextField textField : textFieldList) {
					if(!Pattern.matches(regex[j++], textField.getText())){
						String textFieldName = textField.getName();
						StudentDialog.showErrorPanel(Integer.parseInt(textFieldName));
						StudentDialog.checkIfCanBeValidated();
					} else {
						String textFieldName = textField.getName();
						if(textFieldName.equals("5")) {
							if(StudentController.getInstance().proveriPostojanjeIndeksa(textField.getText())) {
								StudentDialog.showIndexErrorPanel();
								StudentDialog.checkIfCanBeValidated();
							} else {
								StudentDialog.hideIndexErrorPanel();
								StudentDialog.checkIfCanBeValidated();
							}
						} else {
							StudentDialog.hideErrorPanel(Integer.parseInt(textFieldName));
							StudentDialog.checkIfCanBeValidated();
						}
					}
					
				}
				
				if(dialogConfirmButton.validated) {
					StudentController.getInstance().dodajStudenta(textFieldList.get(0).getText(), textFieldList.get(1).getText(),
							localDate,textFieldList.get(2).getText(), textFieldList.get(3).getText(), textFieldList.get(4).getText(),
							textFieldList.get(5).getText(), textFieldList.get(6).getText(),comboAnswers.get(0), comboAnswers.get(1));
						dispose();
				}
			}		
		}
	}
}
