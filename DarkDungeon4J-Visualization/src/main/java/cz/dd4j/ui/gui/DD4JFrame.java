package cz.dd4j.ui.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import cz.cuni.amis.clear2d.engine.C2DPanelStandalone;

public class DD4JFrame extends JFrame {
	
	private class FrameComponentListener extends ComponentAdapter {
		
		@Override
        public void componentResized(ComponentEvent e) {
        	onResize();
        }
        
        @Override
        public void componentHidden(ComponentEvent e) {
        }
        
        @Override
        public void componentShown(ComponentEvent e) {
        }
        
	}

	private FrameComponentListener componentListener = new FrameComponentListener();
	
	public C2DPanelStandalone dungeon;
	
	public JTextArea log;
	public int logLines = 0;
	
	public JScrollPane logScroll;
	
	public DD4JFrame() {
		this(800,600);
	}
	
	public DD4JFrame(int c2dWidth, int c2dHeight) {

		setTitle("DarkDungeon4J");
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		int frameWidth = c2dWidth;
		if (c2dWidth < 800) frameWidth = 800;
		
		// C2DPanel -> 800x600
		// borders -> 16x39
		// bottom log -> 150
		setSize(frameWidth+16, c2dHeight+39+150);
		
		setLayout(null);
		
		dungeon = new C2DPanelStandalone(c2dWidth, c2dHeight, Color.BLACK);
		add(dungeon);
		
		log = new JTextArea();
		log.setFont(Font.decode("Consolas-10"));
		log.setEditable(false);
		
		logScroll = new JScrollPane(log);
		logScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(logScroll);
				
		addComponentListener(componentListener);
		
		onResize();
	}

	private void onResize() {
		int w = getContentPane().getWidth();
		int h = getContentPane().getHeight();
		
		if (h > 300) {	
			dungeon.setBounds(0, 0, w, h - 150);
			logScroll.setBounds(0, h - 150, w, 150);
		} else {
			int dH = h / 2;
			dungeon.setBounds(0, 0, w, dH);
			logScroll.setBounds(0, dH, w, h - dH);
		}
	}
	
	public void log(String msg) {
		
		String text = log.getText() + "\n" + msg;
		
		logLines++;
		
		while (logLines > 100) {
			int index = text.indexOf('\n');
			if (index < 0) {
				logLines = 0;
				break;
			}
			text = text.substring(index+1);
			--logLines;
		}
		
		this.log.setText(text);		
		this.log.setCaretPosition(log.getText().length());	
		
		JScrollBar vertical = logScroll.getVerticalScrollBar();
		if (vertical != null) {
			vertical.setValue( vertical.getMaximum() );
		}
	}

	public void die() {
		dungeon.die();
	}

	

}
