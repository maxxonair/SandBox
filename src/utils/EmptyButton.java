package utils;

import javafx.scene.control.Button;

public class EmptyButton extends Button{

	
	public EmptyButton(int Width) {
		
	 		String emptyStyle=String.format("-fx-background-color: transparent; -fx-border-color: transparent;");
	 		this.setStyle(emptyStyle);		 
	 		this.setMinSize(Width, this.getHeight());
	 		
	}

}
