


package fr.PongEvolution.Jeu;

import java.awt.Point;
import java.util.ArrayList;
import fr.PongEvolution.Balles.Balle;
import fr.PongEvolution.Raquettes.Raquette;






public interface GameInterface {
	public int getLargeur(); //renvoie la largeur totale du terrain de jeu
	public int getHauteur(); //renvoie la hauteur totale du terrain de jeu
	public ArrayList<Integer> getToucheEnfonce(); //renvoie le numéro des touches sur laquelles appuie le joueur
	public Point getPositionSouris(); //renvoie les coordonnées de la souris sur le terrain
	public ArrayList<Raquette> getJoueurs(); //renvoie une liste des raquettes présentes sur le terrain
	public Balle getBalle(); //renvoie la balle utilisée sur le terrain
	public void setBalle(Balle balle);
	public void majScene(); //qui calcule la nouvelle position de tous les éléments présents sur le terrain
}
