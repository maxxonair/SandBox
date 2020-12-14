package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class menuBar {

	
	public static MenuBar create() {
	    MenuBar menuBar = new MenuBar(); 
	    
	    Menu menu = new Menu("Menu"); 
	    Menu file = new Menu("File"); 
	    Menu settings = new Menu("Settings"); 
	    Menu project = new Menu("Project"); 
	    // add menu to menubar 
	    menuBar.getMenus().add(menu); 
	    menuBar.getMenus().add(file); 
	    menuBar.getMenus().add(settings); 
	    menuBar.getMenus().add(project); 
	    
	    return menuBar;
	}
}