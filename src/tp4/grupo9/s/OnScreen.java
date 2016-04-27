package tp4.grupo9.s;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JFrame;

public class OnScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	int width, height;
	double L;

	public OnScreen(double L, int width, int height) {
		this.width = width;
		this.height = height;
		this.L = L;
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setLocationRelativeTo(null);
	}

	public void draw(Particle p) {

		Graphics g = this.getGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());

		double x = ((p.getRx()) / L) * width;
		double y = ((p.getRy()) / L) * height;

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(p.getC());
		Ellipse2D circle = new Ellipse2D.Double(x - p.r / 2, y - p.r / 2, p.r, p.r);
		g2.draw(circle);

	}

}
