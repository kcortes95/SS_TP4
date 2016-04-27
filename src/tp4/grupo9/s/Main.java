package tp4.grupo9.s;

public class Main {
	public static void main(String[] args) {
		SolarSysSimulation s = new SolarSysSimulation(10, 0.1, 0.1);
		s.simulate(100);
	}
}
