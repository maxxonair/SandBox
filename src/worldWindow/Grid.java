package worldWindow;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Grid {

	@SuppressWarnings("exports")
	public static Group createGrid(float size, float delta) {
	    if (delta < 1) {
	        delta = 1;
	    }
	    final PolygonMesh plane = createQuadrilateralMesh(size, size, (int) (size / delta), (int) (size / delta));

	    final PolygonMesh plane2 = createQuadrilateralMesh(size, size, (int) (size / delta / 5), (int) (size / delta / 5));

	    PolygonMeshView meshViewXY = new PolygonMeshView(plane);
	    meshViewXY.setDrawMode(DrawMode.LINE);
	    meshViewXY.setCullFace(CullFace.NONE);

	    PolygonMeshView meshViewXZ = new PolygonMeshView(plane);
	    meshViewXZ.setDrawMode(DrawMode.LINE);
	    meshViewXZ.setCullFace(CullFace.NONE);
	    meshViewXZ.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
	    
		PhongMaterial material = new PhongMaterial();
	    material.setDiffuseColor(Color.BLACK);
	    meshViewXZ.setMaterial(material);

	    PolygonMeshView meshViewYZ = new PolygonMeshView(plane);
	    meshViewYZ.setDrawMode(DrawMode.LINE);
	    meshViewYZ.setCullFace(CullFace.NONE);
	    meshViewYZ.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

	    PolygonMeshView meshViewXY2 = new PolygonMeshView(plane2);
	    meshViewXY2.setDrawMode(DrawMode.LINE);
	    meshViewXY2.setCullFace(CullFace.NONE);
	    meshViewXY2.getTransforms().add(new Translate(size / 1000f, size / 1000f, 0));

	    PolygonMeshView meshViewXZ2 = new PolygonMeshView(plane2);
	    meshViewXZ2.setDrawMode(DrawMode.LINE);
	    meshViewXZ2.setCullFace(CullFace.NONE);
	    meshViewXZ2.getTransforms().add(new Translate(size / 1000f, size / 1000f, 0));
	    meshViewXZ2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
	    meshViewXZ2.setMaterial(material);

	    PolygonMeshView meshViewYZ2 = new PolygonMeshView(plane2);
	    meshViewYZ2.setDrawMode(DrawMode.LINE);
	    meshViewYZ2.setCullFace(CullFace.NONE);
	    meshViewYZ2.getTransforms().add(new Translate(size / 1000f, size / 1000f, 0));
	    meshViewYZ2.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));

	   // return new Group(meshViewXY, meshViewXY2, meshViewXZ, meshViewXZ2 /*, meshViewYZ, meshViewYZ2 */);
	    return new Group( meshViewXZ/*, meshViewXZ2 , meshViewYZ, meshViewYZ2 */);
	}
	
	private static   PolygonMesh createQuadrilateralMesh(float width, float height, int subDivX, int subDivY) {
	    final float minX = - width / 2f;
	    final float minY = - height / 2f;
	    final float maxX =   width / 2f;
	    final float maxY =   height / 2f;

	    final int pointSize = 3;
	    final int texCoordSize = 2;
	    // 4 point indices and 4 texCoord indices per face
	    final int faceSize = 8;
	    int numDivX = subDivX + 1;
	    int numVerts = (subDivY + 1) * numDivX;
	    float points[] = new float[numVerts * pointSize];
	    float texCoords[] = new float[numVerts * texCoordSize];
	    int faceCount = subDivX * subDivY;
	    int faces[][] = new int[faceCount][faceSize];

	    // Create points and texCoords
	    for (int y = 0; y <= subDivY; y++) {
	        float dy = (float) y / subDivY;
	        double fy = (1 - dy) * minY + dy * maxY;

	        for (int x = 0; x <= subDivX; x++) {
	            float dx = (float) x / subDivX;
	            double fx = (1 - dx) * minX + dx * maxX;

	            int index = y * numDivX * pointSize + (x * pointSize);
	            points[index] = (float) fx;
	            points[index + 1] = (float) fy;
	            points[index + 2] = 0.0f;

	            index = y * numDivX * texCoordSize + (x * texCoordSize);
	            texCoords[index] = dx;
	            texCoords[index + 1] = dy;
	        }
	    }
	    
	    

	    // Create faces
	    int index = 0;
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

	            faces[index][0] = p00;
	            faces[index][1] = tc00;
	            faces[index][2] = p10;
	            faces[index][3] = tc10;
	            faces[index][4] = p11;
	            faces[index][5] = tc11;
	            faces[index][6] = p01;
	            faces[index++][7] = tc01;
	        }
	    }

	    int[] smooth = new int[faceCount];

	    PolygonMesh mesh = new PolygonMesh(points, texCoords, faces);
	    mesh.getFaceSmoothingGroups().addAll(smooth);
	    return mesh;
	}
}
