#ifndef ENCODAGE_S_H 
#define ENCODAGE_S_H

#include <vector>
void* FctThreadClient(void* p);
void HandlerSIGINT(int s);
void TraitementConnexion(int sService);

bool VerifierLogin(string user, string password);
string AjoutAuteurS(vector<string> champs);

#endif