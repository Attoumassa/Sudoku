package sudoku;

import java.util.*;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class Grille {
	// Nombre de lignes.
	private int n;
	// Nombre de colonnes.
	private int k;

	private Case[][] grille;

	// Permet de savoir si un changement a été fait pendant la boucle lors de la résolution
	private boolean changement=true;

	// Donner l'ordre de chaque case remplie
	private int classement=1;  


	// Constructeur d'une grille de Sudoku depuis un fichier.
	// avec un fichier qui a déjà des 0
	public Grille(String chemin) throws FileNotFoundException, IOException {
 		FileReader fr = new FileReader(chemin);
 		BufferedReader br = new BufferedReader(fr);
 		String dim = br.readLine();
 		String[] tabDim = dim.split(" ");
 		this.n = Integer.parseInt(tabDim[0]);
 		this.k = Integer.parseInt(tabDim[1]);
 		grille = new Case[n*k][n*k];
 		int val1 = 0;
 		int val2 = 0; // si c'est un nombre et non un chiffre
		// Tableau qui aura toutes les valeurs sans les espaces et les virgules du fichier.
 		int[] tabValeur = new int[n*k*n*k];
 		int a = 0;
		// br.read() donne la valeur en code ASCII et 48 -> 0
 		while((val1 = br.read()) != -1) {
 			val1 = val1-48;
 			if(val1 >= 0 && val1 <= 9) {
 				val2 = br.read();
	 			while(val2 != -1 && val2-48>=0 && val2-48<=9){
	 				val2 = val2 - 48;
	 				val1=(int)val1 * 10 + (int)val2;
	 				val2 = br.read();
	 			}
	 			tabValeur[a] = (int)val1;
	 			a++;
 			}
 		}
		// remplir la grille avec tabValeur
 		for(int i = 0; i < n*k; i++) {
 			for(int j = 0; j < n*k; j++) {
 				grille[i][j] = new Case(tabValeur[i*n*k+j]);
 			}
 		}
	}
	// Constructeur d'une grille de Sudoku depuis un fichier.
	// avec un fichier qui contient une grille pleine
	public Grille(String chemin, int nbZeros) throws FileNotFoundException, IOException {
		this(chemin);
		viderGrille(nbZeros);
	}

	// copié une grille
    public Grille(Grille g) {
		this.n = g.n;
		this.k = g.k;
		this.grille = new Case[n*k][n*k];
		for(int i = 0; i < n*k; i++) {
		    for(int j = 0; j < n*k; j++) {
			this.grille[i][j] = new Case(g.grille[i][j].getValeur());
		    }
		}
    }

    // remplir une grille de 0
    public Grille(int n, int k) {
    	this.n=n;
    	this.k=k;
    	this.grille = new Case[n*k][n*k];
		for(int i = 0; i < n*k; i++) {
		    for(int j = 0; j < n*k; j++) {
			this.grille[i][j] = new Case(0);
		    }
		}
    }
    
	public int getK(){
		return this.k;
	}
	
	public int getN() {
		return this.n;
	}

	public Case[][] getGrille(){
		return this.grille;
	}

	// Affiche la grille de Sudoku.
	public void afficher() {
		traits();
		int taille = Integer.toString(n*k).length();
		for (int i = 0; i < n*k; i++) {
			System.out.print("|");
			for(int a = 0; a < taille ; a++){ 
				System.out.print(" ");
			}
			for (int j = 0; j < n*k;j++) {
				int tailleCase = grille[i][j].toString().length();
				System.out.print(grille[i][j]);
				for(int a = 0; a < taille - tailleCase + 1; a++){ //le bon nombre d'espace en fonction du nombre de chiffre
					System.out.print(" ");
				}
				if((j+1) % k == 0) {
					System.out.print("|");
					for(int a = 0; a < taille ; a++){
						System.out.print(" ");
					}
				}
			}
			for (int j = 0; j < n*k;j++) {
				System.out.print(grille[i][j].afficherSol() + " ");
				if((j+1) % k == 0) System.out.print("| ");
			}
			System.out.println();
			if((i+1) % n == 0) traits();
		}
	}

	// Affiche la déco.
	public void traits() {
		int taille = Integer.toString(n*k).length();
		for(int i = 0; i < (k+1)*n ; i++) {
			for(int j = 0; j < taille + 1; j++){
				System.out.print("-");
			}
		}
		System.out.println("-");
	}

    // Fonction qui place m zéros dans la grille de Sudoku.
	public void viderGrille(int m)
	{
		int taille = n*k;
		int v;
		int i;
		int j;
		int nbBacktracking=0;  
		// si le nombre de zeros est trop elever le backtrackingUNique sera toujours faux
		// et donc boucle infini, donc si le backtrackingUnique est faux plus de 5 fois a la suite boucle s'arrete
		// Tant qu'il en reste à enlever,
		while(m != 0 && nbBacktracking < 3)
		{
			// On choisit une case au hasard, mais la première ligne et colonne ne peuvent pas être atteinte
			Random random = new Random();
			i = random.nextInt(n*k-1) + 1;
			j = random.nextInt(n*k-1) + 1;

			// Si la valeur n'est pas zéro,
			if(grille[i][j].getValeur() != 0)
			{
				// On l'enlève en la stockant quelque part,
				v = grille[i][j].getValeur();
				grille[i][j].setValeur(0);
				// S'il n'y a qu'une seule solution au backtracking,
				if(backtrackingUnique())
				{
					// On fait m--.
					m--;
					nbBacktracking=0;
				}
				// Sinon,
				else
				{
					// On remet l'ancienne valeur.
					grille[i][j].setValeur(v);
					nbBacktracking++;
				}
			}
		}
	}

	// Indique si la grille est complète.
	public boolean isFull() {
		// On vérifie les lignes.
		for(int i = 0; i < n*k; i++) {
			if(!LigneFull(i)) return false;
		}
		// On vérifie les colonnes.
		for(int j = 0; j < n*k; j++) {
			if(!ColonneFull(j)) return false;
		}
		return true;
	}

	// Indique si le numéro appartient à la ligne.
	public boolean appartientLigne(int valeur, int ligne) {
		for(int j = 0; j < n*k; j++) {
			if(grille[ligne][j].getValeur() == valeur) {
				return true;
			}
		}
		return false;
	}

	// Indique si le numéro appartient à la colonne.
	public boolean appartientColonne(int valeur, int colonne) {
		for(int i = 0; i < n*k; i++) {
			if(grille[i][colonne].getValeur() == valeur) {
				return true;
			}
		}
		return false;
	}


	// Indique si le numéro appartient au bloc.
	public boolean appartientBloc(int valeur , int ligne , int colonne) {
        int l = ligne -(ligne%n);
		int c = colonne -(colonne%k); 
		for(int i = l; i < l+n; i++) {
			for(int j = c; j < c+k; j++) {
				if(grille[i][j].getValeur() == valeur) return true;
			}
		}
        return false;
    }


	// Indique si la ligne i est complète.
	public boolean LigneFull(int i) {
		for(int j = 0; j < n*k; j++) {
			if(grille[i][j].estVide()) return false;
		}
		return true;
	}

	// Indique si la colonne j est complète.
	public boolean ColonneFull(int j) {
		for(int i = 0; i < n*k; i++) {
			if(grille[i][j].estVide()) return false;
		}
		return true;
	}

	// Indique si le bloc numBloc est complet.
	public boolean BlocFull(int ligne , int colonne) {
		int l = ligne -(ligne%n);
		int c = colonne -(colonne%k); 
		for(int i = l; i < l+n; i++) {
			for(int j = c; j < c+k; j++) {
				if(grille[i][j].estVide()) return false;
			}
		}
		return true;
	}


	// REGLES

	//REGLE 1

	// Remplir le tableau de solutions d'une case, a utiliser qu'au premier tour
	public void regle1(int ligne, int colonne) {
		for(int i = 0; i < n*k; i++) {
			if(!appartientColonne(i+1, colonne) && !appartientLigne(i+1, ligne) && !appartientBloc(i+1, ligne, colonne)) {
				grille[ligne][colonne].modifSolutions(i+1, true);
			    changement=true;
			}
		}
	}

	//REGLE 2

	// Changer la valeur de la case par la seule valeur qui se trouve dans son tableau de solutions.
	public void regle2(int ligne, int colonne) {
		if(grille[ligne][colonne].tailleSolutions() == 1) {
			grille[ligne][colonne].setValeur(grille[ligne][colonne].getSolutions().get(0));
			miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
			grille[ligne][colonne].setRaison("Règle 2 : La case ne pouvait prendre aucun autre chiffre.");
			grille[ligne][colonne].setNum(classement);
			classement++;
	        changement=true;
		}

	}
	// Mettre à jour les tableaux de solutions possibles des cases de la ligne, de la colonne, et du bloc.
	public void miseAJourSolutions(int valeur, int ligne, int colonne) {
		for(int i = 0; i < n*k; i++) {
			// Suppression de la valeur des cases de la colonne correspondante.
			Case c1 = grille[ligne][i];
			if(i != colonne ) {
				c1.modifSolutions(valeur, false);
			}
			// Suppression de la valeur des cases de la ligne correspondante.
			Case c2=grille[i][colonne];
			if(i != ligne) {
				c2.modifSolutions(valeur, false);
			}
		}
		// Suppression de la valeur des cases du bloc correspondant.
		int l = ligne -(ligne%n);
		int c = colonne -(colonne%k); 
		for(int i = l; i < l+n; i++) {
			for(int j = c; j < c+k; j++) {
				grille[i][j].modifSolutions(valeur,false);
			}
		}
	}

	//REGLE 3

	// Indique s'il ne manque qu'un seul nombre dans la ligne i.
	// public boolean lignePresqueFull(int i) {
	// 	int d = 0;
	// 	for(int j = 0; j < n*k; j++) {
	// 		if(grille[i][j].estVide()) d++;
	// 	}
	// 	if(d == 1) return true;
	// 	return false;
	// }
	// // Indique s'il ne manque qu'un seul nombre dans la colonne j.
	// public boolean colonnePresqueFull(int j) {
	// 	int d = 0;
	// 	for(int i = 0; i < n*k; i++) {
	// 		if(grille[i][j].estVide()) d++;
	// 	}
	// 	if(d == 1) return true;
	// 	return false;
	// }
	// // Indique s'il ne manque qu'un seul nombre dans le bloc b.
	// public boolean BlocPresqueFull(int ligne, int colonne) {
	// 	int l= ligne -(ligne%n);
	// 	int c= colonne - (colonne%k);
	// 	int d=0;
	// 	for (int i=l;i<l+n;i++){
	// 		for(int j=c;j<c+k;j++){
	// 			if(grille[i][j].estVide()) d++;
	// 		}
	// 	}
	// 	if(d == 1) return true;
	// 	return false;
	// }

    // Fonction qui renvoie l'unique chiffre manquant de la ligne.
	public int numLigneSeule(int i) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int j = 0; j < n*k; j++) {
			a.add(new Integer(j+1));
		}
		for(int j = 0; j < n*k; j++) {
			if(grille[i][j].getValeur() != 0) {
				if(!grille[i][j].estVide()) a.remove(new Integer(grille[i][j].getValeur()));
			}
		}
		if(a.size() == 1) return a.get(0);
		else return -1;
	}
	// Fonction qui renvoie l'unique chiffre manquant de la colonne.
	public int numColonneSeule(int j) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int i = 0; i < n*k; i++) {
			a.add(new Integer(j+1));
		}
		for(int i = 0; i < n*k; i++) {
			if(grille[i][j].getValeur() != 0) {
				if(!grille[i][j].estVide()) a.remove(new Integer(grille[i][j].getValeur()));
			}
		}
		if(a.size() == 1) {
			return a.get(0);
		}
		else return -1;
	}
	// Fonction qui renvoie l'unique chiffre manquant du bloc.
	public int numBlocSeule(int ligne, int colonne) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int i = 0; i < n*k; i++) {
			a.add(new Integer(i+1));
		}
		int l = ligne - (ligne%n);
		int c = colonne - (colonne%k); 
		for(int i = l; i < l+n; i++) {
			for(int j = c; j < c+k; j++) {
				if(!grille[i][j].estVide()) a.remove(new Integer(grille[i][j].getValeur()));
			}
		}
		if(a.size() == 1) {
			return a.get(0);
		}
		else return -1;
	}

	// Remplit l'unique chiffre manquant d'une ligne, d'une colonne, ou d'un bloc.
    public void regle3(int ligne, int colonne) {
    	if(grille[ligne][colonne].estVide()){
    		int num1 = numLigneSeule(ligne);
    		int num2 = numColonneSeule(colonne);
    		int num3 = numBlocSeule(ligne, colonne);
    		if(num1 != -1){ 
    			grille[ligne][colonne].setValeur(num1);
    			miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
    			grille[ligne][colonne].setRaison("Règle 3 : C'était le seul chiffre manquant dans la ligne.");
    			grille[ligne][colonne].setNum(classement);
				classement++;
    	        changement=true;
    		}
    		else if(num2 != -1) { 
    			grille[ligne][colonne].setValeur(num2);
    			miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
    			grille[ligne][colonne].setRaison("Règle 3 : C'était le seul chiffre manquant dans la colonne.");
    			grille[ligne][colonne].setNum(classement);
				classement++;
    		    changement=true;
    		}
    		else if(num3 != -1) { 
    			grille[ligne][colonne].setValeur(num3);
    			miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
    			grille[ligne][colonne].setRaison("Règle 3 : C'était le seul chiffre manquant dans le bloc.");
    			grille[ligne][colonne].setNum(classement);
				classement++;
    		    changement=true;
    		}
    	}
    }
    
    // RÈGLE 4 

	// regarde pour la colonne si les tableaux de solution contiennent valeur 
	public boolean valeurSeuleColonne(int val, int ligne, int colonne) {
		for(int j = 0; j < n*k; j++) {
			if(j!=colonne){
				if(grille[ligne][j].getSolutions().contains(val)) return false;
			}
		}
		return true;
	}
	// regarde pour la ligne si les tableaux de solution contiennent valeur 
	public boolean valeurSeuleLigne(int val, int ligne, int colonne) {
		for(int i = 0; i < n*k; i++) {
			if(i!=ligne) {
				if(grille[i][colonne].getSolutions().contains(val)) return false;
			}
		}
		return true;
	}
	// regarde pour le bloc si les tableaux de solution contiennent valeur 
	public boolean valeurSeuleBloc(int val, int ligne, int colonne) {
		int l = ligne - (ligne%n);
		int c = colonne - (colonne%k); 
		for(int i = l; i < l+n; i++) {
			for(int j = c; j < c+k; j++) {
				if(!(i==ligne && j==colonne)) {
					if(grille[i][j].getSolutions().contains(val)) return false;
				}
			}
		}
		return true;
	}

	// si un chiffre ne se trouve que dans un seul tableau de solution alors on le met dans le sudoku
	public void regle4(int ligne, int colonne) {
		if(grille[ligne][colonne].estVide()){
			boolean b = false;
			for(int i = 0; i < grille[ligne][colonne].tailleSolutions(); i++) {
				b = valeurSeuleColonne(grille[ligne][colonne].getSolutions().get(i), ligne, colonne);
				if(b) {
					grille[ligne][colonne].setValeur(grille[ligne][colonne].getSolutions().get(i));
					miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
					grille[ligne][colonne].setRaison("Règle 4 : Le chiffre ne pouvait être nulle part ailleurs dans le bloc.");
					grille[ligne][colonne].setNum(classement);
					classement++;
				    changement=true;
				}
				else {
					b = valeurSeuleLigne(grille[ligne][colonne].getSolutions().get(i), ligne, colonne);
					if(b) {
						grille[ligne][colonne].setValeur(grille[ligne][colonne].getSolutions().get(i));
						miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
						grille[ligne][colonne].setRaison("Règle 4 : Le chiffre ne pouvait être nulle part ailleurs dans le bloc.");
                        grille[ligne][colonne].setNum(classement);
						classement++;
                        changement=true;
					}
					else {
						b = valeurSeuleBloc(grille[ligne][colonne].getSolutions().get(i), ligne, colonne);
						if(b) {
							grille[ligne][colonne].setValeur(grille[ligne][colonne].getSolutions().get(i));
							miseAJourSolutions(grille[ligne][colonne].getValeur(),ligne,colonne);
							grille[ligne][colonne].setRaison("Règle 4 : Le chiffre ne pouvait être nulle part ailleurs dans le bloc.");
                            grille[ligne][colonne].setNum(classement);
							classement++;
                            changement=true;
						}
					}
				}
			}	
		}
	}


    // REGLE 5

    // retourne true si on peut appliquer la regle5 pour cette case
    // et cette valeur sur la ligne
    public boolean regle5LigneAux(int val, int ligne, int colonne){
		int l = ligne -(ligne%n);
		int c = colonne -(colonne%k);
		// on parcourt le bloc
		for(int i=l; i<l+n; i++){
		    for(int j=c; j<c+k; j++){
			// si la case contient val dans ses solutions et que son num
			// de ligne est différent de 'ligne'
				if(grille[i][j].getSolutions().contains(val) && i!=ligne){
				    return false;
				}
		    }
		}
		return true;
    }
    // retourne true si on peut appliquer la regle5 pour cette case
    // et cette valeur sur la colonne
    public boolean regle5ColonneAux(int val, int ligne, int colonne){
		int l = ligne -(ligne%n);
		int c = colonne -(colonne%k);
		// on parcourt le bloc
		for(int i=l; i<l+n; i++){
		    for(int j=c; j<c+k; j++){
			// si la case contient val dans ses solutions et que son num
			// de colonne est différent de 'colonne'
				if(grille[i][j].getSolutions().contains(val) && j!=colonne){
				    return false;
				}
		    }
		}
		return true;
    }
    
    // Supprime 'val' dans les solutions de chaque case de la ligne
    // en dehors du bloc
    public void supprimerValeurLignePasBloc(int val, int ligne, int colonne){
		int min = colonne -(colonne%k);
		int max = min+k-1;
		for(int j=0; j<n*k; j++){
		    if((j<min || j>max) && grille[ligne][j].getSolutions().contains(val)){
				grille[ligne][j].modifSolutions(val,false);
				changement = true;
		    }
		}
    }
    // Supprime 'val' dans les solutions de chaque case de la colonne
    // en dehors du bloc
    public void supprimerValeurColonnePasBloc(int val, int ligne, int colonne){
		int min = ligne -(ligne%n);
		int max = min+n-1;
		for(int j=0; j<n*k; j++){
		    if((j<min || j>max) && grille[j][colonne].getSolutions().contains(val)){
				grille[j][colonne].modifSolutions(val,false);
				changement = true;
		    }
		}
    }

    // si dans une région une valeur se trouve seulement sur une ligne ou une colonne, 
    // mais par dans les autres régions, alors on peut enlever cette valeur du bloc dans les autre lignes/colonnes
    public void regle5(int ligne, int colonne){
		ArrayList<Integer> solutions = grille[ligne][colonne].getSolutions();
		for(int i=0; i<solutions.size(); i++){
		    int val = solutions.get(i);
		    if(regle5LigneAux(val, ligne, colonne)){
				supprimerValeurLignePasBloc(val, ligne, colonne);
		    }
		    if(regle5ColonneAux(val, ligne, colonne)){
				supprimerValeurColonnePasBloc(val, ligne, colonne);
		    }
		}
    }
				
	// REGLE 6

	public void regle6Ligne(int val, int ligne, int colonne){
		int l = ligne - (ligne%n);
		int c = colonne - (colonne%k); 
		boolean b = false;
		for(int j = c; j < c+k; j++){
			if(j!=colonne){
				if(grille[ligne][j].getSolutions().contains(val)) {
					b=true;
				}
			}
		}
		// si val se trouve dans deux tableau de solution de ligne
		if(b){
			boolean bLigne = false;
			// regarde si le reste de la ligne contient val dans les solutions
			for(int j = 0; j < n*k; j++){
				if(!(j >= c && j < c+k)){
					if(grille[ligne][j].getSolutions().contains(val)) {
						bLigne = true;
					}
				}
			}
			// si aucune case n'a val on enleve val du bloc initial sur les autres lignes
			if(!bLigne){
				for(int i = l; i < l+n; i++) {
					if (i != ligne){
						for(int j = c; j < c+k; j++) {
							if(grille[i][j].getSolutions().contains(val)){
								grille[i][j].modifSolutions(val,false);
								changement=true;
							}
						}
					}
				}
			}
		}
	}
	public void regle6Colonne(int val, int ligne, int colonne){
		int l = ligne - (ligne%n);
		int c = colonne - (colonne%k); 
		boolean b = false;
		// regarde val est en double sur la colonne
		for(int i = l; i < l+n; i++){
			if(i!=ligne){
				if(grille[i][colonne].getSolutions().contains(val)) {
					b=true;
				}
			}
		}
		// si val se trouve dans deux tableau de solution de colonne
		if(b){
			boolean bColonne = false;
			// regarde si le reste de la colonne contient val dans les solutions
			for(int i = 0; i < n*k; i++){
				if(!(i >= l && i < l+n)){
					if(grille[i][colonne].getSolutions().contains(val)) {
						bColonne = true;
					}
				}
			}
			// si aucune case n'a val on enleve val du bloc initial sur les autres colonnes
			if(!bColonne){
				for(int i = l; i < l+n; i++) {
					for(int j = c; j < c+k; j++) {
						if(j != colonne){
							if(grille[i][j].getSolutions().contains(val)){
								grille[i][j].modifSolutions(val,false);
								changement=true;
							}
						}
					}
				}
			}
		}
	}

	// si, à l’intérieur d’une Région, deux ou trois chiffres identiques figurent dans une Ligne et 
	// qu’ils ne figurent pas ailleurs sur la même Ligne dans les autres Régions, 
	// on peut alors supprimer sans autre ce chiffre dans la Région où l’on se trouve
	public void regle6(int ligne, int colonne){
		if(grille[ligne][colonne].estVide()){
			// regarde pour chaque chiffre du tableau des solutions
			for(int a = 0; a < grille[ligne][colonne].tailleSolutions(); a++) {
				regle6Ligne(grille[ligne][colonne].getSolutions().get(a),ligne,colonne);
				regle6Colonne(grille[ligne][colonne].getSolutions().get(a),ligne,colonne);
			}
		}
	}

	// REGLE 8

	// lorsque plus de changement miniBactraking sur une case qui n'a que 2 solutions possibles
	public void miniBacktracking() {
		int l=0;
		int c=0;
		boolean b=false; // pour sortir de la boucle
		for(int i=0;i<n*k;i++) {
			for(int j=0;j<n*k;j++) {
				if(grille[i][j].tailleSolutions()!=0) {
					l=i;
					c=j;
					b=true;
					break;
				}
			}
			if(b) break ;
		}
		changement = true;
		ArrayList<Integer> tabSol=grille[l][c].getSolutions();
		for(int i=0;i<tabSol.size();i++) {
			Grille g=new Grille(this);
			g.grille[l][c].setValeur(tabSol.get(i));
			g.miseAJourSolutions(tabSol.get(i),l,c);
			g.resolutionSudoku();
			if(g.isFull()){
				grille=g.grille;
				break;
			}
		}
	}


	public void resolutionSudoku() {
		int a=0; // evite de faire la regle4 et 6 au premier tour, il faut d'abord que les cases aient leur solution avant de pouvoir appliquer la regle4 et 6
		boolean b=false; // pour sortir du while si le backtracking amene une erreur
		while(changement && !isFull()) { 
			// cela va nous aider pour le mini bactracking, si il n'y a pas eu de changement on fait backtraking
			// mais avant d'avoir faire minBacktracking ca va permettre d'arreter le programme pour eviter les boucles infinies
			changement=false; 
			for(int i=0;i<n*k;i++) {
				for(int j=0;j<n*k;j++) {
					if(a!=0 && grille[i][j].estVide() && grille[i][j].tailleSolutions()==0){
						b=true;
						break;	
					}
					if(grille[i][j].estVide()){
						if (a==0) regle1(i,j);
						regle2(i,j);
						regle3(i,j);
						if(a!=0) {
						   regle4(i,j);
						   regle6(i,j);
						}
					}
				}
				if(b) break;
			}
			if(b) break;
			if(!changement) {
				miniBacktracking();
			}
			a++;
		}
	}

	// BACKTRACKING

    public boolean backtracking1(int numLigne, int numColonne) {
      // la 1ère ligne est numérotée 0 donc la dernière n*k-1
      // pareil pour les colonnes
		if(numLigne>n*k-1) return true;
		int next_numLigne = numLigne;
		int next_numColonne = numColonne;
		if(numColonne<n*k-1)
		{
			next_numColonne++;
		}
		else{
			next_numColonne=0;
			next_numLigne++;
		}
    	if(!grille[numLigne][numColonne].estVide())
    	{
			//	System.out.println("Juste avant l'appel recursif");
      		return backtracking1(next_numLigne, next_numColonne);
    	}
    	else{
			for(int i=1; i<=n*k; i++)
			{
				if(!appartientLigne(i,numLigne) && !appartientColonne(i,numColonne)
				&& !appartientBloc(i,numLigne,numColonne))
				{
					grille[numLigne][numColonne].setValeur(i);
					if(backtracking1(next_numLigne,next_numColonne))
					{
						return true;
					}
				}
			}
			grille[numLigne][numColonne].setValeur(0);
			return false;
		}
	}


     public boolean backtracking2(int numLigne, int numColonne){
      // la 1ère ligne est numérotée 0 donc la dernière n*k-1
      // pareil pour les colonnes
		if(numLigne>n*k-1) return true;
		int next_numLigne = numLigne;
		int next_numColonne = numColonne;
		if(numColonne<n*k-1)
		{
			next_numColonne++;
		}
		else{
			next_numColonne=0;
			next_numLigne++;
		}
    	if(!grille[numLigne][numColonne].estVide())
    	{
			//	System.out.println("Juste avant l'appel recursif");
      		return backtracking2(next_numLigne, next_numColonne);
    	}
    	else{
			for(int i=n*k; i>=1; i--)
			{
				if(!appartientLigne(i,numLigne) && !appartientColonne(i,numColonne)
				&& !appartientBloc(i,numLigne,numColonne))
				{
					grille[numLigne][numColonne].setValeur(i);
					if(backtracking2(next_numLigne,next_numColonne))
					{
						return true;
					}
				}
			}
			grille[numLigne][numColonne].setValeur(0);
		//	System.out.println("le dernier false");
			return false;
		}
	}

    public boolean backtrackingUnique() {
		Grille g1 = new Grille(this);
		Grille g2 = new Grille(this);
		g1.backtracking1(0,0);
		g2.backtracking2(0,0);
		for(int i = 0; i < n*k; i++) {
		    for(int j = 0; j < n*k; j++) {
			if(g1.grille[i][j].getValeur() != g2.grille[i][j].getValeur()) {
				return false;
			    }
		    }
		}
		return true;
    }

}
