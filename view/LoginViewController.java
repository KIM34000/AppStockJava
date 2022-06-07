/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

import app.stock.controller.ViewsManager;
import app.stock.model.core.Model;
import app.stock.model.user.User;
import app.stock.tools.crypto.Hasher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author lina7
 */
public class LoginViewController {
	@FXML
	private TextField user;
	@FXML
	private PasswordField password;
	@FXML
	private Button loginbtn;
	@FXML
	private Label authError;

	public void initialize() {
	}

	public void initManager(final LoginViewController loginManager) {
		loginbtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				authorize();
			}
		});
	}

	@FXML
	private String authorize() {
		String login = user.getText();
		String pwd = password.getText();
		System.out.println(login);

		try {
			User us = Model.loadItem(User.class, "SELECT Id_user, login, password FROM utilisateur WHERE login=?;",
					login);
			if (us != null && Hasher.validatePassword(pwd, us.getPasswordHash())) {
				System.out.println("auth ok");
				authError.setText("Valid credentials");
				User.currentUserId = us.getId();
				// show main view
				ViewsManager.showListView();
				// LoginManager.authenticated(login);
				return us.getLogin();
			}
		} catch (SQLException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		System.out.println("auth ko");
		authError.setText("Invalid credentials");
		User.currentUserId = 0;
		return null;
	}
}
