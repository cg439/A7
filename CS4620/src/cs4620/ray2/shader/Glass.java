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


		Vector3d incoming = new Vector3d();
		Vector3d outgoing = new Vector3d();
		outgoing.set(ray.origin).sub(record.location).normalize();

		Colord color = new Colord();
		Ray shadowRay = new Ray();
		
		outIntensity.setZero();
		for(Light light : scene.getLights()) {
			if(!isShadowed(scene, light, record, shadowRay)) {
				incoming.set(light.getDirection(record.location)).normalize();
				
				double dotProd = record.normal.dot(incoming);
				if (dotProd <= 0)
					continue;
				else {
					
					if (depth <= RayTracer.MAX_DEPTH) {
						Vector3d normal = record.normal.clone();
						
						Ray reflection = new Ray(record.location.clone(), incoming.clone().sub(normal.clone().mul(2.0).mul(incoming.clone().dot(normal.clone()))));
						
						double fresnel = fresnel(normal, ray.direction, this.refractiveIndex);
						
						//shadeRay(record.surface.getShader().shade(color, scene, ray, record, depth+1);
					//	System.out.println(record.surface.getShader());
					//	System.out.println(record.surface);
					//	System.out.println(color);
					//	System.out.println(ray.origin + " " + ray.direction);
					//	System.out.println(reflection.origin + " " + reflection.direction);
						outIntensity.add(color.mul(1.0 - fresnel));
						Colord reflectedColor = new Colord();
						
					//	System.out.println(fresnel);
						//IntersectionRecord reflectRecord = new IntersectionRecord();
						
					//	boolean reflectHit = scene.getAnyIntersection(reflection);
								
						RayTracer.shadeRay(reflectedColor, scene, ray, depth+1);
					//	outIntensity.add(reflectedColor);
					//	System.out.println(outIntensity + " at depth " + depth);
						
					}
					
				}
			}
		}
	
		//outIntensity.set(new Colord(1,1,1));
	}	

}