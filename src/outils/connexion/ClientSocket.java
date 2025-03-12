package outils.connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * Gestion d'un client : création d'une connexion cliente
 * @author emds
 *
 */
public class ClientSocket {
	
	/**
	 * Constructeur : crée le socket de type client pour se connecter à un serveur (avec son ip et port d'écoute)
	 * @param delegate instance de la classe vers laquelle il faut transférer les réponses
	 * @param ip adresse IP du serveur
	 * @param port numéro port d'écoute du serveur
	 */
	public ClientSocket (AsyncResponse delegate, String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			System.out.println("connexion serveur réussie");
			// la connexion ne peut se faire que si un objet delegate existe (pour récupérer la réponse)
			if(delegate != null) {
				// création d'une connexion pour ce client, pour la communication avec le serveur (envoi et réception d'informations)
				new Connection(socket, delegate) ;
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "serveur non disponible");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IP incorrecte");
		}
	}
	
}
