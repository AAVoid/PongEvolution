


package fr.PongEvolution.Raquettes;

import java.awt.Image;

import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.Scene;






//Raquette qui sera au bord de l'écran, IA pour empêcher la balle de quitter
//le terrain de jeu par le haut ou le bas
public class RaquetteIAHorizontaleBarriere extends RaquetteIAHorizontale {
	public RaquetteIAHorizontaleBarriere(String description, Image image, boolean invImageBarreAngle, 
			Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain,
			int defense) {
		super(description, 0, image, invImageBarreAngle, imageBoulesEnergie, vitesseBouleEnergie,
				puissanceBouleEnergie, cadenceTirBouleEnergieCreee, configIntBEZoneEffetTerrain, defense);
	}

	public void deplacer(Scene sc) {
		setPositionX((int)(sc.getBalle().getPositionX() 
				+ sc.getBalle().getDiametre() / 2 - getLongueur() / 2));
		//Contrôle de la position pour éviter de quitter le terrain
		if(getPositionX() < sc.getRaquetteVG().getImage().getWidth(null))
			this.setPositionX(sc.getRaquetteVG().getImage().getWidth(null));
		else if(getPositionX() + getImage().getWidth(null) > sc.getTerrain().getImage().getWidth(null)
				- sc.getRaquetteVD().getImage().getWidth(null))
			this.setPositionX(sc.getTerrain().getImage().getWidth(null) - getImage().getWidth(null)
					- sc.getRaquetteVD().getImage().getWidth(null));
	}
}
