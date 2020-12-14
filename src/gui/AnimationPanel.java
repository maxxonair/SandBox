package gui;

import animate.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import worldWindow.WorldView;

public class AnimationPanel {

	private AnchorPane contentPanel;
	private WorldView worldView;
	
	public AnimationPanel(WorldView worldView) {
		
		this.worldView=worldView;
		contentPanel = new AnchorPane();
		
		Button start = new Button("start");
		Button pause = new Button("pause");
		Button stop = new Button("stop");
		
		HBox buttons = new HBox(3);
		buttons.getChildren().add(start);
		buttons.getChildren().add(pause);
		buttons.getChildren().add(stop);
		
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
		
		contentPanel.getChildren().add(buttons);
	}

	public AnchorPane getContentPanel() {
		return contentPanel;
	}
	
}
