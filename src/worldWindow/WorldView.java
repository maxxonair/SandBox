package worldWindow;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import animate.Animation;
import animate.AnimationFile;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import utils.EmptyButton;
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

private SubScene scene;

private AnchorPane anchorPane;
private Animation animation ;
//---------------------------------------------------------------------------
// Environment settings 
//---------------------------------------------------------------------------
private float gridSize = 70000;
private float GRID_RESOLUTION = 500;

private Group environment;
private Group grid;
private Group rootGroup;
// Is grid view active 
private boolean isGrid = true;
// Is flat, textured ground active 
private boolean isFlatEarth = false;
// Is height map + textured ground active 
private boolean isCurvedEarth = false;
private Vec3 environmentBackgroundColor = new Vec3(0.15,0.15,0.15);
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
private   double cameraToTargetDistance = 0;

// Camera movement (Keyboard control)
private boolean running, goNorth, goSouth, goEast, goWest, goForward, goBackward;

//private boolean isZoom = false;
// Camera Field of View [deg]:
private double DEFAULT_CAMERA_FOV = 40;
// Third person camera activated:
private boolean thirdPersonCamera = false; 
// Default Positions:
private Vec3 DEFAULT_CAMERA_POSITION = new Vec3(-6125,-21075,-25050);
private Vec3 DEFAULT_RELATIVE_CAMERA_POSITION = new Vec3(-650,-650,-1000);

private Button cameraMinusX ;
private Button cameraPlusX ;
private Button cameraMinusY ;
private Button cameraPlusY ;
private Button cameraMinusZ ;
private Button cameraPlusZ ;
private int cameraControlIncrement = 25;
private HBox cameraControlGroup;
private Vec3 cameraPosition;
private double cameraNearClip=.001;
private double cameraFarClip = 1000;
//---------------------------------------------------------------------------
// Model control 
//---------------------------------------------------------------------------
private SmartGroup model;
private String modelObjectPath;
private double modelScale=3;
 
private Quaternion quatTemp = new Quaternion(1,0,0,0);

@SuppressWarnings("unused")
private Translate translate;
private Rotate rotateX,rotateY;
private Quaternion ModelAttitude = new Quaternion(1,0,0,0);
//---------------------------------------------------------------------------
//  Head-up Display controls 
//---------------------------------------------------------------------------
private Label HUD_cameraPosition;
private Label HUD_modelPosition;
private Label HUD_modelAttitude;
private Label HUD_animationTime;

private Group HUD_Elements;
//---------------------------------------------------------------------------
// 
//---------------------------------------------------------------------------
private Group trajectory;
private int trajectoryRadius;
private double trajectoryScaleFactor;
private double DEFAULT_TRAJECTORY_SCALE_FACTOR = 0.1;
private AnimationFile animationFile;

