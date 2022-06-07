/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.model.article;

import app.stock.model.article.Article;
import app.stock.model.core.Model;
import app.stock.tools.db.MySql;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lina7
 */
public class Utility extends Article{

//    public Utility(String nom, String etat, String marque, int quantite) {
//		super(nom, etat, marque, quantite);
//	}

	@Override
	public Utility save() throws SQLException {
		boolean isNew = this.id == 0;
		super.save();
		if (isNew) {
			MySql.execute("INSERT INTO ingredient (id_article) VALUES (?);", this.id);
		}
		return this;
	}

	@Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM ingredient WHERE id_article = ?;", this.id);
		super.delete();
	}
}
