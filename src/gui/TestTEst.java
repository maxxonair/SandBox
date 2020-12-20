package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;

public class TestTEst extends Application {
	
    private static float minX;
    private static float minY;
    private static float maxX;
    private static float maxY;
    
    private static final int VIEWPORT_SIZE = 800;
    
    private static final String texturePath = "file:" + System.getProperty("user.dir") + "/resources/MoonHM-LOLA-Tiles-16.png";
    
    public void start(Stage stage) {
    	
    	Group group  = new Group();
    	System.out.println(texturePath);
    	
    	Image image = new Image(texturePath);
    	
        TriangleMesh heightMap = createHeightMap(image, 10, 50, 100);
        final MeshView meshView = new MeshView(
        		heightMap
        );
        
        meshView.setTranslateZ(-500);
        
        group.getChildren().add(meshView);
        
        Scene scene = new Scene(group, VIEWPORT_SIZE, VIEWPORT_SIZE * 9.0/16, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.rgb(40, 40, 40));
        scene.setCamera(new PerspectiveCamera());
        
        stage.setScene(scene);
        stage.show();
        
    }
    
    public static void main(String[] args) {
    	launch(args);
    }

    public static TriangleMesh createHeightMap(Image image, int pskip,
            float maxH, float scale) {
        minX = -(float) image.getWidth() / 2;
        maxX = (float) image.getWidth() / 2;

        minY = -(float) image.getHeight() / 2;
        maxY = (float) image.getHeight() / 2;

        int subDivX = (int) image.getWidth() / pskip;
        int subDivY = (int) image.getHeight() / pskip;

        final int pointSize = 3;
        final int texCoordSize = 2;
        // 3 point indices and 3 texCoord indices per triangle
        final int faceSize = 6;
        int numDivX = subDivX + 1;
        int numVerts = (subDivY + 1) * numDivX;
        float points[] = new float[numVerts * pointSize];
        float texCoords[] = new float[numVerts * texCoordSize];
        int faceCount = subDivX * subDivY * 2;
        int faces[] = new int[faceCount * faceSize];

        // Create points and texCoords
        for (int y = 0; y <= subDivY; y++) {
            float currY = (float) y / subDivY;
            double fy = (1 - currY) * minY + currY * maxY;

            for (int x = 0; x <= subDivX; x++) {
                float currX = (float) x / subDivX;
                double fx = (1 - currX) * minX + currX * maxX;

                int index = y * numDivX * pointSize + (x * pointSize);
                points[index] = (float) fx * scale; // x
                points[index + 1] = (float) fy * scale; // y
                // color value for pixel at point
                int rgb = ((int) image.getPixelReader().getArgb(x * pskip,
                        y * pskip)); // a bug in here somewhere for aarray OB exception
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                points[index + 2] = -((float) ((r + g + b) / 3) / 255)
                        * maxH; // z 

                index = y * numDivX * texCoordSize + (x * texCoordSize);
                texCoords[index] = currX;
                texCoords[index + 1] = currY;
            }//from www. j a  v a 2  s.com
        }

        // Create faces
        for (int y = 0; y < subDivY; y++) {
            for (int x = 0; x < subDivX; x++) {
                int p00 = y * numDivX + x;
                int p01 = p00 + 1;
                int p10 = p00 + numDivX;
                int p11 = p10 + 1;
                int tc00 = y * numDivX + x;
                int tc01 = tc00 + 1;
                int tc10 = tc00 + numDivX;
                int tc11 = tc10 + 1;

                int index = (y * subDivX * faceSize + (x * faceSize)) * 2;
                faces[index + 0] = p00;
                faces[index + 1] = tc00;
                faces[index + 2] = p10;
                faces[index + 3] = tc10;
                faces[index + 4] = p11;
                faces[index + 5] = tc11;

                index += faceSize;
                faces[index + 0] = p11;
                faces[index + 1] = tc11;
                faces[index + 2] = p01;
                faces[index + 3] = tc01;
                faces[index + 4] = p00;
                faces[index + 5] = tc00;
            }
        }
        //to do
        //int smoothingGroups[] = new int[faces.length / faceSize];

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);
        //mesh.getFaceSmoothingGroups().addAll(smoothingGroups);

        return mesh;
    }
}