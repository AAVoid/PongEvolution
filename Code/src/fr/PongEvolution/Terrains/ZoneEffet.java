

package fr.PongEvolution.Terrains;

import java.awt.Rectangle;

import fr.PongEvolution.Jeu.ElementDeJeu;


//PERMETTRA D'IMPLEMENTER UNE ZONE DU TERRAIN QUI AURA UN EFFET SUR UN ELEMENT DE JEU QUELCONQUE 
//QUI S'Y TROUVE PARTIELLEMENT OU TOTALEMENT
public abstract class ZoneEffet {
	private Rectangle zone; //Zone concernée

	public ZoneEffet(Rectangle zone) {
		setZone(zone);
	}

	public Rectangle getZone() {
		return zone;
	}

	public void setZone(Rectangle zone) {
		this.zone = zone;
	}
	
	//EFFET DE LA ZONE D'EFFET : EFFET SUR L'ELEMENT DE JEU QUI S'Y TROUVE PARTIELLEMENT
	//OU TOTALEMENT
	public abstract void appliquerEffet(ElementDeJeu e);
}
