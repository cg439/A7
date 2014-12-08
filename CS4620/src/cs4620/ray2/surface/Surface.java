package cs4620.ray2.surface;

import java.util.ArrayList;

import cs4620.ray2.surface.Surface;
import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Ray;
import cs4620.ray2.shader.Shader;
import egl.math.Colord;
import egl.math.Matrix4d;
import egl.math.Vector3d;

/**
 * Abstract base class for all surfaces.  Provides access for shader and
 * intersection uniformly for all surfaces.
 *
 * @author ags, ss932
 */
public abstract class Surface {
	/* tMat, tMatInv, tMatTInv are calculated and stored in each instance to avoid recomputing */
	
	/** The transformation matrix. */
	protected Matrix4d tMat;
	
	/** The inverse of the transformation matrix. */
	protected Matrix4d tMatInv;
	
	/** The inverse of the transpose of the transformation matrix. */
	protected Matrix4d tMatTInv;
	
	/** The average position of the surface. Usually calculated by taking the average of 
	 * all the vertices. This point will be used in AABB tree construction. */
	protected Vector3d averagePosition;
	
	/** The smaller coordinate (x, y, z) of the bounding box of this surface */
	protected Vector3d minBound;
	
	/** The larger coordinate (x, y, z) of the bounding box of this surface */
	protected Vector3d maxBound; 
	
	/** Shader to be used to shade this surface. */
	protected Shader shader = Shader.DEFAULT_SHADER;
	public void setShader(Shader shader) { this.shader = shader; }
	public Shader getShader() { return shader; }
	
	public Vector3d getAveragePosition() { return averagePosition; } 
	public Vector3d getMinBound() { return minBound; }
	public Vector3d getMaxBound() { return maxBound; }	
	
	/**
	 * Un-transform rayIn using tMatInv 
	 * @param rayIn Input ray
	 * @return tMatInv * rayIn
	 */
	public Ray untransformRay(Ray rayIn) {
		Ray ray = new Ray(rayIn.origin, rayIn.direction);
		ray.start = rayIn.start;
		ray.end = rayIn.end;

		tMatInv.mulDir(ray.direction);
		tMatInv.mulPos(ray.origin);
		return ray;
	}
	
	public void setTransformation(Matrix4d a, Matrix4d aInv, Matrix4d aTInv) {
		tMat = a;
		tMatInv = aInv;
		tMatTInv = aTInv;
		computeBoundingBox();
	}
	
	/**
	 * Tests this surface for intersection with ray. If an intersection is found
	 * record is filled out with the information about the intersection and the
	 * method returns true. It returns false otherwise and the information in
	 * outRecord is not modified.
	 *
	 * @param outRecord the output IntersectionRecord
	 * @param ray the ray to intersect
	 * @return true if the surface intersects the ray
	 */
	public abstract boolean intersect(IntersectionRecord outRecord, Ray ray);

	/**
	 * Compute the bounding box and store the result in
	 * averagePosition, minBound, and maxBound.
	 */
	public abstract void computeBoundingBox();
	
	/**
	 * Add this surface to the array list in. This array list will be used
	 * in the AABB tree construction.
	 */
	public void appendRenderableSurfaces(ArrayList<Surface> in) {
		in.add(this);
	}
	
	public void getMinMax(Vector3d min, Vector3d max, Vector3d p1, Vector3d p2, Vector3d p3, Vector3d p4, Vector3d p5, Vector3d p6, Vector3d p7, Vector3d p8) {
		tMat.mulPos(p1);
		tMat.mulPos(p2);
		tMat.mulPos(p3);
		tMat.mulPos(p4);
		tMat.mulPos(p5);
		tMat.mulPos(p6);
		tMat.mulPos(p7);
		tMat.mulPos(p8);
		float minX = (float) getMin(Double.POSITIVE_INFINITY, p1.x, p2.x, p3.x, p4.x, p5.x, p6.x, p7.x, p8.x);
		float minY = (float) getMin(Double.POSITIVE_INFINITY, p1.y, p2.y, p3.y, p4.y, p5.y, p6.y, p7.y, p8.y);
		float minZ = (float) getMin(Double.POSITIVE_INFINITY, p1.z, p2.z, p3.z, p4.z, p5.z, p6.z, p7.z, p8.z);
		float maxX = (float) getMax(Double.NEGATIVE_INFINITY, p1.x, p2.x, p3.x, p4.x, p5.x, p6.x, p7.x, p8.x);
		float maxY = (float) getMax(Double.NEGATIVE_INFINITY, p1.y, p2.y, p3.y, p4.y, p5.y, p6.y, p7.y, p8.y);
		float maxZ = (float) getMax(Double.NEGATIVE_INFINITY, p1.z, p2.z, p3.z, p4.z, p5.z, p6.z, p7.z, p8.z);
		max.set(new Vector3d(maxX, maxY, maxZ));
		min.set(new Vector3d(minX, minY, minZ));
	}
	
	
	private double getMin(double init, double x, double x2, double x3, double x4, double x5, double x6, double x7, double x8) {
		double min = init;
		if (x < min) {
			min = x;
		}
		if (x2 < min) {
			min = x2;
		}
		if (x3 < min) {
			min = x3;
		}
		if (x4 < min) {
			min = x4;
		}
		if (x5 < min) {
			min = x5;
		}
		if (x6 < min) {
			min = x6;
		}
		if (x7 < min) {
			min = x7;
		}
		if (x8 < min) {
			min = x8;
		}
		return min;
	}
	
	private double getMax(double init, double x, double x2, double x3, double x4, double x5, double x6, double x7, double x8) {
		double max = init;
		if (x > max) {
			max = x;
		}
		if (x2 > max) {
			max = x2;
		}
		if (x3 > max) {
			max = x3;
		}
		if (x4 > max) {
			max = x4;
		}
		if (x5 > max) {
			max = x5;
		}
		if (x6 > max) {
			max = x6;
		}
		if (x7 > max) {
			max = x7;
		}
		if (x8 > max) {
			max = x8;
		}
		return max;
	}
}
