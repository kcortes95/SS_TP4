package tp4.grupo9.s;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class OscillatorSimulation {
	
	// System parameters
	private static final double mass = 70;
	private static final double k = Math.pow(10, 4);
	private static final double gamma = 100;
	
	// Initial conditions
	private static final double r0 = 1;
	private static final double v0 = -gamma/(mass/2);
	
	private Particle current, previous;
	private double tf;
	private double t;
	private double dt, dt2;
	
	public OscillatorSimulation(double tf){
		this.current = new Particle(r0, 0, v0, 0, 0, 0, 1, mass, Color.RED);
		this.tf = tf;
		this.t = 0;
	}
	
	private void beeman(Particle current, Particle previous, double time){
		Particle next = new Particle(1, mass, Color.RED);
		
		//calculate next position
		next.rx = current.rx + current.vx*time + (2.0/3.0)*current.ax*time*time - (1.0/6.0)*previous.ax*time*time;
		
		//predict next vel
		Particle predicted = new Particle(next.rx, 0, 0, 1, mass);
		predicted.vx = current.vx + (3.0/2.0)*current.ax*time-0.5*previous.ax*time;
		
		//calculate next accel using position and predicted vel
		next.ax = getAccel(predicted);
		
		//correct the next vel
		next.vx = current.vx + (1.0/3.0)*next.ax*time + (5.0/6.0)*current.ax*time - (1.0/6.0)*previous.ax*time;
		
		previous = current;
		current = next;
	}
	
	private void velVerlet(double time){
		previous = current;
		
		current.rx = previous.rx + time*previous.vx + time*time*getAccel(previous);
		
		
	}
	
	private double getF(Particle part){
		return -k*part.rx-gamma*part.vx;
	}
	
	private double getAccel(Particle part){
		return getF(part)/part.m;
	}
	
	public void simulate(double dt, double dt2){
		if(dt>dt2 || dt<=0)
			throw new IllegalArgumentException();
		this.dt = dt;
		this.dt2 = dt2;
		double printCounter = 0;
		
		previous = new Particle(eulerPos(current,-dt), eulerVel(current,-dt), 0, 1, mass);
		previous.ax = getAccel(previous);
		
		while(t<tf){
			//beeman(dt);
			if(dt2*printCounter<=t){
				//Output.getInstace().write(current, t);
				printCounter++;
			}
			t+=dt;
		}
	}
	
	private double eulerPos(Particle part, double dt){
		return part.rx + dt*part.vx + dt*dt*getF(part)/(2*part.m);
	}
	
	private double eulerVel(Particle part, double dt){
		return part.vx + dt*getF(part)/part.m;
	}
	
	
	public static void main(String[] args) {
		OscillatorSimulation os = new OscillatorSimulation(50);
		os.simulate(0.01, 0.1);
	}
}
