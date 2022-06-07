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

/**
 *
 * @author lina7
 */
public class EditPasswordViewController {

	private static final int MIN_PASSWORD_LENGHT = 6;
	@FXML
	private PasswordField currentPwd;
	@FXML
	private PasswordField newPwd;
	@FXML
	private PasswordField newPwdConfirm;

	@FXML
	private Label errorLabel;

	@FXML
	private Button cancelButton;
	@FXML
	private Button updateButton;

	private User currentUser;

	public EditPasswordViewController() {
		loadModels();
	}

	public void initialize() {
		loadEvents();
	}

	private void loadModels() {
		try {
			this.currentUser = Model.loadItem(User.class,
					"SELECT Id_user, login, password FROM utilisateur WHERE Id_user=?;", User.currentUserId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadEvents() {
		this.cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showListView();
			}
		});
		this.updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					// check current pwd
					User user = EditPasswordViewController.this.currentUser;
					String currentPass = EditPasswordViewController.this.currentPwd.getText();
					if (user == null || !Hasher.validatePassword(currentPass, user.getPasswordHash())) {
						EditPasswordViewController.this.currentPwd.setText("");
						EditPasswordViewController.this.errorLabel.setText("Invalid current password.");
						return;
					}
					// check validation rules of new pwd
					String newPass = EditPasswordViewController.this.newPwd.getText();
					String newPassConfirm = EditPasswordViewController.this.newPwdConfirm.getText();
					if (!checkNewPasswordRules(newPass, newPassConfirm)) {
						return;
					}
					// pwd is ok, set new pwd
					user.setPasswordHash(Hasher.generateHash(newPass));
					user.save();
					EditPasswordViewController.this.currentPwd.setText("");
					EditPasswordViewController.this.newPwd.setText("");
					EditPasswordViewController.this.newPwdConfirm.setText("");
					EditPasswordViewController.this.errorLabel
							.setText("Password changed successfully. Please logout to continue.");
					EditPasswordViewController.this.cancelButton.setText("Logout");
					EditPasswordViewController.this.cancelButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event2) {
							User.currentUserId = 0;
							ViewsManager.showLoginScreen();
						}
					});

				} catch (GeneralSecurityException | SQLException e) {
					EditPasswordViewController.this.currentPwd.setText("");
					EditPasswordViewController.this.newPwd.setText("");
					EditPasswordViewController.this.newPwdConfirm.setText("");
					EditPasswordViewController.this.errorLabel.setText("Error changing password.");
					e.printStackTrace();
				}
			}
		});
	}

	protected boolean checkNewPasswordRules(String newPassword, String newPasswordConfirm) {
		if (newPassword == null || newPassword.isEmpty()) {
			EditPasswordViewController.this.newPwd.setText("");
			EditPasswordViewController.this.newPwdConfirm.setText("");
			this.errorLabel.setText("New password can not be empty.");
			return false;
		}
		if (newPasswordConfirm == null || newPasswordConfirm.isEmpty()) {
			EditPasswordViewController.this.newPwdConfirm.setText("");
			this.errorLabel.setText("Please confirm new password.");
			return false;
		}
		if (newPassword.length() < MIN_PASSWORD_LENGHT) {
			EditPasswordViewController.this.newPwd.setText("");
			EditPasswordViewController.this.newPwdConfirm.setText("");
			this.errorLabel.setText("Password must have a minimum lenght of " + MIN_PASSWORD_LENGHT + ".");
			return false;
		}

		if (!newPassword.equals(newPasswordConfirm)) {
			EditPasswordViewController.this.newPwd.setText("");
			EditPasswordViewController.this.newPwdConfirm.setText("");
			this.errorLabel.setText("New password and confirmation are not the same.");
			return false;
		}
		this.errorLabel.setText("");
		return true;
	}
}
