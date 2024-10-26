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
#include <algorithm>


vector<string> AjouterAuteur(string lastName, string firstName, string birthDate, int sClient)
{
	vector<string> champs;
	string RecuServeur;
	champs.push_back("ADD_AUTHOR");
	champs.push_back(lastName);
	champs.push_back(firstName);
	champs.push_back(birthDate);

	
	return ReponseAQT(sClient, champs);
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
    	replace(VecLivre[6].begin(), VecLivre[6].end(), ',', '.');  // Remplace la virgule par un point 

    	vector<string> champs;
	champs.push_back("ADD_BOOK");
	
	for (int i = 0; i < VecLivre.size(); i++)
	{
		champs.push_back(VecLivre[i]);
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
			EnvoyerQT.push_back(VecRecu[i]);		// 10GET_SUB#11#18mikail#22mozdalzbda"	
	}
	
	return EnvoyerQT;
}
vector<string> GetAuteur(int sClient)
{
	vector<string> EnvoyerQT;
	vector<string> champs;
	champs.push_back("GET_AUTHORS");
	vector<string> VecRecu;
	


	string trame = CreateTrame(champs);


	if(Send(sClient, trame) == -1){
		printf("Erreur send (ProtocoleC) - logout\n");
		return vector<string>{"Send erreur (ProtocoleC) - logout"};
	}

	trame = Receive(sClient);
	VecRecu = ReadTrame(trame);
	string nomPrenom;
								// -> ici VecRecu[2] équivaut au champ position 3 donc : "Auteur deja existant !"
	
	for (size_t i = 4; i < VecRecu.size(); i=i+4) {
		
		nomPrenom = VecRecu[i] + ", " + VecRecu[i+1];
		EnvoyerQT.push_back(nomPrenom);
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
		EnvoyerQT.push_back("Demande non recue du serveur, veillez reessayer");

		return EnvoyerQT;
	}
	trame =  Receive(sClient);
	VecRecu = ReadTrame(trame);
	
	if(VecRecu[1] == "ko"){
		printf("Erreur send (ProtocoleC) - logout\n");
		
		EnvoyerQT.push_back("ko");
		EnvoyerQT.push_back(VecRecu[2]);

		return EnvoyerQT;
	}
	EnvoyerQT.push_back("ok");
	EnvoyerQT.push_back(VecRecu[2]);		// 10ADD_AUTHOR#2ko#18Auteur deja existant !"	
								// -> ici VecRecu[2] équivaut au champ position 3 donc : "Auteur deja existant !"
	
	// CAS ajouter livre on met l'id du livre apres le message de reponse du serveur 
	if(VecRecu[0] == "ADD_BOOK")
		EnvoyerQT.push_back(VecRecu[3]);
	return EnvoyerQT;
}