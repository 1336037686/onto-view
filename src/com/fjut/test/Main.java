package com.fjut.test;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main extends Box {

  public Main() {
    super(BoxLayout.Y_AXIS);
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          System.out.println("set");
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    Object o = UIManager.get("TextArea[Enabled+NotInScrollPane].borderPainter");

    UIDefaults paneDefaults = new UIDefaults();
    paneDefaults.put("TextPane.borderPainter", o);

    JTextPane pane = new JTextPane();
    pane.setMargin(new Insets(10, 10, 10, 10));

    pane.putClientProperty("Nimbus.Overrides", paneDefaults);
    pane.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
    pane.setText("this \nis \na \ntest\n");
    add(pane);

  }

  public static void main(String[] args) {

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new Main());
    frame.validate();
    frame.pack();
    frame.setVisible(true);
  }

}
