package tp4.grupo9.s;

import java.awt.Color;

public class OscillatorSimulation {
	
	// System parameters
	private static final double mass = 70;
	private static final double k = Math.pow(10, 4);
	private static final double gamma = 100;
	
	// Initial conditions
	private static final double r0 = 1;
	private static final double v0 = -gamma/(mass/2);
	
	private Particle p;
	private double tf;
	private double t;
	
	public OscillatorSimulation(double tf){
		this.p = new Particle(r0, 0, v0, 0, 0, 0, 1, mass, Color.RED);
		this.tf = tf;
		this.t = 0;
	}
	
	private void beeman(double time){
		p.next = new Particle(1, mass, Color.RED);
		
		//calculate next position
		p.next.rx = p.rx + p.vx*time + (2.0/3.0)*p.ax*time*time - (1.0/6.0)*p.ax*time*time;
		
		//predict next vel
		Particle predicted = new Particle(p.next.rx, 0, 0, 1, mass);
		predicted.vx = p.vx + (3.0/2.0)*p.ax*time-0.5*p.ax*time;
		
		//calculate next accel using position and predicted vel
		p.next.ax = getAccel(predicted);
		
		//correct the next vel
		p.next.vx = p.vx + (1.0/3.0)*p.next.ax*time + (5.0/6.0)*p.ax*time - (1.0/6.0)*p.ax*time;
		
		p.previous.rx = p.rx;
		p.previous.vx = p.vx;
		p.previous.ax = p.ax;
		
		p.rx = p.next.rx;
		p.vx = p.next.vx;
		p.ax = getAccel(p.next);
	}
	
	private void verlet(double dt){
		p.next = new Particle(1, mass, Color.red);
		
		p.next.rx = 2*p.rx - p.previous.rx + dt*dt*p.ax;
		
		// Predict next vel
		p.next.vx = p.vx + (3.0/2.0)*p.ax*dt-0.5*p.previous.ax*dt;
		
		p.next.ax = getAccel(p.next);
		
		double nextnextRx = 2*p.next.rx - p.rx + dt*dt*p.next.ax;
		
		p.next.vx = (nextnextRx-p.next.rx)/(2*dt);
		
		p.previous.rx = p.rx;
		p.previous.vx = p.vx;
		p.previous.ax = p.ax;
		
		p.rx = p.next.rx;
		p.vx = p.next.vx;
		p.ax = getAccel(p.next);
	}
	
	private double exactSol(double time){
		return Math.pow(Math.E, -(gamma*time/(2*mass)))*Math.cos(Math.sqrt(k/mass-gamma*gamma/(4*mass*mass))*time);
	}
	
	private double getF(Particle part){
		return -k*part.rx-gamma*part.vx;
	}
	
	private double getAccel(Particle part){
		return getF(part)/part.m;
	}
	
	public void simulate(double dt, double dt2){
		int counter = 0;
		double difference = 0;
		if(dt>dt2 || dt<=0)
			throw new IllegalArgumentException();
		double printCounter = 0;
		
		p.ax = getAccel(p);
		
		p.previous = new Particle(eulerPos(p,-dt), eulerVel(p,-dt), 0, 1, mass);
		p.previous.ax = getAccel(p.previous);
		
		while(t<tf){
			difference += Math.pow(p.rx - exactSol(t),2);
			verlet(dt);
			
			if(dt2*printCounter<=t){
				Output.getInstace().writeOscillate(exactSol(t), t);
				printCounter++;
			}
			t+=dt;
			counter ++;
		}
		System.out.println(difference/counter);
	}
	
	private double eulerPos(Particle part, double dt){
		return part.rx + dt*part.vx + dt*dt*getF(part)/(2*part.m);
	}
	
	private double eulerVel(Particle part, double dt){
		return part.vx + dt*getF(part)/part.m;
	}
	
	
	public static void main(String[] args) {
		OscillatorSimulation os = new OscillatorSimulation(10);
		os.simulate(0.001, 0.01);
	}
}
