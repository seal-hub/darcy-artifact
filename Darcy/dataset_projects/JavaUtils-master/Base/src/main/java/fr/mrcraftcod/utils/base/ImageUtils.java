package fr.mrcraftcod.utils.base;

import java.awt.image.BufferedImage;

public class ImageUtils
{
	public static BufferedImage resizeBufferedImage(BufferedImage image, float width, float height)
	{
		int baseWidth = image.getWidth(), baseHeight = image.getHeight();
		float ratio = baseWidth > baseHeight ? width / baseWidth : height / baseHeight;
		java.awt.Image tmp = image.getScaledInstance((int) (ratio * baseWidth), (int) (ratio * baseHeight), BufferedImage.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage((int) (ratio * baseWidth), (int) (ratio * baseHeight), BufferedImage.TYPE_INT_ARGB);
		buffered.getGraphics().drawImage(tmp, 0, 0, null);
		return buffered;
	}
}
