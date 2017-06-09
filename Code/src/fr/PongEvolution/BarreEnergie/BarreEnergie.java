


package fr.PongEvolution.BarreEnergie;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import fr.PongEvolution.Jeu.Scene;



//Utilisé pour afficher la jauge d'énergie des raquettes joueurs
//VOIR CLASSE RAQUETTE
public class BarreEnergie extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Image imageContourBarre;
	private Image imageBarreVert;
	private Image imageBarreOrange;
	private Image imageBarreRouge;
	private BufferedImage imageBarreAffichee; //buffered pour pouvoir utiliser getSubImage
		//Actualisé dans la méthode setValeurActuelle(int valeurActuelle)
	private int valeurMax;
	private int valeurActuelle;
	private int ancienneValeurActuelle; //Pour savoir si on change de barre ou pas
	private static final int POURCENTAGE_PASSAGE_ORANGE = 50; //En dessous de ce pourcentage d'énergie on affiche la barre orange 
	private static final int POURCENTAGE_PASSAGE_ROUGE = 25; //En dessous de ce pourcentage d'énergie on affichage la barre rouge
	private static final double POURCENTAGE_PASSAGE_ORANGE_2 = (double)POURCENTAGE_PASSAGE_ORANGE / (double)100; 
	private static final double POURCENTAGE_PASSAGE_ROUGE_2 = (double)POURCENTAGE_PASSAGE_ROUGE / (double)100;
	
	public BarreEnergie(Image imageContourBarre, Image imageBarreVert,
			Image imageBarreOrange, Image imageBarreRouge,
			int valeurMax, int valeurActuelle) {
		super();
		this.setImageContourBarre(imageContourBarre);
		this.setImageBarreVert(imageBarreVert);
		this.setImageBarreOrange(imageBarreOrange);
		this.setImageBarreRouge(imageBarreRouge);
		//Initialisation barre actuelle
		double pourcentage = (double)valeurActuelle / (double)valeurMax;
		if(pourcentage <= POURCENTAGE_PASSAGE_ROUGE_2)
			this.setImageBarreAffichee((BufferedImage)imageBarreRouge);
		else if(pourcentage <= POURCENTAGE_PASSAGE_ORANGE_2)
			this.setImageBarreAffichee((BufferedImage)imageBarreOrange);
		else
			this.setImageBarreAffichee((BufferedImage)imageBarreVert);
		this.setValeurMax(valeurMax);
		this.setValeurActuelle(valeurActuelle);
		this.setAncienneValeurActuelle(valeurActuelle);
	}

	public int getValeurMax() {
		return valeurMax;
	}

	public void setValeurMax(int valeurMax) {
		this.valeurMax = valeurMax;
	}

	public int getValeurActuelle() {
		return valeurActuelle;
	}
	
	//METHODE QUI ACTUALISE AUSSI LA BARRE AFFICHEE (VERTE, ORANGE, ROUGE)
	public void setValeurActuelle(int valeurActuelle) {
		this.ancienneValeurActuelle = this.valeurActuelle;
		this.valeurActuelle = valeurActuelle;
		//ON MODIFIE EVENTUELLEMENT LA BARRE AFFICHEE ACTUELLEMENT
		//On détermine la barre qui était affichée avant de modifier la valeur actuelle
		double ancienPourcentage = (double)this.ancienneValeurActuelle / (double)this.valeurMax;
		int ancienneBarre = (ancienPourcentage <= POURCENTAGE_PASSAGE_ROUGE_2) ? 0 :
			(ancienPourcentage <= POURCENTAGE_PASSAGE_ORANGE_2) ? 1 : 2; //0 : barre actuelle rouge ; 1 : orange ; 2 : vert
		//On détermine la barre qui sera affichée avec la nouvelle valeur actuelle
		double nouveauPourcentage = (double)valeurActuelle / (double)valeurMax;
		int nouvelleBarre = (nouveauPourcentage <= POURCENTAGE_PASSAGE_ROUGE_2) ? 0 :
			(nouveauPourcentage <= POURCENTAGE_PASSAGE_ORANGE_2) ? 1 : 2;
		//S'il y a eu un changement de barre entre l'ancienne valeur et la nouvelle
		if(ancienneBarre != nouvelleBarre) {
			if(nouvelleBarre == 0)
				this.setImageBarreAffichee((BufferedImage)imageBarreRouge);
			else if(nouvelleBarre == 1)
				this.setImageBarreAffichee((BufferedImage)imageBarreOrange);
			else
				this.setImageBarreAffichee((BufferedImage)imageBarreVert);
		}
	}

	public int getAncienneValeurActuelle() {
		return ancienneValeurActuelle;
	}

	public void setAncienneValeurActuelle(int ancienneValeurActuelle) {
		this.ancienneValeurActuelle = ancienneValeurActuelle;
	}

	public Image getImageContourBarre() {
		return imageContourBarre;
	}
	
	public void setImageContourBarre(Image imageContourBarre) {
		this.imageContourBarre = imageContourBarre;
	}

	public BufferedImage getImageBarreAffichee() {
		return imageBarreAffichee;
	}

	public void setImageBarreAffichee(BufferedImage imageBarreAffichee) {
		this.imageBarreAffichee = imageBarreAffichee;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Image getImageBarreVert() {
		return imageBarreVert;
	}

	public void setImageBarreVert(Image imageBarreVert) {
		this.imageBarreVert = imageBarreVert;
	}

	public Image getImageBarreOrange() {
		return imageBarreOrange;
	}

	public void setImageBarreOrange(Image imageBarreOrange) {
		this.imageBarreOrange = imageBarreOrange;
	}

	public Image getImageBarreRouge() {
		return imageBarreRouge;
	}

	public void setImageBarreRouge(Image imageBarreRouge) {
		this.imageBarreRouge = imageBarreRouge;
	}

	public static int getPourcentagePassageOrange() {
		return POURCENTAGE_PASSAGE_ORANGE;
	}

	public static int getPourcentagePassageRouge() {
		return POURCENTAGE_PASSAGE_ROUGE;
	}

	public static double getPourcentagePassageOrange2() {
		return POURCENTAGE_PASSAGE_ORANGE_2;
	}

	public static double getPourcentagePassageRouge2() {
		return POURCENTAGE_PASSAGE_ROUGE_2;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Couleur de fond du panel pour correspondre avec celle de la scene
		setBackground(Scene.getCouleurPanneauPouvoirs());
		//ON AFFICHE LA BARRE CENTREE SUR LE PANNEL ENERGIE DE LA SCENE
		Dimension dimPanel = this.getSize();
		//Affichage contour barre
		g.drawImage(imageContourBarre, 
				dimPanel.width / 2 - imageContourBarre.getWidth(null) / 2, 
				dimPanel.height / 2 - imageContourBarre.getHeight(null) / 2, 
				this);
		//Affichage barre
		g.drawImage(imageBarreAffichee.getSubimage(0, 
					0, 
					(int)(((double)valeurActuelle / (double)valeurMax) * imageBarreAffichee.getWidth()), 
					imageBarreAffichee.getHeight()), 
				dimPanel.width / 2 - imageBarreAffichee.getWidth(null) / 2, 
				dimPanel.height / 2 - imageBarreAffichee.getHeight(null) / 2, this);
	}
}
















