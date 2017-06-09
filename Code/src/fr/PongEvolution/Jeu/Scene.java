


package fr.PongEvolution.Jeu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.PongEvolution.Balles.Balle;
import fr.PongEvolution.Balles.Enum_Raquette;
import fr.PongEvolution.BoulesEnergie.BouleEnergie;
import fr.PongEvolution.Main.FenetreDeJeu;
import fr.PongEvolution.Raquettes.Raquette;
import fr.PongEvolution.Raquettes.RaquetteHorizontale;
import fr.PongEvolution.Raquettes.RaquetteIAHorizontaleBarriere;
import fr.PongEvolution.Raquettes.RaquetteVerticale;
import fr.PongEvolution.Terrains.Terrain;




//POUR GERER UNE PARTIE
public class Scene extends JPanel implements GameInterface, KeyListener {
	private static final long serialVersionUID = 1L;

	private static final double NOMBRE_SECONDES_ATTENTE_DEBUT_PARTIE_MANCHE = 3.0d; 
	//On attend avant le début de la partie/la manche
	private static final int COEF_TAILLE_FILL_RECT_PAINT_COMPONENT = 6;
	private static final int INCREMENT_VITESSE_Y_BALLE = 1; //augmentation vitesse de la balle à chaque collision
	private static final int INCREMENT_VITESSE_X_BALLE = 1;
	private static final int RANDOM_VITESSE_MAX = 4; //calcul dans constructeur scene
	private static final int RANDOM_VITESSE_MIN = 2;
	private static final double PROBA_RANDOM_HAUT = 0.5;
	private static final double PROBA_RANDOM_GAUCHE = 0.5;
	private static final int NOMBRE_FPS_JEU = 30; //Le jeu tourne à 30 images par seconde
	private static final int MARGE_VITESSE_MAX_BALLE = 10;
	private static final String TEXTE_SCORES_GROUPBOX = "PLAYERS' SCORES";
	private static final String NOM_POLICE_TEXTE_SCORE_1 = "/Roboto.ttf";
	private static final String NOM_POLICE_TEXTE_SCORE_2 = "/Roboto.ttf";
	private static final String TEXTE_ENERGIE_GROUPBOX = "ENERGY";
	private static final String NOM_ICONE_VS = "/VS2.png";
	private static final int TAILLE_POLICE_SCORE_1 = 15;
	private static final int TAILLE_POLICE_SCORE_2 = 35;
	private static final int[] COULEUR_RGB_LABEL_SCORE_1 = {0, 0, 0};
	private static final int[] COULEUR_RGB_LABEL_SCORE_2 = {255, 0, 0};
	private static final Color COULEUR_PANNEAU_POUVOIRS = 
			//Color.WHITE
			new Color(255, 255, 90)
	;
	private static final Color COULEUR_PANNEAU_SCORES = 
			//Color.WHITE
			new Color(255, 255, 0)
	;
	private static final String CHEMIN_IMAGE_PAUSE = FenetreDeJeu.getCheminImagesAutres() + "/Pause.png";
	private static final double INTERVALLE_UTILISATION_TOUCHE_DEDIEE = 0.3d; //L'utilisation d'une touche
	//dédiée après une utilisation précédente sera reconnue 0.3 seconde après la précédente utilisation
	private RaquetteVerticale raquetteVG; //raquette verticale gauche
	private RaquetteVerticale raquetteVD;
	private RaquetteHorizontale raquetteHH; //raquette verticale haut
	private RaquetteHorizontale raquetteHB;
	private Balle balle;
	private int hauteur;
	private int largeur;
	//hauteur et largeur du terrain de jeu
	//les valeurs seront égales car le terrain est carré => image carrée
	//valeurs renseignée par les dimensions de l'image du terrain
	private Terrain terrain;
	private ArrayList<Integer> listeTouches; //touches appuyées au clavier
	private javax.swing.Timer timerMajScene; //timer pour le rafraichissement d'écran de jeu
	private JPanel panneauSud; //Va contenir les panneaux Scores et Pouvoirs
	private JPanel panneauScores;
	private JPanel panneauEnergie;
	private JLabel labelScoreRaquetteVG_1;
	private JLabel labelScoreRaquetteVD_1;
	private JLabel labelScoreRaquetteVG_2;
	private JLabel labelScoreRaquetteVD_2;
	private JLabel labelVS;
	private ArrayList<BouleEnergie> listeBoulesEnergie; //Pour gérer la collision de deux boules d'énergie ICI
	private boolean debutPartieManche; //Indiquera si la partie/manche commence, de façon à attendre
	//avant le début de celle-ci (avant que la balle commence à bouger) dans la méthode paintComponent
	//Attente que la touche ENTER soit pressée pour démarrer la partie/manche
	private FenetreDeJeu fenDeJeu; //Pour pouvoir fair des captures d'écran
	private boolean partieEnPause; //Pour savoir si la partie est en pause ou non
	private Image imagePause; //Image affichée lorsque la partie est en pause
	private long timestampDerniereCapture; //Pour limiter la vitesse de captures d'écran
	private long timestampDernierePause; //Pour limiter la vitesse de la mise en pause/reprise de la partie
	private long timestampDebutPartie; //Pour gérer le temps de la partie
	private long timestampFinPartie;
	//POUR GERER LE SCORE CIBLE
	private int scoreCible; //Score à atteindre pour que la partie se termine
	private int gagnantPartie; //Pour savoir qui a gagné la partie
		//0 : personne
		//1 : raquetteVG
		//2 : raquetteVD
		//3 : raquetteHH
		//4 : raquetteHB

