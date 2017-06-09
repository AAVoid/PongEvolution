


package fr.PongEvolution.Raquettes;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import fr.PongEvolution.Jeu.ConfigurationInteractionZoneEffetTerrain;
import fr.PongEvolution.Jeu.Scene;






//Raquette qu'on déplace verticalement avec le clavier
public class RaquetteClavierVerticale extends RaquetteVerticale {
	private ConfigurationTouchesVerticale configurationTouchesVerticale; //permet au joueur de
		//configurer ses touches de jeu
	
	public RaquetteClavierVerticale(String description, int vitesseDeplacementY, Image image, 
			boolean invImageBarreAngle, Image imageBoulesEnergie, int vitesseBouleEnergie, int puissanceBouleEnergie,
			double cadenceTirBouleEnergieCreee, ConfigurationInteractionZoneEffetTerrain configIntBEZoneEffetTerrain, 
			int defense, int numeroBarres, 
			ConfigurationTouchesVerticale configurationTouchesVerticale) {
		super(description, vitesseDeplacementY, image, invImageBarreAngle, imageBoulesEnergie, vitesseBouleEnergie,
				puissanceBouleEnergie, cadenceTirBouleEnergieCreee, configIntBEZoneEffetTerrain, defense, numeroBarres);
		setConfigurationTouchesVerticale(configurationTouchesVerticale);
	}
	
	public ConfigurationTouchesVerticale getConfigurationTouchesVerticale() {
		return configurationTouchesVerticale;
	}

	public void setConfigurationTouchesVerticale(ConfigurationTouchesVerticale configurationTouchesVerticale) {
		this.configurationTouchesVerticale = configurationTouchesVerticale;
	}
	
