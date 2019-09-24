package tests;

public abstract class Interactable implements Runnable{
	tests frame;
	spritepoint visual;
	double x;
	double y;
	int spriteindex;
	boolean tostop = false;
	Thread checker = new Thread(this);
	public Interactable(tests t, spritepoint tex, int ind) {
		visual = tex;
		frame = t;
		x = visual.x;
		y = visual.y;
		spriteindex=ind;
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
				interact();
				break;
			}
			if(tostop){
				break;
			}
		}
	}
	public abstract void interact();
	public abstract void otherinteractions();
}
