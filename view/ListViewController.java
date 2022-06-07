/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;

import java.sql.SQLException;
import java.util.List;

import app.stock.controller.ViewsManager;
import app.stock.model.article.Ingredient;
import app.stock.model.article.Utility;
import app.stock.model.core.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 *
 * @author lina7
 */
public class ListViewController {
	@FXML
	private ListView<Ingredient> ingredientsListView;
	@FXML
	private ListView<Utility> utilitiesListView;
	@FXML
	private Button showOrdersListViewButton;
	@FXML
	private Button showSuppliersListViewButton;
	@FXML
	private Button editIngredientLotsButton;
	@FXML
	private Button editUtilityLotsButton;
	@FXML
	private Button deleteIngredientLotsButton;
	@FXML
	private Button deleteUtilityLotsButton;
	@FXML
	private Button logOut;
	@FXML
	private Button changePasswordButton;

	private List<Ingredient> ingredients;
	private List<Utility> utilities;


	public ListViewController() {
		loadModels();
	}
	public void initialize() {
		loadViews();
		loadEvents();
	}
	@SuppressWarnings("unchecked")
	private void loadModels() {
		try {
			this.ingredients = (List<Ingredient>) Model.loadList(Ingredient.class,
					"SELECT article.id_article, article.nom, article.etat, article.marque,(select sum(lot.quantite_achete  ) from lot \n" +
							"where lot.id_article = article.id_article) AS quantity\n" +
							"FROM article\n" +
							"INNER JOIN ingredient ON ingredient.id_article = article.id_article\n" +
							"HAVING quantity <> 0\n" +
							"ORDER BY article.nom;");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			this.utilities = (List<Utility>) Model.loadList(Utility.class,
					"SELECT article.id_article, article.nom, article.etat, article.marque, (select sum(lot.quantite_achete  ) from lot \n" +
							"where lot.id_article = article.id_article) AS quantity\n" +
							"FROM article\n" +
							"INNER JOIN ustensile ON ustensile.id_article = article.id_article\n" +
							"HAVING quantity <> 0\n" +
							"ORDER BY article.nom;");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void loadViews() {
		ObservableList<Ingredient> ingredientsObs = FXCollections.observableArrayList();
		if (this.ingredients != null) {
			for (Ingredient ingredient : this.ingredients) {
				ingredientsObs.add(ingredient);
			}
		}
		this.ingredientsListView.setItems(ingredientsObs);

		ObservableList<Utility> utilitiesObs = FXCollections.observableArrayList();
		if (this.utilities != null) {
			for (Utility utility : this.utilities) {
				utilitiesObs.add(utility);
			}
		}
		this.utilitiesListView.setItems(utilitiesObs);
	}
	private void loadEvents() {
		this.logOut.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// System.exit(0);
				ViewsManager.showLoginScreen();
			}
		});
		this.changePasswordButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showEditPasswordView();
			}
		});
		this.editIngredientLotsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Ingredient selectedItem = ListViewController.this.ingredientsListView.getSelectionModel()
						.getSelectedItem();
				if (selectedItem != null) {
					ViewsManager.showEditLotsView(selectedItem);
				}
			}
		});
		this.editUtilityLotsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Utility selectedItem = ListViewController.this.utilitiesListView.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					ViewsManager.showEditLotsView(selectedItem);
				}
			}
		});
		this.showSuppliersListViewButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showSupplierListScreen();
			}
		});
		this.showOrdersListViewButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showOrderListView();
			}
		});

	}

}
