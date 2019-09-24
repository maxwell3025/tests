package tests;

public class spritepoint {
	double x, y;
	int value;
	tests frame;
	double dist;
	public int age=0;

	public spritepoint(double y, double x, int value, tests a) {
		this.x = x;
		this.y = y;
		this.value = value;
		frame = a;
	}

	public void update() {
		dist = (frame.myx - x) * (frame.myx - x) + (frame.myy - y) * (frame.myy - y);
	}

}
