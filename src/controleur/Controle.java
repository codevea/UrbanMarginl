package controleur;
import vue.EntreeJeu;

public class Controle {

	private EntreeJeu frmEntreeJeu ;
	
	/**
	 * Constructeur
	 */
	private Controle() {
		this.frmEntreeJeu = new EntreeJeu() ;
		this.frmEntreeJeu.setVisible(true);
	}
	
	/**
	 * Méthode de démarrage
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();

	}

}
