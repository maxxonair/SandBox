package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import worldWindow.WorldView;
import worldWindow.worldView;

public class guiMain extends Application {

	private static Stage window;
	private String windowTitle = "SandBox Environment Mark1";
	
	private String objectFilePath = "";
	
	  public static void guiMain(String[] args) {
			// Start GUI
		    launch(args);
		  }
	  
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  public void start(@SuppressWarnings("exports") Stage primaryStage) {
			window = primaryStage;
			window.setOnCloseRequest(e -> {
				e.consume();
				window.close();
			});
			
			WorldView worldView = new WorldView(objectFilePath);
			
			// Set layout
			
		    VBox centerLayout = new VBox(2);
		    // Add 3D window
		    centerLayout.getChildren().add(worldView.start(objectFilePath));
		    // Add lower bar 
			
		    HBox horizontalLayout = new HBox(2);
		    // Add Sidebar
		    // Add centerlayout
		    
		    
		    VBox verticalLayout = new VBox(2);
		    verticalLayout.getChildren().add(menuBar.create());
		    verticalLayout.getChildren().add(horizontalLayout);
		    
		    		
		    
		    // Set Scene
		    Scene scene = new Scene(verticalLayout);

		    window.setScene(scene);

		    window.setMaximized(true);
		    
		    window.setTitle(windowTitle);

		    window.show();
	  }
}
