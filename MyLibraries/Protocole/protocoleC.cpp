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


string AjouterAuteur(string lastName, string firstName, string birthDate, int sClient)
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

vector<string> Login(string login, string password, int sClient)
{
	vector<string> champs;
	champs.push_back("LOGIN");
	champs.push_back(login);
	champs.push_back(password); // on cree un vecteur de string car createtram att un v de string pour pouvoir creer la trame et envoyer au serveur 

	return ReponseAQT(sClient, champs);

}
vector<string> Logout(int sClient)
{
	vector<string> champs;
	champs.push_back("LOGOUT");

	return ReponseAQT(sClient, champs);

}
vector<string> AjouterSujet(string name, int sClient)
{
	vector<string> champs;
	champs.push_back("ADD_SUBJECT");
	champs.push_back(name);

	return ReponseAQT(sClient, champs);

}
vector<string> AjouterLivre(vector<string> VecLivre, int sClient)
{
	vector<string> champs;
	champs.push_back("ADD_BOOK");
	
	for (int i = 0; i < VecLivre.size(); i++)
	{
		champs.push_back(VecLivre[i]);
	}
	
	for (int i = 0; i < VecLivre.size(); i++)
	{
		cout << "Element : " <<  champs[i] << endl;
	}

	return ReponseAQT(sClient, champs);
}
vector<string> GetSubjects(int sClient)
{
	vector<string> EnvoyerQT;
	vector<string> champs;
	champs.push_back("GET_SUBJECTS");
	vector<string> VecRecu;
	


	string trame = CreateTrame(champs);


	if(Send(sClient, trame) == -1){
		printf("Erreur send (ProtocoleC) - logout\n");
		return vector<string>{"Send erreur (ProtocoleC) - logout"};
	}

	trame = Receive(sClient);
	VecRecu = ReadTrame(trame);

								// -> ici VecRecu[2] équivaut au champ position 3 donc : "Auteur deja existant !"
	
	for (size_t i = 4; i < VecRecu.size(); i=i+2) {
	        cout << "VecRecu " << i << ": " << VecRecu[i] <<  endl;
			EnvoyerQT.push_back(VecRecu[i]);		// 10GET_SUB#11#18mikail#22mozdalzbda"	
/*
VecRecu 2: Tous les tuples bien recuperes.
VecRecu 3: 1
VecRecu 4: Science-Fiction
VecRecu 5: 2
VecRecu 6: Thriller
VecRecu 7: 3
VecRecu 8: Informatiqu*/
	}
	
	return EnvoyerQT;
}



vector<string> ReponseAQT(int sClient, vector<string> champs)
{

	string trame = CreateTrame(champs);
	vector<string> EnvoyerQT;
	vector<string> VecRecu;



	if(Send(sClient, trame) == -1){
		printf("Erreur send (ProtocoleC) - logout\n");
		
		EnvoyerQT.push_back("ko");
		EnvoyerQT.push_back("Send erreur (ProtocoleC) - logout");

		return EnvoyerQT;
	}
	trame =  Receive(sClient);
	VecRecu = ReadTrame(trame);
	
	if(VecRecu[1] == "ko"){
		printf("Erreur send (ProtocoleC) - logout\n");
		
		EnvoyerQT.push_back("ko");
		EnvoyerQT.push_back("Send erreur (ProtocoleC) - logout");

		return EnvoyerQT;
	}




	EnvoyerQT.push_back("ok");
	EnvoyerQT.push_back(VecRecu[2]);		// 10ADD_AUTHOR#2ko#18Auteur deja existant !"	
								// -> ici VecRecu[2] équivaut au champ position 3 donc : "Auteur deja existant !"
	
	for (size_t i = 0; i < VecRecu.size(); i++) {
	        cout << "VecRecu " << i << ": " << VecRecu[i] <<  endl;
	}
	
	return EnvoyerQT;
}