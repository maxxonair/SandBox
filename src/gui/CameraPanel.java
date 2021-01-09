package gui;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utils.Formats;
import worldWindow.WorldView;

public class CameraPanel {

	private AnchorPane contentPanel;
	
	public CameraPanel(WorldView worldView) {
		
		contentPanel = new AnchorPane();
		
		VBox content = new VBox(3);
		
		VBox sliderBoxes = new VBox(5);
		
		Label moduleLabel = new Label("Camera Settings:");
		
	    Label cameraFoVLabel = new Label();
	    Slider cameraFoV = new Slider();
	    cameraFoV.setMin(20);
	    cameraFoV.setMax(90);
	    cameraFoV.setValue(worldView.getCamerFoV());
	    cameraFoVLabel.setText("Camera Field of View: "+Formats.decform00.format(cameraFoV.getValue())+ " [deg]");
	    cameraFoV.setMajorTickUnit(10);
	    cameraFoV.setShowTickMarks(true);
	    cameraFoV.valueProperty().addListener(e -> {
	    		worldView.setCameraFoV(cameraFoV.getValue());
	    		cameraFoVLabel.setText("Camera Field of View: "+Formats.decform00.format(cameraFoV.getValue())+ " [deg]");
	    });	 
	    
	    Label cameraSpeedLabel = new Label("Camera Control Speed:");
	    Slider cameraSpeed = new Slider();
	    cameraSpeed.setMin(5);
	    cameraSpeed.setMax(150);
	    cameraSpeed.setValue(worldView.getCameraControlIncrement());
	    cameraSpeedLabel.setText("Camera Control Speed: "+Formats.decform00.format(cameraSpeed.getValue())+ " [clicks]");
	    cameraSpeed.setMajorTickUnit(25);
	    cameraSpeed.setShowTickMarks(true);
	    cameraSpeed.valueProperty().addListener(e -> {
	    		worldView.setCameraControlIncrement((int) cameraSpeed.getValue());
	    		cameraSpeedLabel.setText("Camera Control Speed: "+Formats.decform00.format(cameraSpeed.getValue())+ " [clicks]");
	    });	
	    
	    Label cameraNearClipLabel = new Label("");
	    Slider cameraNearClip = new Slider();
	    cameraNearClip.setMin(.0001);
	    cameraNearClip.setMax(1);
	    cameraNearClip.setValue(worldView.getCameraNearClip());
	    cameraNearClipLabel.setText("Camera Near Clip: "+Formats.decform03.format(cameraNearClip.getValue())+ " [clicks]");
	    cameraNearClip.setMajorTickUnit(0.1);
	    cameraNearClip.setShowTickMarks(true);
	    cameraNearClip.valueProperty().addListener(e -> {
	    		worldView.setCameraNearClip( cameraNearClip.getValue());
	    		cameraNearClipLabel.setText("Camera Near Clip: "+Formats.decform03.format(cameraNearClip.getValue())+ " [clicks]");
	    });	
	    
	    Label cameraFarClipLabel = new Label("");
	    Slider cameraFarClip = new Slider();
	    cameraFarClip.setMin(10);
	    cameraFarClip.setMax(5000);
	    cameraFarClip.setValue(worldView.getCameraFarClip());
	    cameraFarClipLabel.setText("Camera Far Clip: "+Formats.decform00.format(cameraFarClip.getValue())+ " [clicks]");
	    cameraFarClip.setMajorTickUnit(100);
	    cameraFarClip.setShowTickMarks(true);
	    cameraFarClip.valueProperty().addListener(e -> {
	    		worldView.setCameraFarClip( cameraFarClip.getValue());
	    		cameraFarClipLabel.setText("Camera Far Clip: "+Formats.decform00.format(cameraFarClip.getValue())+ " [clicks]");
	    });	
	    
	    
		sliderBoxes.getChildren().add(cameraFoVLabel);
		sliderBoxes.getChildren().add(cameraFoV);
		sliderBoxes.getChildren().add(cameraSpeedLabel);
		sliderBoxes.getChildren().add(cameraSpeed);
		sliderBoxes.getChildren().add(cameraNearClipLabel);
		sliderBoxes.getChildren().add(cameraNearClip);
		sliderBoxes.getChildren().add(cameraFarClipLabel);
		sliderBoxes.getChildren().add(cameraFarClip);
		
	    
		content.setLayoutY(20);
		content.getChildren().add(moduleLabel);
		content.getChildren().add(sliderBoxes);
		
		contentPanel.getChildren().add(content);
	}
	
	@SuppressWarnings("exports")
	public AnchorPane getContentPanel() {
		return contentPanel;
	}
		
}
