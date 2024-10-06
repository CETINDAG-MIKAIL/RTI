#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

#include "protocoleC.h"
#include "TCP.h"
#include "protocole.h"


string ajouterAuteur(string lastName, string firstName, string birthDate, int sClient)
{
	vector<string> champs;
	champs.push_back(lastName);
	champs.push_back(firstName);
	champs.push_back(birthDate);

	string trame = CreateTrame(champs);
	// Le code suivant a été fait rapidement, à vérifier !
	// ici on va recevoir la trame en string
	// envoie au protocole Serveur (avec Send dans TCP.cpp)
	if(Send(sClient, trame)){
		printf("Erreur send (PtrocoleC)\n");
		return "ko";
	}

	if(Receive(sClient).empty())
	{
		printf("Erreur receive (PtrocoleC)\n");
		return "ko";
	}
		
	
	return "ok";
}