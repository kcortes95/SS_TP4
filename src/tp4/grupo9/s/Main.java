package tp4.grupo9.s;

public class Main {
	public static void main(String[] args) {
		SolarSysSimulation s = new SolarSysSimulation(50000, 500, 2500);
		s.simulate(5000000);
	}
}
