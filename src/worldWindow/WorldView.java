package worldWindow;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import gui.Colors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import utils.Formats;
import utils.Quaternion;
import utils.Vec3;
import utils.Vector4;

/**
 * In JavaFX, the camera coordinate system is as follows:

		• X-axis pointing to the right
		
		• Y-axis pointing down
		
		• Z-axis pointing away from the viewer or into the screen.
 */
public class WorldView extends Application{
//---------------------------------------------------------------------------
// Scene control 
//---------------------------------------------------------------------------
double holdValx=0;
double holdValy=0;

private double sceneHeight;
private double sceneWidth;

private Scene parentScene;
private SubScene scene;

private AnchorPane anchorPane;

//---------------------------------------------------------------------------
// Environment settings 
//---------------------------------------------------------------------------
private float GRID_SIZE = 100000;
private float GRID_RESOLUTION = 500;
//---------------------------------------------------------------------------
// Mouse control  
//---------------------------------------------------------------------------
//private   double mouseWheelZoomSensitivity = 0.5;
private   double mouseSensitivity =0.05;
//---------------------------------------------------------------------------
// Camera settings 
//---------------------------------------------------------------------------
private PerspectiveCamera camera;

private   double anchorAngleCameraY=0;
private   double anchorAngleCameraX=0;

private   final DoubleProperty angleCameraY = new SimpleDoubleProperty(0);	
private   final DoubleProperty angleCameraX = new SimpleDoubleProperty(0);
private double cameraToTargetDistance = 0;

// Camera movement (Keyboard control)
private boolean running, goNorth, goSouth, goEast, goWest;

//private boolean isZoom = false;

private double CAMERA_FOV = 40;
//---------------------------------------------------------------------------
// Model control 
//---------------------------------------------------------------------------
private SmartGroup model;
private String modelObjectPath;
private double modelScale=3;

//private Vector3 rotState = new Vector3(0,0,0);
 
private Quaternion quatTemp = new Quaternion(1,0,0,0);

@SuppressWarnings("unused")
private Translate translate;
private Rotate rotateX,rotateY;
//---------------------------------------------------------------------------
//  Head-up Display controls 
//---------------------------------------------------------------------------
private Label HUD_cameraPosition;
private Label HUD_modelPosition;
private Label HUD_animationTime;
//---------------------------------------------------------------------------
// 
//---------------------------------------------------------------------------
	@Override
	public void start(Stage stage) throws Exception{
		//---------------------------------------------------------------------------
		// Model 
		//---------------------------------------------------------------------------
		try {
			model =  loadModel(modelObjectPath);
		} catch (Exception e) {
			model = new SmartGroup();
		}
		// Set init model position
		model.setTranslateX(200);
		model.setTranslateY(0);
		model.setTranslateZ(-300);
		
	    Group root = new Group();
		root.getChildren().add(model);
		
		anchorPane = new AnchorPane();
		//---------------------------------------------------------------------------
		// Camera
		//---------------------------------------------------------------------------
	    camera = new PerspectiveCamera();
		camera.setNearClip(.001);
		camera.setFarClip(100);	
		camera.setFieldOfView(CAMERA_FOV);
		
		SmartGroup cameraGroup = new SmartGroup();
		cameraGroup.getChildren().add(camera);

		camera.getTransforms().addAll(
                rotateY = new Rotate(0, Rotate.Y_AXIS),
                rotateX = new Rotate(-35, Rotate.X_AXIS)
        );
		/*
		camera.setTranslateX(0);
		camera.setTranslateY(-300);
		camera.setTranslateZ(-1000);
		*/
		camera.setTranslateX(-6125);
		camera.setTranslateY(-21075);
		camera.setTranslateZ(-25050);
		//---------------------------------------------------------------------------
		// Environment
		//---------------------------------------------------------------------------
		final Group grid = Grid.createGrid(GRID_SIZE, GRID_RESOLUTION);
		
		Group environment = new Group();
		environment.getChildren().add(grid);
		
		double translateGrid = 0;
		environment.setTranslateX(-translateGrid);
		
		
		root.getChildren().add(environment);
		root.getChildren().add(camera);
		
		//---------------------------------------------------------------------------
		// Scene
		//---------------------------------------------------------------------------
		sceneHeight = 700;
		sceneWidth = 1300;
	    scene = new SubScene(root, sceneHeight, sceneWidth, true, SceneAntialiasing.BALANCED);
		scene.setFill(Colors.backGroundColor);
		scene.setCamera(camera);		
		
		//---------------------------------------------------------------------------
		// Mouse and Keyboard Control
		//---------------------------------------------------------------------------
		//initMouseControl(scene, camera);
		
		parentScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = true; break;
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                    case SHIFT: running = true; break;
				default:
					break;
                }
            }
        });

		parentScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; break;
                    case DOWN:  goSouth = false; break;
                    case LEFT:  goWest  = false; break;
                    case RIGHT: goEast  = false; break;
                    case SHIFT: running = false; break;
				default:
					break;
                }
            }
        });
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int dx = 0, dy = 0, dz = 0;
                int pace = 25;
                if (goNorth) dy -= pace;
                if (goSouth) dy += pace;
                if (goEast)  dx += pace;
                if (goWest)  dx -= pace;
                if (running) { dz = -dy ; dy = 0 ; }

                moveCameraBy(dx, dy, dz);
            }
        };
        timer.start();
        
       anchorPane.getChildren().add(scene);
       
       //---------------------------------------------------------------------------
       // Head-on Display
       //---------------------------------------------------------------------------
	    HUD_cameraPosition = new Label("Camera position [x y z]: ["+camera.getLayoutX()+"  "+camera.getLayoutY()+"  "+camera.getTranslateX()+"]");
	    HUD_cameraPosition.setLayoutY(0);
	    HUD_modelPosition = new Label("Model position [x y z]: ["+model.getTranslateY()+"  "+model.getTranslateY()+"  "+model.getTranslateY()+"]");
	    HUD_modelPosition.setLayoutY(20);
	    HUD_animationTime = new Label("Time [s]: 0");
	    HUD_animationTime.setLayoutY(40);
	    
	    Group HUD_Elements = new Group();
	    HUD_Elements.getChildren().add(HUD_cameraPosition);
	    HUD_Elements.getChildren().add(HUD_modelPosition);
	    HUD_Elements.getChildren().add(HUD_animationTime);
        
		anchorPane.getChildren().add(HUD_Elements);
		
       //---------------------------------------------------------------------------
       // Final setup
       //---------------------------------------------------------------------------
	  // moveCameraTo(-250, -500, -250);
        
	}
	
	
    private void moveCameraBy(int dx, int dy) {
       // if (dx == 0 && dy == 0) return;

        final double cx = camera.getBoundsInLocal().getWidth()  / 2;
        final double cy = camera.getBoundsInLocal().getHeight() / 2;

        double x = cx + camera.getLayoutX() + dx;
        double y = cy + camera.getLayoutY() + dy;

        moveCameraTo(x, y);
    }
    
    private void moveCameraBy(int dx, int dy, int dz) {
        // if (dx == 0 && dy == 0) return;

         final double cx = camera.getBoundsInLocal().getWidth()  / 2;
         final double cy = camera.getBoundsInLocal().getHeight() / 2;

         double x =  camera.getTranslateX() + dx;
         double y =  camera.getTranslateY() + dy;
         double z =  camera.getTranslateZ() + dz;

         moveCameraTo(x, y, z);
     }
    
    public void moveCameraTo(double x, double y) {
        final double cx = camera.getBoundsInLocal().getWidth()  / 2;
        final double cy = camera.getBoundsInLocal().getHeight() / 2;

        camera.relocate(x - cx, y - cy);
        updateHUD();
    }
    
    public void moveCameraTo(double x, double y, double z) {
        camera.setTranslateX(x);
        camera.setTranslateY(y);
        camera.setTranslateZ(z);
        updateHUD();
    }
    
    public void moveModelTo(double x, double y, double z) {
        model.setTranslateX(x);
        model.setTranslateY(y);
        model.setTranslateZ(z);
        updateHUD();
    }
	
	public void modelRotation(Quaternion q) {
			Quaternion qInverse=new Quaternion();
			try {
				qInverse = (Quaternion) quatTemp.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			qInverse.inverse();
			Vector4 vec = matrixRotateNode1(qInverse);
			Point3D rotAxis = new Point3D(vec.x, vec.y, vec.z);
			double  rotAngle =vec.w;
            model.setRotationAxis(rotAxis);
            model.setRotate(rotAngle);   

            vec = matrixRotateNode1(q);
            if (vec != new Vector4(0,0,0,0)) {
				rotAxis = new Point3D(vec.x, vec.y, vec.z);
				rotAngle =vec.w;
	            model.setRotationAxis(rotAxis);
	            model.setRotate(rotAngle); 
	            
				quatTemp = q;
            }			
	}
	    
    public Vector4 matrixRotateNode1(Quaternion q ){
    	
    	Vector4 result = new Vector4(0,0,0,0);
    	
    	double[][] cosMat = q.getCosineMatrix();

        double A11 = cosMat[0][0];
        double A12 = cosMat[0][1];
        double A13 = cosMat[0][2];
        
        double A21 = cosMat[1][0];
        double A22 = cosMat[1][1];
        double A23 = cosMat[1][2];
        
        double A31 = cosMat[2][0];
        double A32 = cosMat[2][1];
        double A33 = cosMat[2][2];
         
        double dd = Math.acos((A11+A22+A33-1d)/2d);
        if(dd!=0d){
            double den=2d*Math.sin(dd);   
            result.w = Math.toDegrees(dd);
            result.x = (A32-A23)/den;
            result.z = (A13-A31);
            result.y = (A21-A12)/den;            
        }
        return result;
    }	
	
	private   void initMouseControl(SubScene scene, Camera camera) {
		
		/**
		 * 
		 *  		Camera movement using smartgroup camera 
		 *  
		 *  
		 */
		

		rotateX.angleProperty().bind(angleCameraY);
		rotateY.angleProperty().bind(angleCameraX);
		
		scene.setOnMousePressed(event -> {
			holdValx = event.getSceneX();
			holdValy = event.getSceneY();
			anchorAngleCameraY = angleCameraY.get();
			anchorAngleCameraX = angleCameraX.get();
		});
		scene.setOnMouseDragged(event ->{
			//angleCameraX.set(anchorAngleCameraX - (anchorCameraX - event.getSceneX())*mouseSensitivity); 
			double cameraAngleX = anchorAngleCameraY + (holdValy - event.getSceneY())*mouseSensitivity ; 
			angleCameraY.set(cameraAngleX);
			double cameraAngleY = anchorAngleCameraX - (holdValx - event.getSceneX())*mouseSensitivity ;
			angleCameraX.set(cameraAngleY);
			//setCameraRotationX(camera, cameraAngleX);
			
			//System.out.println(cameraAngleX+"|"+cameraAngleY);
			
			//camera.translateZProperty().set(cameraToTargetDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
			//camera.translateYProperty().set(cameraToTargetDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
			double CY = - cameraToTargetDistance*Math.sin(Math.toRadians(-cameraAngleX)) ;
			double CZ = - cameraToTargetDistance*(Math.cos(Math.toRadians(-cameraAngleX)));
			double CX =  CZ / Math.sin(Math.toRadians(-cameraAngleY));
			
			camera.translateZProperty().set(CZ);
			camera.translateYProperty().set(CY);
			camera.translateXProperty().set(CX);
			//System.out.println(cameraToTargetDistance+"|"+CX+"|"+CY+"|"+CZ);
			
		});
		/*
		if(isZoom) {
			fxpanel.addMouseWheelListener(new MouseWheelListener() {
	
				@Override
				public void mouseWheelMoved(MouseWheelEvent arg0) {
					// TODO Auto-generated method stub
						double wheelSpeed = arg0.getPreciseWheelRotation();
							if(wheelSpeed>0) {
								cameraToTargetDistance += mouseWheelZoomSensitivity;
							} else {
								if(cameraToTargetDistance>0.5) {
									cameraToTargetDistance -= mouseWheelZoomSensitivity;
								}
							}
							//group.translateZProperty().set(cameraToTargetDistance);
							//camera.translateZProperty().set(cameraToTargetDistance*(1-Math.cos(Math.toRadians(-angleCameraY.getValue()))));
							//camera.translateYProperty().set(cameraToTargetDistance*Math.sin(Math.toRadians(angleCameraY.getValue())));
				double CY = - cameraToTargetDistance*Math.sin(Math.toRadians(-angleCameraY.get()));
				double CZ = - cameraToTargetDistance*(Math.cos(Math.toRadians(-angleCameraY.get())));
				double CX = CZ / Math.sin(Math.toRadians(-angleCameraX.get()));
				camera.translateZProperty().set(CZ);
				camera.translateYProperty().set(CY);
				camera.translateXProperty().set(CX);
				System.out.println(cameraToTargetDistance+"|"+CX+"|"+CY+"|"+CZ);
				}
				
			});
		}
		*/
	}
	
	public   void setCameraRotationX(SmartGroup camera, double angleX) {
		camera.rotateByX(angleX);
	}
	

public SubScene getScene() {
		return scene;
	}

private   SmartGroup loadModel(String fileString) {
    SmartGroup modelRoot = new SmartGroup();
	PhongMaterial material = new PhongMaterial();
    material.setDiffuseColor(Color.SILVER);
    ObjModelImporter importer = new ObjModelImporter();
   // importer.read(url);
   // try {
    importer.read(fileString);

    for (MeshView view : importer.getImport()) {
        modelRoot.getChildren().add(view);
        view.setMaterial(material);
    }
    /*
    } catch (Exception exp) {
    	System.out.println("Error: Loading 3D Model failed.");
    }
*/
modelRoot.setScaleX(modelScale);
modelRoot.setScaleY(modelScale);
modelRoot.setScaleZ(modelScale);
    return modelRoot;
}


@SuppressWarnings("unused")
private   Node prepareAmbientLight(){
	
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setColor(Color.GRAY);

	return ambientLight;
}

public void setModelObjectPath(String modelObjectPath) {
	this.modelObjectPath = modelObjectPath;
}

public WorldView(String objectFilePath, Scene scene) {
	this.modelObjectPath = objectFilePath; 
	this.parentScene = scene;
}

public double getSceneHeight() {
	return sceneHeight;
}

public void setSceneHeight(double sceneHeight) {
	this.sceneHeight = sceneHeight;
    scene.setHeight(sceneHeight);
    //moveCameraTo(0,0);
}

public double getSceneWidth() {
	return sceneWidth;
}

public AnchorPane getAnchorPane() {
	return anchorPane;
}


public void setSceneWidth(double sceneWidth) {
	this.sceneWidth = sceneWidth;
    scene.setWidth(sceneWidth);
    //moveCameraTo(0,0);
}
/*
public static void main(String[] args) {launch(args);}
*/	

public Vec3 getCameraPosition() {
	Vec3 cameraPosition = new Vec3();
	cameraPosition.x = camera.getTranslateX();
	cameraPosition.y = camera.getTranslateY();
	cameraPosition.z = camera.getTranslateZ();
	return cameraPosition;
}

public Vec3 getModelPosition() {
	Vec3 modelPosition = new Vec3();
	modelPosition.x = model.getTranslateX();
	modelPosition.y = model.getTranslateY();
	modelPosition.z = model.getTranslateZ();
	return modelPosition;
}

private void updateHUD() {
	HUD_cameraPosition.setText("Camera position [x y z ]: ["+Formats.decform01.format(camera.getTranslateX())+
			"  "+Formats.decform01.format(camera.getTranslateY())+
			"  "+Formats.decform01.format(camera.getTranslateZ())+"]");
	HUD_modelPosition.setText("Model position [x y z ]: ["+Formats.decform01.format(model.getTranslateX())+"  "+
			Formats.decform01.format(model.getTranslateY())+"  "+
			Formats.decform01.format(model.getTranslateZ())+"]");
}

public void setAnimationTime(double time) {
	HUD_animationTime.setText("Time [s]: "+Formats.decform01.format(time));
}
}
