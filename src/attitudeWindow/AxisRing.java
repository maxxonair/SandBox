package attitudeWindow;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class AxisRing {

    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, Rotate.Z_AXIS);
    
    private Group ring ;
    private int axis;
    private Color color;
    
    
	public AxisRing(double diameter, int axis , @SuppressWarnings("exports") Color color) {
		this.axis = axis;
		this.color = color;
		ring = createRing(diameter, axis);
	}
	
	private Group createRing(double diameter, int axis) {
		Group Ring = new Group();
        Shape circle = createCircle(diameter);
        Rotate ringRot = new Rotate(0, Rotate.Y_AXIS);
        Rotate ringRot2 = new Rotate(0, Rotate.X_AXIS);
        circle.getTransforms().addAll(ringRot);
        circle.getTransforms().addAll(ringRot2);
        
        Ring.getChildren().add(circle);
		
		Ring.getTransforms().addAll(rz, ry, rx);
		
        if ( axis == 2 ) {
        	ringRot.setAngle(90);
        } else if ( axis == 3 ){
        	ringRot2.setAngle(180);
        }
		return Ring;
	}
	
    private Circle createCircle(double diameter) {
        Circle c = new Circle(diameter);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(color);
        return c;
    }
	
	public Group getRing() {
		return ring;
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
