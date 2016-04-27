package tp4.grupo9.s;

public class Main {
	public static void main(String[] args) {
		SolarSysSimulation s = new SolarSysSimulation(1, 0.0001, 0.0001);
		s.simulate(0.5);
	}
}
