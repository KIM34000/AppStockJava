package app.stock.model.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.stock.tools.db.MySql;

public interface Model<T> {
    // ça veut dire que tu dois avoir en paramètre de ma méthode : 
    // une Class de n'importe quel type qui implémente un Model<T>
    // c'est ce qui va fixer le paramètre T
    // si on utilise la méthode avec : loadList(User.class, "SELECT ....");
    //comme User implémente Model<User>
    // alors T = User
    // donc ma méthode va renvoyer : List<User>
    // et partout dans la méthode où il y a T, c'est comme si il y avait User
    // si on appelle la méthode avec une autre classe, alors T=AutreClasse
    // la seule codition, c'est que la classe qui soit passée en parametre doit implémenter Model<T>
    // c'est pour ça : Class<? extends Model<T>> clazz
    // classe de n'importe quel type : Class<?>
    // classe de n'importe quel type qui implémente X : Class<? extends X>
    // classe de n'importe quel type qui implémente Model<T> : Class<? extends Model<T>>
	static <T> List<? extends T> loadList(Class<? extends Model<? extends T>> clazz, String sql, Object... params) throws SQLException {
		List<T> list = new ArrayList<T>();
		try (ResultSet rs = MySql.execute(sql,params)) {
			if (rs != null) {
				rs.beforeFirst();
				while (rs.next()) {
					T item = null;
					try {   
                                            // on implémémente fromResultSet pour le User
						item = (clazz.newInstance()).fromResultSet(rs);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
					list.add(item);
				}
			}
		}
		return list;
	}
        static <T> T loadItem(Class<? extends Model<T>> clazz, String sql, Object... params) throws SQLException {
		try (ResultSet rs = MySql.execute(sql, params)) {
			if (rs != null) {
				rs.beforeFirst();
				while (rs.next()) {
					T item = null;
					try {
						item = (clazz.newInstance()).fromResultSet(rs);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
					return item;
				}
			}
		}
		return null;
	}
//	static <T> T loadItem(Class<? extends Model<T>> clazz, String sql) throws SQLException {
//		try (ResultSet rs = MySql.execute(sql)) {
//			if (rs != null) {
//				rs.beforeFirst();
//				while (rs.next()) {
//					T item = null;
//					try {
//						item = (clazz.newInstance()).fromResultSet(rs);
//					} catch (InstantiationException | IllegalAccessException e) {
//						e.printStackTrace();
//					}
//					return item;
//				}
//			}
//		}
//		return null;
//	}

	T fromResultSet(ResultSet rs) throws SQLException;

	T save() throws SQLException;

	void delete() throws SQLException;
}
