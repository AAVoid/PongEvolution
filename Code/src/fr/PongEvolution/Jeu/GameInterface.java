


package fr.PongEvolution.Jeu;

import java.awt.Point;
import java.util.ArrayList;
import fr.PongEvolution.Balles.Balle;
import fr.PongEvolution.Raquettes.Raquette;






public interface GameInterface {
	public int getLargeur(); //renvoie la largeur totale du terrain de jeu
	public int getHauteur(); //renvoie la hauteur totale du terrain de jeu
	public ArrayList<Integer> getToucheEnfonce(); //renvoie le num�ro des touches sur laquelles appuie le joueur
	public Point getPositionSouris(); //renvoie les coordonn�es de la souris sur le terrain
	public ArrayList<Raquette> getJoueurs(); //renvoie une liste des raquettes pr�sentes sur le terrain
	public Balle getBalle(); //renvoie la balle utilis�e sur le terrain
	public void setBalle(Balle balle);
	public void majScene(); //qui calcule la nouvelle position de tous les �l�ments pr�sents sur le terrain
}
