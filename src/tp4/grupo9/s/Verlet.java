package tp4.grupo9.s;

public class Verlet {

	public static double nextPosition(double initialTime, double deltaTime, Particle p, double force) {

		if (initialTime == 0)
			p.brx = p.getPossition() + deltaTime * p.getVelocity() + (Math.pow(deltaTime, 2) / (2 * p.m)) * force;

		double calc1 = 2 * p.rx - p.brx;
		double calc2 = (Math.pow(deltaTime, 2) / p.m) * force;

		p.vx = (p.prx - p.brx) / (2 * deltaTime);

		// actualizar la posicion de la particula
		p.brx = p.rx;
		p.rx = p.prx;
		p.prx = calc1 - calc2;
		// p.rx = calc1 - calc2;

		return p.rx;

	}

}
