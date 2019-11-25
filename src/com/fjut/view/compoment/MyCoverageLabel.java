package com.fjut.view.compoment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import com.fjut.core.Handler.RefalignHandler;
import com.fjut.pojo.CellPoint;
import com.fjut.pojo.MyCellData;

/**
 * 画线界面
 * @author LGX
 *
 */
public class MyCoverageLabel extends JLabel implements Runnable{	
	
	/**
	 * 起始数据
	 */
	public static volatile List<CellPoint> source;
	/**
	 * 目标数据
	 */
	public static volatile List<CellPoint> target;
	
	/**
	 * 画图开关
	 */
	public static volatile boolean drawLineSwitch = false;	
	
	/**
	 * 本体1需要删除线的节点
	 */
	public static volatile List<String> Onto1RemoveList = new ArrayList<String>();

	/**
	 * 本体2需要删除线的节点
	 */
	public static volatile List<String> Onto2RemoveList = new ArrayList<String>();
	
	public MyCoverageLabel() {
		setBounds(10, 20, 921, 515);
	}
		
	/**
	 * 画线
	 * @param g 
	 * @param source
	 * @param target
	 */
	public void drawLines(Graphics g) {
		if(source == null || target == null || g == null) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(1f));
		
		//获取标准数据
		List<MyCellData> list = RefalignHandler.getRefalign();
		//匹配
		list.forEach((x) -> {
			CellPoint[] point = new CellPoint[2];
			for (CellPoint c : source) {
				if(c.id.equals(x.getEntity1Id()) && !Onto1RemoveList.contains(x.getEntity1Id())) point[0] = c;	
			}
			for (CellPoint c : target) {
				if(c.id.equals(x.getEntity2Id()) && !Onto2RemoveList.contains(x.getEntity2Id())) point[1] = c;
			}
			if(point[0] != null && point[1] != null) {
				//根据相似度设置颜色
				//g2d.setColor(Line.getColorForSim((float)x.getMeasure()));
				g2d.setColor(Color.RED);
				//绘制曲线
				int addy = Math.round((float)((point[1].Y - point[0].Y) / 4));
				CubicCurve2D myCurve = new CubicCurve2D.Double((double)point[0].X, (double)point[0].Y, (double)(point[0].X + 250), (double)(point[0].Y + addy), (double)(point[1].X + 100), (double)(point[1].Y - addy), (double)point[1].X + 440, (double)point[1].Y);
				g2d.draw(myCurve);
				
				//g2d.drawLine((int)point[0].X, (int)point[0].Y, (int)(point[1].X + 450), (int)point[1].Y); //绘制直线
				g2d.setColor(Line.getColorForSim((float)x.getMeasure()));
				//g2d.drawString(x.getMeasure() + "", ((int)point[0].X + (int)(point[1].X + 450)) / 2, ((int)point[0].Y + (int)point[1].Y) / 2);
				//绘制相似度文字
				g2d.drawString(x.getMeasure() + "", ((int)(point[0].X + 250) + (int)(point[1].X + 100)) / 2, ((int)(point[0].Y + addy) + (int)(point[1].Y - addy)) / 2);
			}
		});
		g.dispose();	
	}

	/**
	 * 多线程画线
	 */
	public void run() {
		while(true) {
			if(drawLineSwitch) {
				this.drawLines(this.getGraphics());
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}else {
				this.repaint();
			}	
		}
	}
	
	
	/**
	 * 线条
	 * @author LGX
	 *
	 */
	static class Line {
	    static Color valueBackground;
	    static Color C0_05;
	    static Color C05_15;
	    static Color C15_25;
	    static Color C25_35;
	    static Color C35_45;
	    static Color C45_55;
	    static Color C55_65;
	    static Color C65_75;
	    static Color C75_85;
	    static Color C85_95;
	    static Color C95_100;
	    static Color C100_;
	    
	    static {
	        valueBackground = Color.BLACK;
	        C0_05 = new Color(210, 50, 0);
	        C05_15 = new Color(230, 80, 0);
	        C15_25 = new Color(240, 120, 0);
	        C25_35 = new Color(240, 160, 0);
	        C35_45 = new Color(240, 200, 0);
	        C45_55 = new Color(235, 235, 0);
	        C55_65 = new Color(210, 235, 0);
	        C65_75 = new Color(150, 220, 0);
	        C75_85 = new Color(80, 180, 0);
	        C85_95 = new Color(30, 130, 0);
	        C95_100 = new Color(0, 90, 0);
	        C100_ = new Color(85, 187, 238);
	    }
	    
	    public static Color getColorForSim(float sim) {
	        if ((double)sim < 0.05D) {
	            return C0_05;
	        } else if (sim > 1.0F) {
	            return C100_;
	        } else if ((double)sim > 0.95D) {
	            return C95_100;
	        } else if ((double)sim < 0.15D) {
	            return C05_15;
	        } else if ((double)sim < 0.25D) {
	            return C15_25;
	        } else if ((double)sim < 0.35D) {
	            return C25_35;
	        } else if ((double)sim < 0.45D) {
	            return C35_45;
	        } else if ((double)sim < 0.55D) {
	            return C45_55;
	        } else if ((double)sim < 0.65D) {
	            return C55_65;
	        } else if ((double)sim < 0.75D) {
	            return C65_75;
	        } else if ((double)sim < 0.85D) {
	            return C75_85;
	        } else {
	            return (double)sim < 0.95D ? C85_95 : C45_55;
	        }
	    }
	}
	
}
