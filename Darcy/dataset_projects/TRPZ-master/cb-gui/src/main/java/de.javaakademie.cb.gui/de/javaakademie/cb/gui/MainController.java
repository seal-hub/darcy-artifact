package de.javaakademie.cb.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

import de.javaakademie.cb.api.ConferenceService;
import de.javaakademie.cb.api.model.Session;
import de.javaakademie.cb.api.model.Speaker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 * MainController.
 * 
 * @author Guido.Oelmann
 */
public class MainController implements Initializable {

	@FXML
	private ToolBar toolBar;

	@FXML
	private ListView<Object> listView;

	@FXML
	private VBox details;

	private Button speakerButton;
	private Button sessionsButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button addButton;

	private ServiceFactory serviceFactory;
	private ConferenceService<Speaker> speakerService;
	private ConferenceService<Session> sessionService;

	public void initialize(URL location, ResourceBundle resources) {
		serviceFactory = new ServiceFactory();
		try {
			speakerService = serviceFactory.getSpeakerService();
			sessionService = serviceFactory.getSessionService();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		initTestData();
		initToolbar();
		initActionButtons();
		initListView();
	}

	@FXML
	protected void handleDeleteButtonAction(ActionEvent event) {
		Object entity = listView.getSelectionModel().getSelectedItem();
		if (entity == null) {
			System.out.println("Nothing selected");
		} else if (entity instanceof Speaker) {
			Speaker sp = (Speaker) entity;
			speakerService.remove(sp);
			speakerButton.fire();
			System.out.println("Delete " + sp);
		} else {
			Session se = (Session) entity;
			sessionService.remove(se);
			sessionsButton.fire();
			System.out.println("Delete " + se);
		}
	}

	@FXML
	protected void handleAddButtonAction(ActionEvent event) {
		System.out.println("Add new Item");
	}

	private void clearListView() {
		if (listView.getItems() != null) {
			listView.getItems().removeAll(listView.getSelectionModel().getSelectedItems());
			listView.getItems().clear();
			listView.getItems().removeAll();
			listView.setItems(null);
			listView.getSelectionModel().clearSelection();
			listView.getSelectionModel().clearSelection();
			listView.refresh();
		}
	}

	private void initToolbar() {
		ImageView speakerImage = ImageHelper
				.getImage(MainController.class.getResourceAsStream("/resources/images/speaker.png"));
		ImageView sessionsImage = ImageHelper
				.getImage(MainController.class.getResourceAsStream("/resources/images/sessions.png"));

		speakerButton = new Button("Speaker", speakerImage);
		speakerButton.getStyleClass().add("toolbarButton");
		speakerButton.setContentDisplay(ContentDisplay.TOP);
		// toolBar.getItems().add(speakerButton);
		speakerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearListView();
				Collection<Speaker> speakerList = speakerService.getAll();
				ObservableList<Object> observableSpeakerList = FXCollections
						.observableList(new ArrayList<>(speakerList));
				listView.setItems(observableSpeakerList);
				listView.refresh();
				details.getChildren().clear();
			}
		});
		speakerButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				speakerButton.setContentDisplay(ContentDisplay.TOP);
			}
		});

		sessionsButton = new Button("Sessions", sessionsImage);
		sessionsButton.getStyleClass().add("toolbarButton");
		sessionsButton.setContentDisplay(ContentDisplay.TOP);

		VBox menuButtons = new VBox(20);
		menuButtons.getChildren().addAll(speakerButton, sessionsButton);
		toolBar.getItems().add(menuButtons);

		sessionsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearListView();
				Collection<Session> sessionList = sessionService.getAll();
				ObservableList<Object> observableSessionsList = FXCollections
						.observableList(new ArrayList<>(sessionList));
				listView.setItems(observableSessionsList);
				details.getChildren().clear();
			}
		});
		sessionsButton.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				sessionsButton.setContentDisplay(ContentDisplay.TOP);
			}
		});
	}

	private void initActionButtons() {
		ImageView addImage = ImageHelper
				.getImage(MainController.class.getResourceAsStream("/resources/images/add.png"));
		addImage.setFitHeight(20);
		addImage.setPreserveRatio(true);
		addButton.setGraphic(addImage);

		ImageView removeImage = ImageHelper
				.getImage(MainController.class.getResourceAsStream("/resources/images/remove.png"));
		removeImage.setFitHeight(20);
		removeImage.setPreserveRatio(true);
		deleteButton.setGraphic(removeImage);
	}

	private void initListView() {
		listView.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Object entity = listView.getSelectionModel().getSelectedItem();
				if (entity instanceof Speaker) {
					Speaker sp = (Speaker) entity;
					details.getChildren().clear();

					ImageView speakerImage = ImageHelper
							.getImage(MainController.class.getResourceAsStream(sp.getPicture()));
					Label pictureLabel = new Label("", speakerImage);

					Label academicLabel = new Label(sp.getAcademicDegreeTitle());
					academicLabel.setFont(Font.font("Verdana", 14));
					academicLabel.setTextAlignment(TextAlignment.CENTER);
					academicLabel.setAlignment(Pos.CENTER);

					Label biography = new Label(sp.getBiography());
					biography.setWrapText(true);
					biography.setPadding(new Insets(25));

					details.setPadding(new Insets(25));
					details.setAlignment(Pos.TOP_CENTER);
					details.getChildren().addAll(pictureLabel, academicLabel,
							new Label("(" + sp.getOrganization() + ")"), biography);
				} else {
					// Session se = (Session) entity;
				}
				System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
				// details
			}
		});

		listView.setCellFactory(new Callback<ListView<Object>, ListCell<Object>>() {
			@Override
			public ListCell<Object> call(ListView<Object> p) {
				ListCell<Object> cell = new ListCell<Object>() {
					@Override
					protected void updateItem(Object t, boolean bln) {
						super.updateItem(t, bln);
						setText(null);
						setGraphic(null);
						if (t != null) {
							if (t instanceof Speaker) {
								Speaker sp = (Speaker) t;
								setText(sp.getFirstName() + " " + sp.getLastName());
							} else {
								Session se = (Session) t;
								setText(se.getTitle());
							}
						}
					}
				};
				return cell;
			}
		});
		listView.getSelectionModel().clearSelection();
	}

	private void initTestData() {
		// create speaker testdata
		Speaker speaker1 = new Speaker("Guido", "Oelmann", "Diplom-Informatiker", "deutsch", "Freelancer",
				"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et",
				"/resources/images/guido.png");
		Speaker speaker2 = new Speaker("Darth", "Vader", "NASA flight engineer", "englisch", "Death Star AG",
				"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et",
				"/resources/images/darth.png");
		speakerService.persist(speaker2);
		speakerService.persist(speaker1);

		// create sessions testdata
		Session session1 = new Session("Die Welt ist modular", "deutsch", "Core Java", "track", "summary",
				Arrays.asList(speaker1));
		Session session2 = new Session("Die Welt der Mustermnner", "englisch", "Core Java", "track", "summary",
				Arrays.asList(speaker2));
		sessionService.persist(session2);
		sessionService.persist(session1);
	}

}