#ifndef BDHELPER_H 
#define BDHELPER_H

#include <string>
#include <vector>

using namespace std;

int ConnexionBD();
int DeconnexionBD();
int InsererTuple(const char* table,const  char* colonnes,const  char* valeurs);
int InsererTupleRetourneId(const char* table, const char* colonnes, const char* valeurs);
int ModifierTuple(char* table, char* modifications, char* condition);
int SupprimerTuple(char* table, char* condition);

vector<string> GetAllFromTable(const char* table);
vector<string> GetTuple(const char* table, const char* condition);

#endif