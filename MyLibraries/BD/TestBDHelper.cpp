#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <cstring>

#include <mysql.h>

#include "BDHelper.h"

// pour compiler ce fichier test :
// 		g++ TestBDHelper.cpp BDHelper.cpp -o TestBDHelper -I/usr/include/mysql -L/usr/lib64/mysql -lmysqlclient


void DemanderContinuer();

int main() {
	char nomTable[] = "authors";
	char nomColonnes[] = "last_name, first_name, birth_date";
	char valeurs[] = "'Boss', 'Hugo', '2001-03-01'";
	char modifications[] = "last_name = 'Antoine', first_name = 'Gabriel', birth_date = '2004-05-23'";
	char conditionModif[] = "last_name = 'Boss' AND first_name = 'Hugo'";
	char conditionSupp[] = "last_name = 'Antoine' AND first_name = 'Gabriel'";

	printf("\nJe vais me connecter a la base de donnees...\n");


	if(ConnexionBD() == 1) {
		printf("Connexion etablie !\n");


		if(InsererTuple(nomTable, nomColonnes, valeurs) == 1) {
			printf("Tuple bien insere.\n");

			DemanderContinuer();


			if(ModifierTuple(nomTable, modifications, conditionModif) == 1) {
				printf("Tuple bien modifie.\n");

				DemanderContinuer();

				if(SupprimerTuple(nomTable, conditionSupp) == 1) {
					printf("Tuple bien supprime.\n");

					DemanderContinuer();
				}
				else {
					printf("ERREUR : Suppression echouee.\n");
				}
			} 
			else
				printf("ERREUR : Modification echouee.\n");
		} 
		else
			printf("ERREUR : Insertion echouee.\n");

		vector<string> tuples;
		int nbColonnes;


		printf("\nAffichage de tous les authors :\n");
		tuples = GetAllFromTable("authors");

		nbColonnes = 4;
	    for (size_t i = 0; i < tuples.size(); i++) {
	        printf("%14s", tuples[i].c_str());

	        if ((i + 1) % nbColonnes == 0) {
	            printf("\n");
	        }
	    }


		printf("\n\n\nAffichage de tous les employees :\n");
		tuples = GetAllFromTable("employees");

		nbColonnes = 3;
	    for (size_t i = 0; i < tuples.size(); i++) {
	        printf("%14s", tuples[i].c_str());

	        if ((i + 1) % nbColonnes == 0) {
	            printf("\n");
	        }
	    }

	    printf("\n\n");


		if(DeconnexionBD() == 1)
			printf("Deconnexion reussie.\n");
		else
			printf("ERREUR : Deconnexion echouee.\n");
	} 
	else {
		printf("ERREUR : Erreur de connexion a la base de donnees.\n");
	}

	return 0;
}

void DemanderContinuer()
{
	char choix;

	do {
		printf("\t- Continuer ? (o = oui) -> ");
		scanf(" %c", &choix);
	} while(choix != 'O' && choix != 'o');

	printf("\n\n");
}