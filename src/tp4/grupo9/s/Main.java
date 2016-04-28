package tp4.grupo9.s;

public class Main {
	public static void main(String[] args) {
		SolarSysSimulation s = new SolarSysSimulation(10, 1, 100);
		s.simulate(100000);
	}
}
