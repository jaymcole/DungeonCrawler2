#ifdef GL_ES
    precision highp float;
#endif

float falloff = 0.3; //0.3;
float damper  = 0.1;

float brightness(float maxDistance, vec2 coords, vec2 light)
{
    float dist = (distance(coords, light));
    float brightness = falloff/((dist+(maxDistance*damper) )/maxDistance);
    return brightness;
}

float brightness2 (float intensity, vec2 pixelCoords, vec2 lightCoords) 
{
	return 1.0/(distance(pixelCoords, lightCoords));
}


struct LightSource
{
        vec2 position;
        vec4 color;
        float intensity;
};


uniform LightSource lights[32];
uniform int totalLights;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 worldPosition;
uniform sampler2D u_texture;

uniform float test_brightness;
void main()
{

	gl_FragColor = vec4(0.1,0.1,0.1,1);
	
	for ( int i = 0; i < totalLights; i++ ) {
		gl_FragColor += lights[i].color * brightness(lights[i].intensity, vec2(gl_FragCoord.x/gl_FragCoord.w + worldPosition.x, gl_FragCoord.y/gl_FragCoord.w + worldPosition.y), lights[i].position);		//* brightness(lights[i].intensity, vec2(gl_FragCoord.x, gl_FragCoord.y), lights[i].position);
	}	
	//gl_FragColor /= totalLights;
   	gl_FragColor *= v_color * texture2D(u_texture, v_texCoords);
   	
   	
   	
   	
   	
   	//gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}

