/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.stock.model.article.Article;
import app.stock.model.order.Order;
import app.stock.view.AddSupplierViewController;
import app.stock.view.EditLotsViewController;
import app.stock.view.EditOrderViewController;
import app.stock.view.EditPasswordViewController;
import app.stock.view.ListViewController;
import app.stock.view.ListViewOrderController;
import app.stock.view.LoginViewController;
import app.stock.view.SupplierListViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author lina7
 */
public class ViewsManager {
	public static Scene scene;

	/**
	 *
	 * @param selectedItem
	 */

	private ViewsManager() {
		// nothing
	}

	public static void showLoginScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(LoginViewController.class.getResource("LoginView.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showEditPasswordView() {
		try {
			FXMLLoader loader = new FXMLLoader(EditPasswordViewController.class.getResource("EditPasswordView.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showListView() {
		try {
			FXMLLoader loader = new FXMLLoader(ListViewController.class.getResource("ListView.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showEditLotsView(Article article) {
		try {
			FXMLLoader loader = new FXMLLoader(EditLotsViewController.class.getResource("EditLotsView.fxml"));
			scene.setRoot((Parent) loader.load());
			EditLotsViewController controller = (EditLotsViewController) loader.getController();
			controller.setArticle(article);
			controller.initialize();
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showAddsupplierScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(AddSupplierViewController.class.getResource("AddSupplierView.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showSupplierListScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(SupplierListViewController.class.getResource("SupplierListView.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showOrderListView() {
		try {
			FXMLLoader loader = new FXMLLoader(ListViewOrderController.class.getResource("ListViewOrder.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void showEditOrderView(Order order) {
		try {
			FXMLLoader loader = new FXMLLoader(EditOrderViewController.class.getResource("EditOrderView.fxml"));
			scene.setRoot((Parent) loader.load());
			EditOrderViewController controller = (EditOrderViewController) loader.getController();
			controller.setOrder(order);
			controller.initialize();
		} catch (IOException ex) {
			Logger.getLogger(ViewsManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
