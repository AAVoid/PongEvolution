


package fr.PongEvolution.Raquettes;

import java.awt.Image;

import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.Scene;






//Raquette qui sera au bord de l'écran, IA pour empêcher la balle de quitter 
//le terrain de jeu par la gauche ou la droite
public class RaquetteIAVerticaleBarriere extends RaquetteIAVerticale {
	public RaquetteIAVerticaleBarriere(String description, Image image, boolean invImageBarreAngle,
			Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, 
			int defense) {
		super(description, 0, image, invImageBarreAngle, imageBoulesEnergie, vitesseBouleEnergie,
				puissanceBouleEnergie, cadenceTirBouleEnergieCreee, configIntBEZoneEffetTerrain, defense);
	}

	public void deplacer(Scene sc) {
		setPositionY((int)(sc.getBalle().getPositionY()
				+ sc.getBalle().getDiametre() / 2 - getHauteur() / 2));
		//contrôle de la position pour éviter que la raquette ne sorte du terrain
		if(getPositionY() < sc.getRaquetteHH().getImage().getHeight(null))
			this.setPositionY(sc.getRaquetteHH().getImage().getHeight(null));
		else if(getPositionY() + getImage().getHeight(null) > 
			sc.getTerrain().getImage().getHeight(null) - sc.getRaquetteHB().getImage().getHeight(null))
			this.setPositionY(sc.getTerrain().getImage().getHeight(null) - getImage().getHeight(null)
					- sc.getRaquetteHB().getImage().getHeight(null));
	}
}
