/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.model.article;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import app.stock.model.core.Model;
import app.stock.tools.db.MySql;

public class Lot implements Model<Lot> {
	private boolean isModify;

	private int idArticle;
	private int refOrder;
	private double quantity;
	private double unitCost;
	private Timestamp buyDate;


	public Lot() {
		this.isModify = false;

		this.idArticle = 0;
		this.refOrder = 0;
		this.quantity = 0;
		this.unitCost = 0;
		this.buyDate = null;
	}

	public int getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(int idArticle) {
		this.isModify = true;
		this.idArticle = idArticle;
	}

	public int getRefOrder() {
		return refOrder;
	}

	public void setRefOrder(int refOrder) {
		this.isModify = true;
		this.refOrder = refOrder;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.isModify = true;
		this.quantity = quantity;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.isModify = true;
		this.unitCost = unitCost;
	}

	public Instant getBuyDate() {
		return buyDate == null ? null : buyDate.toInstant();
	}

	public void setBuyDate(Instant buyDate) {
		this.isModify = true;
		this.buyDate = buyDate == null ? null : Timestamp.from(buyDate);
	}

	@Override
	public Lot fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.idArticle = rs.getInt("id_article");
			this.refOrder = rs.getInt("ref_commande");
			this.quantity = rs.getDouble("quantite_achete");
			this.unitCost = rs.getDouble("cout_unitaire");
			this.buyDate = rs.getTimestamp("date_achat");
		}
		return this;
	}
// pas autoincrement car PK est composée de deux FKs
//donc au moment de save, on ne saura pas si la ligne existe dans la bdd ou non
//comme on n'as pas d'autoincrement, on n'a pas besoin de lire le resultSet comme dans Article
//par contre, comme on ne sait pas si la ligne existe ou non, on ne sait pas si il nous faut faire un insert ou un update
        //UPDATE table SET champs_sans_PK WHERE pk1=? AND pk2=? ...;
//la première requete met à jour les champs (sans la pk) si il trouve la ligne, sinon il ne met rien à jour
        //INSERT INTO table (tous les champs) SELECT toutes_les_valeurs WHERE NOT EXISTS (select 1 from table where pk1=? and pk2=? ...);
//la deuxième requete insert la ligne avec les bonnes valeurs, uniquement si la PK n'existe pa       
//rien a mettre dans le IF car on ne connait pas si la ligne existe dans la bdd ou non
@Override
	public Lot save() throws SQLException {
		if (this.isModify) {
			MySql.execute(
					"UPDATE lot SET quantite_achete = ?, cout_unitaire = ?, date_achat = ? "
							+ "WHERE id_article = ? AND ref_commande = ?;",
					this.quantity, this.unitCost, this.buyDate, this.idArticle, this.refOrder);
			MySql.execute("INSERT INTO lot (id_article, ref_commande, quantite_achete, cout_unitaire, date_achat) "
					+ "SELECT ?, ?, ?, ?, ? FROM DUAL "
					+ "WHERE NOT EXISTS (select id_article from lot where id_article = ? and ref_commande = ?);",
					this.idArticle, this.refOrder, this.quantity, this.unitCost, this.buyDate, this.idArticle,
					this.refOrder);
		}
		return this;
	}

	@Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM lot WHERE id_article = ? AND ref_commande = ?;", this.idArticle, this.refOrder);
	}

}