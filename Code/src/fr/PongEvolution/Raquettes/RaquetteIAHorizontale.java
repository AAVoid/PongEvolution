


///NON UTILISE EN JEU


package fr.PongEvolution.Raquettes;

import java.awt.Image;

import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.Scene;






//Raquette déplacée par l'IA horizontalement
public class RaquetteIAHorizontale extends RaquetteHorizontale {
	public RaquetteIAHorizontale(String description, int vitesseDeplacementX, Image image, boolean invImageBarreAngle,
			Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, 
			int defense) {
		super(description, vitesseDeplacementX, image, invImageBarreAngle, imageBoulesEnergie, vitesseBouleEnergie,
				puissanceBouleEnergie, cadenceTirBouleEnergieCreee, configIntBEZoneEffetTerrain, defense);
	}
	
	//On n'affiche pas la barre
	/*public void afficher(Graphics g) {
		g.drawImage(getImage(), getPositionX(), getPositionY(), null);
	}*/

	public void deplacer(Scene sc) {
		//ON PEUT ESSAYER D'AMELIORER CETTE IA, PAR EXEMPLE, QUE LA RAQUETTE SE REPLACE
		//AU CENTRE AUTOMATIQUEMENT SI ELLE VIENT DE RENVOYER LA BALLE, ETC.
		setDirectionX(0);
		if((int)(sc.getBalle().getPositionX() + sc.getBalle().getDiametre() / 2) < 
				(int)(getPositionX() + getLongueur() / 2))
			setDirectionX(-1);
		else
			setDirectionX(1);
		super.deplacer(sc);
		//Contrôle de la position pour éviter de quitter le terrain
		if(getPositionX() < sc.getRaquetteVG().getImage().getWidth(null))
			this.setPositionX(sc.getRaquetteVG().getImage().getWidth(null));
		else if(getPositionX() + getImage().getWidth(null) > sc.getTerrain().getImage().getWidth(null)
				- sc.getRaquetteVD().getImage().getWidth(null))
			this.setPositionX(sc.getTerrain().getImage().getWidth(null) - getImage().getWidth(null)
					- sc.getRaquetteVD().getImage().getWidth(null));
	}
	
	//UNE RAQUETTE IA ET SURTOUT HORIZONTALE NE PEUT PAS CREER DE BOULE D'ENERGIE
	public void verifierCreerBouleEnergie(Scene sc) {		
	}
}
