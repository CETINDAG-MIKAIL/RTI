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
	string RecuServeur;
	champs.push_back("ADD_AUTHOR");
	champs.push_back(lastName);
	champs.push_back(firstName);
	champs.push_back(birthDate);

	string trame = CreateTrame(champs);

	cout << "ajouterAuteur------1 tramea envoyer au serveur = " << trame << endl;

	// Le code suivant a été fait rapidement, à vérifier !
	// ici on va recevoir la trame en string
	// envoie au protocole Serveur (avec Send dans TCP.cpp)
	if(Send(sClient, trame) == -1){
		printf("Erreur send (ProtocoleC)\n");
		return "ko";
	}

	printf("ajouterAuteur------2-----\n");

	if(Receive(sClient).empty()) // on attend la reponse du serveur qui a fait un send 1donc on recoit 0#AjoutAuteur#2ok#12clientlogge
	{
		printf("Erreur receive (ProtocoleC)\n");
		return RecuServeur;
	}
	else
	{
		RecuServeur = Receive(sClient);
	}
	printf("ajouterAuteur------3-----\n");
	
	return RecuServeur;
}

string login(string login, string password, int sClient)
{
	vector<string> champs;
	champs.push_back("LOGIN");
	champs.push_back(login);
	champs.push_back(password); // on cree un vecteur de string car createtram att un v de string pour pouvoir creer la trame et envoyer au serveur 

	string trame = CreateTrame(champs);

	if(Send(sClient, trame) == -1){
		printf("Erreur send (ProtocoleC)\n");
		return "ko";
	}

	printf("login------1-----\n");

	if(Receive(sClient).empty()) // on attend une reponse du serveur 
	{
		printf("Erreur receive (ProtocoleC)\n");
		return "ko";
	}

	return "ok";
}