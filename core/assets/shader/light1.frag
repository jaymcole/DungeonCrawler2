#ifdef GL_ES
    precision highp float;
#endif

// https://gamedev.stackexchange.com/questions/56897/glsl-light-attenuation-color-and-intensity-formula

float falloff = 0.3; //0.3;
float damper  = 0.1;
#define M_PI 3.1415926535897932384626433832795
#define M_E 2.7182818284590452353602874713527

uniform float a;
uniform float b;

float brightness(float maxDistance, vec2 coords, vec2 light)
{
    float dist = (distance(coords, light));
    if (dist > maxDistance*10)
    	return 0;
    float brightness = falloff/((dist+(maxDistance*damper) )/maxDistance);
    return brightness;
}

float brightness2 (float intensity, vec2 pixelCoords, vec2 lightCoords) 
{
	return 1.0/(distance(pixelCoords, lightCoords));
}

float brightness3(float lumens, vec2 coords, vec2 light)
{
    float dist = (distance(coords, light));
  	return (lumens*2) / (4 * M_PI * dist);	
}

float brightness4 (float lumens, vec2 coords, vec2 light) {
 	float minDist = 100;
 	float dist = distance(coords, light) + minDist;
 	float radiusA = lumens * a;
 	float radiusB = lumens * b;
//	return (1.0 / (1.0 + a*dist + b*dist*dist)) * lumens;

	float att;
//	att = 1.0 / (1.0 + 0.1*dist + 0.01*dist*dist) * lumens;
//	att = clamp(1.0 - dist/radiusA, 0.0, 1.0); att *= att;
	att = clamp(1.0 - dist*dist/(radiusA*radiusB), 0.0, 10.0); att *= att;
	return att;
}




struct LightSource
{
        int type;
        vec2 position;
        vec4 color;
        float intensity;
        float nothing;
};


uniform mat4 inverseProjectionMatrix; 
in vec2 texCoord;

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec3 worldPos;
uniform LightSource lights[200];
uniform int totalLights;
uniform vec4 ambientLight;
   
void main() {
    vec4 worldSpacePositionOfScreenFragment = inverseProjectionMatrix * vec4(v_texCoords.xy * 2.0 - 1.0, 0.0, 1.0);
    vec3 processingPosition = vec3(worldSpacePositionOfScreenFragment.xyz/worldSpacePositionOfScreenFragment.w)  ;
    
    //gl_FragColor = vec4(0.0,0.0,0.0,1);
    gl_FragColor = ambientLight;
	for ( int i = 0; i < totalLights; i++ ) {		

		switch(lights[i].type)
		{
			case 1:
				gl_FragColor += lights[i].color * brightness4(lights[i].intensity, vec2(processingPosition.xy) - worldPos.xy, lights[i].position);
				break;
			case 2: 
				gl_FragColor += lights[i].color * brightness3(lights[i].intensity, vec2(processingPosition.xy) - worldPos.xy, lights[i].position);
				break;			
		}
	}	
   	gl_FragColor *= v_color * texture2D(u_texture, v_texCoords);
    
}