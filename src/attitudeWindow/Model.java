package attitudeWindow;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import worldWindow.SmartGroup;

public class Model {
	
	private Group model;
    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, Rotate.Z_AXIS);

	public Model(String fileString, double modelScale) {
		this.model = loadModel( fileString,  modelScale);
		model.getTransforms().addAll(rz, ry, rx);
	}
	
    private   SmartGroup loadModel(String fileString, double modelScale) {
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
    
	public Group getModel() {
		return model;
	}

	public Rotate getRx() {
		return rx;
	}

	public Rotate getRy() {
		return ry;
	}

	public Rotate getRz() {
		return rz;
	}
}
