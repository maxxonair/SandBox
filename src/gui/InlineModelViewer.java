package gui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InlineModelViewer extends Application {
	
  private float MESH_SIZE = 8;
  
  private static float minX;
  private static float minY;
  private static float maxX;
  private static float maxY;

  private static final int VIEWPORT_SIZE = 800;

  private static final double MODEL_SCALE_FACTOR = 40;
  private static final double MODEL_X_OFFSET = 0;
  private static final double MODEL_Y_OFFSET = 0;
  private static final double MODEL_Z_OFFSET = 10000;//VIEWPORT_SIZE / 2;

  private static final String textureLoc = "https://www.sketchuptextureclub.com/public/texture_f/slab-marble-emperador-cream-light-preview.jpg";
  
  private static final String texturePath = "file:" + System.getProperty("user.dir") + "/resources/MoonHM-LOLA-Tiles-16.png";
  
  private Image texture;
  private PhongMaterial texturedMaterial = new PhongMaterial();

  TriangleMesh heightMap = createHeightMap(new Image(texturePath), 100, MESH_SIZE, 10);
  //private MeshView meshView = loadMeshView();
  private MeshView meshView = new MeshView(heightMap);


  private MeshView loadMeshView() {
    float[] points = {
        -MESH_SIZE,  MESH_SIZE, 0,
        -MESH_SIZE, -MESH_SIZE, 0,
         MESH_SIZE,  MESH_SIZE, 0,
         MESH_SIZE, -MESH_SIZE, 0
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

  private Group buildScene() {
    meshView.setTranslateX(VIEWPORT_SIZE / 2 + MODEL_X_OFFSET);
    meshView.setTranslateY(VIEWPORT_SIZE / 2 * 9.0 / 16 + MODEL_Y_OFFSET);
    meshView.setTranslateZ(VIEWPORT_SIZE / 2 + MODEL_Z_OFFSET);
    meshView.setScaleX(MODEL_SCALE_FACTOR);
    meshView.setScaleY(MODEL_SCALE_FACTOR);
    meshView.setScaleZ(MODEL_SCALE_FACTOR);

    return new Group(meshView);
  }

  @Override
  public void start(Stage stage) {
    texture = new Image(textureLoc);
    texturedMaterial.setDiffuseMap(texture);

    Group group = buildScene();

    RotateTransition rotate = rotate3dGroup(group);

    VBox layout = new VBox(
        createControls(rotate),
        createScene3D(group)
    );

    stage.setTitle("Model Viewer");

    Scene scene = new Scene(layout, Color.CORNSILK);
    stage.setScene(scene);
    stage.show();
  }

  private SubScene createScene3D(Group group) {
    SubScene scene3d = new SubScene(group, VIEWPORT_SIZE, VIEWPORT_SIZE * 9.0/16, true, SceneAntialiasing.BALANCED);
    scene3d.setFill(Color.rgb(10, 10, 40));
    scene3d.setCamera(new PerspectiveCamera());
    return scene3d;
  }

  private VBox createControls(RotateTransition rotateTransition) {
    CheckBox cull      = new CheckBox("Cull Back");
    meshView.cullFaceProperty().bind(
        Bindings.when(
            cull.selectedProperty())
              .then(CullFace.BACK)
              .otherwise(CullFace.NONE)
    );
    CheckBox wireframe = new CheckBox("Wireframe");
    meshView.drawModeProperty().bind(
        Bindings.when(
            wireframe.selectedProperty())
              .then(DrawMode.LINE)
              .otherwise(DrawMode.FILL)
    );

    CheckBox rotate = new CheckBox("Rotate");
    rotate.selectedProperty().addListener(observable -> {
      if (rotate.isSelected()) {
        rotateTransition.play();
      } else {
        rotateTransition.pause();
      }
    });

    CheckBox texture = new CheckBox("Texture");
    meshView.materialProperty().bind(
        Bindings.when(
            texture.selectedProperty())
              .then(texturedMaterial)
              .otherwise((PhongMaterial) null)
    );

    VBox controls = new VBox(10, rotate, texture, cull, wireframe);
    controls.setPadding(new Insets(10));
    return controls;
  }

  private RotateTransition rotate3dGroup(Group group) {
    RotateTransition rotate = new RotateTransition(Duration.seconds(10), group);
    rotate.setAxis(Rotate.Y_AXIS);
    rotate.setFromAngle(0);
    rotate.setToAngle(360);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setCycleCount(RotateTransition.INDEFINITE);

    return rotate;
  }

  public static void main(String[] args) {
    System.setProperty("prism.dirtyopts", "false");
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
