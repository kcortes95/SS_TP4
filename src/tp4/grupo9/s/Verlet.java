package tp4.grupo9.s;

public class Verlet {

	public static double nextPosition(double initialTime, double deltaTime, Particle p, double force) {
		double value = 0;

		if (initialTime == 0)
			// lo tengo que calcular con euler
			p.brx = p.getPossition() + deltaTime * p.getVelocity() + (Math.pow(deltaTime, 2) / (2 * p.m)) * force;

		double calc1 = 2 * p.getPossition() - p.brx;
		double calc2 = (Math.pow(deltaTime, 2) / p.m) * force;

		// actualizar la posicion de la particula cuando pasa deltaTime ?!?!?!
		p.brx = p.rx;
		p.rx = calc1 - calc2;

		return p.rx;

	}

}
