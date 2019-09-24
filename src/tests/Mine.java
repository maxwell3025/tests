package tests;

public class Mine implements Runnable {
	tests frame;
	spritepoint visual;
	double x;
	double y;
	int time = 0;
	boolean blownup = false;

	public Mine(tests t, spritepoint tex) {
		visual = tex;
		frame = t;
		x = visual.x;
		y = visual.y;
	}

	public static void main(String[] args) {

	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
			double xdif = x - frame.myx;
			double ydif = y - frame.myy;
			if (xdif * xdif + ydif * ydif < 1) {
				
				if (!blownup) {
					frame.explode(x, y, 1, 20);
					time = 2000;
					frame.playsound("audio/boom.wav");
				}
				blownup = true;

			}
			if (blownup) {
				
				time--;
				visual.value = 11+(-time*time/400000);
			}
			if (time < -1) {
				visual.x=-1000000;
				frame.updatesprites();
				break;
			}
		}
	}
}
