package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import worldWindow.WorldView;

public class GuiMain extends Application {

	private static Stage window;
	private String windowTitle = "SandBox Environment Mark1";
	
	private String objectFilePath = "";
	
	  public static void GuiMain(String[] args) {
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
			
			
			System.out.println(FilePaths.Model3DFilePath);
			WorldView worldView = new WorldView(FilePaths.Model3DFilePath);
			
			// Set layout
			
		    VBox centerLayout = new VBox(2);
		    // Add 3D window
		    centerLayout.getChildren().add(worldView.start());
		    // Add lower bar 
			
		    HBox horizontalLayout = new HBox(2);
		    // Add Sidebar
		    horizontalLayout.getChildren().add(centerLayout);
		    
		    
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
