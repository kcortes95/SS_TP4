package tp4.grupo9.s;

import java.awt.Color;

public class OscillatorSimulation {

	public static void main(String[] args) {
		Particle p = new Particle(3, 0, 1, 0, 2, 0, 1, 1.5, Color.RED);

		double counter = 0;
		double step = 0.0001;

		Output.fileName = "animacion2.xyz";

		double k = 10000;
		double gamma = 1;
		
		p.vx = Math.sqrt(p.m/2);
		
		while (counter <= 1) {
			double force = p.m * p.ax;
			Verlet.nextPosition(counter, step, p, force);
			
			//hay que actualizar la aceleracion (filmina 31)
			//r1 es la velocidad que la saco de la filmina 11
			
			p.ax = (-k*p.rx-gamma*p.vx)/p.m;
			counter += step;
			Output.getInstace().write(p, counter);
		}

	}

}
