package gui;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import worldWindow.WorldView;

public class EnvironmentPanel {
	
	private AnchorPane contentPanel;
	
	public EnvironmentPanel(WorldView worldView) {
		
		contentPanel = new AnchorPane();
		
		VBox checkBoxes = new VBox(3);
	    ToggleGroup group = new ToggleGroup();
		
		RadioButton gridView    = new RadioButton("Set grid view");
		gridView.setToggleGroup(group);
		gridView.setSelected(true);
		RadioButton flatEarth   = new RadioButton("Set flat Earth");
		flatEarth.setToggleGroup(group);
		RadioButton curvedEarth = new RadioButton("Set curved Earth");
		curvedEarth.setToggleGroup(group);
		RadioButton noGround = new RadioButton("Set no Ground");
		noGround.setToggleGroup(group);
		
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
		
		
		checkBoxes.getChildren().add(gridView);
		checkBoxes.getChildren().add(flatEarth);
		checkBoxes.getChildren().add(curvedEarth);
		checkBoxes.getChildren().add(noGround);
		
		checkBoxes.setLayoutY(20);
		
		contentPanel.getChildren().add(checkBoxes);
	}

	@SuppressWarnings("exports")
	public AnchorPane getContentPanel() {
		return contentPanel;
	}
	

}
