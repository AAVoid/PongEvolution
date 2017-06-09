



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
	private static final boolean CENTRER_BALLE_TELEPORTATION = true; //Lorsque l'élément de jeu 
		//sera téléportée il sera centré sur les coordonnées de destination
	private int coordonneesDestinationX; //Coordonnées à laquelle l'élément de jeu sera téléporté
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
		//On vérifie si l'élément de jeu intersecte la zone ET S'IL EST TELEPORTABLE
		if(e.entreEnCollisionRectangle(this.getZone()) && e.isTeleportable()) {
			e.teleporter(getCoordonneesDestinationX(), 
					getCoordonneesDestinationY(), 
					isCentrerBalleTeleportation());
			//L'élément de jeu REAGIT après avoir subit l'effet de la zone
			e.updateApresZoneTeleportation();
		}
	}
}






