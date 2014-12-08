package cs4620.ray2.surface;

import java.util.ArrayList;
import java.util.Iterator;

import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Ray;
import egl.math.Matrix4d;
import egl.math.Vector3d;

public class Group extends Surface {

  /** List of objects under this group. */
  ArrayList<Surface> objs = new ArrayList<Surface>();
  
  /** The transformation matrix associated with this group. */
  private Matrix4d transformMat;
  
  /** A shared temporary matrix */
  static Matrix4d tmp = new Matrix4d();
  
  public Group() {
    transformMat = new Matrix4d();
    transformMat.setIdentity();
  }
  
  /**
   * Compute tMat, tMatInv, tMatTInv for this group and propagate values to the children under it.
   * @param pMat The transformation matrix of the parent for this node.
   * @param pMatInv The inverse of pMat.
   * @param pMatTInv The inverse of the transpose of pMat.
   */
  public void setTransformation(Matrix4d pMat, Matrix4d pMatInv, Matrix4d pMatTInv) {
    // TODO#A7: Compute tMat, tMatInv, tMatTInv using transformMat.
    // Hint: We apply the transformation from bottom up the tree. 
    // i.e. The child's transformation will be applied to objects before its parent's.
	  
	  
	  /*if(ro.parent != null) {
			ro.mWorldTransform.set(ro.sceneObject.transformation).mulAfter(ro.parent.mWorldTransform);
			ro.mWorldTransformIT.set(ro.mWorldTransform.getAxes()).invert().transpose();
		}*/
	  System.out.println("In set transformation method");
	    tMat = transformMat.clone().mulAfter(pMat);
	    System.out.println(tMat);
	    tMatInv = tMat.clone().invert();
	    tMatTInv = tMat.clone().transpose().invert();
	   // tMatTInv = tMat.clone().transpose().invert();
	 //   tMatInv = tMat.clone().invert();
	    /*pMat.mulBefore(transformMat, updatedTMat);
	    Matrix4d updatedTMatInv = updatedTMat.clone().invert();
	    Matrix4d updatedTMatTInv = updatedTMatInv.clone().transpose();
	    tMat = updatedTMat;
	    tMatInv = updatedTMatInv;
	    tMatTInv = updatedTMatTInv;*/
    // TODO#A7: Call setTransformation(tMat, tMatInv, tMatTInv) on each of the children.
	  for(Surface a : this.objs) {
		  a.setTransformation(tMat, tMatInv, tMatTInv);
	  }
	
	  computeBoundingBox();
  }
  
  
  public void setTranslate(Vector3d T) {
	Matrix4d.createTranslation(T, tmp);
    transformMat.mulAfter(tmp);
  }
  
  public void setRotate(Vector3d R) {
    Matrix4d.createRotationX(R.x * Math.PI/180, tmp);
    transformMat.mulAfter(tmp);
    Matrix4d.createRotationY(R.y * Math.PI/180, tmp);
    transformMat.mulAfter(tmp);
    Matrix4d.createRotationZ(R.z * Math.PI/180, tmp);
    transformMat.mulAfter(tmp);
  }
  
  public void setScale(Vector3d S) { 
	Matrix4d.createScale(S, tmp);
    transformMat.mulAfter(tmp);
  }
  
  public void addSurface(Surface a) {
    objs.add(a);
  }
  
  public boolean intersect(IntersectionRecord outRecord, Ray ray) { return false; }
  public void computeBoundingBox() {  }

  public void appendRenderableSurfaces (ArrayList<Surface> in) {
    for (Iterator<Surface> iter = objs.iterator(); iter.hasNext();)
      iter.next().appendRenderableSurfaces(in);
  }
}