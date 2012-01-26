package velir.intellij.cq5.ui.images.icons;

import velir.intellij.cq5.jcr.LightNode;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Factory class used to get icons.
 */
public class IconFactory {
	/**
	 * Private constructor for static class.
	 */
	private IconFactory() {
	}

	public static Icon getIcon(String path) {
		//if we weren't provided a path, return null
		if (path == null || "".equals(path)) {
			return null;
		}

		//get our icon stream for our icon.
		InputStream iconStream = IconFactory.class.getResourceAsStream(path);

		//try to pull out our icon from our icon stream
		Icon icon = null;
		try {
			//only try to pull out our icon if we have bytes in our stream.
			if (iconStream != null && iconStream.available() > 0) {
				//pull out our image bytes from the icon stream.
				byte[] imageData = new byte[iconStream.available()];
				iconStream.read(imageData);

				//create a new image icon from our bytes.
				icon = new ImageIcon(imageData);
			}
		} catch (IOException ex) {
			//TODO: log exception.
			icon = null;
		}

		//return our icon
		return icon;
	}

	public static Icon getIcon(LightNode jcrLightNode) {
		//if we weren't provided a node, return null
		if (jcrLightNode == null) {
			return null;
		}

		//get our jcr node type
		String jcrNodeType = jcrLightNode.getPrimaryNodeType();

		//if we don't have a node type just return null
		if (jcrNodeType == null || "".equals(jcrNodeType)) {
			return null;
		}

		//split our node type on the colon
		String[] split = jcrNodeType.split(":");

		//if we don't have exactly 2 parts then just return
		if (split.length != 2) {
			return null;
		}

		//pull out our folder and file names
		String folder = split[0];
		String file = split[1];

		//get our icon
		return getIcon(folder + "/" + file + ".gif");
	}
}
