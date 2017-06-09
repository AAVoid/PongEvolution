


package fr.PongEvolution.Raquettes;

import java.awt.Image;

import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.Scene;






//Raquette déplacée verticalement par l'IA
public class RaquetteIAVerticale extends RaquetteVerticale {
	public RaquetteIAVerticale(String description, int vitesseDeplacementY, Image image, boolean invImageBarreAngle,
			Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, 
			int defense) {
		super(description, vitesseDeplacementY, image, invImageBarreAngle, imageBoulesEnergie, vitesseBouleEnergie,
				puissanceBouleEnergie, cadenceTirBouleEnergieCreee, configIntBEZoneEffetTerrain, defense, 1);
	}

	public void deplacer(Scene sc) {
		//ON PEUT ESSAYER D'AMELIORER CETTE IA, PAR EXEMPLE, QUE LA RAQUETTE SE REPLACE
		//AU CENTRE AUTOMATIQUEMENT SI ELLE VIENT DE RENVOYER LA BALLE, ETC.
		setDirectionY(0);
		if((int)(sc.getBalle().getPositionY() + sc.getBalle().getDiametre() / 2) < 
				(int)(getPositionY() + getLongueur() / 2))
			setDirectionY(-1);
		else
			setDirectionY(1);
		super.deplacer(sc);
		//contrôle de la position pour éviter que la raquette ne sorte du terrain
		if(getPositionY() < sc.getRaquetteHH().getImage().getHeight(null))
			this.setPositionY(sc.getRaquetteHH().getImage().getHeight(null));
		else if(getPositionY() + getImage().getHeight(null) > 
			sc.getTerrain().getImage().getHeight(null) - sc.getRaquetteHB().getImage().getHeight(null))
			this.setPositionY(sc.getTerrain().getImage().getHeight(null) - getImage().getHeight(null)
					- sc.getRaquetteHB().getImage().getHeight(null));
	}
	
	//UNE RAQUETTE IA NE PEUT PAS CREER DE BOULE D'ENERGIE
	public void verifierCreerBouleEnergie(Scene sc) {		
	}
}
