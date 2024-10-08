#ifndef PROTOCOLEC_H
#define PROTOCOLEC_H

#include <string>

using namespace std;

string AjouterAuteur(string lastName, string firstName, string birthDate, int sClient);
string AjouterLivre(vector<string> VecLivre, int sClient);
string AjouterSujet(string lastName, int sClient);

string GetAuteur(int sClient);

string Login(string login, string password, int sClient);
string Logout(int sClient);
string ReponseAQT(int sClient, vector<string>champs);

#endif