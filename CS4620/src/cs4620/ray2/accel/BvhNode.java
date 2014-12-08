package cs4620.ray2.accel;

import cs4620.ray2.Ray;
import egl.math.Vector3d;

/**
 * A class representing a node in a bounding volume hierarchy.
 * 
 * @author pramook 
 */
public class BvhNode {

	/** The current bounding box for this tree node.
	 *  The bounding box is described by 
	 *  (minPt.x, minPt.y, minPt.z) - (maxBound.x, maxBound.y, maxBound.z).
	 */
	public final Vector3d minBound, maxBound;
	
	/**
	 * The array of children.
	 * child[0] is the left child.
	 * child[1] is the right child.
	 */
	public final BvhNode child[];

	/**
	 * The index of the first surface under this node. 
	 */
	public int surfaceIndexStart;
	
	/**
	 * The index of the surface next to the last surface under this node.	 
	 */
	public int surfaceIndexEnd; 
	
	/**
	 * Default constructor
	 */
	public BvhNode()
	{
		minBound = new Vector3d();
		maxBound = new Vector3d();
		child = new BvhNode[2];
		child[0] = null;
		child[1] = null;		
		surfaceIndexStart = -1;
		surfaceIndexEnd = -1;
	}
	
	/**
	 * Constructor where the user can specify the fields.
	 * @param minBound
	 * @param maxBound
	 * @param leftChild
	 * @param rightChild
	 * @param start
	 * @param end
	 */
	public BvhNode(Vector3d minBound, Vector3d maxBound, BvhNode leftChild, BvhNode rightChild, int start, int end) 
	{
		this.minBound = new Vector3d();
		this.minBound.set(minBound);
		this.maxBound = new Vector3d();
		this.maxBound.set(maxBound);
		this.child = new BvhNode[2];
		this.child[0] = leftChild;
		this.child[1] = rightChild;		   
		this.surfaceIndexStart = start;
		this.surfaceIndexEnd = end;
	}
	
	/**
	 * @return true if this node is a leaf node
	 */
	public boolean isLeaf()
	{
		return child[0] == null && child[1] == null; 
	}
	
	public boolean intersectsSlab(double xMin, double xMax, double xE, double xD, double yMin, double yMax,  double yE, double yD, double zMin, double zMax, double zE, double zD) {
		double tXMin, tXMax, tYMin, tYMax, tZMin, tZMax;
		if (xD >= 0) {
			tXMin = (xMin - xE)/xD;
			tXMax = (xMax - xE)/xD;
		}
		else {
			tXMin = (xMax - xE)/xD;
			tXMax = (xMin - xE)/xD;
		}
		if (yD >= 0) {
			tYMin = (yMin - yE)/yD;
			tYMax = (yMax - yE)/yD;
		}
		else {
			tYMin = (yMax - yE)/yD;
			tYMax = (yMin - yE)/yD;
		}
		if (tXMin > tYMax || tYMin > tXMax) {
			return false;
		}
		if (tYMin > tXMin) {
			tXMin = tYMin;
		}
		if (tYMax < tXMax) {
			tXMax = tYMax;
		}
		if (zD >= 0) {
			tZMin = (zMin - zE)/zD;
			tZMax = (zMax - zE)/zD;
		}
		else {
			tZMin = (zMax - zE)/zD;
			tZMax = (zMin - zE)/zD;
		}
		if (tXMin > tZMax || tZMin > tXMax) {
			return false;
		}
		return true;
	}
	
	/** 
	 * Check if the ray intersects the bounding box.
	 * @param ray
	 * @return true if ray intersects the bounding box
	 */
	public boolean intersects(Ray ray) {
		
		double xMin = minBound.x;
		double yMin = minBound.y;
		double zMin = minBound.z;
		double xMax = maxBound.x;
		double yMax = maxBound.y;
		double zMax = maxBound.z;
		double xE = ray.origin.x;
		double yE = ray.origin.y;
		double zE = ray.origin.z;
		double xD = ray.direction.x;
		double yD = ray.direction.y;
		double zD = ray.direction.z;
		return intersectsSlab(xMin, xMax, xE, xD, yMin, yMax, yE, yD, zMin, zMax, zE, zD);
		
		
		// TODO#A7: fill in this function.
		// Check whether the given ray intersects the AABB of this BvhNode
	}
}
