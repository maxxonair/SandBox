package gui;

import java.io.File;

import animate.Animation;
import animate.AnimationFile;
import interfaces.LoadFromFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
		
		Button start = new Button("start");
		Button pause = new Button("pause");
		Button stop = new Button("stop");
		Label loadedFileLabel = new Label("");
		Label durationLabel = new Label("");
		Label frequencyLabel = new Label("");
		
		
		VBox statisticsPanel = new VBox(2);
		statisticsPanel.getChildren().add(loadedFileLabel);
		statisticsPanel.getChildren().add(durationLabel);
		statisticsPanel.getChildren().add(frequencyLabel);
		
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
		
		openFile.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage stage = new Stage();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                	try {
	                	fileLoader.loadTextData(file, 0.000001);
	                	AnimationFile animationFile = fileLoader.loadTextData(file, 0.1) ;
	                	
	                	animation.setAnimationFile(animationFile);
	                	// Update indicator label
	                	String[] pathElements = file.getPath().split(FILE_DELIMITER);
	                	loadedFileLabel.setText("Loaded Animation File:  "+pathElements[pathElements.length-1]);
	                	durationLabel.setText("Duration:  "+Formats.decform01.format(animationFile.getMaxTime()));
	                	frequencyLabel.setText("Source Frequency:  "+Formats.decform01.format(animationFile.getFrequency()));
	                	
                	} catch (Exception exp ) {
                		System.out.println(exp);
                		System.out.println("Loading file failed");
                	}
                }
		    }
		    	
		    });
		
		
		animationPanel.getChildren().add(buttons);
		animationPanel.getChildren().add(statisticsPanel);
		
		contentPanel.getChildren().add(animationPanel);
	}

	@SuppressWarnings("exports")
	public AnchorPane getContentPanel() {
		return contentPanel;
	}
	
	
}
