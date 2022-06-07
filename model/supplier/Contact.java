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
public class Contact implements Model<Contact> {
    
        protected int id;
	private String nom;
	private String prenom;
        private String telephone;
        
        public Contact() {
		this(null, null, null);
	}
        
        public Contact(String nom, String prenom, String telephone) {
		this.id = 0;
		this.nom = nom;
                this.prenom = prenom;
                this.telephone = telephone;
                              
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
        public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
        public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
        
    @Override
    public Contact fromResultSet(ResultSet rs) throws SQLException {
       if (rs != null) {
			this.id = rs.getInt("id_contact");
			this.nom = rs.getString("nom");
                        this.prenom = rs.getString("prenom");                      
                        this.telephone = rs.getString("telephone");
                        
		}
		return this; 
    }

    @Override
    public Contact save() throws SQLException {
        if (this.id == 0) {
			// TODO ...
			try (ResultSet rs = MySql.execute("INSERT INTO contact (nom,prenom,telephone) VALUES (?, ?, ?);", this.nom, this.prenom, this.telephone)) {
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
		
		MySql.execute("UPDATE contact SET nom = ?, prenom = ?, telephone = ? WHERE id_contact = ?;",this.nom, this.prenom,this.telephone, this.id);
		return this;
    }

    @Override
    public void delete() throws SQLException {
         MySql.execute("DELETE FROM contact WHERE id_contact = ?;", this.id);
    }
    @Override
	public String toString() {
		return this.getNom() + " (nom : " + this.getNom() + ", prenom : " + this.getPrenom() + ", telepone : " +  this.getTelephone() +")";
	}
}
