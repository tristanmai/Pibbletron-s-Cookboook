package pib.cookbook;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class LoginGUI extends JFrame implements ActionListener
{
  private JPanel loginPanel;
  private JLabel loginLabel;
  private JLabel usernameLabel;
  private JTextField usernameField;
  private JLabel passwordLabel;
  private JTextField passwordField;
  private JButton submitButton;
  
  public LoginGUI()
  {
    super("Login Page");
    this.setBounds(100, 200, 1000, 815);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().setLayout(new BorderLayout());
    
    this.loginPanel = new JPanel();
    
  }
}
