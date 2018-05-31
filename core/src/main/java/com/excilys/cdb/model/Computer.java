package com.excilys.cdb.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Objet du modèle Computer.
 *
 */
@Entity
@Table(name = "computer")
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "introduced")
    private LocalDate introduced;

    @Column(name = "discontinued")
    private LocalDate discontinued;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company manufacturer;

    /**
     * Builder pour Computer.
     *
     */
    public static class Builder {

        private final String name;
        private long id;
        private LocalDate introduced;
        private LocalDate discontinued;
        private Company manufacturer;

        /**
         * Constructeur du builder.
         * @param name
         *            le nom du computer
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Ajoute une date introduced.
         * @param value
         *            la date introduced
         * @return le builder avec la date introduced
         */
        public Builder introduced(LocalDate value) {
            introduced = value;
            return this;
        }

        /**
         * Ajoute une date discontinued.
         * @param value
         *            la date discontinued
         * @return le builder avec la date discontinued
         */
        public Builder discontinued(LocalDate value) {
            discontinued = value;
            return this;
        }

        /**
         * Ajoute un manufactureur.
         * @param value
         *            le manufactureur
         * @return le builder avec le manufacturer
         */
        public Builder manufacturer(Company value) {
            manufacturer = value;
            return this;
        }

        /**
         * Ajoute un id.
         * @param value
         *            l'id
         * @return le builder avec l'id
         */
        public Builder id(long value) {
            id = value;
            return this;
        }

        /**
         * Retourne le computer avec les données du buildeur.
         * @return le computer
         */
        public Computer build() {
            if (this.name != null) {
                return new Computer(this);
            }
            return null;
        }
    }

    /**
     * Constructeur privé avec builder.
     * @param builder
     *            le builder contenant les infos
     */
    private Computer(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.manufacturer = builder.manufacturer;
    }

    /**
     * Constructeur vide.
     */
    public Computer() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getIntroduced() {
        return introduced;
    }

    public void setIntroduced(LocalDate introduced) {
        this.introduced = introduced;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(LocalDate discontinued) {
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
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
        result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Computer other = (Computer) obj;
        if (discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!discontinued.equals(other.discontinued)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!introduced.equals(other.introduced)) {
            return false;
        }
        if (manufacturer == null) {
            if (other.manufacturer != null) {
                return false;
            }
        } else if (!manufacturer.equals(other.manufacturer)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
                + ", manufacturer=" + manufacturer + "]";
    }

}
