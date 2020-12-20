package gui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import worldWindow.WorldView;

public class ControlPanel {

	private AnchorPane controlPanel;
	@SuppressWarnings("unused")
	private WorldView worldView; 
	
	private double minWidth = 250;
	
	public ControlPanel(WorldView worldView) {
		
	    controlPanel = new AnchorPane();
	    controlPanel.setMinWidth(minWidth);
	    
	    VBox contentLayout = new VBox(3);
	    
	    this.worldView = worldView;
	    /*
	    Slider modelXSlider = new Slider();
	    modelXSlider.setMin(-1000);
	    modelXSlider.setMax(1000);
	    modelXSlider.setValue(0);
	    modelXSlider.setMajorTickUnit(250);
	    modelXSlider.setMinorTickCount(50);
	    modelXSlider.setShowTickMarks(true);
	    modelXSlider.valueProperty().addListener(e -> {
	    	worldView.moveModelTo(worldView.getModelPosition().x,
	    						  modelXSlider.getValue(),  
	    						  worldView.getModelPosition().z);	
	    });
	     */
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

	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	    controlPanel.setMinWidth(150);
	}
			
}