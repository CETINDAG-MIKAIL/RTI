#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <string>
#include <vector>
#include <TCP.h>

using namespace std;

#include "protocoleC.h"


string ajouterAuteur(string lastName, string firstName, string birthDate)
{
	vector<string> champs;
	int retourS, retourR; 
	champs.push_back(lastName);
	champs.push_back(firstName);
	champs.push_back(birthDate);

	string trame = protocole.createTrame(champs);
	// Le code suivant a été fait rapidement, à vérifier !
	// ici on va recevoir la trame en string
	// envoie au protocole Serveur (avec Send dans TCP.cpp)
	if(retourS = Send(sSocket, trame)== - 1)printf("Erreur send (PtrocoleC)\n");

	if(retourR = receive(sSocket, trame)==-1)printf("Erreur receive (PtrocoleC)\n");
		
	if((retourR || retourS) == -1)
		return 'KO'

	return "OK";
}