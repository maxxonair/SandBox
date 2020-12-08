package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class menuBar {

	
	public static MenuBar create() {
	    MenuBar menuBar = new MenuBar(); 
	    
	    Menu menu = new Menu("Menu"); 
	    // add menu to menubar 
	    menuBar.getMenus().add(menu); 
	    
	    return menuBar;
	}
}
