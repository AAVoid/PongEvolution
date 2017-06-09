


package fr.PongEvolution.Balles;

import java.awt.Graphics;
import java.awt.Image;

import fr.PongEvolution.BoulesEnergie.BouleEnergie;
import fr.PongEvolution.BoulesEnergie.Enum_ProprietaireBouleEnergie;
import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.ElementDeJeu;
import fr.PongEvolution.Jeu.Scene;
import fr.PongEvolution.Raquettes.Raquette;






public class Balle extends ElementDeJeu {
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_ZE_TERRAIN_NORMALE = 
			new ConfigurationInteractionZoneEffetTerrain(true, true);
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_ZE_TERRAIN_TELEPORTABLE = 
			new ConfigurationInteractionZoneEffetTerrain(true, false);
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_ZE_TERRAIN_DIRECTION_CHANGEABLE = 
			new ConfigurationInteractionZoneEffetTerrain(false, true);
	private static final ConfigurationInteractionZoneEffetTerrain CONFIG_ZE_TERRAIN_AUCUN_EFFET = 
			new ConfigurationInteractionZoneEffetTerrain(false, false);
	private int diametre;
	private Enum_Raquette raquetteDerniereCollision; //Pour savoir avec quelle raquette a eu lieu la dernière collision
	private int vitesseMax; //On limite la vitesse maximale de la balle pour éviter des problèmes 
		//(balle qui "saute" une raquette notamment)
		//vitesseMax = LARGEUR = HAUTEUR DE LA BALLE (image de la balle carrée) - MARGE SCENE
	private ConfigurationInteractionZoneEffetTerrain configIntZETerrain;
		
	public Balle(Image image, ConfigurationInteractionZoneEffetTerrain configIntZETerrain) {
		super(0, 0, 0, 0, 0, 0, image);
		setDiametre(getImage().getWidth(null)); //L'image de la balle est considérée carrée
		setRaquetteDerniereCollision(Enum_Raquette.RAQUETTE_AUCUNE); //pas de collision
		setVitesseMax(0); //La SCENE modifie cette valeur
		//En fonction de la configuration des interaction avec les zones d'effets du terrain
		setConfigIntZETerrain(configIntZETerrain);
		setTeleportable(configIntZETerrain.isTeleportable());
		setDirectionChangeable(configIntZETerrain.isDirectionChangeable());
	}
	
	//Utilisé par la Scene pour reseter la balle pour démarrer une nouvelle partie/manche
	public void reset(int positionX, int positionY, int directionX, int directionY,
		int vitesseDeplacementX, int vitesseDeplacementY, int vitesseMax, 
		Enum_Raquette raquetteDerniereCollision) {
		setVitesseMax(vitesseMax);
		setRaquetteDerniereCollision(raquetteDerniereCollision);
		setPositionX(positionX);
		setPositionY(positionY);
		setDirectionX(directionX);
		setDirectionY(directionY);
		//setVitesseDeplacementX(0);
		//setVitesseDeplacementY(0);
		setVitesseDeplacementX(vitesseDeplacementX);
		setVitesseDeplacementY(vitesseDeplacementY);
		setTeleportable(false);
	}

	public ConfigurationInteractionZoneEffetTerrain getConfigIntZETerrain() {
		return configIntZETerrain;
	}

