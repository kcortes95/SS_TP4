package tp4.grupo9.s;

import java.awt.Color;


public class Particle {

	public Particle previous, next;
    static int counter = 1;
    public Vector f;
    public double rx, ry;    
    public double vx, vy;
    public double ax, ay;
    public double r;    
    public double m;   
    private Color c;     
    public int ID;
    public boolean checked = false;

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
    
    public Particle(int ID, double rx, double ry,double vx, double vy, double radius, double mass){
    	this(rx,ry,vx,vy,0,0,radius,mass,Color.red);
    	this.ID = ID;
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
    
    public Particle(double rx, double vx, double ax, double r, double m){
    	this(rx,0,vx,0,ax,0,r,m,Color.RED);
    }
  
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    public double getDistance(Particle other){
    	return Math.sqrt(Math.pow(this.rx-other.rx,2)+Math.pow(this.ry-other.ry, 2));
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
    
    public double getSpeed(){
    	return Math.sqrt(vx*vx+vy*vy);
    }
    
    public double distanceToOrigin(){
    	return Math.sqrt(rx*rx+ry*ry);
    }
}