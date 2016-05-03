package tp4.grupo9.s;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class SolarSysSimulation {
	
	private static final double G = 6.693*Math.pow(10,-11);
	private static final double TOTAL_MASS = 2 * Math.pow(10, 30);
	private static final double MAX_DIST = Math.pow(10, 10);
	private static final double MIN_DIST = Math.pow(10, 9);
	private static final double INTER_RAD = Math.pow(10,6);
	private static final int M = 100;
	
	private double dt, dt2;
	private Particle sun;
	private Set<Particle> particles;
	private Set<Particle> outOfBounds;
	private Grid grid;
	
	
	public SolarSysSimulation(int N, double dt, double dt2){
		double alpha, dist;
		double mass = TOTAL_MASS / N;
		this.outOfBounds = new HashSet<Particle>();
		this.particles = new HashSet<Particle>();
		this.sun = new Particle(0, 0, 0, 0, 0, 0, Math.pow(10,9), TOTAL_MASS, Color.YELLOW);
		this.particles.add(sun);
		for(int i=0; i<N;){
			double x = 2*Math.random()*MAX_DIST-MAX_DIST;
			double y = 2*Math.random()*MAX_DIST-MAX_DIST;
			Particle p1 = new Particle ( x, y,0,0,0,0,INTER_RAD*10/N,mass, Color.blue);
			if(isValidPos(p1, particles)){
				particles.add(p1);
				i++;
				setInitVel(p1);
			}
		}
		this.grid = new LinearGrid(2*MAX_DIST, M, particles);
		this.dt = dt;
		this.dt2 = dt2;
	}
	
	public Vector getF(Particle p){
		if(p.equals(sun))
			return new Vector(0,0);
		double alpha = Math.atan2((sun.ry-p.ry),sun.rx-p.rx);
		double f = G*p.m*sun.m/Math.pow(p.getDistance(sun),2);
		double x = f*Math.cos(alpha);
		double y = f*Math.sin(alpha);
		return new Vector(x,y);
	}
	
	private void beeman(Particle p){
		p.next = new Particle(p.ID, 0, 0, 0, 0, p.r, p.m);
		p.next.rx = p.rx + p.vx*dt + (2.0/3.0)*p.f.x*dt*dt/p.m - (1.0/6.0)*p.previous.f.x*dt*dt/p.m;
		p.next.ry = p.ry + p.vy*dt + (2.0/3.0)*p.f.y*dt*dt/p.m - (1.0/6.0)*p.previous.f.y*dt*dt/p.m;
		p.next.f = getF(p.next);
		p.next.vx = p.vx + (1.0/3.0)*p.next.f.x*dt/p.m + (5.0/6.0)*p.f.x*dt/p.m - (1.0/6.0)*p.previous.f.x*dt/p.m; 
		p.next.vy = p.vy + (1.0/3.0)*p.next.f.y*dt/p.m + (5.0/6.0)*p.f.y*dt/p.m - (1.0/6.0)*p.previous.f.y*dt/p.m;
		
		p.previous.rx = p.rx;
		p.previous.ry = p.ry;
		p.previous.vx = p.vx;
		p.previous.vy = p.vy;
		p.previous.f = p.f;
		
		p.rx = p.next.rx;
		p.ry = p.next.ry;
		p.vx = p.next.vx;
		p.vy = p.next.vy;
		p.f = p.next.f;
	}
	
	public Particle mergePar(Particle p1, Particle p2){
		double xm, ym, r, L1, L2, theta1, theta2, vxt, vyt, Vt,Vr, vxr,vyr,radius,vr1,vr2;
		radius = Math.sqrt(p1.r*p1.r+p2.r*p2.r);
		xm = (p1.rx * p1.m + p2.rx *p2.m )/(p1.m + p2.m);
		ym = (p1.ry * p1.m + p2.ry *p2.m )/(p1.m + p2.m);
		r = Math.sqrt(xm*xm+ym*ym);
		// Cosine rule
		theta1 = (Math.PI*2) - Math.acos(-(Math.pow(p1.rx+p1.vx, 2)+Math.pow(p1.ry+p1.vy,2)-Math.pow(p1.distanceToOrigin(),2)-Math.pow(p1.getSpeed(),2))/(2*p1.distanceToOrigin()*p1.getSpeed()));
		theta2 = (Math.PI*2) - Math.acos(-(Math.pow(p2.rx+p2.vx, 2)+Math.pow(p2.ry+p2.vy,2)-Math.pow(p2.distanceToOrigin(),2)-Math.pow(p2.getSpeed(),2))/(2*p2.distanceToOrigin()*p2.getSpeed()));
		L1 = p1.m*r*p1.getSpeed()*Math.sin(theta1);
		L2 = p2.m*r*p2.getSpeed()*Math.sin(theta2);
		Vt = (L1+L2)/((p1.m+p2.m)*r);
		vxt = (ym/r)*Vt;
    	vyt = (-xm/r)*Vt;
    	vr1 = p1.getSpeed()*Math.cos(theta1);
    	vr2 = p2.getSpeed()*Math.cos(theta2);
    	Vr = (vr1*p1.m+p2.m*vr2)/(p1.m+p2.m);
    	vxr = (xm/r)*Vr;
    	vyr = (ym/r)*Vr;
		return new Particle(xm, ym, vxt+vxr, vyt+vyr, 0, 0, radius, p1.m+p2.m, Color.black);
	}
	
	public void simulate(double totalTime){
		int runs = 0;
		double time = 0, printTime = 0;
		// Set forces and calculate previous
		for(Particle p: particles){
			p.f = getF(p);
			Vector prevPos = eulerPos(p,-dt);
			Vector prevVel = eulerVel(p,-dt);
			p.previous = new Particle(p.ID,prevPos.x,prevPos.y,prevVel.x,prevVel.y,p.r,p.m);
			p.previous.f = getF(p.previous);
		}
		while(time<totalTime){
			if(runs%100==0)
				System.out.println(time);
			if(printTime<=time){
				Output.getInstace().write(particles, printTime);
				printTime += dt2;
			}
			for(Particle p: particles)
				updatePos(p);
			particles.removeAll(outOfBounds);
			outOfBounds.clear();
			for(int i=0; i<grid.getM(); i++){
				for(int j=0; j<grid.getM(); j++)
					//while(checkMerge(grid.getCell(i, j)));
					checkMerge(grid.getCell(i,j));
			}
			time += dt;
			runs++;
		}
	}
	
	private Vector eulerPos(Particle part, double dt){
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
		if(p.distanceToOrigin()>MAX_DIST || p.distanceToOrigin()<MIN_DIST)
			return false;
    	for(Particle p2: set){
    		if(p.getDistance(p2)<INTER_RAD)
    			return false;
    	}
    	return true;
	 }
	 
	private boolean checkMerge(Cell cell){
		clearMarks(cell.getParticles());
		for(Particle p: cell.getParticles()){
			p.checked = true;
			// Check within its own cell
			for(Particle p2: cell.getParticles()){
				if(!p2.checked && p.getDistance(p2)<=INTER_RAD){
					Particle merged = mergePar(p, p2);
					particles.remove(p);
					grid.remove(p);
					particles.remove(p2);
					grid.remove(p2);
					particles.add(merged);
					grid.insert(merged);
					merged.f = getF(merged);
					Vector prevPos = eulerPos(p,-dt);
					Vector prevVel = eulerVel(p,-dt);
					merged.previous = new Particle(merged.ID,prevPos.x,prevPos.y,prevVel.x,prevVel.y,p.r,p.m);
					merged.previous.f = getF(merged.previous);
					return true;
				}
			}
			// Check with neighbouring cells
			for(Cell neighbour: cell.getNeighbours()){
				for(Particle p2: neighbour.getParticles()){
					if(p.getDistance(p2)<=INTER_RAD){
						Particle merged = mergePar(p, p2);
						particles.remove(p);
						grid.remove(p);
						particles.remove(p2);
						grid.remove(p2);
						particles.add(merged);
						grid.insert(merged);
						merged.f = getF(merged);
						Vector prevPos = eulerPos(p,-dt);
						Vector prevVel = eulerVel(p,-dt);
						merged.previous = new Particle(merged.ID,prevPos.x,prevPos.y,prevVel.x,prevVel.y,p.r,p.m);
						merged.previous.f = getF(merged.previous);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void clearMarks(Set<Particle> particles){
		for(Particle p: particles)
			p.checked = false;
	}
	
	private void updatePos(Particle p){
		double cellLength = grid.getL()/grid.getM();
		int cellX = (int) Math.floor(p.rx/cellLength);
		int cellY = (int) Math.floor(p.ry/cellLength);
		beeman(p);
		int newCellX = (int)Math.floor(p.rx/cellLength);
		int newCellY = (int)Math.floor(p.ry/cellLength);
		if(newCellX != cellX ||newCellY != cellY){
			//Out of orbit
			if(newCellX+M/2 < 0 || newCellX+M/2 >= M || newCellY+M/2 < 0 || newCellY+M/2>= M){
				//System.out.println((cellX+M/2)+" "+(cellY+M/2));
				grid.getCell(cellX + M/2, cellY + M/2).getParticles().remove(p);
				outOfBounds.add(p);
				return;
			}
			grid.getCell(cellX + M/2, cellY + M/2).getParticles().remove(p);
			grid.insert(p);
		}
	}

	 
    public void setInitVel(Particle p){
    	double k = 0, m1=p.m, m2=sun.m, r=p.distanceToOrigin(), mu=m1*m2/(m1+m2);
    	double rand = Math.random()*0.2;
    	while(k==0){k=Math.random();}
    	double L = Math.sqrt((G*r*mu*m1*m2)+Math.sqrt(-G*G*(k-1)*r*r*m1*m1*m2*m2*mu*mu)*rand);
    	double V = L/(r*p.m);
    	p.vx = (-p.ry/r)*V;
    	p.vy = (p.rx/r)*V;
    }
    
    public double totalKineticEnergy(Set<Particle> set){
    	double E = 0;
    	for(Particle p: set){
    		E += 0.5*p.m*p.getSpeed()*p.getSpeed();
    	}
    	return E;
    }
    
    public double totalPotentialEnergy(Set<Particle> set){
    	double U = 0;
    	for(Particle p: set){
    		U += -G*p.m*sun.m/p.distanceToOrigin();
    	}
    	return U;
    }
}
