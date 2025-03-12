package outils.son;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.Serializable;

import outils.son.ecouteurs.EcouteurSon;
import outils.son.exceptions.SonErreurDiverse;
import outils.son.exceptions.SonErreurLecture;
import outils.son.exceptions.SonException;
import outils.son.exceptions.SonIntrouvableException;
import outils.son.exceptions.SonTypeException;

/**
 * Représente un son. <br>
 * Les sons les mieux suppoter par Java sont MIDI, AU et certains WAV. <br>
 * Pour savoir si un son est accèpté, le seul moyen est d'essayer. <br>
 * Si le son ne sera plus utilisé, il faut le détruire par la méthode fermer, <br>
 * afin de libérer la mémoire et le flux où se trouve le son. <br>
 * Attention, une fois détruit, le son n'est plus utilisable. <br>
 */

public class Sound
    implements Serializable, Runnable
{
  //Durée du son
  private Duree duree;
  //Flux de kecture audio
  private AudioInputStream lecteurAudio;
  //Format du fichier audio
  private AudioFileFormat formatFichier;
  //Format du son
  private AudioFormat format;
  //Clip jouant le son
  private Clip clip;
  //Thread permettant de jouer le son en tâche de fond
  private Thread thread;
  //Nombre de boucle restante à effectué
  private int tour;
  //pause : inqique si le son est en pause ou non
  //fermerALaFin : indique si le son doit être détruit une fois la derniére boucle de son exécutée
  private boolean pause, fermerALaFin;
  //Ecouteurs des événement sons
  private Vector ecouteurs = new Vector();
  /**
   * Construit un son situé à une URL précise
   * @param url URL du son
   * @throws SonException Si il y a un probléme de construction du son
   */
  public Sound(URL url)
      throws SonException
  {
    this.initialise(url);
  }
  /**
   * Construit un son à partir d'un fichier
   * @param fichier Fichier contenant le son
   * @throws SonException Si il y a un problème de construction du son
   */
  public Sound(File fichier)
      throws SonException
  {
    if(!fichier.exists())
    {
      throw new SonIntrouvableException(fichier);
    }
    this.initialise(fichier);
  }
  //Initialise le son
  private void initialise(File fichier)
      throws SonException
  {
    try
    {
      //Crée le flux
      this.lecteurAudio = AudioSystem.getAudioInputStream(fichier);
      //Récupére le format du fichier son
      this.formatFichier = AudioSystem.getAudioFileFormat(fichier);
      //Récupére le format de codage du son
      this.format = lecteurAudio.getFormat();

      //On ne peut pas ouvrir directement des format ALAW/ULA, il faut les convertir en PCM
      if((this.format.getEncoding() == AudioFormat.Encoding.ULAW) ||
         (this.format.getEncoding() == AudioFormat.Encoding.ALAW))
      {
        //convertion du format
        AudioFormat tmp = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            this.format.getSampleRate(),
            this.format.getSampleSizeInBits() * 2,
            this.format.getChannels(),
            this.format.getFrameSize() * 2,
            this.format.getFrameRate(),
            true);
        //convertion du flux
        this.lecteurAudio = AudioSystem.getAudioInputStream(tmp,
                                                            this.lecteurAudio);
        //On a convertit le format, si bien qu'il change
        this.format = tmp;
      }
      //On crée une information avec le format du flux et en caculant la logueneur totale du son
      DataLine.Info info = new DataLine.Info(
          Clip.class,
          this.lecteurAudio.getFormat(),
          ((int)this.lecteurAudio.getFrameLength() *
           this.format.getFrameSize()));
      //Grac à cette information, on peut creer un clip
      this.clip = (Clip)AudioSystem.getLine(info);
      //On ouvre le son
      reouvrir();
    }
    catch(UnsupportedAudioFileException uafe)
    {
      throw new SonTypeException();
    }
    catch(IOException ioe)
    {
      throw new SonErreurLecture();
    }
    catch(Exception e)
    {
      throw new SonErreurDiverse(e);
    }

    //On calcul la durée du son en microseconde
    this.duree = new Duree(this.longueurSonMicroseconde());
  }
  private void initialise(URL url)
      throws SonException
  {
    try
    {
      //Crée le flux
      this.lecteurAudio = AudioSystem.getAudioInputStream(url);
      //Récupére le format du fichier son
      this.formatFichier = AudioSystem.getAudioFileFormat(url);
      //Récupére le format de codage du son
      this.format = lecteurAudio.getFormat();

      //On ne peut pas ouvrir directement des format ALAW/ULA, il faut les convertir en PCM
      if((this.format.getEncoding() == AudioFormat.Encoding.ULAW) ||
         (this.format.getEncoding() == AudioFormat.Encoding.ALAW))
      {
        //convertion du format
        AudioFormat tmp = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            this.format.getSampleRate(),
            this.format.getSampleSizeInBits() * 2,
            this.format.getChannels(),
            this.format.getFrameSize() * 2,
            this.format.getFrameRate(),
            true);
        //convertion du flux
        this.lecteurAudio = AudioSystem.getAudioInputStream(tmp,
                                                            this.lecteurAudio);
        //On a convertit le format, si bien qu'il change
        this.format = tmp;
      }
      //On crée une information avec le format du flux et en caculant la logueneur totale du son
      DataLine.Info info = new DataLine.Info(
          Clip.class,
          this.lecteurAudio.getFormat(),
          ((int)this.lecteurAudio.getFrameLength() *
           this.format.getFrameSize()));
      //Grac à cette information, on peut creer un clip
      this.clip = (Clip)AudioSystem.getLine(info);
      //On ouvre le son
      reouvrir();
    }
    catch(UnsupportedAudioFileException uafe)
    {
      throw new SonTypeException();
    }
    catch(IOException ioe)
    {
      throw new SonErreurLecture();
    }
    catch(Exception e)
    {
      throw new SonErreurDiverse(e);
    }

    //On calcul la durée du son en microseconde
    this.duree = new Duree(this.longueurSonMicroseconde());
  }
  /**
   * Joue le son une fois
   */
  public void jouer()
  {
    //Si le son n'est pas initialiser, on l'initialise
    if(this.thread == null)
    {
      this.thread = new Thread(this);
      this.thread.start();
    }
    //On va le jouer une fois
    this.tour = 1;
  }
  /**
   * Joue le son plusieurs fois
   * @param nbFois Nombre de fois que le son est joué
   */
  public void boucle(int nbFois)
  {
    //Si le son n'est pas initialiser, on l'initialise
    if(this.thread == null)
    {
      this.thread = new Thread(this);
      this.thread.start();
    }
    //On va le jouer nbFois fois
    this.tour = nbFois;
  }
  /**
   * Joue le son un tn trés grand nombre de fois
   */
  public void boucle()
  {
    this.boucle(Integer.MAX_VALUE);
  }
  /**
   * Action du son, ne jamais appelé cette méthode directement, elle est public pour respecter l'implémentation de Runnable
   */
  public void run()
  {
    //Tant que le son est vivant
    while(this.thread != null)
    {
      //Pause de 0.123 seconde
      try
      {
        this.thread.sleep(123);
      }
      catch(Exception e)
      {}
      //Si on doit jouer le son au moins une fois
      if(this.tour > 0)
      {
        //On lance le son
        this.clip.start();
        //pause de 0.099 seconde (le son est jouer pendant ce temps)
        try
        {
          this.thread.sleep(99);
        }
        catch(Exception e)
        {}
        //Tant que le son n'est pas terminer ou que l'on soit en pause et est vivant
        while((this.clip.isActive() || this.pause) && (this.thread != null))
        {
          //Si on est pas en pause, on avance sur le son
          if(!this.pause)
          {
            this.avancer();
          }
          //Pause de 0.099 seconde
          try
          {
            this.thread.sleep(99);
          }
          catch(Exception e)
          {
            break;
          }
        }
        //Arréte le son
        this.clip.stop();
        //On se place au début du son
        this.placeMicroseconde(0);
        //On à un tour de moins à jouer
        this.tour--;
        if(this.tour < 1)
        {
          //Si on a fini de jouer, on tremine
          this.terminer();
          //Si on doit fermer à la fin, on ferme définitivement le son
          if(this.fermerALaFin)
          {
            this.fermer();
          }
        }
      }
    }
  }
  //Permet de réouvrir le son, ou de l'ouvrir
  private void reouvrir()
      throws Exception
  {
    this.clip.open(this.lecteurAudio);
  }
  /**
   * Met le son en pause
   */
  public void pause()
  {
    //Si on est pas déjà en pause, on se met en pause
    if(!this.pause)
    {
      this.clip.stop();
      this.pause = true;
    }
  }
  /**
   * Reprend le son ou il était rendu (enlève la pause)
   */
  public void reprise()
  {
    //Si on est en pause, on enléve la pause
    if(this.pause)
    {
      pause = false;
      this.clip.start();
    }
  }
  /**
   * Arrête de jouer le son et retour du son au début
   */
  public void stop()
  {
    this.clip.stop();
    this.placeMicroseconde(0);
    this.pause = false;
    this.tour = 0;
    this.thread = null;
  }
  /**
   * Détruit proprement le son
   */
  public void fermer()
  {
    this.stop();
    this.clip.close();
    this.clip = null;
    this.duree = null;
    this.ecouteurs.clear();
    this.ecouteurs = null;
    this.format = null;
    this.formatFichier = null;
  }
  /**
   * Indique si le son sera détruit aprés sa derniére fois ou il joue
   * @return <b>true</b> si le son est détruit quand c'est finit
   */
  public boolean estFermerALaFin()
  {
    return fermerALaFin;
  }
  /**
   * Change l'état de fermeture à la fin
   * @param fermer <b>true</b> pour indiqué que l'on désire que le son soit détruit aprés la derniére fois qu'il joue
   */
  public void setFermerALaFin(boolean fermer)
  {
    this.fermerALaFin = fermer;
  }
  /**
   * Longeur du son en microseconde
   * @return Longueur du son
   */
  public long longueurSonMicroseconde()
  {
    return this.clip.getMicrosecondLength();
  }
  /**
   * Nombre de microsecondes écoulées depuis le début du son
   * @return Durée en microseconde de l'écoute
   */
  public long getRenduMicroseconde()
  {
    return this.clip.getMicrosecondPosition();
  }
  /**
   * Durée de l'écoute
   * @return Durée de l'écoute
   */
  public Duree getRendu()
  {
    return new Duree(this.getRenduMicroseconde());
  }
  /**
   * Place le son à cette durée en milliseconde.
   * @param microseconde Place à laquelle on désire commencé le son
   */
  public void placeMicroseconde(long microseconde)
  {
    this.clip.setMicrosecondPosition(microseconde);
  }
  /**
   * Place le son à cette durée
   * @param duree Place à laquelle on désire commencé le son
   */
  public void placeDuree(Duree duree)
  {
    this.placeMicroseconde(duree.getMicroseconde());
  }
  /**
   * Remet le son au départ
   */
  public void placeDepart()
  {
    this.clip.setMicrosecondPosition(0);
  }
  /**
   * Indique si le son est en pause
   * @return <b> true</b> si le son est en pause
   */
  public boolean estEnPause()
  {
    return this.pause;
  }
  /**
   * Indique si le son est entrain d'être jouer
   * @return <b>true</b> si le son est entrain d'être joué
   */
  public boolean estEntrainDeJouer()
  {
    return!this.pause && (this.tour > 0);
  }
  /**
   * Ajout un écouteur d'événement son
   * @param ecouteur Ecouteur ajouté
   */
  public void ajouteEcouteurSon(EcouteurSon ecouteur)
  {
    if(ecouteur != null)
    {
      this.ecouteurs.addElement(ecouteur);
    }
  }
  /**
   * Retire un écouteur d'événement son
   * @param ecouteur Ecouteur retiré
   */
  public void retireEcouteurSon(EcouteurSon ecouteur)
  {
    if(ecouteur != null)
    {
      this.ecouteurs.removeElement(ecouteur);
    }
  }
  //Indique à tout les écouteurs d'événements son, que le son est terminé
  private void terminer()
  {
    Thread t = new Thread()
    {
      public void run()
      {
        Sound.this.terminer1();
      }
    };
    t.start();
  }
  //Indique à tout les écouteurs d'événements son, que le son est terminé
  private void terminer1()
  {
    int nb = this.ecouteurs.size();
    for(int i = 0; i < nb; i++)
    {
      EcouteurSon ecouteur = (EcouteurSon)this.ecouteurs.elementAt(i);
      ecouteur.sonTermine(this);
    }
  }
  //Indique à tout les écouteurs d'événements son, que le son a avancé
  private void avancer()
  {
    Thread t = new Thread()
    {
      public void run()
      {
        Sound.this.avancer1();
      }
    };
    t.start();
  }
  //Indique à tout les écouteurs d'événements son, que le son a avancé
  private void avancer1()
  {
    int nb = this.ecouteurs.size();
    for(int i = 0; i < nb; i++)
    {
      EcouteurSon ecouteur = (EcouteurSon)this.ecouteurs.elementAt(i);
      ecouteur.sonChangePosition(this);
    }
  }
  /**
   * Renvoie la durée du son
   * @return La durée du son
   */
  public Duree getDuree()
  {
    return this.duree;
  }
}
