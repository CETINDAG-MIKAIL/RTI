#ifndef DBHELPER_H 
#define DBHELPER_H


int ConnexionBD();
int DeconnexionBD();
int InsererTuple(const char* table,const  char* colonnes,const  char* valeurs);
int ModifierTuple(char* table, char* modifications, char* condition);
int SupprimerTuple(char* table, char* condition);

string getAuthors(char* table);
string getBooks(char* table);

#endif