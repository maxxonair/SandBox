package attitudeWindow;

import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;

public class CartesianFrame {

    private static final double WIDTH = 1;
    private static final Color COLOR = Color.WHITE;
    private Group frame ;
    
    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, Rotate.Z_AXIS);
	
	public CartesianFrame(double axisLength, int colorSetting) {
		frame = createFrame(axisLength, colorSetting);
	}
	
    Group createFrame(double axisLength, int colorSetting) {
        Box xAxis = new Box();
        Box yAxis = new Box();
        Box zAxis = new Box();
        if(colorSetting == 1) {
        	xAxis = createBox(axisLength, WIDTH, WIDTH, Color.BLUE);
        	yAxis = createBox(WIDTH, axisLength, WIDTH, Color.GREEN);
        	zAxis = createBox(WIDTH, WIDTH, axisLength, Color.RED);
        } else {
        	xAxis = createBox(axisLength, WIDTH, WIDTH, COLOR);
        	yAxis = createBox(WIDTH, axisLength, WIDTH, COLOR);
        	zAxis = createBox(WIDTH, WIDTH, axisLength, COLOR);
        }
        xAxis.setTranslateX(axisLength/2);
        yAxis.setTranslateY(axisLength/2);
        zAxis.setTranslateZ(axisLength/2);
        Shape arrow = createShape();
        arrow.setTranslateX(axisLength);
      
        
        Group NED = new Group();
        NED.getChildren().add(xAxis);
        NED.getChildren().add(yAxis);
        NED.getChildren().add(zAxis);
        
        NED.getTransforms().addAll(rz, ry, rx);
        return NED;
    }
    
    private Box createBox(double w, double h, double d, Color color) {
        Box b = new Box(w, h, d);
        b.setMaterial(new PhongMaterial(color));
        return b;
    }

    private Shape createShape() {
        Shape s = new Polygon(0, 0, -10, -10, 10, 0, -10, 10);
        s.setStrokeWidth(WIDTH);
        s.setStrokeLineCap(StrokeLineCap.ROUND);
        s.setStroke(COLOR);
        s.setEffect(new Bloom());
        return s;
    }

	public Group getFrame() {
		return frame;
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
