/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import app.stock.controller.ViewsManager;
import app.stock.model.article.Article;
import app.stock.model.article.Lot;
import app.stock.model.core.Model;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;


/**
 *
 * @author lina7
 */
public class EditLotsViewController {
    
        private Article article;
	private List<Lot> lots;

	@FXML
	private TableView<Lot> lotsTableView;
	@FXML
	private Button addButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button quitButton;
     
//    private Article article;   
//    public void setArticle(Article article) {
//    this.article = article;
//}

    public EditLotsViewController() {
	
	}
    //la méthode initialize() est utilisée par le moteur FX pour déclencher l'initialisation de la vue
    public void initialize() {
		if (this.article != null) {
			loadModels();
			loadViews();
			loadEvents();
		}
	}
    public void setArticle(Article article) {
		this.article = article;
	}
    @SuppressWarnings("unchecked")
    //dans la méthode initialize(), on retrouve donc loadModels() qui va charger les données nécessaires à la vue à partir de la base de données.
	private void loadModels() {
		try {
			this.lots = (List<Lot>) Model.loadList(Lot.class,
					"SELECT lot.id_article, lot.ref_commande, lot.quantite_achete, lot.cout_unitaire, lot.date_achat "
							+ "FROM lot WHERE lot.id_article = ? ORDER BY lot.date_achat;",
					article.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    //une fois les données chargées par loadModels(), on initialise les éléments de la vue avec les données dans loadViews()
    private void loadViews() {
		ObservableList<Lot> lotsObs = FXCollections.observableArrayList();
		if (this.lots != null) {
			for (Lot lot : this.lots) {
				lotsObs.add(lot);
			}
		}
		this.lotsTableView.setItems(lotsObs);

		this.lotsTableView.setEditable(true);

		Callback<TableColumn<Lot, String>, TableCell<Lot, String>> defaultTextFieldCellFactory = TextFieldTableCell
				.forTableColumn();

		TableColumn<Lot, String> colOrder = new TableColumn<>("Order");
		TableColumn<Lot, String> colQuantity = new TableColumn<>("Quantity");
		TableColumn<Lot, String> colUnitCost = new TableColumn<>("Unit cost");
		TableColumn<Lot, String> colBuyDate = new TableColumn<>("Buy date");

		colOrder.setCellFactory(col -> {
			TableCell<Lot, String> cell = defaultTextFieldCellFactory.call(col);
			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				TableRow<?> row = cell.getTableRow();
				if (row == null) {
					cell.setEditable(false);
				} else {
					Lot item = (Lot) cell.getTableRow().getItem();
					if (item == null) {
						cell.setEditable(false);
					} else {
						cell.setEditable(true);
						item.setRefOrder(newValue == null ? null : Integer.valueOf(newValue));
					}
				}
				cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), cell.isEditable());
			});
			return cell;
		});

		colQuantity.setCellFactory(col -> {
			TableCell<Lot, String> cell = defaultTextFieldCellFactory.call(col);
			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				TableRow<?> row = cell.getTableRow();
				if (row == null) {
					cell.setEditable(false);
				} else {
					Lot item = (Lot) cell.getTableRow().getItem();
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

		colUnitCost.setCellFactory(col -> {
			TableCell<Lot, String> cell = defaultTextFieldCellFactory.call(col);
			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				TableRow<?> row = cell.getTableRow();
				if (row == null) {
					cell.setEditable(false);
				} else {
					Lot item = (Lot) cell.getTableRow().getItem();
					if (item == null) {
						cell.setEditable(false);
					} else {
						cell.setEditable(true);
						item.setUnitCost(newValue == null ? null : Double.valueOf(newValue));
					}
				}
				cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), cell.isEditable());
			});
			return cell;
		});

		colBuyDate.setCellFactory(col -> {
			TableCell<Lot, String> cell = defaultTextFieldCellFactory.call(col);
			cell.itemProperty().addListener((obs, oldValue, newValue) -> {
				TableRow<?> row = cell.getTableRow();
				if (row == null) {
					cell.setEditable(false);
				} else {
					Lot item = (Lot) cell.getTableRow().getItem();
					if (item == null) {
						cell.setEditable(false);
					} else {
						cell.setEditable(true);
						if (newValue != null) {
							ZonedDateTime datetime = LocalDate.parse(newValue).atStartOfDay().atZone(ZoneOffset.UTC);
							item.setBuyDate(datetime.toInstant());
						} else {
							item.setBuyDate(null);
						}
					}
				}
				cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), cell.isEditable());
			});
			return cell;
		});

		colOrder.setCellValueFactory(item -> new SimpleStringProperty(
				item.getValue().getRefOrder() == 0 ? null : String.valueOf(item.getValue().getRefOrder())));
		colQuantity.setCellValueFactory(item -> new SimpleStringProperty(
				item.getValue().getQuantity() == 0 ? null : String.valueOf(item.getValue().getQuantity())));
		colUnitCost.setCellValueFactory(item -> new SimpleStringProperty(
				item.getValue().getUnitCost() == 0 ? null : String.valueOf(item.getValue().getUnitCost())));
		colBuyDate.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getBuyDate() == null ? null
				: String.valueOf(item.getValue().getBuyDate().atZone(ZoneId.systemDefault()).toLocalDate())));
		this.lotsTableView.getColumns().add(colOrder);
		this.lotsTableView.getColumns().add(colQuantity);
		this.lotsTableView.getColumns().add(colUnitCost);
		this.lotsTableView.getColumns().add(colBuyDate);
	}
 
    //une fois les éléments de la vue initialisés, on branche toutes les actions que l'utilisateur peut faire dans loadEvents()
    private void loadEvents() {
		this.addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Lot lot = new Lot();
				lot.setIdArticle(EditLotsViewController.this.article.getId());

				ObservableList<Lot> items = EditLotsViewController.this.lotsTableView.getItems();
				if (items.size() != 0) {
					Lot last = items.get(items.size() - 1);
					lot.setRefOrder(last.getRefOrder() + 1);
					lot.setQuantity(last.getQuantity());
					lot.setUnitCost(last.getUnitCost());
				} else {
					lot.setQuantity(1);
				}
				lot.setBuyDate(LocalDate.now().atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
				EditLotsViewController.this.lots.add(lot);
				EditLotsViewController.this.lotsTableView.getItems().add(lot);
				EditLotsViewController.this.lotsTableView.refresh();
			}
		});
		this.deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Lot lot = EditLotsViewController.this.lotsTableView.getSelectionModel().getSelectedItem();
				if (lot != null) {
					EditLotsViewController.this.lotsTableView.getItems().remove(lot);
					EditLotsViewController.this.lotsTableView.refresh();
				}
			}
		});
		this.saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					for (Lot lot : EditLotsViewController.this.lots) {
						if (EditLotsViewController.this.lotsTableView.getItems().contains(lot)) {
							lot.save();
						} else {
							lot.delete();
						}
					}
					ViewsManager.showListView();
				} catch (SQLException e) {
					e.printStackTrace();
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
