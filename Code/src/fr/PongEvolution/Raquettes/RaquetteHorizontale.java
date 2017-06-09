


package fr.PongEvolution.Raquettes;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Main.FenetreDeJeu;






public abstract class RaquetteHorizontale extends Raquette {
	private static final String NOM_FICHIER_BARRE_VERTICALE_MILIEU = "/VerticalBar_1T.png";

	public RaquetteHorizontale(String description, int vitesseDeplacementX, Image image, boolean invImageBarreAngle,
			Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, 
			ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, int defense) {
		super(description, 0, 0, 0, 0, vitesseDeplacementX, 0, image, invImageBarreAngle, 
				imageBoulesEnergie, vitesseBouleEnergie, puissanceBouleEnergie, cadenceTirBouleEnergieCreee,
				configIntBEZoneEffetTerrain, defense);
		try {
			setImageBarre(ImageIO.read(new File(FenetreDeJeu.getCheminImagesRaquettes() + NOM_FICHIER_BARRE_VERTICALE_MILIEU)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getNomFichierBarreVerticaleMilieu() {
		return NOM_FICHIER_BARRE_VERTICALE_MILIEU;
	}

	//On ne redefinie pas l'affichage de la raquette car on ne veut pas afficher de barre pour les raquettes Horizontales
	/*public void afficher(Graphics g) {
		super.afficher(g);
		//Affichage de l'image imageBarreAngle
		if(!isInverserImageBarre())
			g.drawImage(getImageBarre(), getPositionX() + getLongueur() / 2, getPositionY() + getHauteur(), null);
		else
			g.drawImage(getImageBarre(), getPositionX() + getLongueur() / 2, getPositionY() - getImageBarre().getHeight(null), null);
	}
	*/
	
	//PAS PREVU QU'UNE RAQUETTE HORIZONTALE PUISSE CREER UNE BOULE D'ENERGIE
	//Crée une boule d'énergie à la bonne position et dans la bonne direction
	public void creerBouleEnergie() {
		//... CODE A ADAPTER PAR RAPPORT A CELUI DE LA CLASSE RAQUETTE VERTICALE
	}
}
