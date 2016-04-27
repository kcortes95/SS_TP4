package tp4.grupo9.s;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class Output {
	private static Output instance = null;
	private static int count = 0;
	public static String fileName = "output.xyz";

	public static Output getInstace() {
		if (instance == null)
			instance = new Output();
		return instance;
	}

	public void write(Set<Particle> particles, double time) {
		if (time == 0) {
			try {
				PrintWriter pw = new PrintWriter(fileName);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
			out.write((particles.size()) + "\n");
			// comment line
			// System.out.println("Frame : " + count++);
			out.write("Comment line\n");
			/*
			 * out.write(25000 + "\t" + 0 + "\t" + 0 + "\t" + 0.005 +
			 * "\t0\t0\t0" + "\n"); out.write(25001 + "\t" + 0 + "\t" + 0.5 +
			 * "\t" + 0.005 + "\t0\t0\t0" + "\n"); out.write(25002 + "\t" + 0.5
			 * + "\t" + 0 + "\t" + 0.005 + "\t0\t0\t0" + "\n"); out.write(25004+
			 * "\t" + 0.5 + "\t" + 0.5 + "\t" + 0.005 + "\t0\t0\t0" + "\n");
			 */
			for (Particle p : particles) {
				out.write(p.ID + "\t" + p.rx + "\t" + p.ry + "\t" + p.r + "\t" + (p.r < 0.05 ? "255" : "0") 
						+ "\t" + (p.r < 0.05 ? "255" : "255") + "\t" + (p.r < 0.05 ? "255" : "255") + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Particle particle, double time) {
		if (time == 0) {
			try {
				PrintWriter pw = new PrintWriter(fileName);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
			out.write((1) + "\n");
			out.write("Comment line\n");
			out.write(particle.ID + "\t" + particle.rx + "\t" + particle.ry + "\t" + particle.r + "\t"
					+ (particle.r < 0.05 ? "255" : "0") + "\t" + (particle.r < 0.05 ? "255" : "255") + "\t"
					+ (particle.r < 0.05 ? "255" : "255") + "\n");

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
