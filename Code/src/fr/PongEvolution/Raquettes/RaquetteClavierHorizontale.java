

//NON PREVU DANS LE JEU MAIS CONSERVE POUR ACCELERER LES AMELIORATIONS EVENTUELLES DU JEU

/*

package fr.PongEvolution.Raquettes;

import java.awt.Image;
import java.util.ArrayList;
import fr.PongEvolution.Jeu.Scene;






//Raquette qu'on d�place horizontalement avec le clavier
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
		//fl�che gauche : 37 ; fl�che haut : 38 ; fl�che droite : 39 ; fl�che bas : 40
		ArrayList<Integer> listeTouches = sc.getToucheEnfonce();
		setDirectionX(0); //pour arr�ter la raquette si on n'appuie sur aucune touche
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
