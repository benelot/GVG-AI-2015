package tutorials.tutorial2;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tools.ElapsedCpuTimer;
import core.game.StateObservation;


public class Visualization extends JPanel {
	public double[][][] values;
	int blockSize;
	
	public Visualization(double[][][] vals, int bs) {
		values = vals;
		blockSize = bs;
	}
	
	public void paintComponent(Graphics g) {
	      super.paintComponent(g);
	  	
			for (int i=0; i<values.length; i++) {
				for (int j=0; j<values[0].length; j++) {
				    	g.setColor(Color.black);  
						g.drawRect(i*(blockSize-1), j*(blockSize-1), blockSize, blockSize);  
						DecimalFormat df = new DecimalFormat("#.##");
						int cx = (int) (i*blockSize*0.99 - blockSize/2);
						int cy = (int) (j*blockSize*0.99 - blockSize/2);
						
						String valStr = df.format(values[i][j][0]);
						int strLen = (int) g.getFontMetrics().getStringBounds(valStr, g).getWidth();
						g.drawString(valStr, cx - strLen/2 - blockSize/4, cy);

						valStr = df.format(values[i][j][1]);
						strLen = (int) g.getFontMetrics().getStringBounds(valStr, g).getWidth();
						g.drawString(valStr, cx - strLen/2 + blockSize/4, cy);

						valStr = df.format(values[i][j][2]);
						strLen = (int) g.getFontMetrics().getStringBounds(valStr, g).getWidth();
						g.drawString(valStr, cx - strLen/2, cy + blockSize/4);

						valStr = df.format(values[i][j][3]);
						strLen = (int) g.getFontMetrics().getStringBounds(valStr, g).getWidth();
						g.drawString(valStr, cx - strLen/2, cy - blockSize/4);
				}
			}
	   }
}