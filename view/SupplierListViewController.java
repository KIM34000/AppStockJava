/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;


import app.stock.controller.ViewsManager;

import app.stock.model.core.Model;
import app.stock.model.supplier.Supplier;
import java.sql.SQLException;
import java.util.List;
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
public class SupplierListViewController {
    @FXML
    private ListView<Supplier> supplierListView;
    @FXML
    private Button addSupplierbutton;
    @FXML
    private Button quitButton;
    @FXML
    private Button deleteSupplierbutton;
    
    private List<Supplier> suppliers;
    
    public SupplierListViewController() {
		loadModels();
	}
    public void initialize() {
		loadViews();
                loadEvents();
	}
    @SuppressWarnings("unchecked")
    private void loadModels(){
    try {
            this.suppliers = (List<Supplier>)Model.loadList(Supplier.class, 
                    "SELECT fournisseur.id_fournisseur, fournisseur.adresse,fournisseur.nom, \n" +
                    "fournisseur.id_contact, contact.nom contact_nom, contact.prenom contact_prenom, contact.telephone contact_telephone \n" +
                    "FROM fournisseur\n" +
                    "INNER JOIN contact ON contact.id_contact = fournisseur.id_contact"
                    );
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    private void loadViews() {
		ObservableList<Supplier> suppliersObs = FXCollections.observableArrayList();
		if (this.suppliers != null) {
			for (Supplier supplier : this.suppliers) {
				suppliersObs.add(supplier);
			}
		}
		this.supplierListView.setItems(suppliersObs);
		
	}
    private void loadEvents() {
		this.quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showListView();
			}
		});		
		this.addSupplierbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// System.exit(0);
				ViewsManager.showAddsupplierScreen();
			}
		});
                this.deleteSupplierbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Supplier supplier = SupplierListViewController.this.supplierListView.getSelectionModel()
						.getSelectedItem();
				if (supplier != null) {
					try {
						supplier.delete();
						SupplierListViewController.this.supplierListView.getItems().remove(supplier);
						SupplierListViewController.this.supplierListView.refresh();
					} catch (SQLException ex) {
						System.err.println(ex);
					}
				}
			}
		});
	}   

  
}
