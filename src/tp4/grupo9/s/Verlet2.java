package tp4.grupo9.s;

public class Verlet2 {

	public static void calculateVelocity(double initialTime, double deltaTime, Particle p, double force) {
		double secondTerm = deltaTime * p.vx + (Math.pow(deltaTime, 2) / 2 * p.m) * force;
		double nextPos = p.rx + secondTerm;
		double beforePos;

		if (initialTime == 0)
			beforePos = p.brx = p.getPossition() + deltaTime * p.getVelocity()
					+ (Math.pow(deltaTime, 2) / (2 * p.m)) * force;

		beforePos = p.rx - secondTerm;

		p.rx = beforePos;

		p.vx = (nextPos - beforePos) / (2 * deltaTime);

	}

}
