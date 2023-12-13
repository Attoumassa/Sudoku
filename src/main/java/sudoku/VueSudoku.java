package sudoku;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

public class VueSudoku extends JFrame implements ActionListener {
	public PlateauGrille plateauGrille;
	private Grille g;
	private Grille gBasique; // pour comparer et savoir quelle case a été rempli
	private JButton backtrackingBouton = new JButton("Backtraking");
	private JButton inductionBouton = new JButton("Induction");
	private JButton genererBouton = new JButton("Générer une nouvelle Grille");
	private JLabel entete = new JLabel();
	private JLabel labelRaison = new JLabel ("Raison : ");

	public VueSudoku(Grille gr){
		g=gr;
		gBasique=new Grille(gr);
		plateauGrille = new PlateauGrille();

		setTitle("Solveur Sudoku");

		JPanel p1 = new JPanel();
		Color myBeige = new Color(249,231,183);
		p1.setBackground(myBeige);
		p1.add(plateauGrille);

		JPanel p2 = new JPanel();
		p2.setBackground(myBeige);
		p2.add(backtrackingBouton);
		p2.add(inductionBouton);
		p2.add(genererBouton);

		Color myBrown = new Color(188,118,26);

		JPanel p3=new JPanel();
	    p3.setBackground(myBrown);
	    int ligne = g.getN();
	    int colonne = g.getK();
	    entete.setText("Grille de taille "+(ligne*colonne)+"X"+(colonne*ligne)+ " dont bloc : "+ ligne+"X"+colonne);
	    p3.add(entete);

		backtrackingBouton.setBackground(myBrown);
		backtrackingBouton.setForeground(Color.white);
		inductionBouton.setBackground(myBrown);
		inductionBouton.setForeground(Color.white);
		genererBouton.setBackground(myBrown);
		genererBouton.setForeground(Color.white);

		getContentPane().add(p1,BorderLayout.CENTER);
		getContentPane().add(p2,BorderLayout.SOUTH);
		getContentPane().add(p3,BorderLayout.NORTH);

		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

		backtrackingBouton.addActionListener(this);
		inductionBouton.addActionListener(this);
		genererBouton.addActionListener(this);
	}

	//mettre a jour la fenetre avec le nouveau sudoku apres son remplissage
	public void nouvelAffichage(){
		getContentPane().removeAll();

		plateauGrille = new PlateauGrille();
		JPanel p1 = new JPanel();
		Color myBeige = new Color(249,231,183);
		
		p1.setBackground(myBeige);
		p1.add(plateauGrille);
		labelRaison.setForeground(Color.black);
		p1.add(labelRaison);

		JPanel p2 = new JPanel();
	    p2.add(genererBouton);

		Color myBrown = new Color(188,118,26);
	    JPanel p3=new JPanel();
	    p3.setBackground(myBrown);
	    int ligne = g.getN();
	    int colonne = g.getK();
	    entete.setText("Grille de taille "+(ligne*colonne)+"X"+(colonne*ligne)+ " dont bloc : "+ ligne+"X"+colonne);
	    p3.add(entete);

		getContentPane().add(p1,BorderLayout.CENTER);
	    getContentPane().add(p2,BorderLayout.SOUTH);
	    getContentPane().add(p3,BorderLayout.NORTH);
		getContentPane().validate();
	}

	//Méthode qui sera appelée lors d'un clic sur le Bouton
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == backtrackingBouton)
		{
			g.backtracking1(0,0);
			nouvelAffichage();
		}
		else if(e.getSource() == inductionBouton)
		{
			g.resolutionSudoku();
			nouvelAffichage();
		}
		else if(e.getSource() == genererBouton)
	    {
          	VueChoixGrille vue = new VueChoixGrille();
			vue.pack();
			vue.setVisible(true);
    	    this.dispose();
  		}

	}

	// Plateau d'une grille
	class PlateauGrille extends JPanel{

		CaseGrille[][] cases;

		public PlateauGrille(){
			JPanel[] tabPanel = new JPanel[g.getN()*g.getK()];
			this.cases = new CaseGrille[g.getN()*g.getK()][g.getN()*g.getK()];
			int k = 0;
			for(int i = 0; i < g.getN()*g.getK(); i++)
			{
				tabPanel[i] = new JPanel();
				tabPanel[i].setLayout(new BoxLayout(tabPanel[i],BoxLayout.LINE_AXIS));
				for(int j = 0; j < g.getN()*g.getK(); j++)
				{
					cases[i][j]= new CaseGrille(i,j,g.getGrille()[i][j].getValeur());
					tabPanel[i].add(cases[i][j]);
					tabPanel[i].setBackground(new Color(249,231,183));
				}
			}
			setBackground(new Color(249,231,183));
			setLayout(new GridLayout(g.getN()*g.getK(),g.getN()*g.getK()));
			for(int i=0;i<tabPanel.length;i++){
				this.add(tabPanel[i],BorderLayout.WEST);
			}
		}

		// modifie le label qui affiche la raison du case
		public void afficherRaison(CaseGrille c){
			if(gBasique.getGrille()[c.ligne][c.colonne].estVide()) {
				String ch=g.getGrille()[c.ligne][c.colonne].getRaison();
				int n=g.getGrille()[c.ligne][c.colonne].getNum();
				labelRaison.setText("<html>Raison : " + ch + "<br>&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp " 
									+ n + "ème case rempli</html>");
			}
		}

		class CaseGrille extends JPanel {
			int ligne;
			int colonne;
			int val;

			public CaseGrille(int l, int c, int v){
				this.ligne = l;
				this.colonne = c;
				this.val=v;
				Color myBrown = new Color(250,210,102);
				setBackground(myBrown);
				setPreferredSize(new Dimension(40,30));

				// (0,0,0,0) =(haut,gauche,bas,droite)
				// mettre les bordures au bon endroit pour que le sudoku soit lisible, séparation des blocs
				if((ligne) % g.getN() == 0){
					if((colonne) % g.getK() == 0){
						setBorder(BorderFactory.createMatteBorder(2, 2, 1, 1, Color.BLACK));
					}
					else if(colonne == g.getN()*g.getK()-1){
						setBorder(BorderFactory.createMatteBorder(2, 1, 1, 2, Color.BLACK));
					}
					else {
						setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK));
					}
				}
				else {
					if((colonne) % g.getK() == 0){
						if(ligne == g.getN()*g.getK()-1){
							setBorder(BorderFactory.createMatteBorder(1, 2, 2, 1, Color.BLACK));
						}
						else {
							setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, Color.BLACK));
						}
					}
					else if(colonne == g.getN()*g.getK()-1 && ligne == g.getN()*g.getK()-1){
						setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK));
					}
					else if(ligne == g.getN()*g.getK()-1){
						setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.BLACK));
					}
					else if(colonne == g.getN()*g.getK()-1){
						setBorder(BorderFactory.createMatteBorder(1, 1, 1, 2, Color.BLACK));
					}
					else {
						setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
					}
				}

				String ch="";
				if(val==0) ch=" ";
				else ch = String.valueOf(g.getGrille()[ligne][colonne]);
				JLabel label = new JLabel(ch);
				if(gBasique.getGrille()[ligne][colonne].estVide()) label.setForeground(Color.red);
				else label.setForeground(Color.black);
				this.add(label);

				// Ajoute un MouseListener (écouteur de souris) qui va
				// intercepter les clicks sur le Paneau
				addMouseListener(new MouseAdapter(){
					public void mouseEntered(MouseEvent e){
					afficherRaison((CaseGrille)e.getSource());
					}
				});
			}
		}	

	}

}
