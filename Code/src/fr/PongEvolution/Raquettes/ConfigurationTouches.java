


package fr.PongEvolution.Raquettes;

import java.util.ArrayList;

public class ConfigurationTouches {
	//Va contenir les informations sur les touches pour 
	//l'utilisation de missile, choix de l'angle de la raquette, choix 
	//du pouvoir à utiliser et utilisation du pouvoir sélectionné
	private int numeroToucheBouleEnergie;
	private int numeroToucheDirectionBoulesEnergieMonter;
	private int numeroToucheDirectionBoulesEnergieDescendre;

	public ConfigurationTouches(int numeroToucheDirectionBoulesEnergieMonter,
			int numeroToucheDirectionBoulesEnergieDescendre,
			int numeroToucheBouleEnergie) {
		setNumeroToucheDirectionBoulesEnergieMonter(numeroToucheDirectionBoulesEnergieMonter);
		setNumeroToucheDirectionBoulesEnergieDescendre(numeroToucheDirectionBoulesEnergieDescendre);
		setNumeroToucheBouleEnergie(numeroToucheBouleEnergie);
	}

	public int getNumeroToucheBouleEnergie() {
		return numeroToucheBouleEnergie;
	}

	public void setNumeroToucheBouleEnergie(int numeroToucheBouleEnergie) {
		this.numeroToucheBouleEnergie = numeroToucheBouleEnergie;
	}
	
	public int getNumeroToucheDirectionBoulesEnergieMonter() {
		return numeroToucheDirectionBoulesEnergieMonter;
	}

	public void setNumeroToucheDirectionBoulesEnergieMonter(
			int numeroToucheDirectionBoulesEnergieMonter) {
		this.numeroToucheDirectionBoulesEnergieMonter = numeroToucheDirectionBoulesEnergieMonter;
	}

	public int getNumeroToucheDirectionBoulesEnergieDescendre() {
		return numeroToucheDirectionBoulesEnergieDescendre;
	}

	public void setNumeroToucheDirectionBoulesEnergieDescendre(
			int numeroToucheDirectionBoulesEnergieDescendre) {
		this.numeroToucheDirectionBoulesEnergieDescendre = numeroToucheDirectionBoulesEnergieDescendre;
	}

	//retourne la configuration sous forme de ArrayList d'Integer
	//Utilisé pour vérifier si deux joueurs n'utilisent pas des touches 
	//similaires avec contains
	public ArrayList<Integer> getConfigurationListe() {
		ArrayList<Integer> listeConfig = new ArrayList<Integer>();
		listeConfig.add(numeroToucheDirectionBoulesEnergieMonter);
		listeConfig.add(numeroToucheDirectionBoulesEnergieDescendre);
		listeConfig.add(numeroToucheBouleEnergie);
		return listeConfig;
	}
}
