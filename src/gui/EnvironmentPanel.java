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
	private double ENVIRONMENT_GRID_MAX = 150000;
	
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
		
	    
		Label environmentSizeLabel = new Label();
		
	    Slider environmentSize = new Slider();
	    environmentSize.setMin(ENVIRONMENT_GRID_MIN);
	    environmentSize.setMax(ENVIRONMENT_GRID_MAX);
	    environmentSize.setValue(worldView.getGridSize());
	    environmentSizeLabel.setText("Environment Size: "+Formats.decform00.format(environmentSize.getValue()));
	    environmentSize.setMajorTickUnit(5000);
	    environmentSize.setShowTickMarks(true);
	    environmentSize.valueProperty().addListener(e -> {
	    		worldView.setGridSize((float) environmentSize.getValue());
	    		environmentSizeLabel.setText("Environment Size: "+Formats.decform00.format(environmentSize.getValue()));
	    });	
	    
	    
	    Label modelScaleLabel = new Label();
	    Slider modelScale = new Slider();
	    modelScale.setMin(1);
	    modelScale.setMax(150);
	    modelScale.setValue(worldView.getModelScale());
	    modelScaleLabel.setText("Model Scale: "+Formats.decform00.format(modelScale.getValue())+ " [clicks]");
	    modelScale.setMajorTickUnit(25);
	    modelScale.setShowTickMarks(true);
	    modelScale.valueProperty().addListener(e -> {
	    	worldView.setModelScale(modelScale.getValue());
	    	modelScaleLabel.setText("Model Scale: "+Formats.decform00.format(modelScale.getValue())+ " [clicks]");
	    });	
	    
	    Label trajectoryScaleLabel = new Label();
	    Slider trajectoryScale = new Slider();
	    trajectoryScale.setMin(0.001);
	    trajectoryScale.setMax(5);
	    trajectoryScale.setValue(worldView.getTrajectoryScaleFactor());
	    trajectoryScaleLabel.setText("Trajectory Scale: "+Formats.decform03.format(trajectoryScale.getValue())+ " [clicks]");
	    trajectoryScale.setMajorTickUnit(0.1);
	    trajectoryScale.setShowTickMarks(true);
	    trajectoryScale.valueProperty().addListener(e -> {
	    	worldView.setTrajectoryScaleFactor(trajectoryScale.getValue());
	    	trajectoryScaleLabel.setText("Trajectory Scale: "+Formats.decform03.format(trajectoryScale.getValue())+ " [clicks]");
	    });	
	    
	    Label environmentBrightnessLabel = new Label();
	    Slider environmentBrightness = new Slider();
	    environmentBrightness.setMin(0.0);
	    environmentBrightness.setMax(1);
	    environmentBrightness.setValue(worldView.getEnvironmentBackgroundColor().x);
	    environmentBrightnessLabel.setText("Environment Brightness: "+Formats.decform00.format(100 * environmentBrightness.getValue())+ " [percent]");
	    environmentBrightness.setMajorTickUnit(0.1);
	    environmentBrightness.setShowTickMarks(true);
	    environmentBrightness.valueProperty().addListener(e -> {
	    	worldView.setEnvironmentBackgroundColor(environmentBrightness.getValue(),environmentBrightness.getValue(),environmentBrightness.getValue());
	    	environmentBrightnessLabel.setText("Environment Brightness: "+Formats.decform00.format(100 * environmentBrightness.getValue())+ " [percent]");
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
		
		sliderBoxes.setLayoutY(20);
		sliderBoxes.getChildren().add(environmentSizeLabel);
		sliderBoxes.getChildren().add(environmentSize);
		sliderBoxes.getChildren().add(modelScaleLabel);
		sliderBoxes.getChildren().add(modelScale);
		sliderBoxes.getChildren().add(trajectoryScaleLabel);
		sliderBoxes.getChildren().add(trajectoryScale);
		sliderBoxes.getChildren().add(environmentBrightnessLabel);
		sliderBoxes.getChildren().add(environmentBrightness);
		
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
