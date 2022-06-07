package app.stock.tools.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySql {
	private static Object lockConnection = new Object();

	private static Connection connection = null;

	private MySql() {
		// nothing, utility class
	}

	private static Connection getConnection() throws SQLException {
		synchronized (lockConnection) {
			// si il n'y a pas une connection on se connect
			if (connection == null || connection.isClosed()) {
				connection = getNewConnection();
			}
		}
		return connection;
	}

	private static Connection getNewConnection() {
		Connection conn = null;

		try {
                    // c'est pour charger la classe du driver mysql
                    // s'assurer qu'il trouve bien la classe du driver
                    // donc dans le classpath
			// Class.forName("com.mysql.jdbc.Driver");
                        // DriverManager s'occupe de sélectionner le driver
                        // grace à la chaine de connection : "jdbc:mysql:...."
                        // driver fait la comunication avec sql server
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nestiapp?serverTimezone=UTC", "root", "");
		} catch (SQLException e) {
			System.out.println(e);
		}
		return conn;
	}

	@SuppressWarnings("resource")
	public static ResultSet execute(String sql, Object... params) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		for (int i = 0; i < params.length; i++) {
			preparedStatement.setObject(i + 1, params[i]);
		}

		boolean isResultSet = preparedStatement.execute();
		if (isResultSet) {
			return preparedStatement.getResultSet();
		}
		ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys != null) {
			return generatedKeys;
		}
		preparedStatement.close();
		conn.close();
		return null;
	}

//	public static ResultSet execute(String sql) throws SQLException {
//            // on récupère un connection valide
//		try (Connection conn = getConnection()) {
//                    // on construit l'object préparedStatement avec la requete sql
//			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
//                            // la méthode execute() renvoie true si la réponse de la requete est un résultSet,
//                            // donc on test le retour
//				boolean isResultSet = preparedStatement.execute();
//                                //  si il y a un resulteSet, on le renvoie
//				if (isResultSet) {
//                                    // il y a un resultSet dans le cas où la requete est un SELECT
//					return preparedStatement.getResultSet();                                
//				}
//                                // une fois j'ai eu resultset connection va fermer
//                                preparedStatement.close();
//		                conn.close();
//                                // pour un UPDATE, INSERT, DELETE : il n'y a pas de resultSet
//				return null;
//			}
//		}
//	}
}