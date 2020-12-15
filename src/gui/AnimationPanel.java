package gui;

import java.io.File;

import animate.Animation;
import animate.AnimationFile;
import interfaces.LoadFromFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import worldWindow.WorldView;

public class AnimationPanel {

	private AnchorPane contentPanel;
	private WorldView worldView;
	
	private LoadFromFile fileLoader = new LoadFromFile();
	
	public AnimationPanel(WorldView worldView) {
		
		this.worldView=worldView;
		contentPanel = new AnchorPane();
		
		Button start = new Button("start");
		Button pause = new Button("pause");
		Button stop = new Button("stop");
		
		final FileChooser fileChooser = new FileChooser();
		Button openFile = new Button("Open");
		
		HBox buttons = new HBox(4);
		buttons.getChildren().add(start);
		buttons.getChildren().add(pause);
		buttons.getChildren().add(stop);
		buttons.getChildren().add(openFile);
		
		Animation animation = new Animation(worldView);
		//animation.setTestAnimationFile();
		
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
                	System.out.println(file);
                	
                	fileLoader.loadTextData(file, 0.000001);
                	AnimationFile animationFile = new AnimationFile("New File");
                	animationFile.setSequenceList( fileLoader.loadTextData(file, 1) );
                	animation.setAnimationFile(animationFile);
                }
		    }
		    	
		    });
		
		contentPanel.getChildren().add(buttons);
	}

	public AnchorPane getContentPanel() {
		return contentPanel;
	}
	
	
}
