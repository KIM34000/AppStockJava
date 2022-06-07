/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.stock.view;

import java.sql.SQLException;

import app.stock.controller.ViewsManager;
import app.stock.model.supplier.Contact;
import app.stock.model.supplier.Supplier;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author lina7
 */
public class AddSupplierViewController {
	@FXML
	private Button okAddSupplierBtn;
	@FXML
	private Button quitButton;
	@FXML
	private TextField nameSupplier;
	@FXML
	private TextField addressSupplier;
	@FXML
	private TextField nameContact;
	@FXML
	private TextField lastNameContact;
	@FXML
	private TextField phoneContact;
	private Supplier supplier;

	public AddSupplierViewController() {

	}

	public void initialize() {
		loadModels();
		loadEvents();
	}

	private void loadModels() {
		this.supplier = new Supplier();
		this.supplier.setContact(new Contact());
	}

	private void loadEvents() {
		this.quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViewsManager.showSupplierListScreen();
			}
		});
		this.okAddSupplierBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Supplier supp = AddSupplierViewController.this.supplier;

					supp.setNom(AddSupplierViewController.this.nameSupplier.getText());
					supp.setAdresse(AddSupplierViewController.this.addressSupplier.getText());

					Contact contact = supp.getContact();
					contact.setNom(AddSupplierViewController.this.nameContact.getText());
					contact.setPrenom(AddSupplierViewController.this.lastNameContact.getText());
					contact.setTelephone(AddSupplierViewController.this.phoneContact.getText());

					supp.save();
					ViewsManager.showSupplierListScreen();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
