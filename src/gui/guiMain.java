package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import worldWindow.WorldView;

public class GuiMain extends Application {

	private static Stage window;
	private String windowTitle = "SandBox Environment Mark1";
	
	//private String objectFilePath = "";
	
  public static void main(String[] args) {
		// Start GUI
	    launch(args);
	    }
  
  public void start(@SuppressWarnings("exports") Stage primaryStage) {
		window = primaryStage;
		window.setOnCloseRequest(e -> {
			e.consume();
			window.close();
		});
	    VBox verticalLayout = new VBox(2);
	    Scene scene = new Scene(verticalLayout);
	    
		// Create worldView
		WorldView worldView = new WorldView(FilePaths.Model3DFilePath, scene);
		try {
			worldView.start(window);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Creating WorldView failed.");
		}
		
		// Set layout			
	    VBox centerLayout = new VBox(2);
	    // Add 3D window
	    centerLayout.getChildren().add(worldView.getAnchorPane());
	    // Add lower bar 
	    Pane consoleBar = new Pane();
	    centerLayout.getChildren().add(consoleBar);
	    			
	    SplitPane horizontalLayout = new SplitPane();
	    
	    // Add Sidebar
	    ControlPanel controlPanel = new ControlPanel(worldView);
	    
	    horizontalLayout.getItems().add(controlPanel.getControlPanel());
	    horizontalLayout.getItems().add(centerLayout);
	    
	    controlPanel.getControlPanel().widthProperty().addListener(e -> {
	    	double newSceneWidth =window.getWidth() - controlPanel.getControlPanel().getWidth();	
	    	worldView.setSceneWidth(newSceneWidth);
	    });
	    
	    
	    //VBox verticalLayout = new VBox(2);
	    verticalLayout.getChildren().add(menuBar.create());
	    verticalLayout.getChildren().add(horizontalLayout);
	    
	    window.widthProperty().addListener((obs, oldVal, newVal) -> {
	    	double newSceneWidth =window.getWidth() - controlPanel.getControlPanel().getWidth();	
	    	worldView.setSceneWidth(newSceneWidth);
	   });

	   window.heightProperty().addListener((obs, oldVal, newVal) -> {
		   double newSceneHeight = window.getHeight() * 0.85;
		   worldView.setSceneHeight(newSceneHeight);
	   });				    
	    // Set Scene
	    window.setScene(scene);
	    
	    scene.getStylesheets().add(
	            GuiMain.class.getResource("DarkStyle.css").toExternalForm());
	    
	    window.setTitle(windowTitle);

	    window.show();
	    window.setMaximized(true);
  }
}
