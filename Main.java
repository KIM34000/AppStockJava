package app.stock;

import app.stock.controller.ViewsManager;
import app.stock.view.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(LoginViewController.class.getResource("LoginView.fxml"));
		Scene scene = new Scene(root, 640, 480);
		ViewsManager.scene = scene;
		ViewsManager.showLoginScreen();
		// ViewsManager.showListView();
		stage.setScene(scene);
		stage.setTitle("Nesti - Stocks");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
//		// ces lignes qui injectent le login admin avec le password (à activer pour initialiser la bdd)
//		String login = "admin";
//		String password = "admin";
//		User newUser = new User(login);
//		newUser.setPasswordHash(Hasher.generateHash(password));
//		newUser.save();
	}
}
