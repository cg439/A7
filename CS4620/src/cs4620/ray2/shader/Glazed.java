package cs4620.ray2.shader;

import cs4620.ray2.shader.Shader;
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
public class Glazed extends Shader {

	/**
	 * The index of refraction of this material. Used when calculating Snell's Law.
	 */
	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }

	/**
	 * The underlying material beneath the glaze.
	 */
	protected Shader substrate;
	public void setSubstrate(Shader substrate) {
		this.substrate = substrate; 
	}
	
	public Glazed() { 
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
		// TODO#A7: fill in this function.
		
		Vector3d outgoing = new Vector3d();
		outgoing.set(ray.origin).sub(record.location).normalize();

		Colord color = new Colord();
		
		outIntensity.setZero();
	
		if (depth <= RayTracer.MAX_DEPTH) {
			
			Colord reflectedColor = new Colord();
			
			Vector3d normal = record.normal.clone();
			Vector3d d = ray.direction.clone();
			double nDotV = normal.dot(outgoing);
			
			double n1, n2;
			double fresnel;
			if (nDotV < 0) {	
				n1 = refractiveIndex;
					n2 = 1f;
				fresnel = fresnel(normal, outgoing, n2);	
		//		normal.negate();
				
			}
			else {
				n2 = refractiveIndex;
				n1 = 1f;
				fresnel = fresnel(normal, outgoing, n2);
				Ray reflection = new Ray(record.location.clone(), ray.direction.clone().sub(normal.clone().mul(2.0).mul(ray.direction.clone().dot(normal.clone()))).normalize());
				reflection.start = Ray.EPSILON;
				reflection.end = Double.POSITIVE_INFINITY;
				RayTracer.shadeRay(reflectedColor, scene, reflection, depth+1);
				outIntensity.add(reflectedColor.mul(fresnel));
			}
			
			substrate.shade(color, scene, ray, record, depth);
			outIntensity.add(color.mul(1.0 - fresnel));

		}
	}
}