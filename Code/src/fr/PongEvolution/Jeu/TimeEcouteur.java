


package fr.PongEvolution.Jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



//UTILISE POUR LE TIMER DANS LA SCENE POUR RAFRAICHIR L'ECRAN DE JEU
//AVEC LA METHODE MAJSCENE
public class TimeEcouteur implements ActionListener {
	Scene sc;
	
	public TimeEcouteur(Scene s) {
		sc = s;
	}
	
	public void actionPerformed(ActionEvent e) { //Le timer exécute un action perform toutes les X millisecondes
		//pour maintenir un certain nombre de FPS, à chaque fois on met à jour la scène
		sc.majScene();
	}
}
