package net.leo.message.client.animation.order;

import java.awt.Graphics;

public interface AnimationOrder {

	void hide();

	int getDelay();

	int getFrameCount();

	void goToEnd();

	void nextFrame();

	void paintFrame(Graphics g);

	void show();

	void stop();
}
