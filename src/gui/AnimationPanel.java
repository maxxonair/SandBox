package gui;


import animate.AnimationFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Formats;
import worldWindow.WorldView;

public class AnimationPanel {

	private AnchorPane contentPanel;
	@SuppressWarnings("unused")
	private WorldView worldView;
	Label loadedFileLabel ;
	Label durationLabel   ;
	Label frequencyLabel  ;
	
	
	public AnimationPanel(WorldView worldView) {
		
		this.worldView=worldView;
		contentPanel = new AnchorPane();
		
		Label unitLabel = new Label("Animation:");
		
		Button start = new Button("start");
		Button pause = new Button("pause");
		Button stop  = new Button("stop");
		
		Button thirdPersonView = new Button("Third Person View");
		
		 loadedFileLabel = new Label("");
		 durationLabel   = new Label("");
		 frequencyLabel  = new Label("");
		
		Label trajectorySizeLabel = new Label("Set Trajectory Size: "+worldView.getTrajectoryRadius()+" [clicks]");
		
	    Slider trajectorySize = new Slider();
	    CheckBox showTrajectory = new CheckBox();
	    showTrajectory.setSelected(true);
	    
	    trajectorySize.setMin(1);
	    trajectorySize.setMax(100);
	    trajectorySize.setValue(worldView.getTrajectoryRadius());
	    trajectorySize.setMajorTickUnit(5);
	    trajectorySize.setShowTickMarks(true);
	    trajectorySize.valueProperty().addListener(e -> {

	    		worldView.setTrajectoryRadius((int) trajectorySize.getValue());
	    		worldView.setShowTrajectory(showTrajectory.isSelected());
	    		
	    		trajectorySizeLabel.setText("Set Trajectory Size: "+worldView.getTrajectoryRadius()+" [clicks]");
	    });	
	    
	    showTrajectory.setText("Show Trajectory");
	    showTrajectory.selectedProperty().addListener(
    	      e -> {
    	         if ( showTrajectory.isSelected() ) {
    	        	worldView.setShowTrajectory(true);  
    	         } else {
    	        	 worldView.setShowTrajectory(false);
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
		
		
		VBox animationPanel = new VBox(2);
		
		HBox buttons = new HBox(4);
		buttons.getChildren().add(start);
		buttons.getChildren().add(pause);
		buttons.getChildren().add(stop);
		
		start.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	worldView.getAnimation().start();
		    }
		});
		
		stop.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	worldView.getAnimation().stop();
		    }
		});
		
		thirdPersonView.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if ( worldView.isThirdPersonCamera() ) {
		    		
		    		worldView.setThirdPersonCamera(false);
		    		worldView.setCameraToLastAbsolutePosition();

		    		String bstyle=String.format("-fx-text-fill: #e8e8e8");
		    		thirdPersonView.setStyle(bstyle);
		    	} else {
		    		worldView.setThirdPersonCamera(true);
		    		worldView.setCameraToLastRelativePosition();
		    		
		    		String bstyle=String.format("-fx-text-fill: #2a7db0");
		    		thirdPersonView.setStyle(bstyle);
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
	
	public void updateAnimationInfo(AnimationFile animationFile, String loadedFile ) {
    	loadedFileLabel.setText("Loaded Animation File:  "+loadedFile);
    	durationLabel.setText("Duration:  "+Formats.decform01.format(animationFile.getMaxTime())+" [s]");
    	frequencyLabel.setText("Source Frequency:  "+Formats.decform01.format(animationFile.getFrequency())+" [Hz]");
	}
	
	
}
