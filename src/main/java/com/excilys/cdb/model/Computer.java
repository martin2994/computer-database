package main.java.com.excilys.cdb.model;

import java.util.Date;

/**
 * Objet du modèle Computer
 *
 */
public class Computer {

	private int id;
	private String name;
	private Date introduced;
	private Date discontinued;
	private Company manufacturer;
	
	public Computer() {}
	
	public Computer(int id, String name, Date introduced, Date discontinued, Company manufacturer) {
		super();
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.manufacturer = manufacturer;
	}

	public Computer(String name, Date introduced, Date discontinued, Company manufacturer) {
		super();
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.manufacturer = manufacturer;
	}

	public Computer(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getIntroduced() {
		return introduced;
	}
	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}
	public Date getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}
	public Company getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Company manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + id;
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
				+ ", manufacturer=" + manufacturer + "]";
	}


	
	
}
