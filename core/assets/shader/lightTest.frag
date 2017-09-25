#ifdef GL_ES
    precision highp float;
#endif

float falloff = 0.3; //0.3;
float damper  = 0.1;

float brightness(float maxDistance, vec2 coords, vec2 light)
{
    float dist = distance(coords, light);
    float brightness = falloff/((dist+(maxDistance*damper) )/maxDistance);
    return brightness;
}

struct LightSource
{
        int Type;
        vec3 Position;
        vec3 Attenuation;
        vec3 Direction;
        vec3 Color;
};

uniform LightSource Light[4];
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 worldPosition;
uniform sampler2D u_texture;

void main()
{
	//gl_FragColor += fragColorTemp * brightness(50, vec2(gl_FragCoord.x, gl_FragCoord.y), vec2(960, 540));
	//gl_FragColor *= vec4(0.4, 0.4, 0.80, 1.0);
	
	gl_FragColor = vec4(0,0,0,1);
	gl_FragColor += vec4(0.0, 0.0, 1, 1.0) * brightness(300, vec2(gl_FragCoord.x, gl_FragCoord.y), vec2(960, 540));
	gl_FragColor += vec4(1, 0.0, 0.0, 1.0) * brightness(300, vec2(gl_FragCoord.x, gl_FragCoord.y), vec2(900, 500));
	gl_FragColor += vec4(0, 1.0, 0.0, 1.0) * brightness(300, vec2(gl_FragCoord.x, gl_FragCoord.y), vec2(1000, 600));
	
	//float max = max(max(gl_FragColor.r, gl_FragColor.g), gl_FragColor.b) / 1;
	
	//gl_FragColor = normalize(gl_FragColor);
   	gl_FragColor *= v_color * texture2D(u_texture, v_texCoords);
   	
   	
   	
   	
   	
   	
    //fragColor += fragColorTemp * brightness(uv, fragColor, maxDistance, fragCoord, light3);
	//fragColor *= lightc3
}

