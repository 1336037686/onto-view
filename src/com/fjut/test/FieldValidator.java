package com.fjut.test;

/*
* Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
*   - Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the following disclaimer.
*
*   - Redistributions in binary form must reproduce the above copyright
*     notice, this list of conditions and the following disclaimer in the
*     documentation and/or other materials provided with the distribution.
*
*   - Neither the name of Oracle or the names of its
*     contributors may be used to endorse or promote products derived
*     from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
* IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
* PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
* EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
* PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DateFormat;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.LayerUI;

public class FieldValidator extends JPanel {
	public FieldValidator() {
	}
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createUI();
			}
		});
	}

	public static void createUI() {
		JFrame f = new JFrame("FieldValidator");

		JComponent content = createContent();

		f.getContentPane().add(content);

		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private static JComponent createContent() {
		Dimension labelSize = new Dimension(80, 20);

		Box box = Box.createVerticalBox();

		// A single LayerUI for all the fields.
		LayerUI<JFormattedTextField> layerUI = new ValidationLayerUI();

		// Number field.
		JLabel numberLabel = new JLabel("Number:");
		numberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		numberLabel.setPreferredSize(labelSize);

		NumberFormat numberFormat = NumberFormat.getInstance();
		JFormattedTextField numberField = new JFormattedTextField(numberFormat);
		numberField.setColumns(16);
		numberField.setFocusLostBehavior(JFormattedTextField.PERSIST);
		numberField.setValue(42);

		JPanel numberPanel = new JPanel();
		numberPanel.add(numberLabel);
		numberPanel.add(new JLayer<JFormattedTextField>(numberField, layerUI));
		JPanel nfp = new JPanel();
		nfp.add(new JLayer<JPanel>(numberPanel, new PanelLayerUI()));

		// Date field.
		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dateLabel.setPreferredSize(labelSize);

		DateFormat dateFormat = DateFormat.getDateInstance();
		JFormattedTextField dateField = new JFormattedTextField(dateFormat);
		dateField.setColumns(16);
		dateField.setFocusLostBehavior(JFormattedTextField.PERSIST);
		dateField.setValue(new java.util.Date());

		JPanel datePanel = new JPanel();
		datePanel.add(dateLabel);
		datePanel.add(new JLayer<JFormattedTextField>(dateField, layerUI));

		// Time field.
		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		timeLabel.setPreferredSize(labelSize);

		DateFormat timeFormat = DateFormat.getTimeInstance();
		JFormattedTextField timeField = new JFormattedTextField(timeFormat);
		timeField.setColumns(16);
		timeField.setFocusLostBehavior(JFormattedTextField.PERSIST);
		timeField.setValue(new java.util.Date());

		JPanel timePanel = new JPanel();
		timePanel.add(timeLabel);
		timePanel.add(new JLayer<JFormattedTextField>(timeField, layerUI));

		// Put them all in the box.
		box.add(Box.createGlue());
		box.add(nfp);
		box.add(Box.createGlue());
		box.add(datePanel);
		box.add(Box.createGlue());
		box.add(timePanel);

		return box;
	}
}

class ValidationLayerUI extends LayerUI<JFormattedTextField> {
	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
        g.setColor(new Color(0, 128, 0, 128));
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
}

class PanelLayerUI extends LayerUI<JPanel> {
	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
        g.setColor(new Color(0, 123, 0, 123));
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
}