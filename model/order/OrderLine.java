package app.stock.model.order;

import java.sql.ResultSet;
import java.sql.SQLException;

import app.stock.model.core.Model;
import app.stock.tools.db.MySql;

public class OrderLine implements Model<OrderLine> {
	private boolean isModify;

	private int idOrder;
	private int idArticle;
	private double quantity;

	public OrderLine() {
		this.isModify = false;
		this.idOrder = 0;
		this.idArticle = 0;
		this.quantity = 0;
	}

	public int getIdCommande() {
		return idOrder;
	}

	public void setIdCommande(int idCommande) {
		this.isModify = true;
		this.idOrder = idCommande;
	}

	public int getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(int idArticle) {
		this.isModify = true;
		this.idArticle = idArticle;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.isModify = true;
		this.quantity = quantity;
	}

	@Override
	public OrderLine fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.idOrder = rs.getInt("id_commande");
			this.idArticle = rs.getInt("id_article");
			this.quantity = rs.getDouble("quantity");
		}
		return this;
	}

	public OrderLine save() throws SQLException {
		if (this.isModify) {
			MySql.execute("UPDATE ligne_commande SET quantity = ? WHERE id_commande = ? AND id_article = ?;",
					this.quantity, this.idOrder, this.idArticle);
			MySql.execute(
					"INSERT INTO ligne_commande (id_commande, id_article, quantity) " + "SELECT ?, ?, ? FROM DUAL "
							+ "WHERE NOT EXISTS (select 1 from ligne_commande where id_commande = ? and id_article = ?);",
					this.idOrder, this.idArticle, this.quantity, this.idOrder, this.idArticle);
		}
		return this;
	}

	@Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM ligne_commande WHERE id_commande = ? AND id_article = ?;", this.idOrder,
				this.idArticle);
	}

}
