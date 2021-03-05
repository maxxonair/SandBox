package attitudeWindow;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import utils.Quaternion;
import utils.Vector4;

public class Rotate {

	private Group content;
	private Quaternion quatTemp = new Quaternion(1,0,0,0);
	
public Rotate(@SuppressWarnings("exports") Group content){
	this.content = content;
}	

	@SuppressWarnings("exports")
	public void rotateModelTo(Quaternion q) {
			Quaternion qInverse=new Quaternion();
			qInverse.w = quatTemp.w;
			qInverse.x = quatTemp.x;
			qInverse.y = quatTemp.y;
			qInverse.z = quatTemp.z;
			qInverse.inverse();
			
			Vector4 vec = matrixRotateNode(qInverse);
			Point3D rotAxis = new Point3D(vec.x, vec.y, vec.z);
			double  rotAngle =vec.w;
			content.setRotationAxis(rotAxis);
			content.setRotate(rotAngle);   

            vec = matrixRotateNode(q);
            if (vec != new Vector4(0,0,0,0)) {
				rotAxis = new Point3D(vec.x, vec.y, vec.z);
				rotAngle =vec.w;
				content.setRotationAxis(rotAxis);
				content.setRotate(rotAngle); 
	            
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
}
