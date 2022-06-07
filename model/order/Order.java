/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.model.order;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import app.stock.model.core.Model;
import app.stock.tools.db.MySql;

/**
 *
 * @author lina7
 */
public class Order implements Model<Order> {

    private boolean isModify;

	private int idOrder;
	private String state;
	private String origin;
	private Timestamp creationDate;

	public Order() {
		this.isModify = false;
		this.idOrder = 0;
		this.state = null;
		this.origin = null;
		this.creationDate = null;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.isModify = true;
		this.idOrder = idOrder;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.isModify = true;
		this.state = state;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.isModify = true;
		this.origin = origin;
	}

	public Instant getCreationDate() {
		return creationDate == null ? null : creationDate.toInstant();
	}

	public void setCreationDate(Instant creationDate) {
		this.isModify = true;
		this.creationDate = creationDate == null ? null : Timestamp.from(creationDate);
	}

	@Override
	public Order fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.idOrder = rs.getInt("id_commande");
			this.state = rs.getString("etat");
			this.origin = rs.getString("provenance");
			this.creationDate = rs.getTimestamp("date_creation");
		}
		return this;
	}

	@Override
	public Order save() throws SQLException {
		if (this.idOrder == 0) {
			try (ResultSet rs = MySql.execute(
					"INSERT INTO commande (etat, date_creation, provenance) VALUES (?, ?, ?);", this.state,
					this.creationDate, this.origin)) {
				if (rs != null) {
					rs.beforeFirst();
					while (rs.next()) {
						this.idOrder = rs.getInt(1);
						return this;
					}
				}

			}
			return this;
		}

		if (isModify) {
			MySql.execute(
					"UPDATE commande SET etat = ?, date_creation = ?, provenance = ? WHERE id_commande = ?;",
					this.state, this.creationDate, this.origin, this.idOrder);
		}
		return this;
	}

	@Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM commande WHERE id_commande = ?;", this.idOrder);
	}
}

