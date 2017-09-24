package assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderAsset {
	DefaultShader shader;
	public ShaderAsset(String vertexShaderPath, String fragmentShaderPath) {
		String vert = Gdx.files.internal("data/test.vertex.glsl").readString();
	       String frag = Gdx.files.internal("data/test.fragment.glsl").readString();
//	       shader = new DefaultShader(renderable, new DefaultShader.Config(vert, frag));
	       
	       shader.init();

	}
}
