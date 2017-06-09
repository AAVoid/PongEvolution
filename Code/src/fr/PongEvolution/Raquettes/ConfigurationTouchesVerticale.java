


package fr.PongEvolution.Raquettes;

import java.util.ArrayList;




public class ConfigurationTouchesVerticale extends ConfigurationTouches {
	//Touches utilisées pour bouger avec une raquette clavier
	//Utile pour que chaque joueur ait sa propre configuration de jeu
	private int numeroToucheHaut;
	private int numeroToucheBas;
	
	public ConfigurationTouchesVerticale(int numeroToucheDirectionBoulesEnergieMonter,
			int numeroToucheDirectionBoulesEnergieDescendre,
			int numeroToucheBouleEnergie,
			int numeroToucheHaut, int numeroToucheBas) {
		super(numeroToucheDirectionBoulesEnergieMonter,
				numeroToucheDirectionBoulesEnergieDescendre,
				numeroToucheBouleEnergie);
		setNumeroToucheHaut(numeroToucheHaut);
		setNumeroToucheBas(numeroToucheBas);
	}

	public int getNumeroToucheHaut() {
		return numeroToucheHaut;
	}

	public void setNumeroToucheHaut(int numeroToucheHaut) {
		this.numeroToucheHaut = numeroToucheHaut;
	}

	public int getNumeroToucheBas() {
		return numeroToucheBas;
	}

	public void setNumeroToucheBas(int numeroToucheBas) {
		this.numeroToucheBas = numeroToucheBas;
	}
	
	//On rajoute les touches de configuration
	public ArrayList<Integer> getConfigurationListe() {
		ArrayList<Integer> listeConfig = super.getConfigurationListe();
		listeConfig.add(numeroToucheHaut);
		listeConfig.add(numeroToucheBas);
		return listeConfig;
	}
}
