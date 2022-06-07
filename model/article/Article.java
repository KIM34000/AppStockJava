/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.model.article;

import app.stock.model.core.Model;
import app.stock.tools.db.MySql;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lina7
 */
//la méthode fromResultSet qui va s'occuper de renseigner les champs de l'objet
public class Article implements Model<Article>{
    protected int id;
	private String nom;
	private String etat;
        private String marque;
        private int quantite;
        
        public Article() {
		this(null, null, null);
	}
	public Article(String nom, String etat, String marque) {
		this.id = 0;
		this.nom = nom;
                this.etat = etat;
                this.marque = marque;
                this.quantite = 0;                
     
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
        public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
        public String getMarque() {
		return marque;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
        public int getQuantite() {
		return quantite;
	}	
	
	@Override
	public Article fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.id = rs.getInt("id_article");
			this.nom = rs.getString("nom");
                        this.etat = rs.getString("etat");
                        this.marque = rs.getString("marque");
                        this.quantite = rs.getInt("quantity");
		}
		return this;
	}

	@Override
	public Article save() throws SQLException {
		if (this.id == 0) {
			// TODO ...
			try (ResultSet rs = MySql.execute("INSERT INTO article (nom, etat, marque) VALUES (?, ?, ?);", this.nom, this.etat, this.marque)) {
				if (rs != null) {
					rs.beforeFirst();
					while (rs.next()) {
// le driver jdbc ne fournit pas le nom de la colonne dans le returnKeys, mais iniquement un resltSet avec des indexes
//c'est le cas pour les autoincrement retournés lorsque on exécute un INSERT
						this.id = rs.getInt(1);
						return this;
					}
				}
                              
			}
			return this;
		}

		// TODO ...
		MySql.execute("UPDATE article SET nom = ?, etat = ?, marque = ? WHERE id_article = ?;", this.nom, this.etat, this.marque, this.id);
		return this;
	}

	@Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM article WHERE id_article = ?;", this.id);
	}
        @Override
	public String toString() {
		return this.getNom() + " (marque : " + this.getMarque() + ", qte : " + this.getQuantite() + ")";
	}

}
