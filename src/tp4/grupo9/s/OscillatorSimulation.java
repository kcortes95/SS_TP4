package tp4.grupo9.s;

import java.awt.Color;
import java.util.*;

public class OscillatorSimulation {

	public static void main(String[] args) {
		Particle p = new Particle(3, 0, 1, 0, 2, 0, 1, 1.5, Color.RED);

		double counter = 0;
		double step = 0.1;

		//OnScreen screen = new OnScreen(100, 800, 600);

		Output.fileName = "testVerlet3.xyz";
		
		double force = p.m  * p.ax;
		while (counter <= 50) {
			Verlet2.calculateVelocity(counter, step, p, force);
			//Verlet.nextPosition(counter, step, p, force);
			counter += step;
			Output.getInstace().write(p, counter);

			//screen.draw(p);
		}

	}

}
