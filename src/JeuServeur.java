import java.util.ArrayList;

/**
 * Gestion du jeu côté serveur
 *
 */
public class JeuServeur extends Jeu {

	/**
	 * Collection de murs
	 */
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>() ;
	/**
	 * Collection de joueurs
	 */
	private ArrayList<Joueur> lesJoueurs = new ArrayList<Joueur>() ;
	
	/**
	 * Constructeur
	 */
	public JeuServeur() {
	}
	
	@Override
	public void connexion() {
	}

	@Override
	public void reception() {
	}
	
	@Override
	public void deconnexion() {
	}

	/**
	 * Envoi d'une information vers tous les clients
	 * fais appel plusieurs fois à l'envoi de la classe Jeu
	 */
	public void envoi() {
	}

	/**
	 * Génération des murs
	 */
	public void constructionMurs() {
	}
	
}
