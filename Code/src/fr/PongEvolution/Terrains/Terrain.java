

package fr.PongEvolution.Terrains;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;






//Pour gérer le terrain de jeu ainsi que tous les paramètres qui le concernent 
//(pour téléportation de balle, changement de direction, etc.)
public class Terrain {
	private String description; //description du terrain affichée au(x) joueur(s)
	private String cheminImage; //car on a besoin de conserver ce chemin
	private Image image; //l'image du terrain
	private int numeroBarres; //Défini le type de barres à utiliser sur ce terrain
		//(pour éviter d'avoir une barre noire sur un terrain sombre, etc. => permet de changer
		//de barre en fonction du terrain)
		//UTILISE DANS LE CONSTRUCTEUR DE Raquette verticle
	private ArrayList<ZoneEffet> listeZonesEffet; //Pour gérer les différentes zones 
		//qui ont un effet sur un élément de jeu (la balle ici) lorsqu'au contact
	private int quantiteEnergieRecupereeMancheRaquettes; //quantité d'énergie que vont récupérer
		//les raquettes à la fin de chaque manche

	public Terrain(String description, String cheminImage, int numeroBarres, 
			int quantiteEnergieRecupereeMancheRaquettes) {
		setDescription(description);
		setCheminImage(cheminImage);
		setNumeroBarres(numeroBarres);
		setQuantiteEnergieRecupereeMancheRaquettes(quantiteEnergieRecupereeMancheRaquettes);
		listeZonesEffet = new ArrayList<ZoneEffet>();
		//Chargement image
		try {
			image = ImageIO.read(new File(cheminImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getQuantiteEnergieRecupereeMancheRaquettes() {
		return quantiteEnergieRecupereeMancheRaquettes;
	}

	public void setQuantiteEnergieRecupereeMancheRaquettes(int quantiteEnergieRecupereeMancheRaquettes) {
		this.quantiteEnergieRecupereeMancheRaquettes = quantiteEnergieRecupereeMancheRaquettes;
	}



	public String getCheminImage() {
		return cheminImage;
	}

	public void setCheminImage(String cheminImage) {
		this.cheminImage = cheminImage;
	}

	public ArrayList<ZoneEffet> getListeZonesEffet() {
		return listeZonesEffet;
	}

	public void setListeZonesEffet(ArrayList<ZoneEffet> listeZonesEffet) {
		this.listeZonesEffet = listeZonesEffet;
	}

	//Ajoute une zone d'effet au terrain
	public void ajouterZoneEffet(ZoneEffet zoneEffet) {
		listeZonesEffet.add(zoneEffet);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumeroBarres() {
		return numeroBarres;
	}

	public void setNumeroBarres(int numeroBarres) {
		this.numeroBarres = numeroBarres;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
