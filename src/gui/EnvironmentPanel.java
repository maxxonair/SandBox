package gui;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utils.Formats;
import worldWindow.WorldView;

public class EnvironmentPanel {
	
	private AnchorPane contentPanel;
	
	private double ENVIRONMENT_GRID_MIN = 1000;
	private double ENVIRONMENT_GRID_MAX = 100000;
	
	public EnvironmentPanel(WorldView worldView) {
		
		contentPanel = new AnchorPane();
		
		VBox content = new VBox(3);
		
		VBox checkBoxes = new VBox(3);
		VBox sliderBoxes = new VBox(3);
		
	    ToggleGroup group = new ToggleGroup();
		
	    Label environmentTypeLabel = new Label("Environment Type:");
	    
		RadioButton gridView    = new RadioButton("Set grid view");
		gridView.setToggleGroup(group);
		gridView.setSelected(true);
		RadioButton flatEarth   = new RadioButton("Set flat Earth");
		flatEarth.setToggleGroup(group);
		RadioButton curvedEarth = new RadioButton("Set curved Earth");
		curvedEarth.setToggleGroup(group);
		RadioButton noGround = new RadioButton("Set no Ground");
		noGround.setToggleGroup(group);
		
	    
		Label environmentSizeLabel = new Label("Environment Size:");
		
	    Slider environmentSize = new Slider();
	    environmentSize.setMin(ENVIRONMENT_GRID_MIN);
	    environmentSize.setMax(ENVIRONMENT_GRID_MAX);
	    environmentSize.setValue(worldView.getGridSize());
	    environmentSize.setMajorTickUnit(5000);
	    environmentSize.setShowTickMarks(true);
	    environmentSize.valueProperty().addListener(e -> {
	    		worldView.setGridSize((float) environmentSize.getValue());
	    		environmentSizeLabel.setText("Environment Size: "+Formats.decform00.format(environmentSize.getValue()));
	    });	
	    
	    Label cameraFoVLabel = new Label("Camera Field of View:");
	    Slider cameraFoV = new Slider();
	    cameraFoV.setMin(20);
	    cameraFoV.setMax(90);
	    cameraFoV.setValue(worldView.getGridSize());
	    cameraFoV.setMajorTickUnit(10);
	    cameraFoV.setShowTickMarks(true);
	    cameraFoV.valueProperty().addListener(e -> {
	    		worldView.setCameraFoV(cameraFoV.getValue());
	    		cameraFoVLabel.setText("Camera Field of View: "+Formats.decform00.format(cameraFoV.getValue())+ " [deg]");
	    });	 
		
		gridView.selectedProperty().addListener(observable -> {
	        if (gridView.isSelected()) {
	        		worldView.selectGridView();
	        } 
	      });
		
		flatEarth.selectedProperty().addListener(observable -> {
	        if (flatEarth.isSelected()) {
	        		worldView.selectFlatEarth();
	        } 
	      });
		
		curvedEarth.selectedProperty().addListener(observable -> {
	        if (curvedEarth.isSelected()) {
	        		worldView.selectCurvedEarth();
	        } 
	      });
		noGround.selectedProperty().addListener(observable -> {
	        if (noGround.isSelected()) {
	        		worldView.selectNoGround();;
	        } 
	      });
		
		checkBoxes.getChildren().add(environmentTypeLabel);
		checkBoxes.getChildren().add(gridView);
		checkBoxes.getChildren().add(flatEarth);
		checkBoxes.getChildren().add(curvedEarth);
		checkBoxes.getChildren().add(noGround);
		
		sliderBoxes.getChildren().add(environmentSizeLabel);
		sliderBoxes.getChildren().add(environmentSize);
		sliderBoxes.getChildren().add(cameraFoVLabel);
		sliderBoxes.getChildren().add(cameraFoV);
		
		content.setLayoutY(20);
		
		content.getChildren().add(checkBoxes);
		content.getChildren().add(sliderBoxes);
		
		contentPanel.getChildren().add(content);
	}

	@SuppressWarnings("exports")
	public AnchorPane getContentPanel() {
		return contentPanel;
	}
	

}
