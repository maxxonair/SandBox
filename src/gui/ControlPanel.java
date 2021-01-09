package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import worldWindow.WorldView;

public class ControlPanel {

    ScrollPane contentPane;
	@SuppressWarnings("unused")
	private WorldView worldView; 
	private AnimationPanel animationPanel ;
	private EnvironmentPanel environmentPanel;
	private CameraPanel cameraPanel;
	
	// private double MINIMUM_WIDTH = 250;
	private double DEFAULT_WIDTH = 250;
	
	public ControlPanel(WorldView worldView) {
		
	    
		contentPane = new ScrollPane();
		contentPane.setPrefWidth(DEFAULT_WIDTH);
		
		VBox contentLayout = new VBox(4);
		
		this.worldView = worldView;
		
		 animationPanel = new AnimationPanel(worldView);
		 environmentPanel = new EnvironmentPanel(worldView);
		 cameraPanel = new CameraPanel(worldView);
		
		contentLayout.getChildren().add(animationPanel.getContentPanel());
		contentLayout.getChildren().add(environmentPanel.getContentPanel());
		contentLayout.getChildren().add(cameraPanel.getContentPanel());
		
		contentPane.setContent(contentLayout);
	    
	}

	
	@SuppressWarnings("exports")
	public ScrollPane getContentPane() {
		return contentPane;
	}

	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}

	public EnvironmentPanel getEnvironmentPanel() {
		return environmentPanel;
	}
	
	public void setPrefWidth(int width) {
	    contentPane.setPrefWidth(DEFAULT_WIDTH);
	}
			
}