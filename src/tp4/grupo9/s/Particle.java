package tp4.grupo9.s;

import java.awt.Color;


public class Particle {

    static int counter = 1;
    
    public double brx = 0, bry= 0; //cual era la posicion anterior de la particula
    public double rx= 0, ry= 0;    
    public double prx= 0, pry= 0; //cual sera la posicion siguiente de la particula
    
    public double vx, vy;
    public double ax, ay;
    public double r;    
    public double m;   
    private Color c;     
    public int ID;

    public Particle(double rx, double ry, double vx, double vy, double ax, double ay, double radius, double mass, Color color) {
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.rx = rx;
        this.ry = ry;
        this.r = radius;
        this.m  = mass;
        this.c  = color;
        this.ID = counter++;
    }
    
    public Particle(double r, double m,  Color c) {
    	 rx = (Math.random() * (0.5-2*r)) + r;
         ry = (Math.random() * (0.5-2*r)) + r;
         vx = 0.1 * (Math.random()*2 - 1);
         vy = Math.sqrt(0.1*0.1-vx*vx)*(Math.random()<0.5?1:-1);
         this.r = r;
         this.m   = m;
         this.c  = c;
         this.ID = counter++;
  	}
  
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    public int hashCode(){
    	return ID;
    }
    
    public boolean equals(Object o){
    	if(o == null)
    		return false;
    	if(o.getClass() != this.getClass())
    		return false;
    	Particle other = (Particle) o;
    	if(other.ID != this.ID)
    		return false;
    	return true;
    }
    
    @Override
    public String toString() {
    	return "" + ID;
    }
    
    public Color getC() {
		return c;
	}
    
    public double getAcceleration(){
    	return Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2));
    }
    
    public double getPossition(){
    	return Math.sqrt(Math.pow(rx, 2) + Math.pow(ry, 2));
    }
    
    public double getVelocity(){
    	return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }
    
    public double getRx() {
		return rx;
	}
    
    public double getRy() {
		return ry;
	}
    
}