	public void setConfigIntZETerrain(
			ConfigurationInteractionZoneEffetTerrain configIntZETerrain) {
		this.configIntZETerrain = configIntZETerrain;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigZeTerrainNormale() {
		return CONFIG_ZE_TERRAIN_NORMALE;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigZeTerrainTeleportable() {
		return CONFIG_ZE_TERRAIN_TELEPORTABLE;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigZeTerrainDirectionChangeable() {
		return CONFIG_ZE_TERRAIN_DIRECTION_CHANGEABLE;
	}

	public static ConfigurationInteractionZoneEffetTerrain getConfigZeTerrainAucunEffet() {
		return CONFIG_ZE_TERRAIN_AUCUN_EFFET;
	}

	public int getVitesseMax() {
		return vitesseMax;
	}

	public void setVitesseMax(int vitesseMax) {
		this.vitesseMax = vitesseMax;
	}

	public int getDiametre() {
		return diametre;
	}

	public void setDiametre(int diametre) {
		this.diametre = diametre;
	}
	
	public Enum_Raquette getRaquetteDerniereCollision() {
		return raquetteDerniereCollision;
	}

	public void setRaquetteDerniereCollision(Enum_Raquette raquetteDerniereCollision) {
		this.raquetteDerniereCollision = raquetteDerniereCollision;
	}

	public void afficher(Graphics g) {	
		g.drawImage(getImage(), getPositionX(), getPositionY(), null);
	}
	
	//MODIFIE LE COMPORTEMENT DE LA BALLE APRES UNE COLLISION AVEC BOULE D'ENERGIE
	//UNE BOULE D'ENERGIE A UN EFFET SUR UNE BALLE 
	//(méthode updateBalleApresCollision(Scene sc, Balle balle, ArrayList<BouleEnergie> listeBoulesEnergie))
	//MAIS UNE BALLE PEUT AUSSI AVOIR UN EFFET
	//EN SE FAISANT TOUCHEE PAR UNE BOULE D'ENERGIE
	//nouvelleDirectionX/Y = -2 => la direction reste inchangée à celle actuelle
	//incrementVitesseX/Y = 0 => la vitesse reste inchangée sur cet axe
	//L'ATTRIBUT raquetteDerniereCollision EST MODIFIE EN FONCTION DU PROPRIETAIRE
	//DE LA BOULE D'ENERGIE QUI L'A TOUCHE
	public void updateApresCollisionBouleEnergie(Scene sc, int nouvelleDirectionX, int nouvelleDirectionY,
		int incrementVitesseX, int incrementVitesseY, BouleEnergie bouleEnergie) {
		//DIRECTIONS
		//X
		if(nouvelleDirectionX != -2)
			setDirectionX(nouvelleDirectionX);
		//Y
		if(nouvelleDirectionY != -2)
			setDirectionY(nouvelleDirectionY);
		//VITESSES
		//X
		if(getVitesseDeplacementX() + incrementVitesseX <= getVitesseMax())
			setVitesseDeplacementX(getVitesseDeplacementX() + incrementVitesseX);
		else
			setVitesseDeplacementX(getVitesseMax());
		//Y
		if(getVitesseDeplacementY() + incrementVitesseY <= getVitesseMax())
			setVitesseDeplacementY(getVitesseDeplacementY() + incrementVitesseY);
		else
			setVitesseDeplacementY(getVitesseMax());
		//CHANGEMENT DE raquetteDerniereCollision 
		//Au cas où si la balle n'a touché de raquette, qu'on compte un point malgré tout en fonction
		//de la raquette qui a crée la boule d'énergie qui l'a faite sortir du terrain
		//LORSQU'UNE BOULE D'ENERGIE TOUCHE UNE RAQUETTE, ON MODIFIE LA DERNIERE RAQUETTE
		//QUI L'A TOUCHEE
		setRaquetteDerniereCollision(
			(bouleEnergie.getProprietaire() == Enum_ProprietaireBouleEnergie.RAQUETTE_VG)
			? Enum_Raquette.RAQUETTE_VG :
			(bouleEnergie.getProprietaire() == Enum_ProprietaireBouleEnergie.RAQUETTE_VD)
			? Enum_Raquette.RAQUETTE_VD :
			(bouleEnergie.getProprietaire() == Enum_ProprietaireBouleEnergie.RAQUETTE_HH)
			? Enum_Raquette.RAQUETTE_HH :
			(bouleEnergie.getProprietaire() == Enum_ProprietaireBouleEnergie.RAQUETTE_HB)
			? Enum_Raquette.RAQUETTE_HB : Enum_Raquette.RAQUETTE_VG
			);
	}
	
	//Méthode appelée après la collision avec une raquette
	public void updateApresCollisionRaquette(Scene sc, Raquette raquette) {
		if(configIntZETerrain.isTeleportable())
			setTeleportable(true);
	}
}









