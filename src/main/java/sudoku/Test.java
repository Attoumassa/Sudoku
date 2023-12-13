package sudoku;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class Test{

	public Test(String s) throws FileNotFoundException, IOException{
		Grille g = new Grille(s);
	    //ModelSudoku modele = new ModelSudoku(g);
	    VueSudoku vue=new VueSudoku(g);
	    vue.pack();
	    vue.setVisible(true);
		// VueChoixGrille vue = new VueChoixGrille();
		// vue.pack();
		// vue.setVisible(true);
  	}

	public static void main(String[] args) throws FileNotFoundException, IOException{
	    // Grille g1 = new Grille("FichierSudoku/SudokuGenererZeros/sudoku_1.txt");
	    // g1.afficher();
	    //System.out.println(g1.backtrackingUnique());
		//g1.backtracking1(0,0);
		//g1.afficher();
	    // g1.resolutionSudoku();
	    // g1.afficher();
	    // System.out.println();
	    // Grille g2 = new Grille("FichierSudoku/Grilles/4x4/Grille1.txt",200);
	    // g2.afficher();
	    // System.out.println(g2.backtrackingUnique());
	    // g2.resolutionSudoku();
	    // g2.afficher();
	    // System.out.println();
	    // Grille g3 = new Grille("FichierSudoku/TestPrecisRegle/sudoku7.txt");
	    // g3.afficher();
	    // System.out.println(g3.backtrackingUnique());
	    // g3.resolutionSudoku();
	    // g3.afficher();
	    // System.out.println();
    	// Grille g2 = new Grille("FichierSudoku/sudokuPlusieursSols.txt");
	    // g2.afficher();
	    // System.out.println(g2.backtrackingUnique());
	    // g2.afficher();

		// GenererSudoku s1=new GenererSudoku(2,3,10);
		// s1.getSudoku().afficher();
		// GenererSudoku s2=new GenererSudoku(5,4,100);
		// s2.getSudoku().afficher();
		// GenererSudoku s3=new GenererSudoku(2,2,60);
		// s3.getSudoku().afficher();
		// GenererSudoku s4=new GenererSudoku(4,4,1000);
		// s4.getSudoku().afficher();
		// GenererSudoku s5=new GenererSudoku(5,4,150);
		// s5.getSudoku().afficher();
		// GenererSudoku s6=new GenererSudoku(8,8,100);
		// s6.getSudoku().afficher();

		Test t=new Test("FichierSudoku/SudokuGenererZeros/sudoku_3.txt");
	}




}
