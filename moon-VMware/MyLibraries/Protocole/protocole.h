#ifndef PROTOCOLE_H
#define PROTOCOLE_H
#define NB_MAX_CLIENTS 100

string CreateTrame(vector<string> champs);
vector<string> ReadTrame(string requete);

#endif