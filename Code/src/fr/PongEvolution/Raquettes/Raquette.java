


package fr.PongEvolution.Raquettes;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import fr.PongEvolution.Balles.Balle;
import fr.PongEvolution.BarreEnergie.BarreEnergie;
import fr.PongEvolution.BoulesEnergie.BouleEnergie;
import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.ElementDeJeu;
import fr.PongEvolution.Jeu.Scene;
import fr.PongEvolution.Main.FenetreDeJeu;






public abstract class Raquette extends ElementDeJeu {
	private static final Enum_DirBouleEnergie dirBouleEnergie_DEFAUT = Enum_DirBouleEnergie.MILIEU;
		//SI ON MODIFIE CETTE VALEUR IL FAUDRA AUSSI MODIFIER L'IMAGE AFFICHEE DE LA BARRE
		//VOIR METHODE reset
	private static final double CADENCE_CHANGEMENT_DIRECTION_BOULE_ENERGIE = 0.1; //pour limiter la vitesse
		//du changement de direction des boules d'�nergie � cr�er pour une raquette : 
		//toutes les 0,1 seconde on pourra changer la direction des boules d'�nergie cr�ees � l'avenir
	//DES CONFIGURATIONS DE BOULES D'ENERGIE PAR RAPPORT AUX ZONES D'EFFETS DU TERRAIN
	//PRETES A L'UTILISATION
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_BOULE_ENERGIE_ZE_TERRAIN_NORMALE = 
			new ConfigurationInteractionZoneEffetTerrain(true, true);
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_BOULE_ENERGIE_ZE_TERRAIN_TELEPORTABLE = 
			new ConfigurationInteractionZoneEffetTerrain(true, false);
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_BOULE_ENERGIE_ZE_TERRAIN_DIRECTION_CHANGEABLE = 
			new ConfigurationInteractionZoneEffetTerrain(false, true);
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_BOULE_ENERGIE_ZE_TERRAIN_AUCUN_EFFET = 
			new ConfigurationInteractionZoneEffetTerrain(false, false);
	private int hauteur;
	private int longueur;
	private int score; //score d'une raquette = le nombre de balle qu'elle a fait sortir du terrain
	private Image imageBarre; //Image de la barre pour la s�lection de la direction de la balle
		//C'EST L'IMAGE QUI EST AFFICHEE
		//Initialisation red�finie dans RaquetteVerticale et RaquetteHorizontale
	private Image imageBarreMilieu; //Utilis� pour m�moriser les autres barres en modifiant imageBarre
	private Image imageBarreHaut; //pour modifier l'image affich�e
	private Image imageBarreBas; //Images initialis�es dans raquetteVerticale, horizontale inutile car non inclus au jeu
	private boolean inverserImageBarre; //pour l'affichage de la barre de direction boule d'�nergie
	private Image imageBoulesEnergie;
	private ArrayList<BouleEnergie> listeBoulesEnergie; //boules d'�nergie que la raquette a cr�e
	private int vitesseBouleEnergie;
	private int puissanceBouleEnergie;
	private Enum_DirBouleEnergie directionBoulesEnergie; //Pour savoir si on envoie des boules d'�nergie
		//vers le haut, le bas ou le milieu, d�termine la barre affich�e
	private String description; //description affich�e au(x) joueur(x) lors du choix de raquette
	private double cadenceTirBouleEnergie; //Une raquette peut cr�er une boule d'�nergie toutes les
		//cadenceTirBouleEnergie secondes
	private long timestampDerniereBouleEnergieCreee; //Actualis� � chaque fois qu'on cr�e une boule
		//d'�nergie et test�e dans la m�thode verifierCreerBouleEnergie(Scene sc)
	private long timestampDernierChangementDirectionBouleEnergie; //Actualis� pour limiter la 
		//rapidit� de changement de direction de boule d'�nergie � cr�er pour une raquette
	private long timestampNouveauDeplaceable; //Actualis� pour indiquer � partir de quelle 
		//valeur de timestamp la raquette peut de nouveau se d�placer
		//Utilis� notamment pour l'effet d'une boule d'�nergie sur une raquette : paralysie
	private int defense; //Pour diminuer voir rendre nul le temps de paralysie apr�s s'�tre fait touch�
		//par une boule d'�nergie
	private ConfigurationInteractionZoneEffetTerrain configurationInteractionBouleEnergieZoneEffetTerrain; 
		//Oui, les noms longs de variables ne me d�rangent absolument pas tant que la lisibilit� est l� 
		//m�me si �a fait bizarre et exag�r�
	private int energie; //Energie de la raquette qui lui permet de tirer des boules d'�nergie
	private static final int ENERGIE_MAX = 100; //Valeur maximale que peut prendre l'�nergie d'une raquette
	private BarreEnergie barreEnergie; //Barre d'�nergie pour afficher l'�nergie disponible pour
		//cr�er des boulges d'�nergie
	private static final int COEF_ENERGIE_PERDUE_BOULE_ENERGIE = 2; //Quand on cr�e une boule d'�nergie
		//l'�nergie diminue de la puissance de la boule d'�nergie multipli�e par ce coefficient
		
