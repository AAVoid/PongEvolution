


package fr.PongEvolution.Raquettes;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.PongEvolution.BoulesEnergie.BouleEnergie;
import fr.PongEvolution.BoulesEnergie.Enum_DirectionBouleEnergie;
import fr.PongEvolution.BoulesEnergie.Enum_ProprietaireBouleEnergie;
import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Main.FenetreDeJeu;





//J'AI CHOISI DE NE PAS SPECIALISER CETTE CLASSE EN RaquetteVerticaleGauche ou Droite car
//CA AURAIT EU POUR CONSEQUENCES QU'IL AURAIT FALLU AUSSI SPECIALISER LES CLASSES
//IA, IA BARRIERE, ETC.
//ON PEUT SAVOIR SI ON A UNE RAQUETTE VERTICALE GAUCHE OU DROITE GRACE AU BOOLEEN D'INVERSION
//DE L'IMAGE DE LA BARRE D'ENERGIE
//LE DIAGRAMME DE CLASSE SERA PLUS SIMPLE DANS CE CAS, ET LE CODE AUSSI PEUT-ETRE
//J'AI FAIT LE CHOIX DE NE PAS SPECIALISER
public abstract class RaquetteVerticale extends Raquette {
	private static final String NOM_FICHIER_BARRE_HORIZONTALE_MILIEU = "/HorizontalBar_";
	private static final String NOM_FICHIER_BARRE_HORIZONTALE_HAUT = "/UpBar_";
	private static final String NOM_FICHIER_BARRE_HORIZONTALE_BAS = "/DownBar_";
	
	public RaquetteVerticale(String description, int vitesseDeplacementY, Image image, boolean invImageBarreAngle,
			Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, 
			int defense, int numeroBarres) {
		super(description, 0, 0, 0, 0, 0, vitesseDeplacementY, image, invImageBarreAngle, 
				imageBoulesEnergie, vitesseBouleEnergie, puissanceBouleEnergie, cadenceTirBouleEnergieCreee,
				configIntBEZoneEffetTerrain, defense);
		//IMAGE BARRE HAUT
		try {
			setImageBarreHaut(ImageIO.read(new File(FenetreDeJeu.getCheminImagesRaquettes() + 
					NOM_FICHIER_BARRE_HORIZONTALE_HAUT + numeroBarres + "T.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//IMAGE BARRE BAS
		try {
			setImageBarreBas(ImageIO.read(new File(FenetreDeJeu.getCheminImagesRaquettes() + 
					NOM_FICHIER_BARRE_HORIZONTALE_BAS + numeroBarres + "T.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//IMAGE BARRE MILIEU
		try {
			setImageBarreMilieu(ImageIO.read(new File(FenetreDeJeu.getCheminImagesRaquettes() + 
					NOM_FICHIER_BARRE_HORIZONTALE_MILIEU + numeroBarres + "T.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//IMAGE AFFICHEE
		//On affiche en fonction de l'état de la direction des boules d'énergie
		if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.HAUT) {
			setImageBarre(getImageBarreHaut());
		}
		else if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.BAS) {
			setImageBarre(getImageBarreBas());
		}
		else //milieu
			setImageBarre(getImageBarreMilieu());
	}

	public static String getNomFichierBarreHorizontaleMilieu() {
		return NOM_FICHIER_BARRE_HORIZONTALE_MILIEU;
	}

	public static String getNomFichierBarreHorizontaleHaut() {
		return NOM_FICHIER_BARRE_HORIZONTALE_HAUT;
	}

	public static String getNomFichierBarreHorizontaleBas() {
		return NOM_FICHIER_BARRE_HORIZONTALE_BAS;
	}

	//On ne redéfinit pas l'affichage de la raquette et de la barre car la barre n'est affichée que pour
	//une raquette verticale clavier
	/*public void afficher(Graphics g) {
		super.afficher(g);
		//Affichage de l'image imageBarreAngle
		if(!isInverserImageBarre()) //la barre doit s'afficher à droite de la raquette naturellement
			g.drawImage(getImageBarre(), getPositionX() + getLongueur(), getPositionY() + getHauteur() / 2, null);
		else //on a une raquette Droite, on place la barre à gauche de la raquette
			g.drawImage(getImageBarre(), getPositionX() - getImageBarre().getWidth(null), getPositionY() + getHauteur() / 2, null);
	}*/
	
	//Crée une boule d'énergie à la bonne position et dans la bonne direction
	public void creerBouleEnergie() {
		int positionX = 0;
		int positionY = (int)(getPositionY() + (int)(getImage().getHeight(null) / 2) - (int)(getImageBoulesEnergie().getHeight(null) / 2));
		if(!isInverserImageBarre()) //on a une raquette verticale Gauche
			positionX = (int)(getPositionX() + getImage().getWidth(null));
		else //raquette verticale Droite
			positionX = (int)(getPositionX() - getImageBoulesEnergie().getWidth(null));
		//CREATION DE LA BOULE D'ENERGIE ET MODIFICATION DE SES ATTRIBUTS EN FONCTION DE LA CONFIGURATION
		//DE LA RAQUETTE
		//---------------------------
		BouleEnergie bouleEnergie_Raquette_VG = new BouleEnergie(Enum_ProprietaireBouleEnergie.RAQUETTE_VG,
				positionX, positionY, 
				(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.HAUT) ? Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_HAUT :
				(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.BAS) ? Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_BAS :
				(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.MILIEU) ? Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT : Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT
				, getImageBoulesEnergie(), getPuissanceBouleEnergie(), getVitesseBouleEnergie());
		bouleEnergie_Raquette_VG.setTeleportable(
				getConfigurationInteractionBouleEnergieZoneEffetTerrain().isTeleportable());
		bouleEnergie_Raquette_VG.setDirectionChangeable(
				getConfigurationInteractionBouleEnergieZoneEffetTerrain().isDirectionChangeable());
		//---------------------------
		BouleEnergie bouleEnergie_Raquette_VD = new BouleEnergie(Enum_ProprietaireBouleEnergie.RAQUETTE_VD,
				positionX, positionY, 
				(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.HAUT) ? Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_HAUT :
				(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.BAS) ? Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_BAS :
				(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.MILIEU) ? Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_TOUT_DROIT : Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT
				, getImageBoulesEnergie(), getPuissanceBouleEnergie(), getVitesseBouleEnergie());
		bouleEnergie_Raquette_VD.setTeleportable(
				getConfigurationInteractionBouleEnergieZoneEffetTerrain().isTeleportable());
		bouleEnergie_Raquette_VD.setDirectionChangeable(
				getConfigurationInteractionBouleEnergieZoneEffetTerrain().isDirectionChangeable());
		//---------------------------
		if(!isInverserImageBarre()) //Raquette verticale Gauche
			getListeBoulesEnergie().add(bouleEnergie_Raquette_VG);
		else //Raquette verticale Droite
			getListeBoulesEnergie().add(bouleEnergie_Raquette_VD);
		//ACTUALISATION TIMESTAMP
		setTimestampDerniereBouleEnergieCreee(System.currentTimeMillis());
	}
}





