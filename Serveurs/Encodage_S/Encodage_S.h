#ifndef ENCODAGE_S_H 
#define ENCODAGE_S_H

#include <vector>
void* FctThreadClient(void* p);
void HandlerSIGINT(int s);
void TraitementConnexion(int sService);

bool VerifierLogin(string user, string password);

string AjoutAuteurS(vector<string> champs);
string AjoutSujetS(vector<string> champs);
string AjoutLivreS(vector<string> champs);
string AjouterTupleBD(string nomTable, vector<string> nomColonnes, vector<string> champs, string typeRequete);
string CreerCondition(vector<string> nomColonnes, vector<string> champs);

string GetAuteursS(vector<string> champs);
string GetSujetsS(vector<string> champs);
//string GetLivresS(vector<string> champs);
string GetTuplesBD(string nomTable, string typeRequete);

#endif