	public Raquette(String description, int positionX, int positionY, int directionX,
			int directionY, int vitesseDeplacementX, int vitesseDeplacementY,
			Image image, boolean invImageBarre, Image imageBoulesEnergie, int vitesseBouleEnergie,
			int puissanceBouleEnergie, double cadenceTirBouleEnergieCreee, 
			ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, int defense) {
		super(positionX, positionY, directionX, directionY,
				vitesseDeplacementX, vitesseDeplacementY, image);
		setDescription(description);
		setHauteur(image.getHeight(null));
		setLongueur(image.getWidth(null));
		setScore(0);
		setInverserImageBarre(invImageBarre);
		setImageBoulesEnergie(imageBoulesEnergie);
		listeBoulesEnergie = new ArrayList<BouleEnergie>();
		setVitesseBouleEnergie(vitesseBouleEnergie);
		setPuissanceBouleEnergie(puissanceBouleEnergie);
		setDirectionBoulesEnergie(dirBouleEnergie_DEFAUT);
		setCadenceTirBouleEnergie(cadenceTirBouleEnergieCreee);
		setTimestampDernierChangementDirectionBouleEnergie(0);
		setTimestampNouveauDeplaceable(0);
		setConfigurationInteractionBouleEnergieZoneEffetTerrain(configIntBEZoneEffetTerrain);
		setDefense(defense);
		setEnergie(ENERGIE_MAX);
		//Barre d'�nergie
		try {
			barreEnergie = new BarreEnergie(
					ImageIO.read(new File(
							FenetreDeJeu.getCheminImagesAutres() + "/EnergyBarBorderT.png")), 
					ImageIO.read(new File(
							FenetreDeJeu.getCheminImagesAutres() + "/EnergyBarGreenT.png")),
					ImageIO.read(new File(
							FenetreDeJeu.getCheminImagesAutres() + "/EnergyBarOrangeT.png")),
					ImageIO.read(new File(
							FenetreDeJeu.getCheminImagesAutres() + "/EnergyBarRedT.png")),
					ENERGIE_MAX, 
					energie
					);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//Utilis� par la Scene pour reseter la raquette pour d�marrer une nouvelle partie/manche
	public void reset(int positionX, int positionY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setDirectionBoulesEnergie(Raquette.getDirbouleenergieDefaut()); //Milieu, voir constante plus haut
		setImageBarre(getImageBarreMilieu()); //A modifier en cons�quence si n�cessaire
		getListeBoulesEnergie().clear();
	}
	
	//Pour r�cup�rer de l'�nergie � la fin de chaque manche de jeu
	public void recupererEnergie(int quantite) {
		energie += quantite;
		if(energie > ENERGIE_MAX)
			energie = ENERGIE_MAX;
	}

	public static int getCoefEnergiePerdueBouleEnergie() {
		return COEF_ENERGIE_PERDUE_BOULE_ENERGIE;
	}

	public ConfigurationInteractionZoneEffetTerrain getConfigurationInteractionBouleEnergieZoneEffetTerrain() {
		return configurationInteractionBouleEnergieZoneEffetTerrain;
	}

	public void setConfigurationInteractionBouleEnergieZoneEffetTerrain(
			ConfigurationInteractionZoneEffetTerrain configurationInteractionBouleEnergieZoneEffetTerrain) {
		this.configurationInteractionBouleEnergieZoneEffetTerrain = configurationInteractionBouleEnergieZoneEffetTerrain;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigBouleEnergieZeTerrainNormale() {
		return CONFIG_BOULE_ENERGIE_ZE_TERRAIN_NORMALE;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigBouleEnergieZeTerrainTeleportable() {
		return CONFIG_BOULE_ENERGIE_ZE_TERRAIN_TELEPORTABLE;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigBouleEnergieZeTerrainDirectionChangeable() {
		return CONFIG_BOULE_ENERGIE_ZE_TERRAIN_DIRECTION_CHANGEABLE;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigBouleEnergieZeTerrainAucunEffet() {
		return CONFIG_BOULE_ENERGIE_ZE_TERRAIN_AUCUN_EFFET;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getEnergie() {
		return energie;
	}

	public void setEnergie(int energie) {
		this.energie = energie;
	}

	public BarreEnergie getBarreEnergie() {
		return barreEnergie;
	}

	public void setBarreEnergie(BarreEnergie barreEnergie) {
		this.barreEnergie = barreEnergie;
	}

	public static int getEnergieMax() {
		return ENERGIE_MAX;
	}

	public long getTimestampNouveauDeplaceable() {
		return timestampNouveauDeplaceable;
	}

	public void setTimestampNouveauDeplaceable(long timestampNouveauDeplaceable) {
		this.timestampNouveauDeplaceable = timestampNouveauDeplaceable;
	}

	public long getTimestampDernierChangementDirectionBouleEnergie() {
		return timestampDernierChangementDirectionBouleEnergie;
	}

	public void setTimestampDernierChangementDirectionBouleEnergie(
			long timestampDernierChangementDirectionBouleEnergie) {
		this.timestampDernierChangementDirectionBouleEnergie = timestampDernierChangementDirectionBouleEnergie;
	}

	public static double getCadenceChangementDirectionBouleEnergie() {
		return CADENCE_CHANGEMENT_DIRECTION_BOULE_ENERGIE;
	}

	public long getTimestampDerniereBouleEnergieCreee() {
		return timestampDerniereBouleEnergieCreee;
	}

	public void setTimestampDerniereBouleEnergieCreee(
			long timestampDerniereBouleEnergieCreee) {
		this.timestampDerniereBouleEnergieCreee = timestampDerniereBouleEnergieCreee;
	}

	public double getCadenceTirBouleEnergie() {
		return cadenceTirBouleEnergie;
	}

	public void setCadenceTirBouleEnergie(double cadenceTirBouleEnergie) {
		this.cadenceTirBouleEnergie = cadenceTirBouleEnergie;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getImageBarreMilieu() {
		return imageBarreMilieu;
	}

	public void setImageBarreMilieu(Image imageBarreMilieu) {
		this.imageBarreMilieu = imageBarreMilieu;
	}

	public Image getImageBarreHaut() {
		return imageBarreHaut;
	}

	public void setImageBarreHaut(Image imageBarreHaut) {
		this.imageBarreHaut = imageBarreHaut;
	}

	public Image getImageBarreBas() {
		return imageBarreBas;
	}

	public void setImageBarreBas(Image imageBarreBas) {
		this.imageBarreBas = imageBarreBas;
	}

	public static Enum_DirBouleEnergie getDirbouleenergieDefaut() {
		return dirBouleEnergie_DEFAUT;
	}

	public Enum_DirBouleEnergie getDirectionBoulesEnergie() {
		return directionBoulesEnergie;
	}

	public void setDirectionBoulesEnergie(
			Enum_DirBouleEnergie directionBoulesEnergie) {
		this.directionBoulesEnergie = directionBoulesEnergie;
	}

	public int getPuissanceBouleEnergie() {
		return puissanceBouleEnergie;
	}

	public void setPuissanceBouleEnergie(int puissanceBouleEnergie) {
		this.puissanceBouleEnergie = puissanceBouleEnergie;
	}

	public Image getImageBoulesEnergie() {
		return imageBoulesEnergie;
	}

	public void setImageBoulesEnergie(Image imageBoulesEnergie) {
		this.imageBoulesEnergie = imageBoulesEnergie;
	}

	public int getVitesseBouleEnergie() {
		return vitesseBouleEnergie;
	}

	public void setVitesseBouleEnergie(int vitesseBouleEnergie) {
		this.vitesseBouleEnergie = vitesseBouleEnergie;
	}

	public ArrayList<BouleEnergie> getListeBoulesEnergie() {
		return listeBoulesEnergie;
	}

	public void setListeBoulesEnergie(ArrayList<BouleEnergie> listeBoulesEnergie) {
		this.listeBoulesEnergie = listeBoulesEnergie;
	}

	public Image getImageBarre() {
		return imageBarre;
	}

	public void setImageBarre(Image imageBarre) {
		this.imageBarre = imageBarre;
	}

	public boolean isInverserImageBarre() {
		return inverserImageBarre;
	}

	public void setInverserImageBarre(boolean inverserImageBarre) {
		this.inverserImageBarre = inverserImageBarre;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void incrementerScore() {
		this.score++;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public int getLongueur() {
		return longueur;
	}

	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}
	
	//Affichage de la raquette
	public void afficher(Graphics g) {
		g.drawImage(getImage(), getPositionX(), getPositionY(), null);
	}
	
	//Affiche toutes les boules d'�nergie cr�ees par la raquette
	public void afficherBoulesEnergie(Graphics g) {
		for(int i = 0 ; i < listeBoulesEnergie.size() ; i++)
			listeBoulesEnergie.get(i).afficher(g);
	}
	
	//Savoir si on doit cr�er une boule d'�nergie
	public abstract void verifierCreerBouleEnergie(Scene sc);
	
	//Pour que les boules d'�nergie soient proprement effac�es en sortant du terrain
	public void verifierSupprimerBoulesEnergie(Scene sc) {
		int tailleInitialeListe = listeBoulesEnergie.size();
		for(int i = 0 ; i < tailleInitialeListe ; i++) //Pour effacer d'un coup 
			//toutes les boules d'�nergie de la raquette
			for(int j = 0 ; j < listeBoulesEnergie.size() ; j++)
				if(listeBoulesEnergie.get(j).verifierEffacer(sc, listeBoulesEnergie)) //Si on a effac� une boule d'�nergie
					break; //on quitte la boucle pour �viter des erreurs car la taille de la liste a chang�
	}
	
	//MODIFIE LE COMPORTEMENT DE LA BALLE APRES UNE COLLISION AVEC UNE RAQUETTE
	//AINSI CHAQUE TYPE DE RAQUETTE PEUT AVOIR UN EFFET PARTICULIER SUR UNE BALLE PAR REDEFINITION
	//CHOSES INTERESSANTES PAR RAPPORT A LA SCENE PAR EXEMPLE POUR 
	//TELEPORTER LA BALLE AU CENTRE DU TERRAIN AVANT QU'ELLE POURSUIVE SON CHEMIN 
	//VERS LA RAQUETTE ADVERSE, ETC.
	//nouvelleDirectionX/Y = -2 => la direction reste inchang�e � celle actuelle
	//incrementVitesseX/Y = 0 => la vitesse reste inchang�e sur cet axe
	public void updateBalleApresCollision(Scene sc, Balle balle, int nouvelleDirectionX, int nouvelleDirectionY,
			int incrementVitesseX, int incrementVitesseY) {
		//DIRECTIONS
		//X
		if(nouvelleDirectionX != -2)
			balle.setDirectionX(nouvelleDirectionX);
		//Y
		if(nouvelleDirectionY != -2)
			balle.setDirectionY(nouvelleDirectionY);
		//VITESSES
		//X
		if(balle.getVitesseDeplacementX() + incrementVitesseX <= balle.getVitesseMax())
			balle.setVitesseDeplacementX(balle.getVitesseDeplacementX() + incrementVitesseX);
		else
			balle.setVitesseDeplacementX(balle.getVitesseMax());
		//Y
		if(balle.getVitesseDeplacementY() + incrementVitesseY <= balle.getVitesseMax())
			balle.setVitesseDeplacementY(balle.getVitesseDeplacementY() + incrementVitesseY);
		else
			balle.setVitesseDeplacementY(balle.getVitesseMax());
		//EFFET DE LA BALLE SUITE A LA COLLISION AVEC LA RAQUETTE
		balle.updateApresCollisionRaquette(sc, this);
	}
	
	//COMPORTEMENT DE LA RAQUETTE APRES AVOIR ETE TOUCHEE PAR UNE BOULE D'ENERGIE
	public void updateApresCollisionBouleEnergie(Scene sc, BouleEnergie bouleEnergie) {
		//Paralysie de la raquette (voir m�thode deplacer de raquette clavier verticale)
		//en tenant compte de sa d�fense aux boules d'�nergie
		setTimestampNouveauDeplaceable(System.currentTimeMillis() + (long)(bouleEnergie.getPuissance() * 1000)
				- (long)(getDefense() * 1000));
	}
	
	//Cr�e une boule d'�nergie � la bonne position et dans la bonne direction apr�s avoir v�rifi�
	//si on peut la cr�er
	public abstract void creerBouleEnergie();
	
	//Actualise la barre d'�nergie pour son affichage en fonction de l'�nergie de la raquette
	public void actualiserBarreEnergie() {
		barreEnergie.setValeurActuelle(energie);
	}
}





