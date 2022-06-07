package app.stock.model.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import app.stock.model.core.Model;
import app.stock.tools.db.MySql;

public class User implements Model<User> {
	public static int currentUserId = 0;

	private int id;
	private String login;
	private String passwordHash;

	public User(String login) {
		this.id = 0;
		this.login = login;
		this.passwordHash = null;
	}

	public User() {
		this(null);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public User fromResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			this.id = rs.getInt("Id_user");
			this.login = rs.getString("login");
			this.passwordHash = rs.getString("password");
		}

		return this;
	}

	@Override
	public User save() throws SQLException {
		if (this.id == 0) {
			try (ResultSet rs = MySql.execute("INSERT INTO utilisateur (login, password) VALUES (?, ?);", this.login,
					this.passwordHash)) {
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

		MySql.execute("UPDATE utilisateur SET login = ?, password = ? WHERE Id_user = ?;", this.login,
				this.passwordHash, this.id);
		return this;
	}

	@Override
	public void delete() throws SQLException {
		MySql.execute("DELETE FROM utilisateur WHERE Id_user = ?" , this.id );
	}

}
