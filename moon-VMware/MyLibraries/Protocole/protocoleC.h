#ifndef PROTOCOLEC_H
#define PROTOCOLEC_H

#include <string>

using namespace std;

vector<string> AjouterAuteur(string lastName, string firstName, string birthDate, int sClient);
vector<string> AjouterLivre(vector<string> VecLivre, int sClient);
vector<string> AjouterSujet(string lastName, int sClient);

vector<string> GetAuteur(int sClient);
vector<string> GetSubjects(int sClient);

vector<string> Login(string login, string password, int sClient);
vector<string> Logout(int sClient);
vector<string> ReponseAQT(int sClient, vector<string>champs);

#endif