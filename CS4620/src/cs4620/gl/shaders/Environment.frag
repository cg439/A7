#version 120

// You May Use The Following Functions As RenderMaterial Input
// vec4 getDiffuseColor(vec2 uv)
// vec4 getNormalColor(vec2 uv)
// vec4 getSpecularColor(vec2 uv)
// vec4 getEnvironmentColor(vec3 dir)

// Lighting Information
const int MAX_LIGHTS = 16;
uniform int numLights;
uniform vec3 lightIntensity[MAX_LIGHTS];
uniform vec3 lightPosition[MAX_LIGHTS];

// Camera Information
uniform vec3 worldCam;
uniform float exposure;

// Shading Information
varying vec3 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates

void main() {
	// TODO A4: Implement environment mapping fragment shader
	
	vec4 finalColor = vec4(0.0, 0.0, 0.0, 0.0);
	//vec3 N = normalize(fN);
	//vec3 V = normalize(worldCam - worldPos.xyz);
	
	finalColor += getEnvironmentColor(fUV);
	
	gl_FragColor = finalColor * exposure; 
}
