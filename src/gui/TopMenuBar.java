package gui;

import java.io.File;

import animate.AnimationFile;
import interfaces.LoadFromFile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import worldWindow.WorldView;

public class TopMenuBar {
	
	private final  FileChooser objectFileChooser = new FileChooser();
	private final  FileChooser animationFileChooser = new FileChooser();
	private LoadFromFile fileLoader = new LoadFromFile();
	private  WorldView worldView;
	private AnimationPanel animationPanel;
	
	private String FILE_DELIMITER = "/";
	
	public TopMenuBar(WorldView view, AnimationPanel animationPanel) {
	    worldView = view;
	    this.animationPanel=animationPanel;
	}
    
	@SuppressWarnings("exports")
	public  MenuBar create() {
	    MenuBar menuBar = new MenuBar(); 
	    
	    Menu  menu = new Menu("Menu"); 
	    Menu file = new Menu("File"); 
	    Menu settings = new Menu("Settings"); 
	    Menu project = new Menu("Project"); 
	    Menu environment = new Menu("Environment");
	    Menu model = new Menu("Model");
	    
	    // add menu to menubar 
	    menuBar.getMenus().add(menu); 
	    menuBar.getMenus().add(file); 
	    menuBar.getMenus().add(settings); 
	    menuBar.getMenus().add(project); 
	    menuBar.getMenus().add(environment);
	    menuBar.getMenus().add(model);
	    
	    addMenuContent(menu);
	    addModelContent(model) ;
	    addProjectContent(project);
	    
	    return menuBar;
	}
	
	private void addMenuContent(Menu menu) {
		MenuItem openObjFile = new MenuItem("Quit SandBox");
		openObjFile.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
		
		menu.getItems().add(openObjFile);
	}
	
	private void addModelContent(Menu menu) {
		
		MenuItem openObjFile = new MenuItem("Load .obj model");
		openObjFile.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage stage = new Stage();
                File file = objectFileChooser.showOpenDialog(stage);
                System.out.println(file.getAbsolutePath());
                if (file != null) {
                	worldView.setModelObjectPath(file.getAbsolutePath());
                	worldView.removeModel();
                	worldView.addModel();
                }
		    }
		});
		
		menu.getItems().add(openObjFile);
	}
	
	private void addProjectContent(Menu menu) {
		MenuItem openAnimationFile = new MenuItem("Create Animation from .csv");
		openAnimationFile.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage stage = new Stage();
                File file = animationFileChooser.showOpenDialog(stage);
                if (file != null) {
                	try {
	                	AnimationFile animationFile = fileLoader.loadCsvData(file, worldView.getTrajectoryScaleFactor()) ;
	                	worldView.setAnimationFile(animationFile);
	                	worldView.getAnimation().setAnimationFile(animationFile);
	                	// Update indicator label
	                	String[] pathElements = file.getPath().split(FILE_DELIMITER);
	                	animationPanel.updateAnimationInfo(animationFile, pathElements[pathElements.length-1]);
	
                	} catch (Exception exp ) {
                		System.out.println(exp);
                		System.out.println("Loading file failed");
                	}
		    }
		    }
		});
		
		menu.getItems().add(openAnimationFile);
	}
}
