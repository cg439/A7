#version 120

// Note: We multiply a vector with a matrix from the left side (M * v)!
// mProj * mView * mWorld * pos

// RenderCamera Input
uniform mat4 mViewProjection;

// RenderObject Input
uniform mat4 mWorld;
uniform mat3 mWorldIT;

// RenderMesh Input
attribute vec4 vPosition; // Sem (POSITION 0)
attribute vec3 vNormal; // Sem (NORMAL 0)
attribute vec2 vUV; // Sem (TEXCOORD 0)

varying vec2 fUV;
varying vec3 fN; // normal at the vertex
varying vec4 worldPos; // vertex position in world coordinates
varying float pi; //pi constant
varying float e; //euler number

void main() {
	// TODO A4: Implement Cook-Torrance vertex shader
	// Calculate Point In World Space
	worldPos = mWorld * vPosition;
	// Calculate Projected Point
	gl_Position = mViewProjection * worldPos;

	// We have to use the inverse transpose of the world transformation matrix for the normal
	fN = normalize((mWorldIT * vNormal).xyz);
	fUV = vUV;
	pi = 3.1416;
	e = 2.7182818284590452353602874713527;
}