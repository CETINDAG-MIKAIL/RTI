#ifndef BDHELPER_H 
#define BDHELPER_H

#include <string>

using namespace std;

int ConnexionBD();
int DeconnexionBD();
int InsererTuple(const char* table,const  char* colonnes,const  char* valeurs);
int ModifierTuple(char* table, char* modifications, char* condition);
int SupprimerTuple(char* table, char* condition);

string getAllFromTable(char* table);
vector<string> getTuple(const char* table, const char* condition);

#endif