package attitudeWindow;


import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import gui.FilePaths;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


/**
 * @see http://stackoverflow.com/a/37370840/230513
 */
public class AttitudeWindow extends Application {

    private static final double SIZE = 300;
    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    private double modelScale=3;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Show some Attitude!");
        double size = 200;
        
        CartesianFrame NED = new CartesianFrame(size, 0);
        CartesianFrame SCB = new CartesianFrame(size, 1);
        AxisRing SCB_X = new AxisRing(size, 1, Color.BLUE);
        AxisRing SCB_Y = new AxisRing(size, 2, Color.GREEN);
        AxisRing Horizon = new AxisRing(size, 3, Color.WHITE);
        Model Spacecraft = new Model(FilePaths.Model3DFilePath, modelScale);
        
        Group SceneContent = new Group();
        SceneContent.getChildren().add(NED.getFrame());
        SceneContent.getChildren().add(SCB.getFrame());
        SceneContent.getChildren().add(SCB_X.getRing());
        SceneContent.getChildren().add(SCB_Y.getRing());
        SceneContent.getChildren().add(Horizon.getRing());
        SceneContent.getChildren().add(Spacecraft.getModel());
        
        Scene scene = new Scene(SceneContent, SIZE * 2, SIZE * 2, true);
        primaryStage.setScene(scene);
        
        scene.setFill(Color.BLACK);
        
        //SetMouseMovement(scene, SCB.getRx(), SCB.getRy());
        //SetMouseMovement(scene, SCB_X.getRx(), SCB_X.getRy());
        scene.setOnMouseMoved((final MouseEvent e) -> {
        	SCB.getRx().setAngle(e.getSceneY() * 360 / scene.getHeight());
        	SCB.getRy().setAngle(e.getSceneX() * 360 / scene.getWidth());
        	SCB_X.getRx().setAngle(e.getSceneY() * 360 / scene.getHeight());
        	SCB_X.getRy().setAngle(e.getSceneX() * 360 / scene.getWidth());
        	SCB_Y.getRx().setAngle(e.getSceneY() * 360 / scene.getHeight());
        	SCB_Y.getRy().setAngle(e.getSceneX() * 360 / scene.getWidth());
        	Spacecraft.getRx().setAngle(e.getSceneY() * 360 / scene.getHeight());
        	Spacecraft.getRy().setAngle(e.getSceneX() * 360 / scene.getWidth());
        	
        });
        
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(SIZE * 6);
        camera.setTranslateZ(-3 * SIZE);
        scene.setCamera(camera);
        scene.setOnScroll((final ScrollEvent e) -> {
            camera.setTranslateZ(camera.getTranslateZ() + e.getDeltaY());
        });
        
        
        primaryStage.show();
        //play();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}
