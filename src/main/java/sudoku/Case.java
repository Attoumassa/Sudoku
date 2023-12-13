package sudoku;

import java.util.*;

public class Case
{
	private int valeur;
	private ArrayList<Integer> solutions = new ArrayList<Integer>();
	private String raison="";
	private int num=0; // classement du remplissage des valeurs du sudoku

	// Constructeur qui attribue un numéro à la case.
	public Case(int n) {
		this.valeur = n;
	}

	// Getter et Setter
	public int getValeur() {
		return this.valeur;
	}
	public void setValeur(int n) {
		this.valeur = n;
		solutions = new ArrayList<Integer>();
	}

	// Indique si la case est vide.
	public boolean estVide() {
		return (valeur == 0);
	}

	public ArrayList<Integer> getSolutions(){
		return solutions;
	}
	public int tailleSolutions(){
		return solutions.size();
	}
	public String getRaison(){
		return raison;
	}
	public void setRaison(String s){
		raison=s;
	}
	public int getNum(){
		return num;
	}
	public void setNum(int n){
		num=n;
	}

	// Affichage
	public String toString() {
		return Integer.toString(this.valeur);
	}
	public String afficherSol(){
		String s="";
		for(int i=0;i<solutions.size();i++){
			s+=solutions.get(i)+" ";
		}
		return s;
	}

	// Modifie le tableau des solutions.
	public void modifSolutions(int n, boolean ajout) {
		boolean estPresent = false;
		// On parcourt solutions.
		for(int i = 0; i < solutions.size(); i++)	{
			// Si le nombre est déjà dans l'ArrayList,
			if(solutions.get(i)==n) {
				estPresent = true;
			}
		}
		// Si le nombre n'est pas déjà présent dans l'ArrayList,
		if(!estPresent && ajout)	{
			// On l'ajoute.
			solutions.add(n);
			// Si le nombre est présent et que le boolean est faux (donc je suis dans le cas de miseAJourSolutions)
		}else if(estPresent && !ajout){
			//J'enleve le nombre
			solutions.remove(new Integer(n));
		}
	}

	// // Modifie le tableau des raisons.
	// public void modifRaisons(String s) {
	// 	boolean estPresent = false;
	// 	// On parcourt raisons.
	// 	for(int i = 0; i < raisons.size(); i++) {
	// 		// Si la chaîne de caractères est déjà dans l'ArrayList,
	// 		if(raisons.get(i).equals(s)) {
	// 			estPresent = true;
	// 		}
	// 	}
	// 	// Si la chaîne de caractères n'est pas déjà présente dans l'ArrayList,
	// 	if(!estPresent) {
	// 		// On l'ajoute.
	// 		raisons.add(s);
	// 	}
	// }

}
