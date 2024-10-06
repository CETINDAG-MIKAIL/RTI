#include "BDHelper.h"
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string>
#include <string.h>
#include <mysql.h>
using namespace std;

MYSQL* connexion;

int ConnexionBD()
{
	connexion = mysql_init(NULL);

	if (mysql_real_connect(connexion,"localhost", "Student", "PassStudent1_","PourStudent",0,NULL,0) == NULL)
	{
		return -1;
	}
	return 1;
}


int DeconnexionBD() {
	if(connexion != NULL) {
		mysql_close(connexion);
		return 1;
	}
	return -1;
}


int InsererTuple(const char* table,const  char* colonnes,const  char* valeurs) {
	int longeur = strlen(table) + strlen(colonnes) + strlen(valeurs) + 30;
	char* requete = (char*)malloc(longeur * sizeof(char));

	sprintf(requete, "INSERT INTO %s (%s) VALUES (%s)", table, colonnes, valeurs);




	if (mysql_query(connexion,requete) != 0) {
		free(requete);
		return -1;
	}
	free(requete);
	return 1;		// requête exécutée avec succès
}


int ModifierTuple(char* table, char* modifications, char* condition) 
{
	int longeur = strlen(table) + strlen(modifications) + strlen(condition) + 30;
    char* requete = (char*)malloc(longeur * sizeof(char));

    sprintf(requete, "UPDATE %s SET %s WHERE %s", table, modifications, condition);
	//-------------------------------------------------------------------------------------------------------
	printf("\nRequete :\n\t%s\n", requete);
	//printf("Adresse du pointeur connexion: %p\n", (void*)connexion);
	//printf("Hôte connecté : %s\n", mysql_get_host_info(connexion));
	//printf("Version du serveur : %s\n", mysql_get_server_info(connexion));
	//-------------------------------------------------------------------------------------------------------

    if (mysql_query(connexion, requete)) {
    	free(requete);
		return -1;
	}
	free(requete);
	return 1;		// requête exécutée avec succès
}


int SupprimerTuple(char* table, char* condition)
{
	int longeur = strlen(table) + strlen(condition) + 30;
	char* requete = (char*)malloc(longeur * sizeof(char));

	sprintf(requete, "DELETE FROM %s WHERE %s", table, condition);

	if (mysql_query(connexion,requete) != 0) {
		free(requete);
		return -1;
	}
	free(requete);
	return 1;		// requête exécutée avec succès
}



string getAuthors(char* table) {
	int longeur = strlen(table) + 30;
	char* requete = (char*)malloc(longeur * sizeof(char));

	sprintf(requete, "SELECT * FROM %s", table);

	if (mysql_query(connexion,requete) != 0) {
		free(requete);
		return "-1";
	}

	// Affichage du Result Set
	MYSQL_RES *ResultSet;
	
	if ((ResultSet = mysql_store_result(connexion)) == NULL)
	{
		return "-1";
	}
	
	MYSQL_ROW ligne;
	int nbChamps = mysql_num_fields(ResultSet);
	
	while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
	{
		for(int i=0; i<nbChamps ; i++)
			printf("%10s\t",ligne[i]);
		
		printf("\n");
	}

	return "OK";
}

string getSubjects(char* table) {
	return "...";
}

