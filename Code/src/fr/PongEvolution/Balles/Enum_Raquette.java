


package fr.PongEvolution.Balles;


//A chaque fois que la balle touche une raquette on change la valeur de son attribut raquetteDerniereCollision
//afin de d�terminer quelle raquette remporte le point si la balle sort de la carte
public enum Enum_Raquette {
	RAQUETTE_AUCUNE, //Valeur au d�but de la manche actuelle
	RAQUETTE_VG,
	RAQUETTE_VD,
	RAQUETTE_HH,
	RAQUETTE_HB;
}