	//AFFICHAGE DE LA RAQUETTE ET DE SA BARRE BOULE D'ENERGIE
	public void afficher(Graphics g) {
		super.afficher(g);
		//AFFICHAGE DE L'IMAGE DE LA BARRE
		if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.HAUT) {
			if(!isInverserImageBarre()) //raquette VG
				g.drawImage(getImageBarre(), getPositionX() + getLongueur(), 
						getPositionY() + getHauteur() / 2 - getImageBarre().getHeight(null), null);
			else //raquette VD
				g.drawImage(getImageBarre(), getPositionX() - getImageBarre().getWidth(null), 
						getPositionY() + getHauteur() / 2 - getImageBarre().getHeight(null), null);
		}
		else if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.BAS) {
			if(!isInverserImageBarre())
				g.drawImage(getImageBarre(), getPositionX() + getLongueur(), getPositionY() + getHauteur() / 2, null);
			else
				g.drawImage(getImageBarre(), getPositionX() - getImageBarre().getWidth(null), 
						getPositionY() + getHauteur() / 2, null);
		}
		else if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.MILIEU) {
			if(!isInverserImageBarre())
				g.drawImage(getImageBarre(), getPositionX() + getLongueur(), getPositionY() + getHauteur() / 2, null);
			else
				g.drawImage(getImageBarre(), getPositionX() - getImageBarre().getWidth(null), 
						getPositionY() + getHauteur() / 2, null);
		}
	}
	
	//DEPLACEMENT DE LA RAQUETTE
	public void deplacer(Scene sc) {
		//On vérifie si on peut se déplacer
		if(getTimestampNouveauDeplaceable() - System.currentTimeMillis() <= 0) {
			//flèche gauche : 37 ; flèche haut : 38 ; flèche droite : 39 ; flèche bas : 40
			ArrayList<Integer> listeTouches = sc.getToucheEnfonce();
			setDirectionY(0); //pour arrêter la raquette si on n'appuie sur aucune touche
			for(int i = 0 ; i < listeTouches.size() ; i++) {
				if(listeTouches.get(i) == configurationTouchesVerticale.getNumeroToucheHaut())
					setDirectionY(-1);
				else if(listeTouches.get(i) == configurationTouchesVerticale.getNumeroToucheBas())
					setDirectionY(1);
			}			
			super.deplacer(sc);
			//contrôle de la position pour éviter que la raquette ne sorte du terrain
			if(getPositionY() < sc.getRaquetteHH().getImage().getHeight(null))
				this.setPositionY(sc.getRaquetteHH().getImage().getHeight(null));
			else if(getPositionY() + getImage().getHeight(null) > 
				sc.getTerrain().getImage().getHeight(null) - sc.getRaquetteHB().getImage().getHeight(null))
				this.setPositionY(sc.getTerrain().getImage().getHeight(null) - getImage().getHeight(null)
						- sc.getRaquetteHB().getImage().getHeight(null));
		}
	}
	
	//SI ON APPLUIE SUR LA BONNE TOUCHE, ON CREE UNE BOULE D'ENERGIE
	//ON PEUT AUSSI CHANGER LA DIRECTION DES BOULES D'ENERGIE
	public void verifierCreerBouleEnergie(Scene sc) {
		///ON VERIFIE SI LA CADENCE DE TIR LE PERMET DEPUIS LA CREATION DE LA DERNIERE BOULE D'ENERGIE
		///ET QU'ON A ASSEZ D'ENERGIE
		//ET QU'ON N'EST PAS PARALYSE A CAUSE D'UNE BOULE D'ENERGIE DE L'ADVERSAIRE
		//SINON ON NE FAIT RIEN
		long timestamp = System.currentTimeMillis();
		if(getTimestampNouveauDeplaceable() - timestamp <= 0
				&& timestamp - getTimestampDerniereBouleEnergieCreee() >= (long)(1000 * getCadenceTirBouleEnergie())
				&& getEnergie() > getPuissanceBouleEnergie() * getCoefEnergiePerdueBouleEnergie()) {
			ArrayList<Integer> listeTouches = sc.getToucheEnfonce();
			for(int i = 0 ; i < listeTouches.size() ; i++) {
				//Creation boule d'énergie
				if(listeTouches.get(i) == configurationTouchesVerticale.getNumeroToucheBouleEnergie()) {
					creerBouleEnergie();
					setEnergie(getEnergie() - getPuissanceBouleEnergie() * getCoefEnergiePerdueBouleEnergie());
				}
				//Changement direction boule d'énergie
				else if(listeTouches.get(i) == configurationTouchesVerticale.getNumeroToucheDirectionBoulesEnergieMonter()) {
					//Controle du timestamp pour limiter la vitesse du changement de direction
					if(System.currentTimeMillis() - getTimestampDernierChangementDirectionBouleEnergie() >= (long)(1000 * getCadenceChangementDirectionBouleEnergie())) {
						if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.BAS) {
							setDirectionBoulesEnergie(Enum_DirBouleEnergie.MILIEU);
							setImageBarre(getImageBarreMilieu());
						}
						else if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.MILIEU) {
							setDirectionBoulesEnergie(Enum_DirBouleEnergie.HAUT);
							if(!isInverserImageBarre()) //Raquette VG
								setImageBarre(getImageBarreHaut());
							else //Raquette VD
								setImageBarre(getImageBarreBas());
						}
						//Actualisation timestamp
						setTimestampDernierChangementDirectionBouleEnergie(System.currentTimeMillis());
					}
				}
				else if(listeTouches.get(i) == configurationTouchesVerticale.getNumeroToucheDirectionBoulesEnergieDescendre()) {
					//Controle du timestamp pour limiter la vitesse du changement de direction
					if(System.currentTimeMillis() - getTimestampDernierChangementDirectionBouleEnergie() >= (long)(1000 * getCadenceChangementDirectionBouleEnergie())) {
						if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.HAUT) {
							setDirectionBoulesEnergie(Enum_DirBouleEnergie.MILIEU);
							setImageBarre(getImageBarreMilieu());
						}
						else if(getDirectionBoulesEnergie() == Enum_DirBouleEnergie.MILIEU) {
							setDirectionBoulesEnergie(Enum_DirBouleEnergie.BAS);
							if(!isInverserImageBarre()) //Raquette VG
								setImageBarre(getImageBarreBas());
							else //Raquette VD
								setImageBarre(getImageBarreHaut());
						}
						//Actualisation timestamp
						setTimestampDernierChangementDirectionBouleEnergie(System.currentTimeMillis());
					}
				}
			}
		}
	}
}






