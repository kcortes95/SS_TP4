package tp4.grupo9.s;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class SolarSysSimulation {
	
	private static int var = 1;
	
	private static final double L = 0.00001;
	private static final double G = 6.693*Math.pow(10,-35);
	private static final double TOTAL_MASS = 2 * Math.pow(10, 20);
	private static final double MAX_DIST = 100.0;
	private static final double MIN_DIST = 10.0;
	private static final double INTER_RAD = Math.pow(10,-2);
	
	private double dt, dt2;
	Set<Particle> previous = new HashSet<>();
	private Set<Particle> particles;
	
	
	public SolarSysSimulation(int N, double dt, double dt2){
		double alpha, dist;
		double mass = TOTAL_MASS / N;
		this.particles = new HashSet<Particle>();
		this.particles.add(new Particle(0, 0,0, 0, 0, 0, 10, TOTAL_MASS, Color.YELLOW));
		for(int i=0; i<N;){
			alpha = Math.random()*2*Math.PI;
			dist = Math.random()* (MAX_DIST-MIN_DIST) + MIN_DIST;
			Particle p1 = new Particle ( dist * Math.cos(alpha), dist* Math.sin(alpha),0,0,0,0,1,mass, Color.blue);
			if(isValidPos(p1, particles)){
				particles.add(p1);
				i++;
				p1.setInitVel(L);
			}
		}
		this.dt = dt;
		this.dt2 = dt2;
	}
	
	public Vector getF(Particle p, Set<Particle> set){
		Vector force = new Vector(0,0);
		for(Particle p2: set){
			if(!p.equals(p2)){
				double alpha = Math.atan2((p2.ry-p.ry),p2.rx-p.rx);
				double f = G*p.m*p2.m/Math.pow(p.getDistance(p2),2);
				System.out.println("id: " + p.ID + " f: " + f);
				force.x += f*Math.cos(alpha);
				force.y += f*Math.sin(alpha);
			}
		}
		return force;
	}
	
	private void beeman(Particle current, Particle prev, double time){
		Particle next = new Particle(current.ID, 0, 0, 0, 0, current.r, current.m);
		
		//calculate next position
		next.rx = current.rx + current.vx*time + (2.0/3.0)*current.f.x*time*time/current.m - (1.0/6.0)*prev.f.x*time*time/current.m;
		next.ry = current.ry + current.vy*time + (2.0/3.0)*current.f.y*time*time/current.m - (1.0/6.0)*prev.f.y*time*time/current.m;
		
		//predict next vel
		Particle predicted = new Particle(next.rx, next.ry,0,0, 0, 0,0, current.m, Color.red);
		predicted.vx = current.vx + (3.0/2.0)*current.f.x*time/current.m-0.5*prev.f.x*time/current.m;
		predicted.vy = current.vy + (3.0/2.0)*current.f.y*time/current.m-0.5*prev.f.y*time/current.m;
		
		//calculate next accel using position and predicted vel
		next.f = getF(predicted, particles);
		
		//correct the next vel
		next.vx = current.vx + (1.0/3.0)*next.f.x*time/current.m + (5.0/6.0)*current.f.x*time/current.m - (1.0/6.0)*prev.f.x*time/current.m;
		next.vy = current.vy + (1.0/3.0)*next.f.y*time/current.m + (5.0/6.0)*current.f.y*time/current.m - (1.0/6.0)*prev.f.y*time/current.m;
		
		prev.rx = current.rx;
		prev.ry = current.ry;
		prev.vx = current.vx;
		prev.vy = current.vy;
		prev.f = current.f;
		
		current.rx = next.rx;
		current.ry = next.ry;
		current.vx = next.vx;
		current.vy = next.vy;
	}
	
	public Particle mergePar(Particle p1, Particle p2){
		double xm, ym, r, L1, L2, theta1, theta2, vxt, vyt, Vt, Z,Vf, vxf,vyf,radius;
		radius = Math.sqrt(p1.r*p1.r+p2.r*p2.r);
		xm = (p1.rx * p1.m + p2.rx *p2.m )/(p1.m + p2.m);
		ym = (p1.ry * p1.m + p2.ry *p2.m )/(p1.m + p2.m);
		r = Math.sqrt(xm*xm+ym*ym);
		theta1 = Math.acos(Math.sqrt(p1.rx*p1.rx+p1.ry*p1.ry)/Math.sqrt(p1.vx*p1.vx+p1.vy*p1.vy));
		theta2 = Math.acos(Math.sqrt(p2.rx*p2.rx+p2.ry*p2.ry)/Math.sqrt(p2.vx*p2.vx+p2.vy*p2.vy));
		L1 = p1.m*r*Math.sqrt(p1.vx*p1.vx+p1.vy*p1.vy)*Math.sin(theta1);
		L2 = p2.m*r*Math.sqrt(p2.vx*p2.vx+p2.vy*p2.vy)*Math.sin(theta2);
		Vt = (L1+L2)/((p1.m+p2.m)*r);
		vxt = (-ym/r)*Vt;
    	vyt = (xm/r)*Vt;
    	Vf = (p1.m*p1.getSpeed()+p2.m*p2.getSpeed())/(p1.m+p2.m);
    	Z = Math.sqrt((Vf*Vf-vxt*vxt-vyt*vyt)/(vxt*vxt+vyt*vyt));
    	vxf = vxt-vyt*Z;
    	vyf = vyt+vxt*Z;
		return new Particle(xm, ym, vxf, vyf, 0, 0, radius, p1.m+p2.m, Color.black);
	}
	
	public void simulate(double totalTime){
		double time = 0, printTime = 0;
		//checkMerge(particles);
		// Set forces and calculate previous
		for(Particle p: particles){
			p.f = getF(p, particles);
			Vector prevPos = eulerPos(p,-dt);
			Vector prevVel = eulerVel(p,-dt);
			Particle prevPart = new Particle(p.ID,prevPos.x,prevPos.y,prevVel.x,prevVel.y,p.r,p.m);
			previous.add(prevPart);
		}
		// Set forces for previous
		System.out.println("F of prev");
		for(Particle prev: previous){
			prev.f = getF(prev,previous);
		}
		System.out.println("End of prev");
		while(time<totalTime){
			if(printTime<=time){
				Output.getInstace().write(particles, printTime);
				printTime += dt2;
			}
			for(Particle p: particles){
				beeman(p,getParticle(p.ID, previous),dt);
			}
			for(Particle p: particles){
				p.f = getF(p, particles);
			}
			//checkMerge(particles);
			time += dt;
		}
	}
	
	private Vector eulerPos(Particle part, double dt){
		System.out.println(part.rx + " " + part.ry);
		double x = part.rx + dt*part.vx + dt*dt*part.f.x/(2*part.m);
		double y = part.ry + dt*part.vy + dt*dt*part.f.y/(2*part.m);
		return new Vector(x,y);
	}
	
	private Vector eulerVel(Particle part, double dt){
		double velx = part.vx + dt*part.f.x/part.m;
		double vely = part.vy + dt*part.f.y/part.m;
		return new Vector(velx,vely);
	}
	
	 private boolean isValidPos(Particle p, Set<Particle> set){
    	for(Particle p2: set){
    		if(p.getDistance(p2)<INTER_RAD)
    			return false;
    	}
    	return true;
	 }
	 
	private void checkMerge(Set<Particle> particles){
		System.out.println(var++);
		boolean mergefound = true;
		while(mergefound){
			System.out.println(var);
			mergefound = false;
			for(Particle p: particles){
				if(!p.checked){
					for(Particle p2: particles){
						if(!p2.checked && !p.equals(p2)){
							if(p.getDistance(p2)<INTER_RAD){
								mergefound=true;
								Particle merged = mergePar(p, p2);
								particles.remove(p);
								particles.remove(p2);
								particles.add(merged);
								previous.remove(p);
								previous.remove(p2);
								p.f = getF(p,particles);
								Vector prevPos = eulerPos(p,-dt);
								Vector prevVel = eulerVel(p,-dt);
								Particle prevPart = new Particle(merged.ID,prevPos.x,prevPos.y,prevVel.x,prevVel.y,p.r,p.m);
								previous.add(prevPart);
								break;
							}
						}
					}
					p.checked=true;
				}
				if(mergefound){
					break;
				}
			}
			for(Particle p:particles){
				p.checked=false;
			}
		}
		System.out.println("sali");
	}
	
	private Particle getParticle(int id, Set<Particle> set){
		for(Particle p2: set){
			if(p2.ID==id)
				return p2;
		}
		return null;
	}
}
