

package fr.PongEvolution.Terrains;

import java.awt.Rectangle;

import fr.PongEvolution.Jeu.ElementDeJeu;



//PERMET D'IMPLEMENTER UNE ZONE DU TERRAIN QUI FERA CHANGER LA DIRECTION D'UN ELEMENT DE JEU
public class ZoneChangementDirection extends ZoneEffet {
	private int nouvelleDirectionX; //-1, 0, 1 OU -2 (direction inchangée)
	private int nouvelleDirectionY;
	
	public ZoneChangementDirection(Rectangle zone, int nouvelleDirectionX,
			int nouvelleDirectionY) {
		super(zone);
		setNouvelleDirectionX(nouvelleDirectionX);
		setNouvelleDirectionY(nouvelleDirectionY);
	}

	public int getNouvelleDirectionX() {
		return nouvelleDirectionX;
	}

	public void setNouvelleDirectionX(int nouvelleDirectionX) {
		this.nouvelleDirectionX = nouvelleDirectionX;
	}

	public int getNouvelleDirectionY() {
		return nouvelleDirectionY;
	}

	public void setNouvelleDirectionY(int nouvelleDirectionY) {
		this.nouvelleDirectionY = nouvelleDirectionY;
	}

	public void appliquerEffet(ElementDeJeu e) {
		//On vérifie si l'élément de jeu intersecte la zone ET S'IL EST DEPLACEABLE
		if(e.entreEnCollisionRectangle(this.getZone()) && e.isDirectionChangeable()) {
			if(getNouvelleDirectionX() != -2)
				e.setDirectionX(getNouvelleDirectionX());
			if(getNouvelleDirectionY() != -2)
				e.setDirectionY(getNouvelleDirectionY());
			e.updateApresZoneChangementDirection();
		}
	}
}
