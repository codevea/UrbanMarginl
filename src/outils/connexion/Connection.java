package outils.connexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Gestion de la connexion entre 2 ordinateurs distants
 * @author emds
 *
 */
public class Connection extends Thread {
	
	/**
	 * canal d'entrée
	 */
	private ObjectInputStream in ;
	/**
	 * canal de sortie
	 */
	private ObjectOutputStream out ; 
	/**
	 * objet de lien avec une autre classe qui implémente AsyncResponse pour transférer les réponses
	 */
	private AsyncResponse delegate;

	/**
	 * Constructeur : crée une connexion à partir d'un socket (contenant les spécificités de l'ordinateur distant)
	 * @param socket objet de connexion de type serveur ou client
	 * @param delegate instance de la classe vers laquelle il faut transférer les réponses
	 */
	public Connection(Socket socket, AsyncResponse delegate) {
		this.delegate = delegate;
		// création du canal de sortie pour envoyer des informations
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream()) ;
		} catch (IOException e) {
			System.out.println("erreur création canal out : "+e);
			System.exit(0);
		}
		// création du canal d'entrée pour recevoir des informations
		try {
			this.in = new ObjectInputStream(socket.getInputStream()) ;
		} catch (IOException e) {
			System.out.println("erreur création canal in : "+e);
			System.exit(0);
		}
		// démarrage du thread d'écoute (attente d'un message de l'ordi distant)
		this.start() ;
		// envoi de l'instance de connexion vers la classe qui implémente AsyncResponse pour récupérer la réponse
		this.delegate.reception(this, "connexion", null);
	}
	
	/**
	 * Envoi d'un objet vers l'ordinateur distant, sur le canal de sortie
	 * @param unObjet contieny l'objet à envoyer
	 */
	public synchronized void envoi(Object unObjet) {
		// l'envoi ne peut se faire que si un objet delegate existe (pour récupérer la réponse)
		if(delegate != null) {
			try {
				this.out.reset();
				out.writeObject(unObjet);
				out.flush();
			} catch (IOException e) {
				System.out.println("erreur d'envoi sur le canal out : "+e);
			}
		}
	}
	
	/**
	 * Méthode thread qui permet d'attendre des messages provenant de l'ordi distant
	 */
	public void run() {
		// permet de savoir s'il faut continuer à écouter
		boolean inOk = true ;
		// objet qui va récupérer l'information reçue
		Object reception ;
		// boucle tant qu'il faut écouter
		while (inOk) {
			try {
				// réception d'un objet sur le canal d'entrée
				reception = in.readObject();
				// envoi de l'information reçue vers la classe qui implémente AsyncResponse pour récupérer la réponse
				delegate.reception(this, "reception", reception);
			} catch (ClassNotFoundException e) {
				// problème grave qui ne devrait pas se produire : arrêt du programme
				System.out.println("erreur de classe sur réception : "+e);
				System.exit(0);
			} catch (IOException e) {
				// envoi de l'information de déconnexion  vers la classe qui implémente AsyncResponse pour récupérer la réponse
				delegate.reception(this, "deconnexion", null);
				// demande d'arrêter de boucler sur l'attente d'une réponse
				inOk = false ;
				// l'ordinateur distant n'est plus accessible
				System.out.println("l'ordinateur distant est déconnecté");
				// fermeture du canal d'entrée
				try {
					in.close();
				} catch (IOException e1) {
					System.out.println("la fermeture du canal d'entrée a échoué : "+e);
				}
			}
		}
		
	}
	
}
