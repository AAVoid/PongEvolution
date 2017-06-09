

//NON PREVU DANS LE JEU MAIS CONSERVE POUR ACCELERER LES AMELIORATIONS EVENTUELLES DU JEU

/*

package fr.PongEvolution.Raquettes;

import java.awt.Image;
import java.util.ArrayList;
import fr.PongEvolution.Jeu.Scene;






//Raquette qu'on déplace horizontalement avec le clavier
public class RaquetteClavierHorizontale extends RaquetteHorizontale {
	private ConfigurationTouchesHorizontale configurationTouchesHorizontale; //permet au joueur de 
		//configurer ses touches de jeu
	
	public RaquetteClavierHorizontale(int vitesseDeplacementX, Image image, boolean invImageBarreAngle,
			ConfigurationTouchesHorizontale configurationTouchesHorizontale) {
		super(vitesseDeplacementX, image, invImageBarreAngle);
		setConfigurationTouchesHorizontale(configurationTouchesHorizontale);
	}

	public ConfigurationTouchesHorizontale getConfigurationTouchesHorizontale() {
		return configurationTouchesHorizontale;
	}

	public void setConfigurationTouchesHorizontale(ConfigurationTouchesHorizontale configurationTouchesHorizontale) {
		this.configurationTouchesHorizontale = configurationTouchesHorizontale;
	}

	public void deplacer(Scene sc) {
		//flèche gauche : 37 ; flèche haut : 38 ; flèche droite : 39 ; flèche bas : 40
		ArrayList<Integer> listeTouches = sc.getToucheEnfonce();
		setDirectionX(0); //pour arrêter la raquette si on n'appuie sur aucune touche
		for(int touche : listeTouches) {
			if(touche == configurationTouchesHorizontale.getNumeroToucheGauche())
				setDirectionX(-1);
			else if(touche == configurationTouchesHorizontale.getNumeroToucheDroite())
				setDirectionX(1);
		}
		super.deplacer(sc);
	}
}
*/
