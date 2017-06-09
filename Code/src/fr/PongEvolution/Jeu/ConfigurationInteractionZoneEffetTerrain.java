


package fr.PongEvolution.Jeu;



//POUR CONFIGURER COMMENT REAGIT UN ELEMENT DE JEU PAR RAPPORT A UNE ZONE 
//D'EFFET DU TERRAIN
//PAR EXEMPLE, CERTAINES RAQUETTES AURONT DES BOULES D'ENERGIE QUI NE POURRONT PAS ETRE TELEPORTABLES
//PAR UNE ZONE DU TERRAIN, DONT LA DIRECTION NE PEUT PAS CHANGER, ETC.
//ON PEUT PAR EXEMPLE IMAGINER UNE BOULE D'ENERGIE GENKIDAMA QUI N'EST PAS AFFECTEE PAR LES EFFETS DU TERRAIN 
//TELLEMENT ELLE EST PUISSANTE

//CLASSE UTILISEE AFIN DE SIMPLIFIER LE CONSTRUCTEUR D'UNE RAQUETTE AFIN D'EVITER D'AVOIR PLEIN DE
//PARAMETRES BOOLEENS A INDIQUER, ICI ON A UNE CONFIGURATION DECLAREE AVANT L'INSTANCIATION D'UNE RAQUETTE
//ET PASSEE EN PARAMETRE
public class ConfigurationInteractionZoneEffetTerrain {
	private boolean teleportable; //Si l'élément de jeu est téléportable
	private boolean directionChangeable; //Si sa direction peut être changée
	
	public ConfigurationInteractionZoneEffetTerrain(boolean teleportable,
			boolean directionChangeable) {
		super();
		this.teleportable = teleportable;
		this.directionChangeable = directionChangeable;
	}
	
	public boolean isTeleportable() {
		return teleportable;
	}
	
	public void setTeleportable(boolean teleportable) {
		this.teleportable = teleportable;
	}
	public boolean isDirectionChangeable() {
		return directionChangeable;
	}
	public void setDirectionChangeable(boolean directionChangeable) {
		this.directionChangeable = directionChangeable;
	}
}








