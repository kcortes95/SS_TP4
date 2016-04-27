package tp4.grupo9.s;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;


public class Output {
	private static Output instance = null;
	private static int count = 0;
	
	public static Output getInstace(){
		if(instance == null)
			instance = new Output();
		return instance;
	}

	public void write(Set<Particle> particles, double time){
		if(time == 0){
			try{
				PrintWriter pw = new PrintWriter("output.xyz");
				pw.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.xyz", true)))) {
			double limit = Math.pow(10,10);
			out.write((particles.size()+ 4) + "\n");
			//comment line
			//System.out.println("Frame : " + count++);
			out.write("Comment line\n");
			out.write(100000 + "\t" + -limit + "\t" + -limit + "\t" + 10 + "\t0\t0\t0" + "\n");
			out.write(100001 + "\t" + -limit + "\t" + limit + "\t" + 10 + "\t0\t0\t0" + "\n");
			out.write(100002 + "\t" + limit + "\t" + -limit + "\t" + 10 + "\t0\t0\t0" + "\n");
			out.write(100004+ "\t" + limit + "\t" + limit + "\t" + 10 + "\t0\t0\t0" + "\n");
			for(Particle p: particles)
				out.write(p.ID + "\t" + p.rx + "\t" + p.ry + "\t" + p.r + "\t" + (p.r<5?"255":"0") + "\t" + (p.r<5?"255":"255") + "\t" + (p.r<5?"255":"255")  + "\n");
			//out.write(time + "\t " + p.rx + "\n");
			out.close();
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
}
