


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
		//du changement de direction des boules d'énergie à créer pour une raquette : 
		//toutes les 0,1 seconde on pourra changer la direction des boules d'énergie créees à l'avenir
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
	private Image imageBarre; //Image de la barre pour la sélection de la direction de la balle
		//C'EST L'IMAGE QUI EST AFFICHEE
		//Initialisation redéfinie dans RaquetteVerticale et RaquetteHorizontale
	private Image imageBarreMilieu; //Utilisé pour mémoriser les autres barres en modifiant imageBarre
	private Image imageBarreHaut; //pour modifier l'image affichée
	private Image imageBarreBas; //Images initialisées dans raquetteVerticale, horizontale inutile car non inclus au jeu
	private boolean inverserImageBarre; //pour l'affichage de la barre de direction boule d'énergie
	private Image imageBoulesEnergie;
	private ArrayList<BouleEnergie> listeBoulesEnergie; //boules d'énergie que la raquette a crée
	private int vitesseBouleEnergie;
	private int puissanceBouleEnergie;
	private Enum_DirBouleEnergie directionBoulesEnergie; //Pour savoir si on envoie des boules d'énergie
		//vers le haut, le bas ou le milieu, détermine la barre affichée
	private String description; //description affichée au(x) joueur(x) lors du choix de raquette
	private double cadenceTirBouleEnergie; //Une raquette peut créer une boule d'énergie toutes les
		//cadenceTirBouleEnergie secondes
	private long timestampDerniereBouleEnergieCreee; //Actualisé à chaque fois qu'on crée une boule
		//d'énergie et testée dans la méthode verifierCreerBouleEnergie(Scene sc)
	private long timestampDernierChangementDirectionBouleEnergie; //Actualisé pour limiter la 
		//rapidité de changement de direction de boule d'énergie à créer pour une raquette
	private long timestampNouveauDeplaceable; //Actualisé pour indiquer à partir de quelle 
		//valeur de timestamp la raquette peut de nouveau se déplacer
		//Utilisé notamment pour l'effet d'une boule d'énergie sur une raquette : paralysie
	private int defense; //Pour diminuer voir rendre nul le temps de paralysie après s'être fait touché
		//par une boule d'énergie
	private ConfigurationInteractionZoneEffetTerrain configurationInteractionBouleEnergieZoneEffetTerrain; 
		//Oui, les noms longs de variables ne me dérangent absolument pas tant que la lisibilité est là 
		//même si ça fait bizarre et exagéré
	private int energie; //Energie de la raquette qui lui permet de tirer des boules d'énergie
	private static final int ENERGIE_MAX = 100; //Valeur maximale que peut prendre l'énergie d'une raquette
	private BarreEnergie barreEnergie; //Barre d'énergie pour afficher l'énergie disponible pour
		//créer des boulges d'énergie
	private static final int COEF_ENERGIE_PERDUE_BOULE_ENERGIE = 2; //Quand on crée une boule d'énergie
		//l'énergie diminue de la puissance de la boule d'énergie multipliée par ce coefficient
		
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
		//Barre d'énergie
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
	
	//Utilisé par la Scene pour reseter la raquette pour démarrer une nouvelle partie/manche
	public void reset(int positionX, int positionY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setDirectionBoulesEnergie(Raquette.getDirbouleenergieDefaut()); //Milieu, voir constante plus haut
		setImageBarre(getImageBarreMilieu()); //A modifier en conséquence si nécessaire
		getListeBoulesEnergie().clear();
	}
	
	//Pour récupérer de l'énergie à la fin de chaque manche de jeu
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
	
	//Affiche toutes les boules d'énergie créees par la raquette
	public void afficherBoulesEnergie(Graphics g) {
		for(int i = 0 ; i < listeBoulesEnergie.size() ; i++)
			listeBoulesEnergie.get(i).afficher(g);
	}
	
	//Savoir si on doit créer une boule d'énergie
	public abstract void verifierCreerBouleEnergie(Scene sc);
	
	//Pour que les boules d'énergie soient proprement effacées en sortant du terrain
	public void verifierSupprimerBoulesEnergie(Scene sc) {
		int tailleInitialeListe = listeBoulesEnergie.size();
		for(int i = 0 ; i < tailleInitialeListe ; i++) //Pour effacer d'un coup 
			//toutes les boules d'énergie de la raquette
			for(int j = 0 ; j < listeBoulesEnergie.size() ; j++)
				if(listeBoulesEnergie.get(j).verifierEffacer(sc, listeBoulesEnergie)) //Si on a effacé une boule d'énergie
					break; //on quitte la boucle pour éviter des erreurs car la taille de la liste a changé
	}
	
	//MODIFIE LE COMPORTEMENT DE LA BALLE APRES UNE COLLISION AVEC UNE RAQUETTE
	//AINSI CHAQUE TYPE DE RAQUETTE PEUT AVOIR UN EFFET PARTICULIER SUR UNE BALLE PAR REDEFINITION
	//CHOSES INTERESSANTES PAR RAPPORT A LA SCENE PAR EXEMPLE POUR 
	//TELEPORTER LA BALLE AU CENTRE DU TERRAIN AVANT QU'ELLE POURSUIVE SON CHEMIN 
	//VERS LA RAQUETTE ADVERSE, ETC.
	//nouvelleDirectionX/Y = -2 => la direction reste inchangée à celle actuelle
	//incrementVitesseX/Y = 0 => la vitesse reste inchangée sur cet axe
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
		//Paralysie de la raquette (voir méthode deplacer de raquette clavier verticale)
		//en tenant compte de sa défense aux boules d'énergie
		setTimestampNouveauDeplaceable(System.currentTimeMillis() + (long)(bouleEnergie.getPuissance() * 1000)
				- (long)(getDefense() * 1000));
	}
	
	//Crée une boule d'énergie à la bonne position et dans la bonne direction après avoir vérifié
	//si on peut la créer
	public abstract void creerBouleEnergie();
	
	//Actualise la barre d'énergie pour son affichage en fonction de l'énergie de la raquette
	public void actualiserBarreEnergie() {
		barreEnergie.setValeurActuelle(energie);
	}
}