@SuppressWarnings("exports")
public WorldView(String objectFilePath, Scene scene) {
	this.modelObjectPath = objectFilePath; 
}
	@Override
	public void start(@SuppressWarnings("exports") Stage stage) throws Exception{
		//---------------------------------------------------------------------------
		// Model 
		//---------------------------------------------------------------------------
		trajectoryScaleFactor = DEFAULT_TRAJECTORY_SCALE_FACTOR;
	    rootGroup = new Group();
		addModel();
		
		anchorPane = new AnchorPane();
		//---------------------------------------------------------------------------
		// Camera
		//---------------------------------------------------------------------------
	    camera = new PerspectiveCamera();
		camera.setNearClip(cameraNearClip);
		camera.setFarClip(cameraFarClip);	
		camera.setFieldOfView(DEFAULT_CAMERA_FOV);
		
		SmartGroup cameraGroup = new SmartGroup();
		cameraGroup.getChildren().add(camera);

		camera.getTransforms().addAll(
                rotateY = new Rotate(0, Rotate.Y_AXIS),
                rotateX = new Rotate(-35, Rotate.X_AXIS)
        );
		camera.setTranslateX(DEFAULT_CAMERA_POSITION.x);
		camera.setTranslateY(DEFAULT_CAMERA_POSITION.y);
		camera.setTranslateZ(DEFAULT_CAMERA_POSITION.z);
		
		cameraPosition = new Vec3();
		cameraPosition.x = DEFAULT_CAMERA_POSITION.x ;
		cameraPosition.y = DEFAULT_CAMERA_POSITION.y ;
		cameraPosition.z = DEFAULT_CAMERA_POSITION.z ;
		//---------------------------------------------------------------------------
		// Environment
		//---------------------------------------------------------------------------
		grid = Grid.createGrid(gridSize, GRID_RESOLUTION);
		
	    environment = new Group();
	    resetEnvironment();
		
		double translateGrid = 0;
		environment.setTranslateX(-translateGrid);
		
		rootGroup.getChildren().add(environment);
		rootGroup.getChildren().add(camera);
		
		//---------------------------------------------------------------------------
		// Scene
		//---------------------------------------------------------------------------
		sceneHeight = 700;
		sceneWidth = 1300;
	    scene = new SubScene(rootGroup, sceneHeight, sceneWidth, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.color(environmentBackgroundColor.x,
								  environmentBackgroundColor.y,
								  environmentBackgroundColor.z));
		scene.setCamera(camera);	
		
		//---------------------------------------------------------------------------
		// Mouse and Keyboard Control
		//---------------------------------------------------------------------------
		//initMouseControl(scene, camera);
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
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

                if (goNorth) dy -= cameraControlIncrement;
                if (goSouth) dy += cameraControlIncrement;
                if (goEast)  dx += cameraControlIncrement;
                if (goWest)  dx -= cameraControlIncrement;
                if (goForward) dz += cameraControlIncrement;
                if (goBackward) dz -= cameraControlIncrement;
                if (running) { dz = -dy ; dy = 0 ; }
                
                if (thirdPersonCamera) {
                	DEFAULT_RELATIVE_CAMERA_POSITION.x += dx;
                	DEFAULT_RELATIVE_CAMERA_POSITION.y += dy;
                	DEFAULT_RELATIVE_CAMERA_POSITION.z += dz;
                } else {
                	moveCameraBy(dx, dy, dz);
                }
            }
        };
        timer.start();
        
       anchorPane.getChildren().add(scene);
       
       //---------------------------------------------------------------------------
       // Head-on Display
       //---------------------------------------------------------------------------
	    HUD_cameraPosition = new Label("Camera position (x y z) [m]: ["+camera.getLayoutX()+"  "+camera.getLayoutY()+"  "+camera.getTranslateZ()+"]");
	    HUD_cameraPosition.setLayoutY(0);
	    HUD_modelPosition = new Label("Model position (x y z) [m]: ["+model.getTranslateY()+"  "+model.getTranslateY()+"  "+model.getTranslateZ()+"]");
	    HUD_modelPosition.setLayoutY(20);
	    HUD_modelAttitude = new Label("Model attitude (r p y) [deg]: ["+getModelAttitude().x+"  "+getModelAttitude().y+"  "+getModelAttitude().z+"]");
	    HUD_modelAttitude.setLayoutY(40);
	    HUD_animationTime = new Label("Time [s]: 0");
	    HUD_animationTime.setLayoutY(60);
	    
	    HUD_Elements = new Group();
        
		anchorPane.getChildren().add(HUD_Elements);
       //---------------------------------------------------------------------------
       // Camera control 
       //---------------------------------------------------------------------------
		
		 cameraMinusX = new Button("X-");
		 cameraPlusX = new Button("X+");
		
		 cameraMinusY = new Button("Y-");
		 cameraPlusY = new Button("Y+");
		
		 cameraMinusZ = new Button("Z-");
		 cameraPlusZ = new Button("Z+");
		 
		 int stdWidth = 40;
		 cameraMinusX.setMinSize(stdWidth, cameraMinusX.getHeight());
		 cameraPlusX.setMinSize(stdWidth, cameraPlusX.getHeight());
		 cameraMinusY.setMinSize(stdWidth, cameraMinusY.getHeight());
		 cameraPlusY.setMinSize(stdWidth, cameraPlusY.getHeight());
		 cameraMinusZ.setMinSize(stdWidth, cameraMinusZ.getHeight());
		 cameraPlusZ.setMinSize(stdWidth, cameraPlusZ.getHeight());
		 
 		String bstyle=String.format("-fx-background-color: transparent");
 		cameraMinusX.setStyle(bstyle);
		
		VBox cameraControlVertical1 = new VBox(4);
		VBox cameraControlVertical2 = new VBox(4);
		VBox cameraControlVertical3 = new VBox(5);
		
		cameraControlGroup = new HBox(3);
		
		cameraControlVertical1.getChildren().add(new EmptyButton(stdWidth));
		cameraControlVertical1.getChildren().add(new EmptyButton(stdWidth));
		cameraControlVertical1.getChildren().add(cameraMinusX);
		cameraControlVertical1.getChildren().add(new EmptyButton(stdWidth));
		
		cameraControlVertical2.getChildren().add(cameraMinusZ);
		cameraControlVertical2.getChildren().add(cameraMinusY);
		cameraControlVertical2.getChildren().add(cameraPlusY);
		cameraControlVertical2.getChildren().add(cameraPlusZ);
		
		cameraControlVertical3.getChildren().add(new EmptyButton(stdWidth));
		cameraControlVertical3.getChildren().add(new EmptyButton(stdWidth));
		cameraControlVertical3.getChildren().add(cameraPlusX);
		cameraControlVertical3.getChildren().add(new EmptyButton(stdWidth));
		
		cameraControlGroup.getChildren().add(cameraControlVertical1);
		cameraControlGroup.getChildren().add(cameraControlVertical2);
		cameraControlGroup.getChildren().add(cameraControlVertical3);
		
		cameraControlGroup.setLayoutY(650);
		cameraControlGroup.setLayoutX(40);
		
		
		cameraMinusX.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) {
	        	goWest = true;
	        } else {
	        	goWest = false;
	        }
	    });
		
		cameraPlusX.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) {
	        	goEast = true;
	        } else {
	        	goEast = false;
	        }
	    });
		
		cameraMinusY.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) {
	        	goNorth = true;
	        } else {
	        	goNorth = false;
	        }
	    });
		
		cameraPlusY.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) {
	        	goSouth = true;
	        } else {
	        	goSouth = false;
	        }
	    });
		
		cameraMinusZ.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) {
	        	goForward = true;
	        } else {
	        	goForward = false;
	        }
	    });
		
		cameraPlusZ.pressedProperty().addListener((observable, wasPressed, pressed) -> {
	        if (pressed) {
	        	goBackward = true;
	        } else {
	        	goBackward = false;
	        }
	    });
		
		
		trajectoryRadius = 15;
		trajectory = new Group();
		environment.getChildren().add(trajectory);
		animation = new Animation(this);
		animation.setTestAnimationFile();
		
	    addHudElements();
	}
		
    @SuppressWarnings("unused")
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

        // final double cx = camera.getBoundsInLocal().getWidth()  / 2;
        // final double cy = camera.getBoundsInLocal().getHeight() / 2;

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
        cameraPosition.x = x;
        cameraPosition.y = y;
        cameraPosition.z = z;
        updateHUD();
    }
    
    public void moveModelTo(double x, double y, double z) {
        model.setTranslateX(x);
        model.setTranslateY(y);
        model.setTranslateZ(z);
        updateHUD();
        if (thirdPersonCamera) {
            camera.setTranslateX(x+DEFAULT_RELATIVE_CAMERA_POSITION.x);
            camera.setTranslateY(y+DEFAULT_RELATIVE_CAMERA_POSITION.y);
            camera.setTranslateZ(z+DEFAULT_RELATIVE_CAMERA_POSITION.z);
        } else {
    		camera.setTranslateX(DEFAULT_CAMERA_POSITION.x);
    		camera.setTranslateY(DEFAULT_CAMERA_POSITION.y);
    		camera.setTranslateZ(DEFAULT_CAMERA_POSITION.z);
        }
    }
	
	@SuppressWarnings("exports")
	public void roateModelTo(Quaternion q) {
			Quaternion qInverse=new Quaternion();
			qInverse.w = quatTemp.w;
			qInverse.x = quatTemp.x;
			qInverse.y = quatTemp.y;
			qInverse.z = quatTemp.z;
			qInverse.inverse();
			
			Vector4 vec = matrixRotateNode(qInverse);
			Point3D rotAxis = new Point3D(vec.x, vec.y, vec.z);
			double  rotAngle =vec.w;
            model.setRotationAxis(rotAxis);
            model.setRotate(rotAngle);   

            vec = matrixRotateNode(q);
            if (vec != new Vector4(0,0,0,0)) {
				rotAxis = new Point3D(vec.x, vec.y, vec.z);
				rotAngle =vec.w;
	            model.setRotationAxis(rotAxis);
	            model.setRotate(rotAngle); 
	            
				quatTemp = q;
            }			
	}
	    
    @SuppressWarnings("exports")
	public Vector4 matrixRotateNode(Quaternion q ){
    	
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
	
	@SuppressWarnings("unused")
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
	

@SuppressWarnings("exports")
public SubScene getScene() {
		return scene;
}

private   SmartGroup loadModel(String fileString) {
    SmartGroup modelRoot = new SmartGroup();
	PhongMaterial material = new PhongMaterial();
    material.setDiffuseColor(Color.SILVER);
    ObjModelImporter importer = new ObjModelImporter();

    importer.read(fileString);

    for (MeshView view : importer.getImport()) {
        modelRoot.getChildren().add(view);
        view.setMaterial(material);
    }

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

public double getSceneHeight() {
	return sceneHeight;
}

public void setSceneHeight(double sceneHeight) {
	this.sceneHeight = sceneHeight;
    scene.setHeight(sceneHeight);
    anchorPane.setPrefHeight(sceneHeight);
}

public double getSceneWidth() {
	return sceneWidth;
}

@SuppressWarnings("exports")
public AnchorPane getAnchorPane() {
	return anchorPane;
}


public void setSceneWidth(double sceneWidth) {
	this.sceneWidth = sceneWidth;
    scene.setWidth(sceneWidth);
    anchorPane.setPrefWidth(sceneWidth);
}	

@SuppressWarnings("exports")
public Vec3 getCameraPosition() {
	Vec3 cameraPosition = new Vec3();
	cameraPosition.x = camera.getTranslateX();
	cameraPosition.y = camera.getTranslateY();
	cameraPosition.z = camera.getTranslateZ();
	return cameraPosition;
}

@SuppressWarnings("exports")
public Vec3 getModelPosition() {
	Vec3 modelPosition = new Vec3();
	modelPosition.x = model.getTranslateX();
	modelPosition.y = model.getTranslateY();
	modelPosition.z = model.getTranslateZ();
	return modelPosition;
}

private void updateHUD() {
	HUD_cameraPosition.setText("Camera position (x y z) [m]: ["+
				 Formats.decform01.format(camera.getTranslateX())+
			"  "+Formats.decform01.format(camera.getTranslateY())+
			"  "+Formats.decform01.format(camera.getTranslateZ())+"]");
	HUD_modelPosition.setText("Model position (x y z) [m]: ["+
			Formats.decform01.format(model.getTranslateX())+"  "+
			Formats.decform01.format(model.getTranslateY())+"  "+
			Formats.decform01.format(model.getTranslateZ())+"]");
}

public void setAnimationTime(double time) {
	HUD_animationTime.setText("Time [s]: "+Formats.decform01.format(time));
}


public void setThirdPersonCamera(boolean thirdPersonCamera) {
	this.thirdPersonCamera = thirdPersonCamera;
}


public boolean isThirdPersonCamera() {
	return thirdPersonCamera;
}

private Vec3 getModelAttitude() {
	return ModelAttitude.getRollPitchYaw();
}

public void setCameraFoV(double FoV) {
	camera.setFieldOfView(FoV);
}

@SuppressWarnings("exports")
public void updateModelAttitude(Quaternion q) {

	this.ModelAttitude = q;
	Vec3 rollPitchYaw = getModelAttitude();
	
	HUD_modelAttitude.setText("Model attitude (r p y) [deg]: ["+
			Formats.decform01.format(Math.toDegrees(rollPitchYaw.x))+"  "+
			Formats.decform01.format(Math.toDegrees(rollPitchYaw.y))+"  "+
			Formats.decform01.format(Math.toDegrees(rollPitchYaw.z))+"]");
}

public void setCameraToAbsoluteDefaultPosition() {
	camera.setTranslateX(DEFAULT_CAMERA_POSITION.x);
	camera.setTranslateY(DEFAULT_CAMERA_POSITION.y);
	camera.setTranslateZ(DEFAULT_CAMERA_POSITION.z);
	updateHUD();
}

public void setCameraToLastAbsolutePosition() {
	camera.setTranslateX(cameraPosition.x);
	camera.setTranslateY(cameraPosition.y);
	camera.setTranslateZ(cameraPosition.z);
	updateHUD();
}

public void setCameraToRelativeDefaultPostion() {
    camera.setTranslateX(getModelPosition().x+DEFAULT_RELATIVE_CAMERA_POSITION.x);
    camera.setTranslateY(getModelPosition().y+DEFAULT_RELATIVE_CAMERA_POSITION.y);
    camera.setTranslateZ(getModelPosition().z+DEFAULT_RELATIVE_CAMERA_POSITION.z);
    updateHUD();
}

public void selectGridView() {
	isGrid 		  = true;
	isFlatEarth   = false;
	isCurvedEarth = false;
	resetEnvironment();
}

public void selectFlatEarth() {
	isGrid 		  = false;
	isFlatEarth   = true;
	isCurvedEarth = false;
	resetEnvironment();
}

public void selectCurvedEarth() {
	isGrid 		  = false;
	isFlatEarth   = false;
	isCurvedEarth = true;
	resetEnvironment();
}

public void selectNoGround() {
	isGrid 		  = false;
	isFlatEarth   = false;
	isCurvedEarth = false;
	resetEnvironment();
}



public void setGridSize(float gridSize) {
	this.gridSize = gridSize;
	try {
		resetEnvironment();
	} catch (Exception exp ) {
		
	}
}

public float getGridSize() {
	return gridSize;
}

private void resetEnvironment() {
	// Remove all
	environment.getChildren().clear();
	// Add Content 
	if ( isGrid ) {
		grid = Grid.createGrid(gridSize, GRID_RESOLUTION);
		environment.getChildren().add(grid);	
	} 
	if ( isFlatEarth ) {
		Ground ground = new Ground(gridSize);
		environment.getChildren().add(ground.getGround());
	}
	if ( isCurvedEarth ) {
		
	}
}

public void maximize(@SuppressWarnings("exports") Stage stage) {
	setSceneWidth(stage.getWidth());
	setSceneHeight(stage.getHeight());
}

public void minimize(@SuppressWarnings("exports") Stage stage) {
	double newSceneWidth  = stage.getWidth() - 200;	
	setSceneWidth(newSceneWidth);
    double newSceneHeight = stage.getHeight() * 0.99;
    setSceneHeight(newSceneHeight);
}
public int getCameraControlIncrement() {
	return cameraControlIncrement;
}

public void setCameraControlIncrement(int cameraControlIncrement) {
	this.cameraControlIncrement = cameraControlIncrement;
}

public void createTrajectory() {
	try {
		for (int i=0;i<animationFile.getSequenceLength();i++) {
			Sphere sphere = new Sphere(trajectoryRadius);
			sphere.setTranslateX(animationFile.getSequence().get(i).position.x);
			sphere.setTranslateY(animationFile.getSequence().get(i).position.y);
			sphere.setTranslateZ(animationFile.getSequence().get(i).position.z);
			trajectory.getChildren().add(sphere);
		}
	} catch (NullPointerException exp ) {
		
	}
}

public void deleteTrajectory() {
	trajectory.getChildren().clear();
}

public void setAnimationFile(AnimationFile animationFile) {
	this.animationFile = animationFile;
	deleteTrajectory();
	createTrajectory();
}
public void setTrajectoryRadius(int trajectoryRadius) {
	this.trajectoryRadius = trajectoryRadius;
}

public int getTrajectoryRadius() {
	return trajectoryRadius;
}

public void addHudElements() {
	try {
	    HUD_Elements.getChildren().add(HUD_cameraPosition);
	    HUD_Elements.getChildren().add(HUD_modelPosition);
	    HUD_Elements.getChildren().add(HUD_modelAttitude);
	    HUD_Elements.getChildren().add(HUD_animationTime);
	    
	    HUD_Elements.getChildren().add(cameraControlGroup);
	} catch ( IllegalArgumentException exp ) {
		
	}
}

public void showHudElements() {
	anchorPane.getChildren().add(HUD_Elements);
}

public void hideHudElements() {
	anchorPane.getChildren().remove(HUD_Elements);
}

public void deleteHudElements() {
		HUD_Elements.getChildren().clear();
}

public double getCamerFoV() {
	return camera.getFieldOfView();
}

public void removeModel() {
	rootGroup.getChildren().remove(model);
}

public void addModel() {
	try {
		model =  loadModel(modelObjectPath);
	} catch (Exception e) {
		model = new SmartGroup();
		System.out.println(e.getMessage());
	}
	// Set init model position
	model.setTranslateX(200);
	model.setTranslateY(0);
	model.setTranslateZ(-300);
	rootGroup.getChildren().add(model);
}

public Animation getAnimation() {
	return animation;
}

public void setAnimation(Animation animation) {
	this.animation = animation;
}

public void setModelScale(double scaleFactor) {
	model.setScaleX(modelScale);
	model.setScaleY(modelScale);
	model.setScaleZ(modelScale);
	this.modelScale = scaleFactor;
}

public double getModelScale() {
	return modelScale;
}
public double getTrajectoryScaleFactor() {
	return trajectoryScaleFactor;
}
public void setTrajectoryScaleFactor(double trajectoryScaleFactor) {
	double scaleCorrect = trajectoryScaleFactor / this.trajectoryScaleFactor;
	for(int i=0;i<animation.getAnimationFile().getSequenceLength();i++) {		
		animation.getAnimationFile().getSequence().get(i).position.x = scaleCorrect * animation.getAnimationFile().getSequence().get(i).position.x;
		animation.getAnimationFile().getSequence().get(i).position.y = scaleCorrect * animation.getAnimationFile().getSequence().get(i).position.y;
		animation.getAnimationFile().getSequence().get(i).position.z = scaleCorrect * animation.getAnimationFile().getSequence().get(i).position.z;
	}
	this.trajectoryScaleFactor = trajectoryScaleFactor;
	deleteTrajectory();
	createTrajectory();
}
public double getCameraNearClip() {
	return cameraNearClip;
}
public void setCameraNearClip(double cameraNearClip) {
	camera.setNearClip(cameraNearClip);
	this.cameraNearClip = cameraNearClip;
}
public double getCameraFarClip() {
	return cameraFarClip;
}
public void setCameraFarClip(double cameraFarClip) {
	camera.setFarClip(cameraFarClip);
	this.cameraFarClip = cameraFarClip;
}

public void setEnvironmentBackgroundColor(double r, double g, double b) {
	scene.setFill(Color.color(r,g,b));
	environmentBackgroundColor.x = r;
	environmentBackgroundColor.y = g;
	environmentBackgroundColor.z = b;
}

@SuppressWarnings("exports")
public Vec3 getEnvironmentBackgroundColor() {
	return environmentBackgroundColor;
}


}
