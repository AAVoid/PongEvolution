


package fr.PongEvolution.BoulesEnergie;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import fr.PongEvolution.Balles.Balle;
import fr.PongEvolution.Jeu.ElementDeJeu;
import fr.PongEvolution.Jeu.Scene;
import fr.PongEvolution.Raquettes.Raquette;



public class BouleEnergie extends ElementDeJeu {
	private static final int DIRECTION_X_DEFAUT = 0; //Pour tout de suite voir s'il y a un problème
	private static final int DIRECTION_Y_DEFAUT = 0;
	private Enum_ProprietaireBouleEnergie proprietaire; //Utilisé notamment dans la Scene pour 
		//collisions entre boules d'énergie
	private int puissance; //nombre de secondes pendant lesquelles l'ennemi
		//sera immobilisé ET nombre de points de vie perdus par le bonus
	private int vitesse; //nombre de déplacement par tour de boucle dans deplacer()
	
	public BouleEnergie(Enum_ProprietaireBouleEnergie proprietaire, int positionX, int positionY, 
			Enum_DirectionBouleEnergie direction, 
			Image image, int puissance, int vitesse) {
		//ON VA S'AMUSER UN PEU AVEC LES CONDITIONS TERNAIRES SACHANT QUE L'APPEL DU CONSTRUCTEUR
		//DE LA CLASSE MERE DOIT ETRE LA PREMIERE INSTRUCTION (ON NE PEUT DONC PAS TRAVAILLER
		//AVEC DES IF, ON CONTOURNE LE PROBLEME AVEC DES CONDITIONS TERNAIRES DE CONDITIONS TERNAIRES...
		//SUPER : 
		//super(positionX, positionY, directionX, directionY,
		//		vitesseDeplacementX, vitesseDeplacementY, image);
		super(positionX, positionY, 
			//--Direction X
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_TOUT_DROIT) ? -1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_HAUT) ? -1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_BAS) ? -1 : 
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_HAUT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_BAS) ? 1 : DIRECTION_X_DEFAUT,
			//---DirectionY
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_TOUT_DROIT) ? 0 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_HAUT) ? -1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_BAS) ? 1 : 
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT) ? 0 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_HAUT) ? -1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_BAS) ? 1 : DIRECTION_Y_DEFAUT,
			//---VitesseDeplacementX : toujours à 1, la vraie vitesse est celle de l'attribut vitesse
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_TOUT_DROIT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_HAUT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_BAS) ? 1 : 
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_HAUT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_BAS) ? 1 : 1,
			//---VitesseDeplacementY : toujours à 1, la vraie vitesse est celle de l'attribut vitesse
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_TOUT_DROIT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_HAUT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_GAUCHE_BAS) ? 1 : 
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_TOUT_DROIT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_HAUT) ? 1 :
			(direction == Enum_DirectionBouleEnergie.DIRECTION_BOULE_ENERGIE_DROITE_BAS) ? 1 : 1,
			//---image
			image);
		setProprietaire(proprietaire);
		setPuissance(puissance);
		setVitesse(vitesse);
		setTeleportable(true); //Une boule d'énergie est toujours téléportable
	}

	public Enum_ProprietaireBouleEnergie getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(Enum_ProprietaireBouleEnergie proprietaire) {
		this.proprietaire = proprietaire;
	}

	public static int getDirectionXDefaut() {
		return DIRECTION_X_DEFAUT;
	}

	public static int getDirectionYDefaut() {
		return DIRECTION_Y_DEFAUT;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	public int getPuissance() {
		return puissance;
	}

	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}
	
	//Affiche la boule d'énergie
	public void afficher(Graphics g) {	
		g.drawImage(getImage(), getPositionX(), getPositionY(), null);
	}
	
	//DEPLACE LA BOULE D'ENERGIE ET TEST LES COLLISIONS AVEC LE HAUT ET LE BAS DU TERRAIN
	//TEST LA COLLISION AVEC UNE RAQUETTE VERTICALE ET LUI APPLIQUE SON EFFET
	//Déplace la boule d'énergie et test les collisions avec le haut/bas du terrain et les raquette verticales
	public void deplacer(Scene sc) {
		ArrayList<Raquette> listeJoueurs = sc.getJoueurs();
		for(int i = 0; i < vitesse ; i++) {
			super.deplacer(sc); //On se déplace de 1 pixel en X et 0 ou 1 pixel en Y voir constructeur plus haut
			//Dans la méthode getJoueurs de Scene on connait l'ordre d'ajout des raquettes, donc leur position
			//dans la liste des joueurs
			//ORDRE : raquetteVG (0), raquetteVD (1), raquetteHH (2), raquetteHB (3)
			//TEST DE COLLISION AVEC LE HAUT ET LE BAS DU TERRAIN
			if(getPositionY() <= listeJoueurs.get(2).getImage().getHeight(null)) {//rebonds haut terrain
				setDirectionY(1);
			}
			else if(getPositionY() + getImage().getHeight(null) >= //rebond bas terrain
				sc.getTerrain().getImage().getHeight(null) - listeJoueurs.get(3).getImage().getHeight(null)) {
				setDirectionY(-1);
			}
			///TEST DE COLLISION AVEC RAQUETTE VG ET VD ET APPLICATION DE L'EFFET
			//Les raquette HH et HB ne subissent pas l'effet des boules d'énergie
			updateRaquetteApresCollision(sc, listeJoueurs.get(0), listeJoueurs.get(1));
		}
	}
	
	//EFFET APPLIQUE SUR UNE RAQUETTE LORS D'UNE COLLISION
	//ON EFFACE LA BOULE D'ENERGIE
	public void updateRaquetteApresCollision(Scene sc, Raquette raquetteVG, 
			Raquette raquetteVD) {
		if(this.entreEnCollision(raquetteVG)) {
			raquetteVG.updateApresCollisionBouleEnergie(sc, this); //réaction de la raquette
			//On essaye de supprimer la boule d'énergie de la liste de toutes les 
			//raquettes car b n'appartient pas forcément à cette raquette
			sc.getRaquetteVG().getListeBoulesEnergie().remove(this);
			sc.getRaquetteVD().getListeBoulesEnergie().remove(this);
			sc.getRaquetteHH().getListeBoulesEnergie().remove(this);
			sc.getRaquetteHB().getListeBoulesEnergie().remove(this);
		}
		else if(this.entreEnCollision(raquetteVD)) {
			raquetteVD.updateApresCollisionBouleEnergie(sc, this);
			sc.getRaquetteVG().getListeBoulesEnergie().remove(this);
			sc.getRaquetteVD().getListeBoulesEnergie().remove(this);
			sc.getRaquetteHH().getListeBoulesEnergie().remove(this);
			sc.getRaquetteHB().getListeBoulesEnergie().remove(this);
		}
	}
	
	//Pour que la boule soit effacée en quittant le terrain
	public boolean verifierEffacer(Scene sc, ArrayList<BouleEnergie> listeBoulesEnergie) {
		if(getPositionX() > sc.getTerrain().getImage().getWidth(null)) {
			listeBoulesEnergie.remove(this);
			return true; //la boule d'énergie a été effacée
		}
		else if(getPositionX() + getImage().getWidth(null ) < 0) {
			listeBoulesEnergie.remove(this);
			return true;
		}
		return false; //la boule d'énergie n'a pas été effacée
	}
	
	//APRES UNE COLLISION AVEC UNE BALLE ON MODIFIE SON COMPORTEMENT
	//PAR REDEFINITION, CHAQUE TYPE DE BOULE D'ENERGIE POURRA AVOIR UN EFFET BIEN PARTICULIER
	//SUR UNE BALLE
	//ON EFFACE LA BOULE D'ENERGIE LORS DE LA COLLISION
	public void updateBalleApresCollision(Scene sc, Balle balle, ArrayList<BouleEnergie> listeBoulesEnergie) {
		//En fonction de la direction de la boule d'énergie, on modifie la direction X de la balle
		switch(this.getDirectionX()) {
			case -1: //boule d'énergie va vers la gauche
				balle.updateApresCollisionBouleEnergie(sc, -1, -2, puissance, 0, this);
				listeBoulesEnergie.remove(this);
				//On peut effectuer des actions spéciales par rapport à la balle, la téléporter, etc.
				//grâce à la scène en paramètre
				break;
			case 1: //boule d'énergie va vers la droite
				balle.updateApresCollisionBouleEnergie(sc, 1, -2, puissance, 0, this);
				listeBoulesEnergie.remove(this);
				break;
			case 0: //boule d'énergie immobile : ce cas n'arrivera jamais normalement
				//si une boule d'énergie s'arrête (genre une sorte de feu follet posé sur le terrain...)
				//c'est sa vitesse qui va chuter à 0, pas sa direction :)
				//=> possibilité de créer des boules d'énergie spéciale par héritage
				//mais ce cas case 0 ne devrait jamais avoir lieu
				balle.updateApresCollisionBouleEnergie(sc, 1, -2, 0, 0, this);
				listeBoulesEnergie.remove(this);
				break;
		}
	}
}




