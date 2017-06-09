



package fr.PongEvolution.Terrains;

import java.awt.Rectangle;

import fr.PongEvolution.Jeu.ElementDeJeu;



//VA PERMETTRE DE GERER LA TELEPORTATION D'UN ELEMENT DE JEU
//SUR LE TERRAIN DURANT LA PARTIE
//PEUT AUSSI ETRE UTILISE COMME ETANT UNE << ZONE DE TRANSLATION >>
//C'EST-A-DIRE QUE LA BALLE EST TELEPORTEE A PROXIMITE DE CETTE ZONE DE TELEPORTATION 
//ET NON A UN AUTRE POINT DE TELEPORTATION
//
//A L'UTILISATEUR DE VERIFIER QUE LA BALLE N'EST PAS TELEPORTEE OU ELLE NE DOIT PAS, EN 
//CAS DE TELEPORTATION CLASSIQUE OU DE TELEPORTATION POUR SIMULER UNE TRANSLATION A PROXIMITE
public class ZoneTeleportationTranslation extends ZoneEffet {
	private static final boolean CENTRER_BALLE_TELEPORTATION = true; //Lorsque l'�l�ment de jeu 
		//sera t�l�port�e il sera centr� sur les coordonn�es de destination
	private int coordonneesDestinationX; //Coordonn�es � laquelle l'�l�ment de jeu sera t�l�port�
	private int coordonneesDestinationY;
	
	public ZoneTeleportationTranslation(Rectangle zone, int coordonneesDestinationX,
			int coordonneesDestinationY) {
		super(zone);
		setCoordonneesDestinationX(coordonneesDestinationX);
		setCoordonneesDestinationY(coordonneesDestinationY);
	}
	
	public static boolean isCentrerBalleTeleportation() {
		return CENTRER_BALLE_TELEPORTATION;
	}
	
	public int getCoordonneesDestinationX() {
		return coordonneesDestinationX;
	}

	public void setCoordonneesDestinationX(int coordonneesDestinationX) {
		this.coordonneesDestinationX = coordonneesDestinationX;
	}

	public int getCoordonneesDestinationY() {
		return coordonneesDestinationY;
	}

	public void setCoordonneesDestinationY(int coordonneesDestinationY) {
		this.coordonneesDestinationY = coordonneesDestinationY;
	}
	
	public void appliquerEffet(ElementDeJeu e) {
		//On v�rifie si l'�l�ment de jeu intersecte la zone ET S'IL EST TELEPORTABLE
		if(e.entreEnCollisionRectangle(this.getZone()) && e.isTeleportable()) {
			e.teleporter(getCoordonneesDestinationX(), 
					getCoordonneesDestinationY(), 
					isCentrerBalleTeleportation());
			//L'�l�ment de jeu REAGIT apr�s avoir subit l'effet de la zone
			e.updateApresZoneTeleportation();
		}
	}
}






