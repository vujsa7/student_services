package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AbstractStudentTable extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7920025647227272600L;
	
	private static AbstractStudentTable instance = null;

	public static AbstractStudentTable getInstance() {
		if (instance == null) {
			instance = new AbstractStudentTable();
		}
		return instance;
	}
	
	private List<Student> studenti;
	private List<String> kolone;
	private List<Student> defaultStudenti;
	
	private AbstractStudentTable() {
		
		initStudente();
		
		this.kolone = new ArrayList<String>();
		this.kolone.add("Indeks");
		this.kolone.add("Ime");
		this.kolone.add("Prezime");
		this.kolone.add("Godina studija");
		this.kolone.add("Status");
		this.kolone.add("Prosek");
	}
	
	private void initStudente() {
		this.studenti = new ArrayList<Student>();
		studenti.add(new Student("Pera", "Peric", LocalDate.of(1973, 10, 13), "Bulevar oslobodjenja 2", "060111222", "peraperic@gmail.com",
				"RA2/2020", "2019", "I(prva)", "Budzet", "9.2", new ArrayList<Predmet>(), new ArrayList<Predmet>()));
		studenti.add(new Student("Mika", "Mikic", LocalDate.of(1973, 10, 14), "Bulevar oslobodjenja 2", "060111222", "peraperic@gmail.com",
				"RA1/2019", "2019", "II(druga)", "Budzet", "8.9", new ArrayList<Predmet>(), new ArrayList<Predmet>()));
		studenti.add(new Student("Ivan", "Ivanovic", LocalDate.of(1973, 10, 15), "Bulevar oslobodjenja 2", "060111222", "peraperic@gmail.com",
				"RA3/2018", "2019", "III(treca)", "Samofinansiranje", "9.1", new ArrayList<Predmet>(), new ArrayList<Predmet>() ));
		
		defaultStudenti = studenti;
	}
	
	public List<Student> getStudenti() {
		return studenti;
	}
	
	public void setStudente(List<Student> studenti) {
		this.studenti = studenti;
	}
	
	public List<String> getKolone() {
		return kolone;
	}
	
	public void setKolone(List<String> kolone) {
		this.kolone = kolone;
	}
	
	@Override
	public int getRowCount() {
		return studenti.size();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}
	
	@Override
	public String getColumnName(int column) {
		return this.kolone.get(column);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Student student = this.studenti.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return student.getBrojIndeksa();
		case 1:
			return student.getIme();
		case 2:
			return student.getPrezime();
		case 3:
			return student.getTrenutnaGodStudija();
		case 4:
			return student.getStatus();
		case 5:
			return student.getProsek();
		default:
			return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
	    return getValueAt(0, column).getClass();
	}
	
	public Student getRow(int rowIndex) {
		return this.studenti.get(rowIndex);
	}
	
	public void removeRow(int rowIndex) {
		this.studenti.remove(rowIndex);
	}

	public void dodajStudenta(String ime, String prezime, LocalDate datumRodjenja, String adresaStanovanja,
			String kontaktTelefon, String emailAdresa, String brojIndeksa, String godUpisa, String trenutnaGodStudija,
			String status) {
		
		this.studenti.add(new Student(ime, prezime, datumRodjenja, adresaStanovanja, kontaktTelefon,
				emailAdresa, brojIndeksa, godUpisa, trenutnaGodStudija, status, null, new ArrayList<Predmet>(), new ArrayList<Predmet>()));
	}

	public List<Student> getDefaultStudenti() {
		return defaultStudenti;
	}

	public void setTrazeniStudenti(List<Student> trazeniStudenti) {
		this.defaultStudenti = studenti;
		this.studenti = trazeniStudenti;
	}
	
	public void setDefaultStudente() {
		this.studenti = defaultStudenti;
	}

	public ArrayList<Predmet> nabaviNepolozenePredmeteStudenta(String brIndex) {
		for(Student student : studenti) {
			if(student.getBrojIndeksa() == brIndex) {
				return (ArrayList<Predmet>) student.getNepolozeniIspiti();
			}
		}
		return null;
	}

	public boolean nekiStudentImaPolozenIspit(String sifraPredmeta) {
		for(Student s : studenti) {
			for(Predmet p : s.getPolozeniIspiti()) {
				if(p.getSifraPredmeta() == sifraPredmeta) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean nekiStudentImaNepolozenIspit(String sifraPredmeta) {
		for(Student s : studenti) {
			for(Predmet p : s.getNepolozeniIspiti()) {
				if(p.getSifraPredmeta() == sifraPredmeta) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int vratiGodinuStudenta(String godina) {
		if(godina == "I(prva)") return 1;
		else if(godina == "II(druga)") return 2;
		else if(godina == "III(treća)") return 3;
		else if(godina == "IV(četvrta)") return 4;
		else if(godina == "V(master studije)") return 5;
		else if(godina == "VI(doktorske studije)") return 6;
		else if(godina == "VII(doktorske studije)") return 7;
		else return 8;
		
		
	}
}
