#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <string>
#include <mysql.h>
#include <vector>
#include <sstream>
#include <mysql/mysql.h>

#include "BDHelper.h"

using namespace std;

MYSQL* connexion;

int ConnexionBD()
{
	connexion = mysql_init(NULL);

	if (mysql_real_connect(connexion,"localhost", "Student", "PassStudent1_", "PourStudent", 0, NULL, 0) == NULL)
	{
		return -1;
	}

	printf("Connexion etablie !\n");
	return 1;
}


int DeconnexionBD() {
	if(connexion != NULL) {
		mysql_close(connexion);
		return 1;
	}
	return -1;
}


int InsererTuple(const char* table, const char* colonnes, const char* valeurs) 
{
	int longeur = strlen(table) + strlen(colonnes) + strlen(valeurs) + 30;
	char* requete = (char*)malloc(longeur * sizeof(char));

	sprintf(requete, "INSERT INTO %s (%s) VALUES (%s)", table, colonnes, valeurs);

	//printf("\n-------INSERER-TUPLE = %s\n\n", requete);

	if (mysql_query(connexion,requete) != 0) {
		free(requete);
		return -1;
	}
	free(requete);
	return 1;		// requête exécutée avec succès
}


int InsererTupleRetourneId(const char* table, const char* colonnes, const char* valeurs) 
{
	int longeur = strlen(table) + strlen(colonnes) + strlen(valeurs) + 30;
	char* requete = (char*)malloc(longeur * sizeof(char));
	int id = -1;

	sprintf(requete, "INSERT INTO %s (%s) VALUES (%s)", table, colonnes, valeurs);

	//printf("\n-------INSERER-TUPLE = %s\n\n", requete);///////////////////////////

    if (mysql_query(connexion, requete) != 0) {
        free(requete);
        return -1;
    }

    // retourner l'ID de l'insertion
    return mysql_insert_id(connexion);
}


int ModifierTuple(char* table, char* modifications, char* condition) 
{
	int longeur = strlen(table) + strlen(modifications) + strlen(condition) + 30;
    char* requete = (char*)malloc(longeur * sizeof(char));

    sprintf(requete, "UPDATE %s SET %s WHERE %s", table, modifications, condition);

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



vector<string> GetAllFromTable(const char* table) 
{
	vector<string> tuples;
	int longeur = strlen(table) + 30;
	char* requete = (char*)malloc(longeur * sizeof(char));

	sprintf(requete, "SELECT * FROM %s", table);

	if (mysql_query(connexion,requete) != 0) {
		free(requete);
		return vector<string>{};
	}


	// Affichage du Result Set
	MYSQL_RES *ResultSet;
	
	if ((ResultSet = mysql_store_result(connexion)) == NULL)
	{
		return vector<string>{};
	}
	
	MYSQL_ROW ligne;
	int nbChamps = mysql_num_fields(ResultSet);
	


	while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
	{
		for(int i=0 ; i < nbChamps ; i++) {
			tuples.push_back(ligne[i]);
		}
	}

	return tuples;
}

vector<string> GetTuple(const char* table, const char* condition)
{
    int longueur = strlen(table) + strlen(condition) + 40;  // Espace supp pour "SELECT * FROM et WHERE"
    char* requete = (char*)malloc(longueur * sizeof(char));


    sprintf(requete, "SELECT * FROM %s WHERE %s", table, condition);

    //printf("-----GetTuple, requete = %s\n", requete);////////////////////////////////


    if (mysql_query(connexion, requete) != 0) {
        free(requete);
        return {};
    }
    free(requete);

    MYSQL_RES* ResultSet = mysql_store_result(connexion);
    if (ResultSet == NULL) {
        return {};
    }

    // Traitement des résultats
    vector<string> tuples;
    MYSQL_ROW ligne;
    int nbChamps = mysql_num_fields(ResultSet);

    while ((ligne = mysql_fetch_row(ResultSet)) != NULL) {
        for (int i = 0; i < nbChamps; ++i) {
            if (ligne[i] != NULL) {
                tuples.push_back(string(ligne[i]));
            } else {
                tuples.push_back("NULL");
            }
        }
    }

    mysql_free_result(ResultSet);

    return tuples;
}