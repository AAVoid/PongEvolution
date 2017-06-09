




package fr.PongEvolution.Main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.PongEvolution.Balles.Balle;
import fr.PongEvolution.Jeu.Scene;
import fr.PongEvolution.MusiqueSon.MusiqueMP3;
import fr.PongEvolution.Raquettes.ConfigurationTouchesVerticale;
import fr.PongEvolution.Raquettes.Raquette;
import fr.PongEvolution.Raquettes.RaquetteClavierVerticale;
import fr.PongEvolution.Raquettes.RaquetteVerticale;
import fr.PongEvolution.Terrains.Terrain;
import fr.PongEvolution.Terrains.ZoneChangementDirection;
import fr.PongEvolution.Terrains.ZoneTeleportationTranslation;



//FENETRE DU JEU
public class FenetreDeJeu extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int HAUTEUR = 500; //Taille de base de la fenêtre sachant
	private static final int LARGEUR = 500; //qu'elle est redimensionnée pour s'adapter au terrain
	private static final int MARGE_LARGEUR_IMAGE_TERRAIN = 5; //Pour que l'image d'un terrain
	private static final int MARGE_HAUTEUR_IMAGE_TERRAIN = 165; //soit toujours bien affichée
	//doit être supérieur à la taille de //l'image du terrain
	private static final String NOM = "Pong Evolution - Aymerik ABOSO";
	private static final String CHEMIN_CAPTURES = "Screenshots";
	private static final String CHEMIN_RESSOURCES = "Resources";
	private static final String CHEMIN_IMAGES = CHEMIN_RESSOURCES + "/Images";
	private static final String CHEMIN_IMAGES_BALLES = CHEMIN_IMAGES + "/Balls";
	private static final String CHEMIN_IMAGES_BONUS = CHEMIN_IMAGES + "/Bonuses";
	private static final String CHEMIN_IMAGES_RAQUETTES = CHEMIN_IMAGES + "/Paddles";
	private static final String CHEMIN_IMAGES_TERRAINS = CHEMIN_IMAGES + "/Maps";
	private static final String CHEMIN_IMAGES_AUTRES = CHEMIN_IMAGES + "/Others";
	private static final String CHEMIN_IMAGES_BOULES_ENERGIE = CHEMIN_IMAGES + "/EnergyBalls";
	private static final String CHEMIN_MUSIQUES = CHEMIN_RESSOURCES + "/Musics";
	private static final String CHEMIN_SONS = CHEMIN_RESSOURCES + "/Sounds";
	private static final String CHEMIN_FONTS = CHEMIN_RESSOURCES + "/Fonts";
	private static final boolean REDIMENSINNABLE = false; //si la fenêtre est redimensionnable ou non
	private static final double TEMPS_ENTRE_FADING_FADOUT_INTRO = 3d; 
	//Temps en secondes après qu'une image ce soit affichée, avant de disparaitre : POUR L'INTRODUCTION DU 
	//JEU NOTAMMENT dans demarrer()
	private static final double DUREE_FADE_IN_OUT_INTRO = 2.0d; 
	//Durée de fade (utilisé notamment pour l'introduction du jeu dans demarrer()
	private static final double TEMPS_APRES_FADE_OUT_INTRO = 0.5d; //Temps d'attente après le fadeOut
	//d'une image avant le fadeIn et fadeOut d'une autre
	//Utilisé dans l'introduction du jeu dans demarrer()
	private static final String FORMAT_NOM_FICHIER_CAPTURE_ECRAN = "EEE dd MMM yyyy HHmmss"; 
	//Format du nom du fichier de capture cree à partir de la date de la capture (voir captureDEcran())
	private static final String EXTENSION_FICHIER_CAPTURE_ECRAN = "png";
	private static final int NUMERO_TOUCHE_CAPTURE_ECRAN = 80; //TOUCHE P pour faire une capture d'écran durant une partie
	private static final int NUMERO_TOUCHE_PAUSE = 78; //TOUCHE N pour mettre la partie en pause
	private static final double DUREE_FADE_IN_OUT_PRESENTATION_PARTIE = 1d;
	private static final double ATTENTE_PRESENTATION_PARTIE = 6d;
	private static final String NOM_FONT_PRESENTATION_PARTIE = "/Roboto.ttf";
	private static final int TAILLE_POLICE_PRESENTATION_PARTIE = 40;
	private static final int[] COULEUR_RGB_TEXTE_PRESENTATION_PARTIE = {255, 255, 255};
	private static final int TAILLE_MAX_CHAINE_TRONQUER_CHAINE = 16;
	private Container contenu; //content pane de la fenêtre
	private static MusiqueMP3 musiqueDeFond; //Musique jouée

	//CONSTRUCTEUR DE LA FENETRE
	public FenetreDeJeu() {
		super();
		setSize(LARGEUR, HAUTEUR);
		setTitle(NOM);
		setLocationRelativeTo(null);
		setResizable(REDIMENSINNABLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contenu = getContentPane(); //CONTENT PANE DE LA FENETRE
		setLayout(new BorderLayout()); //BorderLayout
		//ICONE DE LA FENETRE
		try {
			setIconImage(ImageIO.read(new File(CHEMIN_IMAGES_AUTRES + "/GameIconT.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//CURSEUR DE LA FENETRE
		getRootPane().setCursor(java.awt.Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(CHEMIN_IMAGES_AUTRES + "/cursorSwordT.png").getImage(), new Point(1,1),"customCursor"));
	}

	//AFFICHE L'INTRODUCTION DU JEU JUSQU'AU MENU PRINCIPAL
	public void afficherIntroductionJeu() {
		//DEMARRAGE DE LA MUSIQUE DE MENU PRINCIPAL
		musiqueDeFond = new MusiqueMP3(CHEMIN_MUSIQUES + /*"/Baccano - Guns & Roses.mp3"*/
				"/Epic Soul Factory - The Glorious Ones.mp3", true);
		setVisible(true);
		musiqueDeFond.lire();

		//iX1 : image X FadeIn
		//iX2 : image X FadeOut
		//---------------------------IMAGE 1 FADEIN
		ImageFadeInOut i11 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Intro1.png", 
				0f, 1f, DUREE_FADE_IN_OUT_INTRO);
		contenu.add(i11, BorderLayout.CENTER);
		setVisible(true);
		while(!i11.checkFonduTermine()) //On attend que le fadeIng soit totalement terminée
			repaint();
		//System.out.println("ok");
		attendreXSecondes(TEMPS_ENTRE_FADING_FADOUT_INTRO);
		//------------------------IMAGE 1 FADEOUT
		ImageFadeInOut i12 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Intro1.png", 
				1f, 0f, DUREE_FADE_IN_OUT_INTRO);
		contenu.add(i12, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i11);
		while(!i12.checkFonduTermine())
			repaint();
		//System.out.println("ok");
		attendreXSecondes(TEMPS_APRES_FADE_OUT_INTRO);
		//------------------------IMAGE 2 FADEIN
		ImageFadeInOut i21 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Intro2.jpg", 
				0f, 1f, DUREE_FADE_IN_OUT_INTRO);
		contenu.add(i21, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i12);
		while(!i21.checkFonduTermine())
			repaint();
		//System.out.println("ok");
		attendreXSecondes(TEMPS_ENTRE_FADING_FADOUT_INTRO);
		//------------------------IMAGE 2 FADEOUT
		ImageFadeInOut i22 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Intro2.jpg", 
				1f, 0f, DUREE_FADE_IN_OUT_INTRO);
		contenu.add(i22, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i21);
		while(!i22.checkFonduTermine())
			repaint();
		//System.out.println("ok");
		//attendreXSecondes(TEMPS_ENTRE_FADING_FADOUT_INTRO);

		//------------------------IMAGE 3 FADEIN : IMAGE DU MENU
		ImageFadeInOut i31 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Menu.jpg", 
				0f, 1f, DUREE_FADE_IN_OUT_INTRO);
		contenu.add(i31, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i22);
		while(!i31.checkFonduTermine())
			repaint();
		//System.out.println("ok");
		attendreXSecondes(TEMPS_APRES_FADE_OUT_INTRO);
		//------------------------ON RETIRE LA DERNIERE IMAGE D'INTRODUCTION DE LA CONTENT PANE
		contenu.remove(i31);
	}

	//AFFICHAGE DE L'INTRODUCTION DU JEU JUSQ'AU MENU PRINCIPAL
	public void demarrer() {
		setVisible(true);
		//Introduction du jeu
		afficherIntroductionJeu();
		//PREPARATION DE LA SCENE DE JEU
		//TERRAIN
		Terrain terrain = null;
		terrain = new Terrain("Test map", CHEMIN_IMAGES_TERRAINS + "/Map_3.png", 3, 30);
		//AJOUT DES ZONES DE TELEPORTATION AU TERRAIN
		terrain.ajouterZoneEffet(new ZoneTeleportationTranslation(new Rectangle(188, 167, 20, 20),
				463, 362));
		terrain.ajouterZoneEffet(new ZoneTeleportationTranslation(new Rectangle(453, 371, 20, 20),
				198, 177));
		terrain.ajouterZoneEffet(new ZoneChangementDirection(new Rectangle(124, 113, 20, 20), -2, -1));
		terrain.ajouterZoneEffet(new ZoneChangementDirection(new Rectangle(271, 241, 20, 20), 1, -2));
		terrain.ajouterZoneEffet(new ZoneChangementDirection(new Rectangle(155, 351, 20, 20), -1, -1));
		terrain.ajouterZoneEffet(new ZoneChangementDirection(new Rectangle(563, 84, 20, 20), -2, 1));
		terrain.ajouterZoneEffet(new ZoneChangementDirection(new Rectangle(395, 167, 20, 20), 1, 1));
		terrain.ajouterZoneEffet(new ZoneChangementDirection(new Rectangle(441, 304, 20, 20), -1, -2));
		//RAQUETTE VG
		RaquetteVerticale raquetteVG = null;
		try {
			//raquetteVG = new RaquetteIAVerticaleBarriere(ImageIO.read(new File("raquetteVerticaleT.png")));
			raquetteVG = new RaquetteClavierVerticale("", 30, ImageIO.read(new File(CHEMIN_IMAGES_RAQUETTES + 
					"/VerticalPaddle_1T.png")), false, 
					ImageIO.read(new File(CHEMIN_IMAGES_BOULES_ENERGIE + 
							"/EnergyBall_1T.png")), 10, 2, 0.3, Raquette.getConfigBouleEnergieZeTerrainNormale(), 2,
							terrain.getNumeroBarres(), 
							new ConfigurationTouchesVerticale(/*69, 68, 81, 38, 40*/
									90, 83, 68, 65, 81)); 
			//69 = E, 68 = D, 81 = Q
		} catch (IOException e) {
			e.printStackTrace();
		}
		//RAQUETTE VD
		RaquetteVerticale raquetteVD = null;
		try {
			/*raquetteVD = new RaquetteIAVerticaleBarriere(ImageIO.read(new File(CHEMIN_IMAGES_RAQUETTES +
					"/raquetteVerticaleT.png")), true);*/
			raquetteVD = new RaquetteClavierVerticale("", 30, ImageIO.read(new File(CHEMIN_IMAGES_RAQUETTES + 
					"/VerticalPaddle_1T.png")), true, 
					ImageIO.read(new File(CHEMIN_IMAGES_BOULES_ENERGIE + 
							"/EnergyBall_2T.png")), 3, 6, 1.3, Raquette.getConfigBouleEnergieZeTerrainNormale(), 1,
							terrain.getNumeroBarres(),
							new ConfigurationTouchesVerticale(/*82, 70, 83, 38, 40*/
									85, 74, 72, 73, 75)); 
			//82 = R, 70 = F, 83 = S
		} catch (IOException e) {
			e.printStackTrace();
		}
		//BALLE
		Balle balle = null;
		try {
			balle = new Balle(ImageIO.read(new File(CHEMIN_IMAGES_BALLES + "/ball_1T.png")),
					Balle.getConfigZeTerrainNormale());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int scoreCible = 10;
		demarrerPartie(raquetteVG, raquetteVD, balle, terrain, scoreCible);
	}

	public void demarrerPartie(RaquetteVerticale raquetteVG, 
			RaquetteVerticale raquetteVD, Balle balle, Terrain terrain, int scoreCible) {
		String pseudoRaquetteVG = "Sangoku";
		String pseudoRaquetteVD = "Freezer";
		//PRESENTATION DE LA PARTIE
		//On tronque les chaînes pour qu'elles ne sortent pas de la fenêtre à l'affichage lors de la
		//présentation
		presenterPartie(tronquerChaine(pseudoRaquetteVG),
				tronquerChaine(pseudoRaquetteVD));
		//ON MET LA FENETRE A LA BONNE TAILLE
		//On place la fenêtre à la bonne taille de façon à ce que tout le terrain soit visible
		setSize(terrain.getImage().getWidth(null) + MARGE_LARGEUR_IMAGE_TERRAIN, 
				terrain.getImage().getHeight(null) + MARGE_HAUTEUR_IMAGE_TERRAIN);
		setLocationRelativeTo(null); //On recentre la fenêtre à l'écran
		ImageFadeInOut i1 = new ImageFadeInOut(terrain.getCheminImage(), 0f, 1f, 0.5d);
		contenu.add(i1, BorderLayout.CENTER);
		setVisible(true);
		while(!i1.checkFonduTermine())
			continue;
		Scene scene = new Scene(this, raquetteVG, raquetteVD, balle, terrain, pseudoRaquetteVG, 
				pseudoRaquetteVD, scoreCible);
		addScene(scene);
		contenu.remove(i1);
		addKeyListener(scene);
		setVisible(true);
		//ON ATTEND QUE LA PARTIE SOIT TERMINEE POUR POURSUIVRE
	}

	//Pour ajouter la Scene convenablement à la fenêtre car c'est particulier ici
	public void addScene(Scene sc) {
		contenu.add(sc, BorderLayout.CENTER);
		contenu.add(sc.getPanneauSud(), BorderLayout.SOUTH);
	}

	//Met le programme en pause pendant un certain nombre de secondes
	//On peut utiliser Thread.sleep(nombreMillisecondes) mais ici on peut 
	//rajouter des actions à effectuer dans la boucle while si nécessaire
	public static void attendreXSecondes(double nombreSecondes) {
		long timestampCible = System.currentTimeMillis() + (long)(nombreSecondes * 1000);
		while(timestampCible - System.currentTimeMillis() > 0) {
			continue;
		}
	}

	//FAIT UNE CAPTURE D'ECRAN ET L'ENREGISTRE AUTOMATIQUEMENT DANS LE DOSSIER DE CAPTURES
	//A LA RACINE DU PROJET
	public void captureDEcran() {
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		Rectangle tailleFenetre = new Rectangle(this.getLocationOnScreen(), this.getSize());
		BufferedImage captureEcran = robot.createScreenCapture(tailleFenetre);
		//RECUPERATION DE LA DATE ACTUELLE POUR CONSTITUER LE NOM DU FICHIER CAPTURE
		Date dateActuelle = new Date(); //Recuperation de la date actuelle
		//FORMAT DE LA DATE QUI DEVIENDRA LE NOM DU FICHIER DE CAPTURE
		SimpleDateFormat formatDate = new SimpleDateFormat(FORMAT_NOM_FICHIER_CAPTURE_ECRAN);
		String nomFichier = formatDate.format(dateActuelle) + "." + EXTENSION_FICHIER_CAPTURE_ECRAN;
		try {
			ImageIO.write(captureEcran, EXTENSION_FICHIER_CAPTURE_ECRAN, new File (
					CHEMIN_CAPTURES + "/" + nomFichier));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//POUR POUVOIR EVITER D'AVOIR DES PSEUDOS TROP LONGS (ET QUI SORTENT DE L'ECRAN DANS 
	//presenterPartie(...). 
	//ON CONTROLE LA LONGUEUR DES PSEUDOS et on renvoie une version raccourcie si nécessaire
	public static String tronquerChaine(String chaine) {
		if(chaine.length() > TAILLE_MAX_CHAINE_TRONQUER_CHAINE)
			return chaine.substring(0, TAILLE_MAX_CHAINE_TRONQUER_CHAINE) + ".";
		else
			return chaine;
	}

	//PRESENTATION DE LA PARTIE
	//PRESENTATION DES JOUEURS QUI S'AFFRONTENT
	public void presenterPartie(String pseudoJoueurGauche, String pseudoJoueurDroit) {
		//ON DEMARRE LA MUSIQUE DU TERRAIN
		//musiqueDeFond.arreter();
		//musiqueDeFond = new MusiqueMP3(CHEMIN_MUSIQUES + "/Epic Soul Factory - The Glorious Ones.mp3", true);
		//musiqueDeFond.lire();
		JPanel panneau = new JPanel();
		panneau.setLayout(new BorderLayout());
		//---------------------------FADEIN
		ImageFadeInOut i11 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Announcement.png", 
				0f, 1f, DUREE_FADE_IN_OUT_PRESENTATION_PARTIE);
		panneau.add(i11, BorderLayout.CENTER);
		contenu.add(panneau, BorderLayout.CENTER);
		setVisible(true);
		while(!i11.checkFonduTermine()) //On attend que le fadeIng soit totalement terminée
			repaint();
		//CHARGEMENT FONT PERSONNALISEE
		Font police = null;
		try {
			police = Font.createFont(Font.TRUETYPE_FONT, new File(
					CHEMIN_FONTS + NOM_FONT_PRESENTATION_PARTIE)).deriveFont(Font.PLAIN, TAILLE_POLICE_PRESENTATION_PARTIE);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ON REMOVE L'IMAGE QUI A FADE
		panneau.remove(i11);
		//ON RECUPERE LE CONTEXTE GRAPHIQUE POUR POUVOIR AFFICHER L'IMAGE (SINON ON NE POURRA PAS
		//CHANGER DE LAYOUT POUR AFFICHER LES LABEL CORRECTEMENT
		Graphics g = panneau.getGraphics();
		//Image de fond
		Image imageFond = null;
		try {
			imageFond = ImageIO.read(new File(
					CHEMIN_IMAGES_AUTRES + "/Announcement.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Image VS
		Image imageVS = null;
		try {
			imageVS = ImageIO.read(new File(
					CHEMIN_IMAGES_AUTRES + "/VS.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(imageFond, 0, 0, this);
		g.drawImage(imageVS, getWidth() / 2 - imageVS.getWidth(null) / 2, 
				getHeight() / 2 - imageVS.getHeight(null) / 2, this);
		g.setFont(police);
		g.setColor(new Color(COULEUR_RGB_TEXTE_PRESENTATION_PARTIE[0],
				COULEUR_RGB_TEXTE_PRESENTATION_PARTIE[1], COULEUR_RGB_TEXTE_PRESENTATION_PARTIE[2]));
		//CALCUL DES COORDONNEES DES TEXTES
		Point coordonneesTexteHaut = new Point();
		//Point coordonneesTexteMilieu = new Point();
		Point coordonneesTexteBas = new Point();
		//Pour pouvoir mieux placer les textes à l'écran
		FontMetrics metricsPolice = g.getFontMetrics(police);
		//---------
		coordonneesTexteHaut.x = (int)(this.getWidth() / 2)
				- (int)((metricsPolice.stringWidth(pseudoJoueurGauche)) / 2);
		coordonneesTexteHaut.y = (int)(this.getHeight() / 4)
				- (int)((metricsPolice.getHeight()) / 2) + metricsPolice.getAscent();
		//---------
		coordonneesTexteBas.x = (int)(this.getWidth() / 2) 
				- (int)((metricsPolice.stringWidth(pseudoJoueurDroit)) / 2);
		coordonneesTexteBas.y = (int)(this.getHeight() / 4 * 3)
				- (int)((metricsPolice.getHeight()) / 2) + metricsPolice.getAscent();
		//---------
		g.drawString(pseudoJoueurGauche, coordonneesTexteHaut.x, coordonneesTexteHaut.y);
		g.drawString(pseudoJoueurDroit, coordonneesTexteBas.x, coordonneesTexteBas.y);
		attendreXSecondes(ATTENTE_PRESENTATION_PARTIE);
		//------------------------FADEOUT
		ImageFadeInOut i12 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/Announcement.png",
				1f, 0f, DUREE_FADE_IN_OUT_PRESENTATION_PARTIE);
		panneau.add(i12, BorderLayout.CENTER);
		setVisible(true);
		while(!i12.checkFonduTermine())
			repaint();
		contenu.remove(panneau);
	}

	public static int getTailleMaxChaineTronquerChaine() {
		return TAILLE_MAX_CHAINE_TRONQUER_CHAINE;
	}

	public static int[] getCouleurRgbTextePresentationPartie() {
		return COULEUR_RGB_TEXTE_PRESENTATION_PARTIE;
	}

	public static String getNomFontPresentationPartie() {
		return NOM_FONT_PRESENTATION_PARTIE;
	}

	public static int getTaillePolicePresentationPartie() {
		return TAILLE_POLICE_PRESENTATION_PARTIE;
	}

	public static double getDureeFadeInOutPresentationPartie() {
		return DUREE_FADE_IN_OUT_PRESENTATION_PARTIE;
	}

	public static double getAttentePresentationPartie() {
		return ATTENTE_PRESENTATION_PARTIE;
	}

	public static int getNumeroTouchePause() {
		return NUMERO_TOUCHE_PAUSE;
	}

	public static int getNumeroToucheCaptureEcran() {
		return NUMERO_TOUCHE_CAPTURE_ECRAN;
	}

	public static String getFormatNomFichierCaptureEcran() {
		return FORMAT_NOM_FICHIER_CAPTURE_ECRAN;
	}

	public static String getExtensionFichierCaptureEcran() {
		return EXTENSION_FICHIER_CAPTURE_ECRAN;
	}

	public static String getCheminCaptures() {
		return CHEMIN_CAPTURES;
	}

	public static MusiqueMP3 getMusiqueDeFond() {
		return musiqueDeFond;
	}

	public static void setMusiqueDeFond(MusiqueMP3 musiqueDeFond) {
		FenetreDeJeu.musiqueDeFond = musiqueDeFond;
	}

	public Container getContenu() {
		return contenu;
	}

	public void setContenu(Container contenu) {
		this.contenu = contenu;
	}

	public static double getTempsEntreFadingFadoutIntro() {
		return TEMPS_ENTRE_FADING_FADOUT_INTRO;
	}

	public static double getDureeFadeInOutIntro() {
		return DUREE_FADE_IN_OUT_INTRO;
	}

	public static double getTempsApresFadeOutIntro() {
		return TEMPS_APRES_FADE_OUT_INTRO;
	}

	public static boolean isRedimensinnable() {
		return REDIMENSINNABLE;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getCheminImagesBoulesEnergie() {
		return CHEMIN_IMAGES_BOULES_ENERGIE;
	}

	public static String getCheminFonts() {
		return CHEMIN_FONTS;
	}

	public static String getCheminImagesAutres() {
		return CHEMIN_IMAGES_AUTRES;
	}

	public static int getMargeLargeurImageTerrain() {
		return MARGE_LARGEUR_IMAGE_TERRAIN;
	}

	public static int getMargeHauteurImageTerrain() {
		return MARGE_HAUTEUR_IMAGE_TERRAIN;
	}

	public static int getHauteur() {
		return HAUTEUR;
	}

	public static int getLargeur() {
		return LARGEUR;
	}

	public static String getNom() {
		return NOM;
	}

	public static String getCheminRessources() {
		return CHEMIN_RESSOURCES;
	}

	public static String getCheminImages() {
		return CHEMIN_IMAGES;
	}

	public static String getCheminImagesBalles() {
		return CHEMIN_IMAGES_BALLES;
	}

	public static String getCheminImagesBonus() {
		return CHEMIN_IMAGES_BONUS;
	}

	public static String getCheminImagesRaquettes() {
		return CHEMIN_IMAGES_RAQUETTES;
	}

	public static String getCheminImagesTerrains() {
		return CHEMIN_IMAGES_TERRAINS;
	}

	public static String getCheminMusiques() {
		return CHEMIN_MUSIQUES;
	}

	public static String getCheminSons() {
		return CHEMIN_SONS;
	}
}





