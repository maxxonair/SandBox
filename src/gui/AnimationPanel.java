package gui;

import java.io.File;

import animate.Animation;
import animate.AnimationFile;
import interfaces.LoadFromFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Formats;
import worldWindow.WorldView;

public class AnimationPanel {

	private AnchorPane contentPanel;
	@SuppressWarnings("unused")
	private WorldView worldView;
	
	private LoadFromFile fileLoader = new LoadFromFile();
	
	private String FILE_DELIMITER = "/";
	
	public AnimationPanel(WorldView worldView) {
		
		this.worldView=worldView;
		contentPanel = new AnchorPane();
		
		Label unitLabel = new Label("Animation:");
		
		Button start = new Button("start");
		Button pause = new Button("pause");
		Button stop  = new Button("stop");
		
		Button thirdPersonView = new Button("TPV");
		
		Label loadedFileLabel = new Label("");
		Label durationLabel   = new Label("");
		Label frequencyLabel  = new Label("");
		
		Label trajectorySizeLabel = new Label("Set Trajectory Size: "+worldView.getTrajectoryRadius()+" [clicks]");
		
	    Slider trajectorySize = new Slider();
	    trajectorySize.setMin(1);
	    trajectorySize.setMax(100);
	    trajectorySize.setValue(worldView.getTrajectoryRadius());
	    trajectorySize.setMajorTickUnit(5);
	    trajectorySize.setShowTickMarks(true);
	    trajectorySize.valueProperty().addListener(e -> {
	    		worldView.deleteTrajectory();
	    		worldView.setTrajectoryRadius((int) trajectorySize.getValue());
	    		worldView.createTrajectory();
	    		
	    		trajectorySizeLabel.setText("Set Trajectory Size: "+worldView.getTrajectoryRadius()+" [clicks]");
	    });	
	    
	    CheckBox showTrajectory = new CheckBox();
	    showTrajectory.setSelected(true);
	    showTrajectory.setText("Show Trajectory");
	    showTrajectory.selectedProperty().addListener(
    	      e -> {
    	         if ( showTrajectory.isSelected() ) {
    	        	worldView.deleteTrajectory();
    	        	worldView.createTrajectory();
    	         } else {
    	        	 worldView.deleteTrajectory();
    	         }
    	      });
	    
	    CheckBox showHud = new CheckBox();
	    showHud.setSelected(true);
	    showHud.setText("Show HUD elements");
	    showHud.selectedProperty().addListener(
    	      e -> {
    	         if ( showHud.isSelected() ) {
    	        	worldView.showHudElements();
    	         } else {
    	        	worldView.hideHudElements();
    	         }
    	      });
		
		VBox statisticsPanel = new VBox(2);
		statisticsPanel.getChildren().add(loadedFileLabel);
		statisticsPanel.getChildren().add(durationLabel);
		statisticsPanel.getChildren().add(frequencyLabel);
		statisticsPanel.getChildren().add(thirdPersonView);
		
		final FileChooser fileChooser = new FileChooser();
		Button openFile = new Button("Open");
		
		VBox animationPanel = new VBox(2);
		
		HBox buttons = new HBox(4);
		buttons.getChildren().add(start);
		buttons.getChildren().add(pause);
		buttons.getChildren().add(stop);
		buttons.getChildren().add(openFile);
		
		Animation animation = new Animation(worldView);
		animation.setTestAnimationFile();
		
		start.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	animation.start();
		    }
		});
		
		stop.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	animation.stop();
		    }
		});
		
		thirdPersonView.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if ( worldView.isThirdPersonCamera() ) {
		    		worldView.setThirdPersonCamera(false);
		    		worldView.setCameraToAbsoluteDefaultPosition();

		    		String bstyle=String.format("-fx-text-fill: #e8e8e8");
		    		thirdPersonView.setStyle(bstyle);
		    	} else {
		    		worldView.setThirdPersonCamera(true);
		    		worldView.setCameraToRelativeDefaultPostion();
		    		
		    		String bstyle=String.format("-fx-text-fill: #2a7db0");
		    		thirdPersonView.setStyle(bstyle);
		    	}		    	
		    }
		});
		
		openFile.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage stage = new Stage();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                	try {
	                	AnimationFile animationFile = fileLoader.loadCsvData(file, 0.1) ;
	                	
	                	worldView.setAnimationFile(animationFile);
	                	
	                	animation.setAnimationFile(animationFile);
	                	// Update indicator label
	                	String[] pathElements = file.getPath().split(FILE_DELIMITER);
	                	loadedFileLabel.setText("Loaded Animation File:  "+pathElements[pathElements.length-1]);
	                	durationLabel.setText("Duration:  "+Formats.decform01.format(animationFile.getMaxTime())+" [s]");
	                	frequencyLabel.setText("Source Frequency:  "+Formats.decform01.format(animationFile.getFrequency())+" [Hz]");
	                	
                	} catch (Exception exp ) {
                		System.out.println(exp);
                		System.out.println("Loading file failed");
                	}
                	
                }
		    }
		    	
		    });
		
		animationPanel.getChildren().add(unitLabel);
		animationPanel.getChildren().add(buttons);
		animationPanel.getChildren().add(statisticsPanel);
		animationPanel.getChildren().add(trajectorySizeLabel);
		animationPanel.getChildren().add(trajectorySize);
		animationPanel.getChildren().add(showTrajectory);
		animationPanel.getChildren().add(showHud);
		
		contentPanel.getChildren().add(animationPanel);
	}

	@SuppressWarnings("exports")
	public AnchorPane getContentPanel() {
		return contentPanel;
	}
	
	
}
