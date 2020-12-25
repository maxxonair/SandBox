package gui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import worldWindow.WorldView;

public class ControlPanel {

	private AnchorPane controlPanel;
	@SuppressWarnings("unused")
	private WorldView worldView; 
	
	// private double MINIMUM_WIDTH = 250;
	private double DEFAULT_WIDTH = 250;
	
	public ControlPanel(WorldView worldView) {
		
	    controlPanel = new AnchorPane();
	    //controlPanel.setMinWidth(MINIMUM_WIDTH);
	    controlPanel.setPrefWidth(DEFAULT_WIDTH);
	    
	    VBox contentLayout = new VBox(3);
	    
	    this.worldView = worldView;
	    
	    AnimationPanel animationPanel = new AnimationPanel(worldView);
	    EnvironmentPanel environmentPanel = new EnvironmentPanel(worldView);
	    
	    contentLayout.getChildren().add(animationPanel.getContentPanel());
	    contentLayout.getChildren().add(environmentPanel.getContentPanel());
	    
	    controlPanel.getChildren().add(contentLayout);
	}

	@SuppressWarnings("exports")
	public AnchorPane getControlPanel() {
		return controlPanel;
	}
			
}