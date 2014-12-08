#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)

// Lighting Information
const int MAX_LIGHTS = 16;
uniform int numLights;
uniform vec3 lightIntensity[MAX_LIGHTS];
uniform vec3 lightPosition[MAX_LIGHTS];
uniform vec3 ambientLightIntensity;

// Camera Information
uniform vec3 worldCam;
uniform float exposure;

// Shading Information
// 0 : smooth, 1: rough
uniform float roughness;
varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates
varying float pi; //pi constant
varying float e; //euler number

void main()
{
    // TODO A4: Implement reflection mapping fragment shader
	//gl_FragColor = vec4(1.0); 
	vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);
	vec3 N = normalize(fN);
	vec3 V = normalize(worldCam - worldPos.xyz);
	vec4 kd = clamp(getDiffuseColor(fUV), 0.0, 1.0);
	  vec4 ks = clamp(getSpecularColor(fUV), 0.0, 1.0);
	for (int i = 0; i < numLights; i++) {
	  float r = length(lightPosition[i] - worldPos.xyz);
	  vec3 L = normalize(lightPosition[i] - worldPos.xyz); 
	  vec3 H = normalize(L + V);
	  float nDotL = dot(N,L);
	  float nDotV = dot(N,V);
	  float nDotH = dot(N,H);
	  float vDotH = dot(V,H);
	  
	  // calculate Fresnal term
	  float fresnal = 0.04 + (0.96)*pow((1-vDotH), 5.0);
		
	  // calculate multifaceted distribution
	  
	  float num = pow(nDotH,2.0) - 1;
	  float denom = pow(roughness*nDotH, 2.0);
	  float exponent = num/denom;
	  float euler = pow(e,exponent);
	  float mainDenom = denom * nDotH * nDotH;
	  float distribution = euler/mainDenom;
	  
	  // calculate attenuation
	  
	  float attenuation = min(1.0, min(2*nDotH*nDotV/vDotH, 2*nDotH*nDotL/vDotH));
	  
	  
	  // calculate diffuse term
	 // vec4 Idiff = getDiffuseColor(fUV) * max(dot(N, L), 0.0);
	  //Idiff = clamp(Idiff, 0.0, 1.0);

	  // calculate specular term
	  //vec4 Ispec = getSpecularColor(fUV) * pow(max(dot(N, H), 0.0), shininess);
	  //Ispec = clamp(Ispec, 0.0, 1.0);
	  
	  // calculate ambient term
	  //vec4 Iamb = getDiffuseColor(fUV);
	 // Iamb = clamp(Iamb, 0.0, 1.0);
	finalColor += (((ks*fresnal*attenuation*distribution)/(nDotV*nDotL*pi))+kd)*max(nDotL, 0.0)*vec4(lightIntensity[i], 0.0)/(r*r);
	//  finalColor += vec4(lightIntensity[i], 0.0) * (Idiff + Ispec) / (r*r) + vec4(ambientLightIntensity, 0.0) * Iamb;
	}
	finalColor += kd*ambientLightIntensity;
	gl_FragColor = finalColor * exposure; 
	
	
}