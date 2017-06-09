


package fr.PongEvolution.Jeu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;






public abstract class ElementDeJeu {
	private static final boolean TELEPORTABLE_PAR_DEFAUT = false;
	private static final boolean DIRECTION_CHANGEABLE_PAR_DEFAUT = true;
	private int positionX;
	private int positionY;
		//en pixel 
		//ces coordonn�es repr�sentent le coin haut gauche de l'objet conventionnellement
	private int directionX; 
	private int directionY;
		//-1 ; 0 ou 1 pour d�terminer si l'objet se d�place 
		//vers la gauche (-1), la droite (1) ou ne se d�place pas (0) (voir m�thode deplacer()) sur l'axe X
		//ou le bas (1) ou le haut (-1) ou est immobile (0) sur l'axe Y
	private int vitesseDeplacementX;
	private int vitesseDeplacementY;
		//en nombre de pixels par image (it�ration de boucle de jeu)
	private Image image; //image de l'�l�ment graphique
	private boolean teleportable; //D�termine si l'objet est t�l�portable
		//notament pour �viter les t�l�portations en boucle car une fois t�l�port�,
		//l'�l�ment graphique sera s�rement dans une autre zone de t�l�portation (ou non mais au cas o� disons)
		//on rend l'objet t�l�portable que lorsqu'il touche une raquette (ici seule la balle sera 
		//t�l�portable pour l'instant)
	private boolean directionChangeable; //Si la direction est changeable par une zone de
		//changement de direction du terrain
		
	public ElementDeJeu(int positionX, int positionY, int directionX,
			int directionY, int vitesseDeplacementX, int vitesseDeplacementY, Image image) {
		setPositionX(positionX);
		setPositionY(positionY);
		setDirectionX(directionX);
		setDirectionY(directionY);
		setVitesseDeplacementX(vitesseDeplacementX);
		setVitesseDeplacementY(vitesseDeplacementY);
		setImage(image);
		setTeleportable(TELEPORTABLE_PAR_DEFAUT);
		setDirectionChangeable(DIRECTION_CHANGEABLE_PAR_DEFAUT);
	}
	
	//METHODE APPELLEE LORSQU'UN ELEMENT DE JEU SUBIT L'EFFET D'UNE ZONE DE TELEPORTATION DU TERRAIN
	public void updateApresZoneTeleportation() {
		setTeleportable(false);
	}
		
	//METHODE APPELLEE LORSQU'UN ELEMENT DE JEU SUBIT L'EFFET D'UNE ZONE DE DE CHANGEMENT
	//DE DIRECTION DU TERRAIN
	public void updateApresZoneChangementDirection() {
		//Inutile de passer � false
		//setDirectionChangeable(false);
	}
	
	public static boolean isTeleportableParDefaut() {
		return TELEPORTABLE_PAR_DEFAUT;
	}

	public static boolean isDirectionChangeableParDefaut() {
		return DIRECTION_CHANGEABLE_PAR_DEFAUT;
	}

	public boolean isDirectionChangeable() {
		return directionChangeable;
	}

	public void setDirectionChangeable(boolean directionChangeable) {
		this.directionChangeable = directionChangeable;
	}

	public boolean isTeleportable() {
		return teleportable;
	}

	public void setTeleportable(boolean teleportable) {
		this.teleportable = teleportable;
	}

	public int getPositionX() {
		return positionX;
	}
	
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	
	public int getPositionY() {
		return positionY;
	}
	
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	
	public void teleporter(int positionX, int positionY, boolean centrer) {
		//l'attribut centrer indique si on doit centrer l'�l�ment graphique sur la position ou non
		if(centrer) {
			setPositionX(positionX - (int)(getImage().getWidth(null) / 2));
			setPositionY(positionY - (int)(getImage().getHeight(null) / 2));
		}
		else {
			setPositionX(positionX);
			setPositionY(positionY);
		}
	}
	
	public int getDirectionX() {
		return directionX;
	}
	
	public void setDirectionX(int directionX) {
		this.directionX = directionX;
	}
	
	public int getDirectionY() {
		return directionY;
	}
	
	public void setDirectionY(int directionY) {
		this.directionY = directionY;
	}
	
	public int getVitesseDeplacementX() {
		return vitesseDeplacementX;
	}
	
	public void setVitesseDeplacementX(int vitesseDeplacementX) {
		this.vitesseDeplacementX = vitesseDeplacementX;
	}
	
	public int getVitesseDeplacementY() {
		return vitesseDeplacementY;
	}
	
	public void setVitesseDeplacementY(int vitesseDeplacementY) {
		this.vitesseDeplacementY = vitesseDeplacementY;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
		
	public void deplacer(Scene sc) {
		setPositionX(positionX + directionX * vitesseDeplacementX);
		setPositionY(positionY + directionY * vitesseDeplacementY);
	}
	
	public abstract void afficher(Graphics g);
	
	///TESTER LA COLLISION AVEC UN AUTRE ELEMENT DE JEU
	public boolean entreEnCollision(ElementDeJeu e) {
		//METHODE AVEC LA CLASSE RECTANGLE ET SA METHODE INTERSECTS
		Rectangle cetElement = 
				new Rectangle(positionX, positionY, image.getWidth(null), image.getHeight(null));
		Rectangle autreElement = 
				new Rectangle(e.positionX, e.positionY, e.image.getWidth(null), e.image.getHeight(null));
		return cetElement.intersects(autreElement);
	}
	
	//TESTER LA COLLISION AVEC UN RECTANGLE (UTILISE POUR GERER LES EFFETS SPECIAUX DU TERRAIN NOTAMMENT)
	public boolean entreEnCollisionRectangle(Rectangle r) {
		Rectangle cetElement = 
				new Rectangle(positionX, positionY, image.getWidth(null), image.getHeight(null));
		return cetElement.intersects(r);
	}
}














