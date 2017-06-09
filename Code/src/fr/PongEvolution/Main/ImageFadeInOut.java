

package fr.PongEvolution.Main;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



//PERMET DE GERER UN AFFICHAGE/EFFACEMENT D'IMAGE AVEC UN FONDU (EN MODIFIANT SON OPACITE)
//OPACITE 1 : OPAQUE
//OPACITE 0 : TRANSPARENT
//LA DUREE PRECISEE N'EST EN RIEN ABSOLUE
//LA DUREE REELLE DU FADING VA LE PLUS POSSIBLE SE RAPPROCHER DE CELLE-CI, MAIS RIEN N'EST ABSOLU


//EXEMPLE DE TEST
/*public static void main(String[] args) {
    JFrame frame = new JFrame("TEST ImageFadeInOut");
    frame.add(new ImageFadeInOut("Map_1.png", 1f, 0f, 10d));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}*/

//UTILISE NOTAMMENT DANS LA CLASSE FenetreDeJeu POUR AFFICHER L'INTRODUCTION DU JEU
//EXEMPLE D'UTILISATION PLUS CONCRET POUR FAIRE UNE INTRODUCTION

/*
	public void afficherIntroductionJeu() {
		//iX1 : image X FadeIn
		//iX2 : image X FadeOut
		//IMAGE 1 FADEIN
		ImageFadeInOut i11 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/BlackBackground.png", 
			0f, 1f, DUREE_FADE_IN_OUT);
		contenu.add(i11, BorderLayout.CENTER);
		setVisible(true);
		while(!i11.checkFonduTermine()) //On attend que le fadeIng soit totalement terminée
			repaint();
		System.out.println("ok");
		attendreXSecondes(TEMPS_ENTRE_FADING_FADOUT);
		//IMAGE 1 FADEOUT
		ImageFadeInOut i12 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/BlackBackground.png", 
			1f, 0f, DUREE_FADE_IN_OUT);
		contenu.add(i12, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i11);
		while(!i12.checkFonduTermine())
			repaint();
		System.out.println("ok");
		attendreXSecondes(TEMPS_APRES_FADE_OUT);
		//IMAGE 2 FADEIN
		ImageFadeInOut i21 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/VSBar.png", 
			0f, 1f, DUREE_FADE_IN_OUT);
		contenu.add(i21, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i12);
		while(!i21.checkFonduTermine())
			repaint();
		System.out.println("ok");
		attendreXSecondes(TEMPS_ENTRE_FADING_FADOUT);
		//IMAGE 2 FADEOUT
		ImageFadeInOut i22 = new ImageFadeInOut(CHEMIN_IMAGES_AUTRES + "/VSBar.png", 
			1f, 0f, DUREE_FADE_IN_OUT);
		contenu.add(i22, BorderLayout.CENTER);
		setVisible(true);
		contenu.remove(i21);
		while(!i22.checkFonduTermine())
			repaint();
		System.out.println("ok");
		attendreXSecondes(TEMPS_APRES_FADE_OUT);
	}
*/


