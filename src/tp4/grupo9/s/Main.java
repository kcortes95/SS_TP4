package tp4.grupo9.s;

public class Main {
	public static void main(String[] args) {
		SolarSysSimulation s = new SolarSysSimulation(100, 200, 2500);
		s.simulate(100000000);
	}
}
