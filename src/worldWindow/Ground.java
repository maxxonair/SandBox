package worldWindow;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Ground {

	
	  private float meshSize;

	  private static final int VIEWPORT_SIZE = 800;

	  private static final double MODEL_SCALE_FACTOR = 1;
	  private static final double MODEL_X_OFFSET = 0;
	  private static final double MODEL_Y_OFFSET = 0;
	  private static final double MODEL_Z_OFFSET = 0;
	  
	  private static final String texturePath = "file:" + System.getProperty("user.dir") + "/resources/marsSurface.jpg";
	  
	  private Image texture;
	  private PhongMaterial texturedMaterial = new PhongMaterial();
	  
	  private Group ground;
	  private MeshView meshView;
	  
	  public Ground(float meshSize) {
		  	this.meshSize = meshSize;
		    texture = new Image(texturePath);
		    texturedMaterial.setDiffuseMap(texture);

		    meshView = loadMeshView();	    
		    ground = buildScene(meshView);
		    setTexture();
		    
		    // Rotate ground to horizontal
		    ground.setRotationAxis(new Point3D(1,0,0));
		    ground.setRotate(-90);
	  }
	  
	  public void setTexture() {
		  meshView.setMaterial(texturedMaterial);
	  }
	  
	  public void removeTexture() {
		  meshView.setMaterial((PhongMaterial) null);
	  }
	  
	  private MeshView loadMeshView() {
		    float[] points = {
		        -meshSize,  meshSize, 0,
		        -meshSize, -meshSize, 0,
		         meshSize,  meshSize, 0,
		         meshSize, -meshSize, 0
		    };
		    float[] texCoords = {
		        1, 1,
		        1, 0,
		        0, 1,
		        0, 0
		    };
		    int[] faces = {
		        2, 2, 1, 1, 0, 0,
		        2, 2, 3, 3, 1, 1
		    };

		    TriangleMesh mesh = new TriangleMesh();
		    mesh.getPoints().setAll(points);
		    mesh.getTexCoords().setAll(texCoords);
		    mesh.getFaces().setAll(faces);

		    return new MeshView(mesh);
		  }
	  
	  private Group buildScene(MeshView meshView) {
		    meshView.setTranslateX(VIEWPORT_SIZE / 2 + MODEL_X_OFFSET);
		    meshView.setTranslateY(VIEWPORT_SIZE / 2 * 9.0 / 16 + MODEL_Y_OFFSET);
		    meshView.setTranslateZ(VIEWPORT_SIZE / 2 + MODEL_Z_OFFSET);
		    meshView.setScaleX(MODEL_SCALE_FACTOR);
		    meshView.setScaleY(MODEL_SCALE_FACTOR);
		    meshView.setScaleZ(MODEL_SCALE_FACTOR);

		    return new Group(meshView);
		  }

	@SuppressWarnings("exports")
	public Group getGround() {
		return ground;
	}
	  
	  
}
