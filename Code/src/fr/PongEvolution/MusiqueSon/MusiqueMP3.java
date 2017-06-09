


package fr.PongEvolution.MusiqueSon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;



//POUR LA LECTURE D'UNE MUSIQUE .MP3
//LE TRAITEMENT EST TRES SIMPLE, CEPENDANT
//UNE FOIS QU'ON ARRETE UNE CHANSON (OU QU'ELLE S'ARRETE ALORS QUE LA LECTURE EN BOUCLE N'EST PAS ACTIVEE), 
//IL FAUT DESALLOUER L'INSTANCE MusiqueMP3 DU CODE QUI L'A
//INSTANCIE ET EN REALLOUER UNE NOUVELLE CAR ON NE PEUT PAS METTRE UNE MUSIQUE EN PAUSE
public class MusiqueMP3 {
	private String cheminFichier; 
	private boolean lireEnBoucle; //Pour savoir si la musique doit être lue en boucle ou non
	private Player lecteur; //Pour la lecture du fichier
	private Thread thread; //Thread utilisé pour la lecture de la musique

	public MusiqueMP3(String cheminFichier, boolean lireEnBoucle) {
		super();
		setCheminFichier(cheminFichier);
		setLireEnBoucle(lireEnBoucle);
		initLecteur(cheminFichier);
		//THREAD DE LA METHODE lire()
		initThread();
	}

	public void initLecteur(String cheminFichier) {
		FileInputStream fichier = null;
		try {
			fichier = new FileInputStream(this.cheminFichier);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			lecteur = new Player(fichier);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

	//INITIALISER/REINITIALISER LE THREAD DE LECTURE
	public void initThread() {
		this.thread = new Thread() { 
			public void run() {
				try {
					lecteur.play(); //On lit la musique au moins une fois
					//LOOP?
					while(true) {
						while(isLireEnBoucle() && lecteur.isComplete()) { //Si on termine la lecture de la musique
							lecteur.close();
							initLecteur(cheminFichier); //On doit forcément reseter l'objet pour
							//recommencer la lecture...
							lecteur.play();
						}
					}
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		};
	}

	//POUR LIRE LE FICHIER
	public void lire() {
		thread.start();
	}

	//POUR ARRETER LA LECTURE
	//ENSUITE IL NE RESTE PLUS QU'A SUPPRIMER CETTE INSTANCE (ON REFERENCIE VERS NULL)
	//DANS LE CODE APPELANT
	public void arreter() {
		if(thread.isAlive())
			thread.interrupt();
		lecteur.close();
	}

	public boolean isLireEnBoucle() {
		return lireEnBoucle;
	}

	public void setLireEnBoucle(boolean lireEnBoucle) {
		this.lireEnBoucle = lireEnBoucle;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public String getCheminFichier() {
		return cheminFichier;
	}

	public void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	public Player getLecteur() {
		return lecteur;
	}

	public void setLecteur(Player lecteur) {
		this.lecteur = lecteur;
	}
}






