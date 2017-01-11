package com.maxeremin.client;

import com.maxeremin.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RestaurantMenuWA implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	int type = 1; //type by default
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Label errorLabel = new Label();
		
		//Open
		final Button openXMLButton = new Button("Open");
		openXMLButton.addStyleName("openXMLButton");
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("openXMLButtonContainer").add(openXMLButton);

		// Focus the cursor on the name field when the app loads
		openXMLButton.setFocus(true);
			
		final DialogBox doneMessage = new DialogBox();
		doneMessage.setText("Done");
		doneMessage.setAnimationEnabled(true);
		final Button closeDoneMessage = new Button("Close");
		final HTML doneMessageLabel = new HTML();
		
		VerticalPanel dialogVPanelDM = new VerticalPanel();
		
		dialogVPanelDM.add(doneMessageLabel);
		dialogVPanelDM.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanelDM.add(closeDoneMessage);
		doneMessage.setWidget(dialogVPanelDM);

		// Add a handler to close the DialogBox
		closeDoneMessage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doneMessage.hide();
				openXMLButton.setEnabled(true);
				openXMLButton.setFocus(true);
			}
		});
		
		class OpenXMLHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
		

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {

				greetingService.readTypes(new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						
						String details = caught.getMessage();
					    if (caught instanceof RuntimeException) {
					      details = "Company '" + ((RuntimeException)caught).getMessage() + "' was delisted";
					    }

					    
						// Show the RPC error message to the user
						doneMessage.setText("Failure");
						//serverResponseLabel.addStyleName("serverResponseLabelError");
						errorLabel.setText(details);//caught.getMessage());
						doneMessageLabel.setHTML(details);
						doneMessage.center();
						closeDoneMessage.setFocus(true);
					}

					public void onSuccess(Void result) {
						
					}
				});
				
				greetingService.readMenu(new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						String details = caught.getMessage();
					    if (caught instanceof RuntimeException) {
					      details = "Company '" + ((RuntimeException)caught).getMessage() + "' was delisted";
					    }

					    
						// Show the RPC error message to the user
						doneMessage.setText("Failure");
						//serverResponseLabel.addStyleName("serverResponseLabelError");
						errorLabel.setText(details);//caught.getMessage());
						doneMessageLabel.setHTML(details);
						doneMessage.center();
						closeDoneMessage.setFocus(true);
					}

					public void onSuccess(Void result) {
						doneMessage.setText("Response");
						doneMessageLabel.setText("Done");
						
						doneMessage.center();
						closeDoneMessage.setFocus(true);
						errorLabel.setText("Done");
					}
				});
			}
		}

		// Add a handler to send the name to the server
		OpenXMLHandler OXMLHandler = new OpenXMLHandler();
		openXMLButton.addClickHandler(OXMLHandler);

		//Add
		final Label addTitleLbl = new Label("Insert name of new item");
		final Label addPriceLbl = new Label("Insert price");
		final Label addTypeLbl = new Label("Choose type");
		final Button addBtn = new Button("Add");
		final TextBox addName = new TextBox();
		final TextBox addPrice = new TextBox();
		final RadioButton firstDishA = new RadioButton("addTypeGroup", "First");
		final RadioButton secondDishA = new RadioButton("addTypeGroup", "Second");
		final RadioButton saladDishA = new RadioButton("addTypeGroup", "Salad");
		final RadioButton sweetDishA = new RadioButton("addTypeGroup", "Sweet");
		firstDishA.setValue(true);
		
		RootPanel.get("addContainer").add(addTitleLbl);
		RootPanel.get("addContainer").add(addName);
		RootPanel.get("addContainer").add(addPriceLbl);
		RootPanel.get("addContainer").add(addPrice);
		RootPanel.get("addContainer").add(addTypeLbl);
		RootPanel.get("addContainer").add(firstDishA);
		RootPanel.get("addContainer").add(secondDishA);
		RootPanel.get("addContainer").add(saladDishA);
		RootPanel.get("addContainer").add(sweetDishA);
		RootPanel.get("addContainer").add(addBtn);
		
		firstDishA.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				type = 1;
			}
		});
		
		secondDishA.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				type = 2;
			}
		});
		
		saladDishA.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				type = 3;
			}
		});
		
		sweetDishA.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				type = 4;
			}
		});
		
		class addHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
		

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				String name = addName.getText();
				
				double price = Double.valueOf(addPrice.getText());
				
				
				greetingService.add(name, type, price, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						errorLabel.setText(caught.getMessage());
					}

					public void onSuccess(Void result) {
						errorLabel.setText("Done");
					}
				});
			}
		}

		addHandler addH = new addHandler();
		addBtn.addClickHandler(addH);
		
		//Find
		
		//Delete
		
		//Update
		
		//Add data from another file
		
		//Save
		
		//Print Menu

	}
}
