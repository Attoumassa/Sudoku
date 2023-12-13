package sudoku;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFormattedTextField;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.JOptionPane;


public class VueChoixGrille extends JFrame implements ActionListener{
  	// implements ActionListener
  	private JPanel top = new JPanel();
  	private JTextField jtext1, jtext2, jtext3;
	private JLabel ligne = new JLabel("Nombre de ligne pour un bloc (n)");
  	private JLabel colonne = new JLabel("Nombre de colonne pour un bloc (k)");
  	private JLabel zeros = new JLabel("Nombre de zeros à enlever");
  	private JButton okBouton =new JButton("OK");


  	public VueChoixGrille(){
	    setTitle("Solveur Sudoku");
	    setSize(800,600);

	    top.setBackground(Color.white);
	    top.setLayout(new GridLayout(5,2));

	    jtext1 = new JTextField();
	    jtext2 = new JTextField();
	    jtext3 = new JTextField();

	    Font police = new Font("Arial",Font.BOLD,14);
	    jtext1.setFont(police);
	    jtext1.setPreferredSize(new Dimension (150,30));
	    jtext1.setForeground(Color.blue);
	    //l'ecouteur de clavier
	    jtext1.addKeyListener(new ClavierListener());

	    jtext2.setFont(police);
	    jtext2.setPreferredSize(new Dimension (150,30));
	    jtext2.setForeground(Color.blue);
	    //l'ecouteur de clavier
	    jtext2.addKeyListener(new ClavierListener());

	    jtext3.setFont(police);
	    jtext3.setPreferredSize(new Dimension (150,30));
	    jtext3.setForeground(Color.blue);
	    //l'ecouteur de clavier
	    jtext3.addKeyListener(new ClavierListener());

	    top.add(ligne);
	    top.add(jtext1);
	    top.add(colonne);
	    top.add(jtext2);
	    top.add(zeros);
	    top.add(jtext3);
	    top.add(okBouton,BorderLayout.SOUTH);

	    okBouton.addActionListener(this);

	    setContentPane(top);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    setVisible(true);
  	}



  	class ClavierListener implements KeyListener {

	    public void keyPressed(KeyEvent event){}
	    public void keyTyped(KeyEvent event){}

	    //Methode s'assurant que l'utilisateur tape bien des caracteres numeriques au clavier
	    public void keyReleased(KeyEvent event) throws NumberFormatException{
	      	JOptionPane jop = new JOptionPane();

	      	if(event.getSource()==jtext1){
	      		String c=jtext1.getText();
	      		if(!isNumeric(c) && !jtext1.getText().equals("")){
	      			jop.showMessageDialog(null,"Veuillez saisir un chiffre","Information", JOptionPane.INFORMATION_MESSAGE);
	      			jtext1.setText("");
	      		}
	    	}
		    if(event.getSource()==jtext2){
		        String c=jtext2.getText();
	      		if(!isNumeric(c) && !jtext2.getText().equals("")){
	      			jop.showMessageDialog(null,"Veuillez saisir un chiffre","Information", JOptionPane.INFORMATION_MESSAGE);
	      			jtext2.setText("");
	      		}
		    }
		    if(event.getSource()==jtext3){
		        String c=jtext3.getText();
	      		if(!isNumeric(c) && !jtext3.getText().equals("")){
	      			jop.showMessageDialog(null,"Veuillez saisir un chiffre","Information", JOptionPane.INFORMATION_MESSAGE);
	      			jtext3.setText("");
	      		}
		    }
	  	}
  	}

  	// verifie que le string entré est bien un chiffre
  	public boolean isNumeric(String s) throws NumberFormatException{
        try{
        	int i = Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
	    return true;
 	}

 	// verifie si le sudoku ne sera pas trop grand
  	public boolean isValid(int i, int j){
  		if(i*j<=25) return true;
  		else {
  			jtext1.setText("");
  			jtext2.setText("");
  			jtext3.setText("");
  			JOptionPane jop = new JOptionPane();
  			jop.showMessageDialog(null,"La multiplication de n et k doit être inférieur à 25","Information", JOptionPane.INFORMATION_MESSAGE);
  			return false;
  		}
  	}

  	//
    public void actionPerformed(ActionEvent e){
      	if (e.getSource() == okBouton)
  		{
	        int ligne = Integer.parseInt(jtext1.getText());
	        int colonne = Integer.parseInt(jtext2.getText());
	        int nbZeros = Integer.parseInt(jtext3.getText());
	        if(isValid(ligne,colonne)){
				try{
	            	GenererSudoku g1 = new GenererSudoku(ligne,colonne,nbZeros);
	              	VueSudoku vue=new VueSudoku(g1.getSudoku());
	        	    vue.pack();
	        	    vue.setVisible(true);
	        	    this.dispose();
	          		// if(ligne >= 3 || colonne >=3){ 
	          		// 	this.pack();
	          		// }
	          	}catch(IOException ex){
	            	System.out.println("Une erreur est survenue");
	          	}
        	}
      	}
    }


  	// private void pause(){
	  //   try{
	  //     	Thread.sleep(1000);
	  //   } catch (InterruptedException e){
	  //    	e.printStackTrace();
	  //   }
  	// }

}