	public Scene(FenetreDeJeu fenetre, RaquetteVerticale raquetteVG, RaquetteVerticale raquetteVD,
			Balle balle, Terrain terrain, String pseudoRaquetteVG, String pseudoRaquetteVD,
			int scoreCible) {
		super();
		setPartieEnPause(false);
		setFenDeJeu(fenetre);
		try {
			setImagePause(
					ImageIO.read(new File(getCheminImagePause()))
					);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setTimestampDerniereCapture(0L);
		setTimestampDernierePause(0L);
		setTimestampDebutPartie(System.currentTimeMillis());
		setTimestampFinPartie(0L);
		setScoreCible(scoreCible);
		setGagnantPartie(0); //Personne n'a gagné la partie
		setTerrain(terrain);
		setHauteur(terrain.getImage().getHeight(null));
		setLargeur(terrain.getImage().getWidth(null));
		setRaquetteVG(raquetteVG);
		setRaquetteVD(raquetteVD); 
		///INTELLIGENCES ARTIFICIELLES
		Image imageRaquetteIA = null;
		try {
			imageRaquetteIA = ImageIO.read(new File(
					FenetreDeJeu.getCheminImagesRaquettes() + "/HorizontalPaddle_1T.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Image imageBouleEnergieRaquetteIA = null;
		try {
			imageBouleEnergieRaquetteIA = ImageIO.read(new File(
					FenetreDeJeu.getCheminImagesBoulesEnergie() + "/EnergyBall_1T.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//RAQUETTE HH : IA BARRIERE
		raquetteHH = new RaquetteIAHorizontaleBarriere("", imageRaquetteIA, false, 
				imageBouleEnergieRaquetteIA, 3, 3, 0.0, Raquette.getConfigBouleEnergieZeTerrainAucunEffet(), 0);
		//RAQUETTE HB : IA BARRIERE
		raquetteHB = new RaquetteIAHorizontaleBarriere("", imageRaquetteIA, true, 
				imageBouleEnergieRaquetteIA, 3, 3, 0.0, Raquette.getConfigBouleEnergieZeTerrainAucunEffet(), 0);
		//raquetteHB.setInverserImageBarreAngle(true);
		///POSITIONS DES OBJETS ET VITESSE DE LA BALLE
		setRaquetteHB(raquetteHB);
		resetRaquettes();
		setBalle(balle);
		resetBalle();
		///GESTION DES TOUCHES
		listeTouches = new ArrayList<Integer>();
		//AFFICHAGES DES SCORES ET AUTRES DANS DES JPANELS
		//POUR LES SCORES
		labelScoreRaquetteVG_1 = new JLabel();
		labelScoreRaquetteVG_1.setHorizontalAlignment(JLabel.CENTER);
		labelScoreRaquetteVD_1 = new JLabel();
		labelScoreRaquetteVD_1.setHorizontalAlignment(JLabel.CENTER);
		labelScoreRaquetteVG_2 = new JLabel();
		labelScoreRaquetteVG_2.setHorizontalAlignment(JLabel.CENTER);
		labelScoreRaquetteVD_2 = new JLabel();
		labelScoreRaquetteVD_2.setHorizontalAlignment(JLabel.CENTER);
		labelVS = new JLabel();
		labelVS.setHorizontalAlignment(JLabel.CENTER);
		//CHARGEMENT FONT PERSONNALISEE
		Font labelScoreFont1 = null;
		try {
			labelScoreFont1 = Font.createFont(Font.TRUETYPE_FONT, new File(
					FenetreDeJeu.getCheminFonts() + NOM_POLICE_TEXTE_SCORE_1)).deriveFont(Font.PLAIN, TAILLE_POLICE_SCORE_1);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//---------
		Font labelScoreFont2 = null;
		try {
			labelScoreFont2 = Font.createFont(Font.TRUETYPE_FONT, new File(
					FenetreDeJeu.getCheminFonts() + NOM_POLICE_TEXTE_SCORE_2)).deriveFont(Font.PLAIN, TAILLE_POLICE_SCORE_2);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		labelVS.setIcon(new ImageIcon(FenetreDeJeu.getCheminImagesAutres() + NOM_ICONE_VS));
		labelScoreRaquetteVG_1.setFont(labelScoreFont1);
		labelScoreRaquetteVG_1.setForeground(
				new Color(COULEUR_RGB_LABEL_SCORE_1[0], COULEUR_RGB_LABEL_SCORE_1[1], COULEUR_RGB_LABEL_SCORE_1[2]));
		labelScoreRaquetteVG_1.setText(pseudoRaquetteVG);
		//labelScoreRaquetteVG_1.setForeground(new Color(0, 0, 0));
		labelScoreRaquetteVG_2.setText("" + raquetteVG.getScore());
		labelScoreRaquetteVG_2.setForeground(
				new Color(COULEUR_RGB_LABEL_SCORE_2[0], COULEUR_RGB_LABEL_SCORE_2[1], COULEUR_RGB_LABEL_SCORE_2[2]));
		labelScoreRaquetteVG_2.setFont(labelScoreFont2);
		labelScoreRaquetteVD_1.setFont(labelScoreFont1);
		labelScoreRaquetteVD_1.setForeground(
				new Color(COULEUR_RGB_LABEL_SCORE_1[0], COULEUR_RGB_LABEL_SCORE_1[1], COULEUR_RGB_LABEL_SCORE_1[2]));
		labelScoreRaquetteVD_1.setText(pseudoRaquetteVD);
		labelScoreRaquetteVD_1.setForeground(new Color(0, 0, 0));
		labelScoreRaquetteVD_2.setText("" + raquetteVD.getScore());
		labelScoreRaquetteVD_2.setForeground(
				new Color(COULEUR_RGB_LABEL_SCORE_2[0], COULEUR_RGB_LABEL_SCORE_2[1], COULEUR_RGB_LABEL_SCORE_2[2]));
		labelScoreRaquetteVD_2.setFont(labelScoreFont2);
		panneauSud = new JPanel();
		panneauSud.setLayout(new GridLayout(2,1));
		panneauScores = new JPanel();
		panneauScores.setLayout(new GridLayout(1, 5));
		panneauScores.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)), 
				TEXTE_SCORES_GROUPBOX));
		panneauScores.add(labelScoreRaquetteVG_1);
		panneauScores.add(labelScoreRaquetteVG_2);
		panneauScores.add(labelVS);
		panneauScores.add(labelScoreRaquetteVD_2);
		panneauScores.add(labelScoreRaquetteVD_1);
		//PANNEAU ENERGIE
		panneauEnergie = new JPanel();
		panneauEnergie.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)), 
				TEXTE_ENERGIE_GROUPBOX));
		panneauEnergie.setLayout(new GridLayout(1,2));
		panneauEnergie.add(raquetteVG.getBarreEnergie());
		panneauEnergie.add(raquetteVD.getBarreEnergie());
		panneauSud.add(panneauEnergie);
		panneauSud.add(panneauScores);
		//LISTE BOULES D'ENERGIE
		listeBoulesEnergie = new ArrayList<BouleEnergie>();
		//TIMER POUR ANIMATION
		//On limite le jeu à NOMBRE_FPS_JEU images par seconde
		timerMajScene = new javax.swing.Timer(1000 / NOMBRE_FPS_JEU, new TimeEcouteur(this));
		timerMajScene.start();
		setDebutPartieManche(true); //La partie commence
	}

	public int getGagnantPartie() {
		return gagnantPartie;
	}

	public void setGagnantPartie(int gagnantPartie) {
		this.gagnantPartie = gagnantPartie;
	}

	public int getScoreCible() {
		return scoreCible;
	}

	public void setScoreCible(int scoreCible) {
		this.scoreCible = scoreCible;
	}

	//Replace les raquette sur le terrain comme en début de partie/manche
	public void resetRaquettes() {
		raquetteVG.reset(0, 
				(int)(hauteur / 2 - raquetteVG.getHauteur() / 2));
		raquetteVD.reset((int)(largeur - raquetteVD.getLongueur()), 
				(int)(hauteur / 2 - raquetteVD.getHauteur() / 2));
		raquetteHH.reset((int)(largeur / 2 - raquetteHH.getLongueur() / 2), 
				0);
		raquetteHB.reset((int)(largeur / 2 - raquetteHB.getLongueur() / 2), 
				(int)(hauteur - raquetteHB.getHauteur()));
	}

	//Pour actualiser l'affichage de la fenêtre
	public void actualiserAffichage() {
		panneauSud.repaint();
		repaint();
	}

	//Replace la balle sur le terrain comme en début de partie/manche
	public void resetBalle() {
		int vitesseBalleX = (int)((Math.random() * (RANDOM_VITESSE_MAX - RANDOM_VITESSE_MIN)) + RANDOM_VITESSE_MIN);
		int vitesseBalleY = (int)((Math.random() * (RANDOM_VITESSE_MAX - RANDOM_VITESSE_MIN)) + RANDOM_VITESSE_MIN);
		int directionBalleX = Math.random() > PROBA_RANDOM_GAUCHE ? 1 : -1;
		int directionBalleY = Math.random() > PROBA_RANDOM_HAUT ? 1 : -1;		
		balle.reset(largeur / 2 - balle.getDiametre() / 2, 
				hauteur / 2 - balle.getDiametre() / 2, 
				directionBalleX, 
				directionBalleY, 
				vitesseBalleX, 
				vitesseBalleY, 
				(balle.getImage().getWidth(null) - MARGE_VITESSE_MAX_BALLE <= 0) ? 1 : 
					balle.getImage().getWidth(null) - MARGE_VITESSE_MAX_BALLE, 
					Enum_Raquette.RAQUETTE_AUCUNE);
	}
	
	//Actualiser la barre d'énergie des raquettes
	public void actualiserBarreEnergieRaquettes() {
		raquetteVG.actualiserBarreEnergie();
		raquetteVD.actualiserBarreEnergie();
		//raquetteHH.actualiserBarreEnergie();
		//raquetteHB.actualiserBarreEnergie();
	}
	
	//Augmente l'énergie des raquettes à la fin d'une manche
	public void augmenterEnergieRaquettes() {
		raquetteVG.recupererEnergie(terrain.getQuantiteEnergieRecupereeMancheRaquettes());
		raquetteVD.recupererEnergie(terrain.getQuantiteEnergieRecupereeMancheRaquettes());
		//raquetteHH.recupererEnergie(terrain.getQuantiteEnergieRecupereeMancheRaquettes());
		//raquetteHB.recupererEnergie(terrain.getQuantiteEnergieRecupereeMancheRaquettes());
	}

	public static String getNomIconeVs() {
		return NOM_ICONE_VS;
	}

	public long getTimestampDebutPartie() {
		return timestampDebutPartie;
	}

	public void setTimestampDebutPartie(long timestampDebutPartie) {
		this.timestampDebutPartie = timestampDebutPartie;
	}

	public long getTimestampFinPartie() {
		return timestampFinPartie;
	}

	public void setTimestampFinPartie(long timestampFinPartie) {
		this.timestampFinPartie = timestampFinPartie;
	}

	public static double getIntervalleUtilisationToucheDediee() {
		return INTERVALLE_UTILISATION_TOUCHE_DEDIEE;
	}

	public long getTimestampDerniereCapture() {
		return timestampDerniereCapture;
	}

	public void setTimestampDerniereCapture(long timestampDerniereCapture) {
		this.timestampDerniereCapture = timestampDerniereCapture;
	}

	public long getTimestampDernierePause() {
		return timestampDernierePause;
	}

	public void setTimestampDernierePause(long timestampDernierePause) {
		this.timestampDernierePause = timestampDernierePause;
	}

	public Image getImagePause() {
		return imagePause;
	}

	public void setImagePause(Image imagePause) {
		this.imagePause = imagePause;
	}

	public static String getCheminImagePause() {
		return CHEMIN_IMAGE_PAUSE;
	}

	public boolean isPartieEnPause() {
		return partieEnPause;
	}

	public void setPartieEnPause(boolean partieEnPause) {
		this.partieEnPause = partieEnPause;
	}

	public FenetreDeJeu getFenDeJeu() {
		return fenDeJeu;
	}

	public void setFenDeJeu(FenetreDeJeu fenDeJeu) {
		this.fenDeJeu = fenDeJeu;
	}

	public static double getNombreSecondesAttenteDebutPartieManche() {
		return NOMBRE_SECONDES_ATTENTE_DEBUT_PARTIE_MANCHE;
	}

	public boolean isDebutPartieManche() {
		return debutPartieManche;
	}

	public void setDebutPartieManche(boolean debutPartieManche) {
		this.debutPartieManche = debutPartieManche;
	}

	public ArrayList<BouleEnergie> getListeBoulesEnergie() {
		return listeBoulesEnergie;
	}

	public void setListeBoulesEnergie(ArrayList<BouleEnergie> listeBoulesEnergie) {
		this.listeBoulesEnergie = listeBoulesEnergie;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getCoefTailleFillRectPaintComponent() {
		return COEF_TAILLE_FILL_RECT_PAINT_COMPONENT;
	}

	public ArrayList<Integer> getListeTouches() {
		return getToucheEnfonce();
	}

	public void setListeTouches(ArrayList<Integer> listeTouches) {
		this.listeTouches = listeTouches;
	}

	public static int[] getCouleurRgbLabelScore1() {
		return COULEUR_RGB_LABEL_SCORE_1;
	}

	public static int getTaillePoliceScore1() {
		return TAILLE_POLICE_SCORE_1;
	}

	public static String getNomPoliceTexteScore1() {
		return NOM_POLICE_TEXTE_SCORE_1;
	}

	public static Color getCouleurPanneauScores() {
		return COULEUR_PANNEAU_SCORES;
	}

	public static Color getCouleurPanneauPouvoirs() {
		return COULEUR_PANNEAU_POUVOIRS;
	}

	public JPanel getPanneauSud() {
		return panneauSud;
	}

	public void setPanneauSud(JPanel panneauSud) {
		this.panneauSud = panneauSud;
	}

	public static int[] getCouleurRgbLabelScore2() {
		return COULEUR_RGB_LABEL_SCORE_2;
	}

	public JLabel getLabelVS() {
		return labelVS;
	}

	public void setLabelVS(JLabel labelVS) {
		this.labelVS = labelVS;
	}

	public JPanel getPanneauEnergie() {
		return panneauEnergie;
	}

	public void setPanneauEnergie(JPanel panneauEnergie) {
		this.panneauEnergie = panneauEnergie;
	}

	public static String getTexteEnergieGroupbox() {
		return TEXTE_ENERGIE_GROUPBOX;
	}

	public static String getNomPoliceTexteScore2() {
		return NOM_POLICE_TEXTE_SCORE_2;
	}

	public static int getTaillePoliceScore2() {
		return TAILLE_POLICE_SCORE_2;
	}

	public static String getTexteScoresGroupbox() {
		return TEXTE_SCORES_GROUPBOX;
	}

	public JLabel getLabelScoreRaquetteVG_1() {
		return labelScoreRaquetteVG_1;
	}

	public void setLabelScoreRaquetteVG_1(JLabel labelScoreRaquetteVG_1) {
		this.labelScoreRaquetteVG_1 = labelScoreRaquetteVG_1;
	}

	public JLabel getLabelScoreRaquetteVD_1() {
		return labelScoreRaquetteVD_1;
	}

	public void setLabelScoreRaquetteVD_1(JLabel labelScoreRaquetteVD_1) {
		this.labelScoreRaquetteVD_1 = labelScoreRaquetteVD_1;
	}

	public JLabel getLabelScoreRaquetteVG_2() {
		return labelScoreRaquetteVG_2;
	}

	public void setLabelScoreRaquetteVG_2(JLabel labelScoreRaquetteVG_2) {
		this.labelScoreRaquetteVG_2 = labelScoreRaquetteVG_2;
	}

	public JLabel getLabelScoreRaquetteVD_2() {
		return labelScoreRaquetteVD_2;
	}

	public void setLabelScoreRaquetteVD_2(JLabel labelScoreRaquetteVD_2) {
		this.labelScoreRaquetteVD_2 = labelScoreRaquetteVD_2;
	}

	public JPanel getPanneauScores() {
		return panneauScores;
	}

	public void setPanneauScores(JPanel panneauScore) {
		this.panneauScores = panneauScore;
	}

	//MET A JOUR LA LISTE DES BOULES D'ENERGIE SUR LE TERRAIN
	public void updateListeBoulesEnergie() {
		//On efface la liste et on la reconstruit
		listeBoulesEnergie.clear();
		listeBoulesEnergie.addAll(raquetteVG.getListeBoulesEnergie());
		listeBoulesEnergie.addAll(raquetteVD.getListeBoulesEnergie());
		listeBoulesEnergie.addAll(raquetteHH.getListeBoulesEnergie());
		listeBoulesEnergie.addAll(raquetteHB.getListeBoulesEnergie());
	}

	//TEST LES COLLISIONS ENTRE LES BOULES D'ENERGIE ET LES EFFACE EN CONSEQUENCE 
	//EN FONCTION DE LEUR PUISSANCE
	//Boule de puissance plus faible effacée et puissance de la boule effacée 
	//soustraite à celle qui survit
	//Si les deux boules ont la même puissance elles sont toutes les deux effacées
	//On ne test la collision que si les boules n'ont pas le même propriétaire
	public void checkCollisionsBoulesEnergie() {
		updateListeBoulesEnergie();
		ArrayList<BouleEnergie> listeCopie = new ArrayList<BouleEnergie>(listeBoulesEnergie);
		for(int i = 0 ; i < listeBoulesEnergie.size() ; i++) {
			listeCopie.remove(listeBoulesEnergie.get(i)); //éviter collision avec elle-même
			for(int j = 0 ; j < listeCopie.size() ; j++) {
				if(listeBoulesEnergie.get(i).entreEnCollision(listeCopie.get(j))) {
					//PROPRIETAIRES DIFFERENTS ?
					if(listeBoulesEnergie.get(i).getProprietaire() == listeCopie.get(j).getProprietaire())	
						continue;
					///ON COMPARE LES PUISSANCES
					if(listeBoulesEnergie.get(i).getPuissance() > listeCopie.get(j).getPuissance()) {
						listeBoulesEnergie.get(i).setPuissance(listeBoulesEnergie.get(i).getPuissance()
								- listeCopie.get(j).getPuissance());
						//On efface la boule d'énergie de la listeCopie et des raquettes
						raquetteVG.getListeBoulesEnergie().remove(listeCopie.get(j));
						raquetteVD.getListeBoulesEnergie().remove(listeCopie.get(j));
						raquetteHH.getListeBoulesEnergie().remove(listeCopie.get(j));
						raquetteHB.getListeBoulesEnergie().remove(listeCopie.get(j));
					}
					else if(listeBoulesEnergie.get(i).getPuissance() < listeCopie.get(j).getPuissance()) {
						listeCopie.get(j).setPuissance(listeCopie.get(j).getPuissance()
								- listeBoulesEnergie.get(i).getPuissance());
						raquetteVG.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
						raquetteVD.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
						raquetteHH.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
						raquetteHB.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
					}
					else { //Puissances égales
						raquetteVG.getListeBoulesEnergie().remove(listeCopie.get(j));
						raquetteVD.getListeBoulesEnergie().remove(listeCopie.get(j));
						raquetteHH.getListeBoulesEnergie().remove(listeCopie.get(j));
						raquetteHB.getListeBoulesEnergie().remove(listeCopie.get(j));
						//---
						raquetteVG.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
						raquetteVD.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
						raquetteHH.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
						raquetteHB.getListeBoulesEnergie().remove(listeBoulesEnergie.get(i));
					}
				}
			}
		}
	}

	//BOULES D'ENERGIE
	public void deplacerBoulesEnergie() {
		ArrayList<Raquette> listeJoueurs = getJoueurs();
		ArrayList<BouleEnergie> listeBoulesEnergie;
		for(int i = 0 ; i < listeJoueurs.size() ; i++) {
			listeBoulesEnergie = listeJoueurs.get(i).getListeBoulesEnergie();
			for(int j = 0 ; j < listeBoulesEnergie.size() ; j++)
				listeBoulesEnergie.get(j).deplacer(this);
		}
	}

	public void deplacerBalle() {
		balle.deplacer(this);
	}

	public void deplacerRaquettes() {
		raquetteHH.deplacer(this);
		raquetteHB.deplacer(this);
		raquetteVG.deplacer(this);
		raquetteVD.deplacer(this);
	}

	//Vérifier si une raquette clavier veut créer une boule d'énergie
	public void verifierCreerBouleEnergie() {
		raquetteHH.verifierCreerBouleEnergie(this);
		raquetteHB.verifierCreerBouleEnergie(this);
		raquetteVG.verifierCreerBouleEnergie(this);
		raquetteVD.verifierCreerBouleEnergie(this);
	}

	//Controle des boules d'énergie sur le terrain pour les supprimer si elles le quitte
	public void verifierSupprimerBouleEnergie() {
		raquetteHH.verifierSupprimerBoulesEnergie(this);
		raquetteHB.verifierSupprimerBoulesEnergie(this);
		raquetteVG.verifierSupprimerBoulesEnergie(this);
		raquetteVD.verifierSupprimerBoulesEnergie(this);
	}

	public void afficherBoulesEnergie(Graphics g) {
		raquetteHH.afficherBoulesEnergie(g);
		raquetteHB.afficherBoulesEnergie(g);
		raquetteVG.afficherBoulesEnergie(g);
		raquetteVD.afficherBoulesEnergie(g);
	}

	//VERIFIER COLLISION BOULE ENERGIE ET BALLE
	public void verifierCollisionBouleEnergieBalle() {
		ArrayList<Raquette> listeJoueurs = getJoueurs();
		ArrayList<BouleEnergie> listeBoulesEnergie;
		int tailleInitialeListe = 0; //Pour s'assurer qu'on efface bien toutes les boules d'énergie
		//de chaque raquette qui sont rentrées en collision avec le balle
		for(int i = 0 ; i < listeJoueurs.size() ; i++) {
			listeBoulesEnergie = listeJoueurs.get(i).getListeBoulesEnergie();
			tailleInitialeListe = listeBoulesEnergie.size();
			for(int j = 0 ; j < tailleInitialeListe ; j++)
				for(int k = 0 ; k < listeBoulesEnergie.size() ; k++)
					if(listeBoulesEnergie.get(k).entreEnCollision(balle)) {
						listeBoulesEnergie.get(k).updateBalleApresCollision(this, balle, listeBoulesEnergie);
						break; //On quitte la boucle for car on a modifié sa taille 
						//(suppression de la boule d'énergie dans la méthode update
					}
		}
	}

	//VERIFIE SI ON VEUT FAIRE UNE CAPTURE D'ECRAN
	public void verifierTouchesDediees() {
		//On récupère une copie de la liste des touches enfoncées
		ArrayList<Integer> listeTouche = getToucheEnfonce();
		for(int i = 0 ; i < listeTouche.size() ; i++) {
			//Capture d'écran
			if(listeTouche.get(i) == FenetreDeJeu.getNumeroToucheCaptureEcran()) {
				//Vérification afin de limiter la vitesse de demande de capture
				if(System.currentTimeMillis() >= getTimestampDerniereCapture() + (long)(INTERVALLE_UTILISATION_TOUCHE_DEDIEE * 1000)) {
					fenDeJeu.captureDEcran();
					setTimestampDerniereCapture(System.currentTimeMillis()); //Mise à jour timestamp pour limiter
					//la vitesse de demande de capture d'écran
				}
			}
			//Pause/Reprise de la partie
			if(listeTouche.get(i) == FenetreDeJeu.getNumeroTouchePause()) {
				//Vérification afin de limiter la vitesse de demande de mise en pause/reprise de la partie
				if(System.currentTimeMillis() >= getTimestampDernierePause() + (long)(INTERVALLE_UTILISATION_TOUCHE_DEDIEE * 1000)) {
					this.setPartieEnPause(!partieEnPause);
					setTimestampDernierePause(System.currentTimeMillis());
				}
			}
		}
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public javax.swing.Timer getTimerMajScene() {
		return timerMajScene;
	}

	public void setTimerMajScene(javax.swing.Timer timerMajScene) {
		this.timerMajScene = timerMajScene;
	}

	public static int getIncrementVitesseYBalle() {
		return INCREMENT_VITESSE_Y_BALLE;
	}

	public static int getIncrementVitesseXBalle() {
		return INCREMENT_VITESSE_X_BALLE;
	}

	public static int getRandomVitesseMax() {
		return RANDOM_VITESSE_MAX;
	}

	public static int getRandomVitesseMin() {
		return RANDOM_VITESSE_MIN;
	}

	public static double getProbaRandomHaut() {
		return PROBA_RANDOM_HAUT;
	}

	public static double getProbaRandomGauche() {
		return PROBA_RANDOM_GAUCHE;
	}

	public static int getNombreFpsJeu() {
		return NOMBRE_FPS_JEU;
	}

	public static int getMargeVitesseMaxBalle() {
		return MARGE_VITESSE_MAX_BALLE;
	}

	public RaquetteVerticale getRaquetteVG() {
		return raquetteVG;
	}

	public void setRaquetteVG(RaquetteVerticale raquetteVG) {
		this.raquetteVG = raquetteVG;
	}

	public RaquetteVerticale getRaquetteVD() {
		return raquetteVD;
	}

	public void setRaquetteVD(RaquetteVerticale raquetteVD) {
		this.raquetteVD = raquetteVD;
	}

	public RaquetteHorizontale getRaquetteHH() {
		return raquetteHH;
	}

	public void setRaquetteHH(RaquetteHorizontale raquetteHH) {
		this.raquetteHH = raquetteHH;
	}

	public RaquetteHorizontale getRaquetteHB() {
		return raquetteHB;
	}

	public void setRaquetteHB(RaquetteHorizontale raquetteHB) {
		this.raquetteHB = raquetteHB;
	}

	public int getLargeur() {
		return largeur;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public ArrayList<Integer> getToucheEnfonce() {
		ArrayList<Integer> listeTouchesCopie = new ArrayList<Integer>(listeTouches);
		return listeTouchesCopie;
		//Copie (des références) de la listeTouches originale pour éviter que la vraie liste soit modifiée ici 
		//(supression d'un élément lorsqu'une touche est relâchée ou 
		//ajout d'un élément lorsqu'une touche est pressée) et que ça provoque une exception lors de son parcours
		//dans une raquette
	}

	//CAR TOUS LES JPANEL PEUVENT TESTER LA POSITION DE LA SOURIS
	public Point getPositionSouris() {
		return getMousePosition();
	}

	public ArrayList<Raquette> getJoueurs() {
		ArrayList<Raquette> listeJoueurs = new ArrayList<Raquette>();
		listeJoueurs.add(raquetteVG);
		listeJoueurs.add(raquetteVD);
		listeJoueurs.add(raquetteHH);
		listeJoueurs.add(raquetteHB);
		return listeJoueurs;
	}

	public void setBalle(Balle balle) {
		this.balle = balle;
	}

	public Balle getBalle() {
		return balle;
	}

	//Vérifie si la balle est affectée par une zone d'effets
	public void checkZonesEffetBalle() {
		for(int i = 0 ; i < terrain.getListeZonesEffet().size() ; i++)
			terrain.getListeZonesEffet().get(i).appliquerEffet(balle);
	}

	//Vérifie toutes les boules d'énergie du terrain pour voir sur lesquelles on applique
	//l'effet d'une zone d'effet
	public void checkZonesEffetBoulesEnergie() {
		//On vérifie si l'effet est activable sur la liste de boules d'énergie de chaque raquettes
		ArrayList<Raquette> listeRaquettes = getJoueurs();
		ArrayList<BouleEnergie> listeBoulesEnergie = new ArrayList<BouleEnergie>();
		for(int i = 0 ; i < terrain.getListeZonesEffet().size() ; i++)
			for(int j = 0 ; j < listeRaquettes.size() ; j++) {
				listeBoulesEnergie = listeRaquettes.get(j).getListeBoulesEnergie();
				for(int k = 0 ; k < listeBoulesEnergie.size() ; k++)
					terrain.getListeZonesEffet().get(i).appliquerEffet(listeBoulesEnergie.get(k));
			}
	}

	//GROSSE METHODE DE LA CLASSE QUI GERE LA COHERENCE DE TOUTE LA PARTIE
	//RETOURNE TRUE SI LA PARTIE EST TERMINEE (SCORE CIBLE ATTEINT)
	//ET FALSE SINON
	public void majScene() {
		//SI ON EST EN UN DEBUT DE PARTIE/MANCHE, ON ATTEND QUE LA TOUCHE ENTER SOIT PRESSEE
		if(isDebutPartieManche()) {
			actualiserAffichage();
			FenetreDeJeu.attendreXSecondes(getNombreSecondesAttenteDebutPartieManche());
			setDebutPartieManche(false);
		}
		//ON VERIFIE SI ON FAIT UNE CAPTURE D'ECRAN OU QU'ON MET EN PAUSE
		verifierTouchesDediees();
		if(!partieEnPause) { //Si la partie n'est pas en pause uniquement
			//TEST DES COLLISIONS
			//BALLE ET RAQUETTES
			if(raquetteHH.entreEnCollision(balle)) {
				//balle.setRaquetteDerniereCollision(Enum_Raquette.RAQUETTE_HH);
				raquetteHH.updateBalleApresCollision(this, balle, -2, 1, 0, INCREMENT_VITESSE_Y_BALLE);
			}
			else if(raquetteHB.entreEnCollision(balle)) {
				//balle.setRaquetteDerniereCollision(Enum_Raquette.RAQUETTE_HB);
				raquetteHB.updateBalleApresCollision(this, balle, -2, -1, 0, INCREMENT_VITESSE_Y_BALLE);
			}
			else if(raquetteVG.entreEnCollision(balle)) {
				balle.setRaquetteDerniereCollision(Enum_Raquette.RAQUETTE_VG);
				raquetteVG.updateBalleApresCollision(this, balle, 1, -2, INCREMENT_VITESSE_X_BALLE, 0);
			}
			else if(raquetteVD.entreEnCollision(balle)) {
				balle.setRaquetteDerniereCollision(Enum_Raquette.RAQUETTE_VD);
				raquetteVD.updateBalleApresCollision(this, balle, -1, -2, INCREMENT_VITESSE_X_BALLE, 0);
			}
			//BOULE D'ENERGIE ET BALLE
			actualiserBarreEnergieRaquettes();
			verifierCollisionBouleEnergieBalle();
			//DEPLACEMENT DES OBJETS
			deplacerBalle();
			deplacerRaquettes();
			deplacerBoulesEnergie();
			//CREATION ET SUPPRESSION BOULES D'ENERGIE
			verifierCreerBouleEnergie(); //on crée des boules d'énergie si nécessaire
			verifierSupprimerBouleEnergie(); //on supprime les boules d'énergie si elles sont en dehors du terrain
			//ON VERIFIE LES COLLISIONS ENTRE LES BOULES D'ENERGIE
			checkCollisionsBoulesEnergie();
			//ON VERIFIE LES EFFETS DU TERRAIN
			checkZonesEffetBalle(); //Pour la balle
			checkZonesEffetBoulesEnergie(); //Pour les boules d'énergie
			//ON VERIFIE SI LA BALLE A QUITTE LE TERRAIN ET ON MET A JOUR LES SCORE
			int typeSortie = (balle.getPositionX() < -balle.getDiametre()) ? 1 : //sortie à gauche
				(balle.getPositionX() > this.largeur) ? 2 : //sortie à droite
				(balle.getPositionY() < -balle.getDiametre()) ? 3 : //sortie en haut
				(balle.getPositionY() > this.hauteur) ? 4 : 0; //sortie en bas et pas de sortie
			if(typeSortie >= 1 && typeSortie <= 4) { //La balle a quitté le terrain
				/*On sait que la balle est sortie du terrain mais il faut voir le cas où une raquette VG la touche
				et qu'elle sort du terrain par la gauche (à cause d'un retour de balle causé par une zone d'effet
				du terrain.
				Cas identique pour la raquette VD*/
				switch(balle.getRaquetteDerniereCollision()) {
				case RAQUETTE_AUCUNE: //balle sortie dès le début de partie sans avoir touché de raquette
					break;
				case RAQUETTE_HH: //balle touchée la dernière fois par la raquette HH
					raquetteHH.incrementerScore();
					break;
				case RAQUETTE_HB: //balle touchée la dernière fois par la raquette HB
					raquetteHB.incrementerScore();
					break;
				case RAQUETTE_VG: //balle touchée la dernière fois par la raquette VG
					//Si la balle sort par la gauche
					if(typeSortie == 1)
						raquetteVD.incrementerScore();
					else //Sinon
						raquetteVG.incrementerScore();
					break;
				case RAQUETTE_VD: //balle touchée la dernière fois par la raquette VD
					//Si la balle sort par la droite
					if(typeSortie == 2)
						raquetteVG.incrementerScore();
					else
						raquetteVD.incrementerScore();
					break;
				}
				//Mise à jour de l'affichage des scores
				labelScoreRaquetteVG_2.setText("" + raquetteVG.getScore());
				labelScoreRaquetteVD_2.setText("" + raquetteVD.getScore());
				//On replace les raquettes et balle comme en début de partie/manche
				resetRaquettes();
				resetBalle();
				//On redonne de l'énergie aux raquettes
				augmenterEnergieRaquettes();
				//Une nouvelle partie va recommencer
				setDebutPartieManche(true);
				//CONTROLE SI PARTIE TERMINEE
				//0 : personne
				//1 : raquetteVG
				//2 : raquetteVD
				//3 : raquetteHH
				//4 : raquetteHB
				setGagnantPartie(
						(raquetteVG.getScore() == scoreCible) ? 1 :
						(raquetteVD.getScore() == scoreCible) ? 2 : 0
						);
			}
		}
	}

	//GESTION DES TOUCHES
	public void keyPressed(KeyEvent e) {
		if(!listeTouches.contains(e.getKeyCode()))
			listeTouches.add(e.getKeyCode()); //permet d'éviter des bugs car j'ai constaté
		//que la touche était ajoutée plusieurs fois dans le tableau, or, on veut
		//un unique élément par touche sinon, bug où la raquette ne peut plus s'arrêter
	}

	public void keyReleased(KeyEvent e) {
		listeTouches.remove(new Integer(e.getKeyCode()));
	}

	public void keyTyped(KeyEvent e) {
		//Utilisé pour les bonus et les boules d'énergie
		//if(!listeTouches.contains(e.getKeyCode()))
		//	listeTouches.add(e.getKeyCode());
	}

	//Affichage de la scène
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//On efface l'écran pour éviter d'avoir des traces de précédentes images
		setBackground(Color.WHITE);
		/*g.fillRect(0, 0, COEF_TAILLE_FILL_RECT_PAINT_COMPONENT * FenetreDeJeu.getLargeur(), 
				COEF_TAILLE_FILL_RECT_PAINT_COMPONENT * FenetreDeJeu.getHauteur());*/
		g.drawImage(this.terrain.getImage(), 0, 0, this); //le terrain
		panneauEnergie.setBackground(COULEUR_PANNEAU_POUVOIRS);
		panneauScores.setBackground(COULEUR_PANNEAU_SCORES);
		raquetteVG.afficher(g);
		raquetteVD.afficher(g);
		raquetteHH.afficher(g);
		raquetteHB.afficher(g);
		afficherBoulesEnergie(g);
		balle.afficher(g);
		//SI LA PARTIE EST EN PAUSE ON AFFICHE L'IMAGE DE PAUSE EN LA CENTRANT
		if(partieEnPause)
			g.drawImage(imagePause, 
					fenDeJeu.getWidth() / 2 - imagePause.getWidth(null) / 2, 
					fenDeJeu.getHeight() / 2 - imagePause.getHeight(null) / 2, this);
		//Actualisation affichage : IL FAUT ACTUALISER TOUS LES AUTRES ELEMENTS DONT LES PANNEAUX
		//car je n'ai pas add le panneauSud à this car cela pose des problèmes avec l'affichage de l'image
		//, même en redimenssionnant this avec setSize() ou setPreferedSize()
		//(Les panneaux (et donc le panneauSud) s'affiche derrière l'image et même pas sous elle...
		//C'est pour ça que dans FenetreDeJeu on a une méthode addScene qui ajoute la Scene + le panneauSud
		//qui est indépendant)
		//panneauSud.repaint();
		//repaint();
		actualiserAffichage();
	}
}