public class ImageFadeInOut extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final int INTERVALLE_APPEL_TIMER = 100; 
		//timer appelé toutes les 1000/INTERVALLE_APPEL_TIMER secondes
		//Plus ce nombre est petit et plus le fading sera fluide, non brutal
	private Image image; //Image qui sera affichée
    private javax.swing.Timer timer; //Utilisé pour la vitesse d'augmentation de l'opacité de l'image
    private float alphaFrom; //Opacité de l'image initiale
    private float alphaTo; //Opacité de l'image cible
    private float alphaActuel; //Opacité actuelle
    private float variationAlpha; //Variation d'opacité par appel du timer
    private double dureeFading; //Durée du fading
    
  //POUR LES TEST VOIR QUE LE FADING A BIEN LIEU EN DUREE SECONDES
    private boolean booleenTimeStampFinAffiche; 
    private long timestampDebut;
    private long timestampFin;
    
    //Initialisation de façon à préparer l'affichage de l'image et la variation de son
    //opacité, passant de alphaFrom à alphaTo en durée secondes
    //alphaFrom n'est pas nécessairement plus petit que alphaTo
    //alphaFrom et alphaTo compris entre 0.0f (transparent) et 1.0f (opaque)
    
    //On passe le chemin relatif d'une image en paramètre
    public ImageFadeInOut(String cheminImage, float alphaFrom, float alphaTo, double dureeFading) {
    	setAlphaFrom(alphaFrom);
    	setAlphaTo(alphaTo);
    	setAlphaActuel(alphaFrom);
    	setDureeFading(dureeFading);
    	setBooleenTimeStampFinAffiche(false);
        try {
			image = ImageIO.read(new File(cheminImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
        /*EXEMPLE :
        alphaFrom = 0
        alphaTo = 0.5 => delta = 0.5 - 0 = 0.5
        duree = 2 secondes = 2 * 1000 = 2000 millisecondes
        variation alpha par tour de boucle :
        	2000 / constante (exemple 100 => le timer exécute l'action listener tous les 10èmes de secondes)
        	= 20 tours de boucle
        	et 0.5 / 20 = 0,025 = variation alpha
        	LA CONSTANTE 
        ***Si duree = 10 secondes = 10 * 1000 = 10 000 millisecondes
        variation alpha par tour de boucle :
        	10 000 / 100 = 100 tours de boucle
        	et 0.5 / 100 = 0,005 = variation alpha*/
        setVariationAlpha(
        	(float)((alphaTo - alphaFrom) / (dureeFading * 1000 / INTERVALLE_APPEL_TIMER))
        	);
        timer = new javax.swing.Timer(INTERVALLE_APPEL_TIMER, this);
        timer.start();
        setTimestampDebut(System.currentTimeMillis());
    } 
    
    //On passe une image en paramètre
    public ImageFadeInOut(Image image, float alphaFrom, float alphaTo, double dureeFading) {
    	setAlphaFrom(alphaFrom);
    	setAlphaTo(alphaTo);
    	setAlphaActuel(alphaFrom);
    	setDureeFading(dureeFading);
    	setBooleenTimeStampFinAffiche(false);
    	setImage(image);
        /*EXEMPLE :
        alphaFrom = 0
        alphaTo = 0.5 => delta = 0.5 - 0 = 0.5
        duree = 2 secondes = 2 * 1000 = 2000 millisecondes
        variation alpha par tour de boucle :
        	2000 / constante (exemple 100 => le timer exécute l'action listener tous les 10èmes de secondes)
        	= 20 tours de boucle
        	et 0.5 / 20 = 0,025 = variation alpha
        	LA CONSTANTE 
        ***Si duree = 10 secondes = 10 * 1000 = 10 000 millisecondes
        variation alpha par tour de boucle :
        	10 000 / 100 = 100 tours de boucle
        	et 0.5 / 100 = 0,005 = variation alpha*/
        setVariationAlpha(
        	(float)((alphaTo - alphaFrom) / (dureeFading * 1000 / INTERVALLE_APPEL_TIMER))
        	);
        timer = new javax.swing.Timer(INTERVALLE_APPEL_TIMER, this);
        timer.start();
        setTimestampDebut(System.currentTimeMillis());
    } 
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        //On modifie l'opacité
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaActuel));
        g2d.drawImage(image, 0, 0, null);
        //System.out.println(alphaActuel);
    }
    
  //MODIFICATION DE L'OPACITE DE L'IMAGE EN FONCTION DU TIMER
    public void actionPerformed(ActionEvent e) {
    	alphaActuel += variationAlpha; //la variation peut être positive ou négative
    	//On a deux cas, soit alphaFrom < alphaTo, soit alphaFrom > alphaTo
    	if(alphaFrom < alphaTo) { //alphaFrom < alphaTo
    		if(alphaActuel > alphaTo) {
            	alphaActuel = alphaTo;
                timer.stop();
                if(!isBooleenTimeStampFinAffiche()) {
                	setTimestampFin(System.currentTimeMillis());
                	/*System.out.println("Debut : " + timestampDebut + "\n"
                			+ "Fin : " + timestampFin + "\n"
                			+ "Duree : " + (timestampFin - timestampDebut) / 1000 + " secondes");*/
                }
                setBooleenTimeStampFinAffiche(true);
            }
    	}
    	else { //alphaFrom > alphaTo
    		if(alphaActuel < alphaTo) {
            	alphaActuel = alphaTo;
                timer.stop();
                if(!isBooleenTimeStampFinAffiche()) {
                	setTimestampFin(System.currentTimeMillis());
                	/*System.out.println("Debut : " + timestampDebut + "\n"
                			+ "Fin : " + timestampFin + "\n"
                			+ "Duree : " + (timestampFin - timestampDebut) / 1000 + " secondes");*/
                }
                setBooleenTimeStampFinAffiche(true);
            }
    	}
        repaint(); //Actualisation de l'affichage
    }
    
    //POUR VERIFIER SI LE FONDU EST TERMINE
    public boolean checkFonduTermine() {
    	return (alphaActuel == alphaTo);
    }

	public long getTimestampDebut() {
		return timestampDebut;
	}

	public void setTimestampDebut(long timestampDebut) {
		this.timestampDebut = timestampDebut;
	}

	public long getTimestampFin() {
		return timestampFin;
	}

	public void setTimestampFin(long timestampFin) {
		this.timestampFin = timestampFin;
	}

	public boolean isBooleenTimeStampFinAffiche() {
		return booleenTimeStampFinAffiche;
	}

	public void setBooleenTimeStampFinAffiche(boolean booleenTimeStampFinAffiche) {
		this.booleenTimeStampFinAffiche = booleenTimeStampFinAffiche;
	}

	public float getAlphaActuel() {
		return alphaActuel;
	}

	public void setAlphaActuel(float alphaActuel) {
		this.alphaActuel = alphaActuel;
	}

	public float getVariationAlpha() {
		return variationAlpha;
	}

	public void setVariationAlpha(float variationAlpha) {
		this.variationAlpha = variationAlpha;
	}

	public static int getIntervalleAppelTimer() {
		return INTERVALLE_APPEL_TIMER;
	}

	public float getAlphaFrom() {
		return alphaFrom;
	}

	public void setAlphaFrom(float alphaFrom) {
		this.alphaFrom = alphaFrom;
	}

	public float getAlphaTo() {
		return alphaTo;
	}

	public void setAlphaTo(float alphaTo) {
		this.alphaTo = alphaTo;
	}
    
	public double getDureeFading() {
		return dureeFading;
	}
    
	public void setDureeFading(double dureeFading) {
		this.dureeFading = dureeFading;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Image getImage() {
		return image;
	}
    
	public void setImage(Image image) {
		this.image = image;
	}
	
	public javax.swing.Timer getTimer() {
		return timer;
	}
	
	public void setTimer(javax.swing.Timer timer) {
		this.timer = timer;
	}
}



