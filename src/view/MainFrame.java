package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.CustomMath;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
	private class MainPanel extends JPanel {
		public void paintComponent(Graphics g) {
			drawPointsAndCurves((Graphics2D) g);
	    }
	}

	private MainPanel contentPane;
	private boolean isDrag = false;
	private int selectIndex;
	private int rectWidth = 15;
	private Point2D[] pointArr = new Point2D[4];
	private int pointArrNowSize = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new MainPanel();
		contentPane.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (pointArrNowSize < 4) {
					Graphics2D g = (Graphics2D) contentPane.getGraphics();
					double halfRectWidth = rectWidth / 2.0;
					
					pointArr[pointArrNowSize] = new Point2D.Double(e.getX(), e.getY());
					g.setColor(Color.BLUE);
					g.fill(new Rectangle2D.Double(pointArr[pointArrNowSize].getX() - halfRectWidth,
							pointArr[pointArrNowSize].getY() - halfRectWidth, rectWidth, rectWidth));
					pointArrNowSize++;
					if (pointArrNowSize == 4) {
						drawBezierCurve(g, pointArr[0], pointArr[1], pointArr[2], pointArr[3]);
					}
				} else {
					for (int index=0; index<pointArr.length; index++) {
						double halfRectWidth = rectWidth / 2.0;
						
						if (e.getX() >= pointArr[index].getX() - halfRectWidth &&
								e.getX() <= pointArr[index].getX() + halfRectWidth &&
								e.getY() >= pointArr[index].getY() - halfRectWidth &&
								e.getY() <= pointArr[index].getY() + halfRectWidth) {
							isDrag = true;
							selectIndex = index;
						}
					}
				}
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDrag) {
					pointArr[selectIndex].setLocation(e.getX(), e.getY());
					drawPointsAndCurves((Graphics2D) contentPane.getGraphics());
				}
			}
		});
		contentPane.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				isDrag = false;
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	private void drawPointsAndCurves(Graphics2D g) {
		BufferedImage bImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D bImgG = (Graphics2D) bImg.getGraphics();
		
		bImgG.setColor(Color.WHITE);
		bImgG.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		bImgG.setColor(Color.BLUE);
		for (int index=0; index<pointArrNowSize; index++) {
			double halfRectWidth = rectWidth / 2.0;
			
			bImgG.fill(new Rectangle2D.Double(pointArr[index].getX() - halfRectWidth,
					pointArr[index].getY() - halfRectWidth, rectWidth, rectWidth));
		}
		if (pointArrNowSize == 4) {
			drawBezierCurve(bImgG, pointArr[0], pointArr[1], pointArr[2], pointArr[3]);
		}
		g.drawImage(bImg, 0, 0, null);
	}
	
	private void drawBezierCurve(Graphics2D g, Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
		if (CustomMath.isNearLine(p0, p1, p2, p3)) {
			g.draw(new Line2D.Double(p0, p3));
		} else {
			Point2D p01 = new Point2D.Double((p0.getX() + p1.getX()) / 2, (p0.getY() + p1.getY()) / 2);
			Point2D p12 = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
			Point2D p23 = new Point2D.Double((p2.getX() + p3.getX()) / 2, (p2.getY() + p3.getY()) / 2);
			Point2D p012 = new Point2D.Double((p01.getX() + p12.getX()) / 2, (p01.getY() + p12.getY()) / 2);
			Point2D p123 = new Point2D.Double((p12.getX() + p23.getX()) / 2, (p12.getY() + p23.getY()) / 2);
			Point2D pQ = new Point2D.Double((p012.getX() + p123.getX()) / 2, (p012.getY() + p123.getY()) / 2);
			
			drawBezierCurve(g, p0, p01, p012, pQ);
			drawBezierCurve(g, pQ, p123, p23, p3);
		}
	}	
}
