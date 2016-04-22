package tp4.grupo9.s;

import java.awt.Color;

public class OscillatorSimulation {

	public static void main(String[] args) {
		Particle p = new Particle(3, 0, 1, 0, 2, 0, 1, 1.5, Color.RED);

		double counter = 0;
		double step = 0.2;
		while (counter <= 5) {
			double value = Verlet.nextPosition(counter, step, p, p.m * p.getAcceleration());
			System.out.println("counter: " + counter + " ************* position: " + value);
			counter += step;
			Output.getInstace().write(p, counter);
		}

	}

}
