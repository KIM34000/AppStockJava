/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

import app.stock.controller.ViewsManager;
import app.stock.model.core.Model;
import app.stock.model.order.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author lina7
 */
public class ListViewOrderController {
	private List<Order> orders;

	@FXML
	private TableView<Order> ordersTableView;
	@FXML
	private Button addButton;
	@FXML
	private Button editButton;
	@FXML
	private Button receiveButton;
	@FXML
	private Button quitButton;

// appel de methode :
//        une instruction finie toujours par ';'
//        que ce soit un appel de methode, une déclaration de variable, ou une affectation
//        les arguments d'une méthode sont tous séparés par des ',' et sont toujours entre '(' et ')'
	public void initialize() {
		loadModels();
		loadViews();
		loadEvents();
	}

	@SuppressWarnings("unchecked")
	private void loadModels() {
		try {
			this.orders = (List<Order>) Model.loadList(Order.class,
					"SELECT commande.id_commande, commande.etat, commande.date_creation, commande.provenance "
							+ "FROM commande ORDER BY commande.date_creation;");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// load, c'est pour charger tout ce qu'il faut pour la^^ vue
	private void loadViews() {
		ObservableList<Order> ordersObs = FXCollections.observableArrayList();
		if (this.orders != null) {
			for (Order order : this.orders) {
				ordersObs.add(order);
			}
		}
		this.ordersTableView.setItems(ordersObs);

		TableColumn<Order, String> colOrder = new TableColumn<>("Order");
		TableColumn<Order, String> colState = new TableColumn<>("State");
		TableColumn<Order, String> colDate = new TableColumn<>("Date");
		TableColumn<Order, String> colProvenance = new TableColumn<>("Provenance");

		colOrder.setCellValueFactory(item -> new SimpleStringProperty(
				item.getValue().getIdOrder() == 0 ? null : String.valueOf(item.getValue().getIdOrder())));
		colState.setCellValueFactory(item -> new SimpleStringProperty(
				item.getValue().getState() == null ? null : String.valueOf(item.getValue().getState())));
		colDate.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getCreationDate() == null ? null
				: String.valueOf(item.getValue().getCreationDate().atZone(ZoneId.systemDefault()).toLocalDate())));
		colProvenance.setCellValueFactory(item -> new SimpleStringProperty(
				item.getValue().getOrigin() == null ? null : String.valueOf(item.getValue().getOrigin())));
		this.ordersTableView.getColumns().add(colOrder);
		this.ordersTableView.getColumns().add(colState);
		this.ordersTableView.getColumns().add(colDate);
		this.ordersTableView.getColumns().add(colProvenance);
	}

	private void loadEvents() {
		this.addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showEditOrderView(null);
			}
		});
		this.editButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Order order = ListViewOrderController.this.ordersTableView.getSelectionModel().getSelectedItem();
				if (order != null) {
					ViewsManager.showEditOrderView(order);
				}
			}
		});

		this.receiveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Order order = ListViewOrderController.this.ordersTableView.getSelectionModel().getSelectedItem();
				if (order != null) {
					try {
						order.setState("received");
						order.save();
						ListViewOrderController.this.ordersTableView.refresh();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		this.quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showListView();
			}
		});
	}
}