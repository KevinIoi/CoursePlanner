import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;


public class InvalidInput extends JDialog
{

  public InvalidInput()
  {
    super();
  }

  public InvalidInput(JFrame parent, String str)
  {
    super(parent, str);
    setTitle("Notice");
    setSize(500, 150);
    getParent().setEnabled(false);
    setLayout(new GridLayout(2,1));
   
    JLabel text = new JLabel(str,SwingConstants.CENTER);
    add(text);
    
    JPanel container = new JPanel(new FlowLayout(1, 20, 10));
    JButton close = new JButton("OK");
    close.addActionListener(leave -> {
      setVisible(false);
      dispose();
      getParent().setEnabled(true);
      parent.toFront();
    });
    container.add(close);
    add(container);

    //just to make sure the parent window get reactivated on close
    addWindowListener(new WindowListener(){
      public void windowOpened(WindowEvent e) {}
      public void windowIconified(WindowEvent e) {}
      public void windowDeiconified(WindowEvent e) {}
      public void windowDeactivated(WindowEvent e) {}
      public void windowClosed(WindowEvent e) {
        getParent().setEnabled(true);
      }
      public void windowActivated(WindowEvent e){}
      public void windowClosing(WindowEvent e) {
        getParent().setEnabled(true);
      }
    });
    
    setVisible(true);
  }

  public InvalidInput(JDialog parent, String str)
  {
    super(parent, str);
    setTitle("Notice");
    setSize(500, 150);
    getParent().setEnabled(false);
    setLayout(new GridLayout(2,1));
   
    JLabel text = new JLabel(str,SwingConstants.CENTER);
    add(text);
    
    JPanel container = new JPanel(new FlowLayout(1, 20, 10));
    JButton close = new JButton("OK");
    close.addActionListener(leave -> {
      setVisible(false);
      dispose();
      getParent().setEnabled(true);
      parent.toFront();
    });
    container.add(close);
    add(container);

    //just to make sure the parent window get reactivated on close
    addWindowListener(new WindowListener(){
      public void windowOpened(WindowEvent e) {}
      public void windowIconified(WindowEvent e) {}
      public void windowDeiconified(WindowEvent e) {}
      public void windowDeactivated(WindowEvent e) {}
      public void windowClosed(WindowEvent e) {
        getParent().setEnabled(true);
      }
      public void windowActivated(WindowEvent e){}
      public void windowClosing(WindowEvent e) {
        getParent().setEnabled(true);
      }
    });
    
    setVisible(true);
  }



}