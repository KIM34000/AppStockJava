/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import app.stock.controller.ViewsManager;
import app.stock.model.core.Model;
import app.stock.model.order.Order;
import app.stock.model.order.OrderLine;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

/**
 *
 * @author lina7
 */
public class EditOrderViewController {
	private Order order;
	private List<OrderLine> orderLines;

	@FXML
	private TextField stateOrder;
	@FXML
	private TextField origineOrder;

	@FXML
	private TableView<OrderLine> orderLinesTableView;
	@FXML
	private Button addOrderLineButton;
	@FXML
	private Button deleteOrderLineButton;

	@FXML
	private Button deleteButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button quitButton;

	public EditOrderViewController() {

	}

	// la méthode initialize() est utilisée par le moteur FX pour déclencher
	// l'initialisation de la vue
	public void initialize() {
		if (this.order != null) {
			loadModels();
		} else {
			this.orderLines = new ArrayList<>();
		}
		loadViews();
		loadEvents();
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@SuppressWarnings("unchecked")
	// dans la méthode initialize(), on retrouve donc loadModels() qui va
	// charger les données nécessaires à la vue à partir de la base de données.
	private void loadModels() {
		try {
			this.orderLines = (List<OrderLine>) Model.loadList(OrderLine.class,
					"SELECT ligne_commande.id_commande, ligne_commande.id_article, ligne_commande.quantity "
							+ "FROM ligne_commande WHERE ligne_commande.id_commande = ? ORDER BY ligne_commande.id_article;",
					order.getIdOrder());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// une fois les données chargées par loadModels(), on initialise les
	// éléments de la vue avec les données dans loadViews()
	private void loadViews() {
		if (this.order != null) {
			this.stateOrder.setText(order.getState());
			this.origineOrder.setText(order.getOrigin());
		}

		ObservableList<OrderLine> orderLinesObs = FXCollections.observableArrayList();
		if (this.orderLines != null) {
			for (OrderLine orderLine : this.orderLines) {
				orderLinesObs.add(orderLine);
			}
		}
		this.orderLinesTableView.setItems(orderLinesObs);

		this.orderLinesTableView.setEditable(true);

		if (this.orderLinesTableView.getColumns().isEmpty()) {
			Callback<TableColumn<OrderLine, String>, TableCell<OrderLine, String>> defaultTextFieldCellFactory = TextFieldTableCell
					.forTableColumn();

			TableColumn<OrderLine, String> colArticle = new TableColumn<>("Article");
			TableColumn<OrderLine, String> colQuantity = new TableColumn<>("Quantity");

			colArticle.setCellFactory(col -> {
				TableCell<OrderLine, String> cell = defaultTextFieldCellFactory.call(col);
				cell.itemProperty().addListener((obs, oldValue, newValue) -> {
					TableRow<?> row = cell.getTableRow();
					if (row == null) {
						cell.setEditable(false);
					} else {
						OrderLine item = (OrderLine) cell.getTableRow().getItem();
						if (item == null) {
							cell.setEditable(false);
						} else {
							cell.setEditable(true);
							item.setIdArticle(newValue == null ? null : Integer.valueOf(newValue));
						}
					}
					cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), cell.isEditable());
				});
				return cell;
			});

			colQuantity.setCellFactory(col -> {
				TableCell<OrderLine, String> cell = defaultTextFieldCellFactory.call(col);
				cell.itemProperty().addListener((obs, oldValue, newValue) -> {
					TableRow<?> row = cell.getTableRow();
					if (row == null) {
						cell.setEditable(false);
					} else {
						OrderLine item = (OrderLine) cell.getTableRow().getItem();
						if (item == null) {
							cell.setEditable(false);
						} else {
							cell.setEditable(true);
							item.setQuantity(newValue == null ? null : Double.valueOf(newValue));
						}
					}
					cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), cell.isEditable());
				});
				return cell;
			});

			colArticle.setCellValueFactory(item -> new SimpleStringProperty(
					item.getValue().getIdArticle() == 0 ? null : String.valueOf(item.getValue().getIdArticle())));
			colQuantity.setCellValueFactory(item -> new SimpleStringProperty(
					item.getValue().getQuantity() == 0 ? null : String.valueOf(item.getValue().getQuantity())));
			this.orderLinesTableView.getColumns().add(colArticle);
			this.orderLinesTableView.getColumns().add(colQuantity);
		}
	}

	// une fois les éléments de la vue initialisés, on branche toutes les
	// actions que l'utilisateur peut faire dans loadEvents()
	private void loadEvents() {
		this.addOrderLineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderLine orderLine = new OrderLine();
				if (EditOrderViewController.this.order != null) {
					orderLine.setIdCommande(EditOrderViewController.this.order.getIdOrder());
				}
				orderLine.setQuantity(1);

				EditOrderViewController.this.orderLines.add(orderLine);
				EditOrderViewController.this.orderLinesTableView.getItems().add(orderLine);
				EditOrderViewController.this.orderLinesTableView.refresh();
			}
		});
		this.deleteOrderLineButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				OrderLine orderLine = EditOrderViewController.this.orderLinesTableView.getSelectionModel()
						.getSelectedItem();
				if (orderLine != null) {
					EditOrderViewController.this.orderLinesTableView.getItems().remove(orderLine);
					EditOrderViewController.this.orderLinesTableView.refresh();
				}
			}
		});
		this.deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					if (EditOrderViewController.this.order != null) {
						for (OrderLine line : EditOrderViewController.this.orderLines) {
							line.delete();
						}
						EditOrderViewController.this.order.delete();
						EditOrderViewController.this.order = null;
						ViewsManager.showOrderListView();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		this.saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Order ord = EditOrderViewController.this.order;
					if (ord == null) {
						ord = new Order();
					}

					ord.setState(EditOrderViewController.this.stateOrder.getText());
					ord.setOrigin(EditOrderViewController.this.origineOrder.getText());
					if (ord.getIdOrder() == 0) {
						ord.setCreationDate(LocalDate.now().atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
					}
					ord.save();

					for (OrderLine orderLine : EditOrderViewController.this.orderLines) {
						if (EditOrderViewController.this.orderLinesTableView.getItems().contains(orderLine)) {
							if (orderLine.getIdCommande() == 0) {
								orderLine.setIdCommande(ord.getIdOrder());
							}
							orderLine.save();
						} else {
							orderLine.delete();
						}
					}
					ViewsManager.showOrderListView();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		this.quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showOrderListView();
			}
		});
	}
}
