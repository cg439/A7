package cs4620.ray2.shader;

import cs4620.ray2.IntersectionRecord;
import cs4620.ray2.Light;
import cs4620.ray2.Ray;
import cs4620.ray2.Scene;
import egl.math.Color;
import egl.math.Colord;
import egl.math.Vector3d;

public class CookTorrance extends Shader {

	/** The color of the diffuse reflection. */
	protected final Colord diffuseColor = new Colord(Color.White);
	public void setDiffuseColor(Colord diffuseColor) { this.diffuseColor.set(diffuseColor); }

	/** The color of the specular reflection. */
	protected final Colord specularColor = new Colord(Color.White);
	public void setSpecularColor(Colord specularColor) { this.specularColor.set(specularColor); }

	/** The roughness controlling the roughness of the surface. */
	protected double roughness = 1.0;
	public void setRoughness(double roughness) { this.roughness = roughness; }

	/**
	 * The index of refraction of this material. Used when calculating Snell's Law.
	 */
	protected double refractiveIndex;
	public void setRefractiveIndex(double refractiveIndex) { this.refractiveIndex = refractiveIndex; }
	
	public CookTorrance() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "CookTorrance " + diffuseColor + " " + specularColor + " " + roughness + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the CookTorrance shading model.
	 *
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 * @param depth The recursion depth.
	 */
	@Override
	public void shade(Colord outIntensity, Scene scene, Ray ray, IntersectionRecord record, int depth) {
		// TODO#A7 Fill in this function.
		// 1) Loop through each light in the scene.
		// 2) If the intersection point is shadowed, skip the calculation for the light.
		//	  See Shader.java for a useful shadowing function.
		// 3) Compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 4) Compute the color of the point using the CookTorrance shading model. Add this value
		//    to the output.

		Vector3d incoming = new Vector3d();
		Vector3d outgoing = new Vector3d();
		outgoing.set(ray.origin).sub(record.location).normalize();

		Colord color = new Colord();
		Ray shadowRay = new Ray();
		
		outIntensity.setZero();
		for(Light light : scene.getLights()) {
			if(!isShadowed(scene, light, record, shadowRay)) {
				incoming.set(light.getDirection(record.location).normalize());
				
				double dotProd = record.normal.dot(incoming);
				if (dotProd <= 0)
					continue;
				else {
					Vector3d halfVec = new Vector3d();
					halfVec.set(incoming).add(outgoing).normalize();
					
					double halfDotNormal = Math.max(0.0, halfVec.dot(record.normal));
					
					
					
					Vector3d N = record.normal.clone();
					
					
					Vector3d V = outgoing.clone();
					
					 float r = (float) ray.origin.clone().sub(record.location).len();
					  Vector3d L = light.getDirection(record.location).normalize();
					  Vector3d H = L.clone().add(V).normalize();
					  float nDotL = (float) N.dot(L);
					  float nDotV = (float) N.dot(V);
					  float nDotH = (float) N.dot(H);
					  float vDotH = (float) V.dot(H);
					  
					  Colord kd = this.diffuseColor;
					  Colord ks = this.specularColor;
					  
					  // calculate Fresnal term
					  float fresnal = (float) this.fresnel(record.normal, outgoing, refractiveIndex);
					  // calculate multifaceted distribution
					  
					  float num = (float) (Math.pow(nDotH,2.0) - 1);
					  float denom = (float) Math.pow(roughness*nDotH, 2.0);
					  float exponent = num/denom;
					  float euler = (float) Math.pow(Math.E, exponent);
					  float mainDenom = denom * nDotH * nDotH;
					  float distribution = euler/mainDenom;
					  
					  double factor = Math.pow(halfDotNormal, exponent);
						double rSq = light.getRSq(record.location);
					  
					  // calculate attenuation
					  
					  float attenuation = (float) Math.min(1.0, Math.min(2*nDotH*nDotV/vDotH, 2*nDotH*nDotL/vDotH));
					  
					  
					  // calculate diffuse term
					 // vec4 Idiff = getDiffuseColor(fUV) * max(dot(N, L), 0.0);
					  //Idiff = clamp(Idiff, 0.0, 1.0);

					  // calculate specular term
					  //vec4 Ispec = getSpecularColor(fUV) * pow(max(dot(N, H), 0.0), shininess);
					  //Ispec = clamp(Ispec, 0.0, 1.0);
					  
					  // calculate ambient term
					  //vec4 Iamb = getDiffuseColor(fUV);
					 // Iamb = clamp(Iamb, 0.0, 1.0);
					  
					  Vector3d a = ks.clone().mul(fresnal).mul(attenuation).mul(distribution);
					  float b = (float) (nDotV * nDotL * Math.PI);
					  Vector3d c = a.div(b).add(kd);
					color.set(c.mul(Math.max(nDotL, 0.0)).mul(light.intensity.div(r*r)));
					//  finalColor += vec4(lightIntensity[i], 0.0) * (Idiff + Ispec) / (r*r) + vec4(ambientLightIntensity, 0.0) * Iamb;
					outIntensity.add(color);
				}
			}
		}

	}
}
