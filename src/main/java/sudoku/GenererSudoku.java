package sudoku;

import java.util.*;
import java.lang.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GenererSudoku{

	private Grille sudoku;
	private int n;
	private int k;
	private static int nbSudoku=24;
	private int num;

	public GenererSudoku(int n, int k, int nbZeros) throws  IOException{
		num=nbSudoku;
		nbSudoku++;
		this.n=n;
		this.k=k;
		sudoku=new Grille(n,k);
		remplirGrille();
		produitGrillePleineAleatoire(60);
		viderGrille(nbZeros);
		enregistrerFichier();
	}

	public Grille getSudoku(){
		return this.sudoku;
	}

    public void enregistrerFichier() throws  IOException{
    	
		File f=new File("FichierSudoku/SudokuGenererZeros/sudoku_"+num+".txt"); 
		f.createNewFile();
		FileWriter fw=new FileWriter(f);
		fw.write(n+" "+k);  
		fw.write("\n"); 
		for(int i=0;i<n*k;i++){
			for(int j=0;j<n*k;j++){
				fw.write(sudoku.getGrille()[i][j].getValeur()+", ");
			}
		}
		fw.close(); // fermer le fichier à la fin des traitements

    }

	public void remplirGrille(){
		int taille =n*k;
		int[] ta=permutation(taille);
		for(int i=0;i<taille;i++){
			for(int j=0;j<taille;j++){
				sudoku.getGrille()[i][j].setValeur(ta[j]);
			}

			if(i!=0 && i%n==1){
				deplacement(ta);
				echangeK(ta);
			}
			else{
				deplacement(ta);
			}
			
		}
	}
	//permutation pour faire un sudoku plus melanger
	public int[] permutation(int m) {
    	int[] t=new int[m];
    	for(int i=0;i<m;i++){
			t[i]=i+1;
		}
		for(int i=0;i<m;i++){
			int r=(int)(Math.random()*(m));
			if(i!=r){
				int tmp=t[i];
				t[i]=t[r];
				t[r]=tmp;
			}
		}
		return t;
    }
	// decale le tableau de chiffre par k : 123456 -> 345612 si k=2
	public void deplacement(int[] t){
		int[] tab=new int[k];
		for(int i=0;i<k;i++){
			tab[i]=t[i];
		}
		for(int i=0;i<t.length-k;i++){
			int c=t[i+k];
			t[i]=c;
		}
		for(int i=0;i<k;i++){
			t[i+t.length-k]=tab[i];
		}
	}
	//decale les chiffre entre k : 123456 -> 214365 si k=2
	public void echangeK(int[] t){
		for(int i=0;i<n;i++){
			int a=t[i*k];
			for(int j=i*k;j<(i+1)*k-1;j++){
				int c=t[j+1];
				t[j]=c;
			}
			t[(i+1)*k-1]=a;
		}
	}

    public void echangeLigne(int l,int m){
		for (int j=0;j<n*k;j++){
		    int tmp=sudoku.getGrille()[l][j].getValeur();
		    sudoku.getGrille()[l][j].setValeur(sudoku.getGrille()[m][j].getValeur());
		    sudoku.getGrille()[m][j].setValeur(tmp);
		}
    }

    public void echangeColonne(int l,int m){
		for (int i=0;i<n*k;i++){
			int tmp=sudoku.getGrille()[i][l].getValeur();
		    sudoku.getGrille()[i][l].setValeur(sudoku.getGrille()[i][m].getValeur());
		    sudoku.getGrille()[i][m].setValeur(tmp);
		}
    }

    public static Random rand = new Random () ;
    public static int randRange ( int a , int b ) {
		return rand . nextInt (b - a ) + a ;
    }

    public void echangeLigneAleatoire(){
		int c=randRange(0,k);
		int a=randRange(0,n)+n*c;
		int b=randRange(0,n)+n*c;
		while(a==b){
		    b=randRange(0,n)+n*c; 
		}
		echangeLigne(a,b);
    }

    public void echangeColonneAleatoire(){
		int c=randRange(0,n);
		int a=randRange(0,k)+k*c;
		int b=randRange(0,k)+k*c;
		while(a==b){
		    b=randRange(0,k)+k*c; 
		}
		echangeColonne(a,b);
    }

    public void produitGrillePleineAleatoire(int l){
		for (int i=0;i<l;i++){
		    int x=randRange(0,2);
		    if(x==0) echangeLigneAleatoire();
		    else echangeColonneAleatoire();
		}
    }

    public void viderGrille(int nbZeros){
    	sudoku.viderGrille(nbZeros);
    }
  
}