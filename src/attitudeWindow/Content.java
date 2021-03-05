package attitudeWindow;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.PathTransition.OrientationType;
import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

final class Content {

    private static final Duration DURATION = Duration.seconds(4);
    private static final Color COLOR = Color.WHITE;
    private static final double WIDTH = 1;
    
    final Group group = new Group();
    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, Rotate.Z_AXIS);
    
    //private final Box xAxis;
    //private final Box yAxis;
    //private final Box zAxis;
    private  Shape circle;
    private  Shape arrow;
    
    private double axisLength = 100;
    
     Animation animation;
    
    public Content() {

    }
        

   // public  Content create(double size) {
        //Content c = new Content(size);
        //c.group.getChildren().addAll(c.arrow, c.circle,
        //    c.xAxis, c.yAxis, c.zAxis);
      //  Group NED = createNED();
        //c.group.getChildren().addAll(NED);
        //c.group.getTransforms().addAll(c.rz, c.ry, c.rx);
        //c.group.setTranslateX(-size );
        //c.group.setTranslateY(-size );
        //c.group.setTranslateZ(size );
        //c.rx.setAngle(35);
        //c.ry.setAngle(-45);
        //return c;
   // }
	/*
    private Content(double size) {

        xAxis = createBox(axisLength, WIDTH, WIDTH);
        xAxis.setTranslateX(axisLength/2);
        yAxis = createBox(WIDTH, axisLength, WIDTH);
        yAxis.setTranslateY(axisLength/2);
        zAxis = createBox(WIDTH, WIDTH, axisLength);
        zAxis.setTranslateZ(axisLength/2);

        circle = createCircle(2*size);
        arrow = createShape();
        arrow.setTranslateX(axisLength);
        animation = new ParallelTransition(
            createTransition(circle, arrow),
            createTimeline(axisLength / 2));
    }
            */
    Group createNED() {
        Box xAxis = createBox(axisLength, WIDTH, WIDTH);
        xAxis.setTranslateX(axisLength/2);
        Box yAxis = createBox(WIDTH, axisLength, WIDTH);
        yAxis.setTranslateY(axisLength/2);
        Box zAxis = createBox(WIDTH, WIDTH, axisLength);
        zAxis.setTranslateZ(axisLength/2);
        Shape arrow = createShape();
        arrow.setTranslateX(axisLength);
        
        Group NED = new Group();
        NED.getChildren().add(xAxis);
        NED.getChildren().add(yAxis);
        NED.getChildren().add(zAxis);
        return NED;
    }

    private Circle createCircle(double size) {
        Circle c = new Circle(size / 4);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(COLOR);
        return c;
    }

    private Box createBox(double w, double h, double d) {
        Box b = new Box(w, h, d);
        b.setMaterial(new PhongMaterial(COLOR));
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

    private Transition createTransition(Shape path, Shape node) {
        PathTransition t = new PathTransition(DURATION, path, node);
        t.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
        t.setCycleCount(Timeline.INDEFINITE);
        t.setInterpolator(Interpolator.LINEAR);
        return t;
    }

    private Timeline createTimeline(double size) {
        Timeline t = new Timeline();
        t.setCycleCount(Timeline.INDEFINITE);
        t.setAutoReverse(true);
        KeyValue keyX = new KeyValue(group.translateXProperty(), size);
        KeyValue keyY = new KeyValue(group.translateYProperty(), size);
        KeyValue keyZ = new KeyValue(group.translateZProperty(), -size);
        KeyFrame keyFrame = new KeyFrame(DURATION.divide(2), keyX, keyY, keyZ);
        t.getKeyFrames().add(keyFrame);
        return t;
    }
    
    	
}
