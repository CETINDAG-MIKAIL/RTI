#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <string>
#include <vector>
#include <sstream>

using namespace std;

#include "protocole.h"

string CreateTrame(vector<string> champs)
{
    string requete;
    string longueurChamp;

    for(int i=0 ; i < champs.size() ; i++) 
    {
        longueurChamp = to_string(champs[i].size());
        requete += longueurChamp;
        requete += '#';
        requete += champs[i];
    }
    
    return requete;
}

vector<string> ReadTrame(string requete) 
{
    vector<string> champs;      // Vecteur pour stocker les champs extraits
    stringstream ss(requete);   // Utilisation de stringstream pour savoir utiliser getline
    string longueurStr, champ;
    char *buffer;

    // Lire la requête tant qu'il y a des données
    while (getline(ss, longueurStr, '#')) {

        buffer = new char[stoi(longueurStr)];

        ss.read(buffer, stoi(longueurStr));

        champ = string(buffer, stoi(longueurStr));

        champs.push_back(string(buffer, stoi(longueurStr)));
    }

    delete[] buffer;

    return champs;
}