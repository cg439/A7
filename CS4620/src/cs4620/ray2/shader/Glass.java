package cs4620.ray2.shader;

import cs4620.ray2.Light;
import cs4620.ray2.RayTracer;
import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Ray;
import cs4620.ray2.Scene;
import egl.math.Colord;
import egl.math.Vector3d;

/**
 * A Phong material.
 *
 * @author ags, pramook
 */
public class Glass extends Shader {

	/**
	 * The index of refraction of this material. Used when calculating Snell's Law.
	 */
	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }


	public Glass() { 
		refractiveIndex = 1.0;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "glass " + refractiveIndex + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the Glass shading model.
	 *
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 * @param depth The recursion depth.
	 */
	@Override
	public void shade(Colord outIntensity, Scene scene, Ray ray, IntersectionRecord record, int depth) {

		Vector3d outgoing = new Vector3d();
		outgoing.set(ray.origin).sub(record.location).normalize();

		Colord color = new Colord();
		
		outIntensity.setZero();
	
		if (depth <= RayTracer.MAX_DEPTH) {
			
			Colord reflectedColor = new Colord();
			
//						IntersectionRecord reflectRecord = new IntersectionRecord();
			Vector3d normal = record.normal.clone();
			Vector3d d = ray.direction.clone();
			Vector3d n = record.normal.clone();
			Vector3d t = new Vector3d();
			double dDotN =  d.dot(n);
			double nDotV = normal.dot(outgoing);
			
			if (nDotV < 0) {
				normal.negate();
			}
			
			double dscr = 1 - ((1-(Math.pow(dDotN,2))/(refractiveIndex*refractiveIndex)));
			double fresnel = fresnel(normal, outgoing, this.refractiveIndex);
			if (dscr >= 0) {
				
				t.set(d. clone(). sub(n. clone(). mul(dDotN). div(refractiveIndex)));
				t.sub(n.clone().mul(Math.sqrt(dscr)));
				t.normalize();
				Ray refraction = new Ray(record.location.clone(), t);
				refraction.start = Ray.EPSILON;
			//	refraction.end = Double.POSITIVE_INFINITY;
				refraction.end = ray.end;
				Colord refractedColor = new Colord();
				RayTracer.shadeRay(refractedColor, scene, refraction, depth+1);
				outIntensity.add(refractedColor.mul(1.0 - fresnel));
			}
			
			
			
			Ray reflection = new Ray(record.location.clone(), ray.direction.clone().sub(normal.clone().mul(2.0).mul(ray.direction.clone().dot(normal.clone()))).normalize());
			reflection.start = Ray.EPSILON;
			reflection.end = Double.POSITIVE_INFINITY;
			RayTracer.shadeRay(reflectedColor, scene, reflection, depth+1);
			outIntensity.add(reflectedColor.mul(fresnel));
			
			}
		}


}