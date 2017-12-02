package ecu.se.assetManager;

import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

/**
 * 
 *	Shader asset. Stores shader information.
 */
public class ShaderAsset extends Asset {
	DefaultShader shader;

	public ShaderAsset(String vertexShaderPath, String fragmentShaderPath) {
		shader.init();
	}

	@Override
	public void dispose() {
		if (shader != null)
			shader.dispose();
	}
}
