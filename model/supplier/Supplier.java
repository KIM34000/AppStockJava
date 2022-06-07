/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.model.supplier;

import app.stock.model.core.Model;
import app.stock.tools.db.MySql;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author lina7
 */
public class Supplier implements Model<Supplier> {
    	private int id;
	private String adresse;
	private String nom;
        private Contact contact;
                
        public Supplier() {
		this(null, null);
	}
        public Supplier(String adresse, String nom) {
		this.id = 0;
		this.nom = nom;
                this.adresse = adresse;              
                this.contact = null;             
     	}
        public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
        public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
        public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
        public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
               
        
    @Override    
    public Supplier fromResultSet(ResultSet rs) throws SQLException {
       if (rs != null) {
			this.id = rs.getInt("id_fournisseur");
			this.nom = rs.getString("nom");
                        this.adresse = rs.getString("adresse");                        
                        this.contact = new Contact();
                        this.contact.setId(rs.getInt("id_contact"));
                        this.contact.setNom(rs.getString("contact_nom"));
                        this.contact.setPrenom(rs.getString("contact_prenom"));
                        this.contact.setTelephone(rs.getString("contact_telephone"));
		}
		return this;
    }
    
    @Override
    public Supplier save() throws SQLException {
        if (this.contact != null) {
			this.contact = this.contact.save();
		}
        if (this.id == 0) {
			try (ResultSet rs = MySql.execute("INSERT INTO fournisseur (adresse, nom, id_contact) VALUES (?, ?, ?);",
					this.adresse, this.nom, this.contact.getId())) {
				if (rs != null) {
					rs.beforeFirst();
					while (rs.next()) {
						this.id = rs.getInt(1);
						return this;
					}
				}
			}
			return this;
		}
		
		MySql.execute("UPDATE fournisseur SET adresse = ?, nom = ?, id_contact = ? " + "WHERE id_fournisseur = ?;",
				this.adresse, this.nom, this.contact.getId(), this.id);
		return this;
    }

    @Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM fournisseur WHERE id_fournisseur = ?;", this.id);
		if (this.contact != null) {
			this.contact.delete();
		}
	}
    @Override
	public String toString() {
		return this.getNom() + " (nom : " + this.getNom() + ", adresse : " + this.getAdresse() + ", contact_nom : " + this.contact.getNom() +")";
	}
    
}
