package tests;

public class healthpack extends Interactable {

	public healthpack(tests t, spritepoint tex, int ind) {
		super(t, tex,ind);
	}

	public void interact() {
		System.out.println("good");
		//if (frame.health >= 100) {
			//if (frame.health + 20 < 100) {
				frame.health += 20;
			//} else {
			//	frame.health = 100;
			//}
			frame.touse.remove(visual);
			frame.healthpacks.remove(this);
		//}

	}

	public void otherinteractions() {
		if((frame.myx-this.x)*(frame.myx-this.x)+(frame.myy-this.y)*(frame.myy-this.y)>10000){
			frame.touse.remove(visual);
			frame.healthpacks.remove(this);
			tostop=true;
		}
	}

}
