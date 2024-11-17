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


    
    return to_string(requete.size()) + '#' + requete;
}

vector<string> ReadTrame(string requete) 
{
    vector<string> champs;      // Vecteur pour stocker les champs extraits
    stringstream ss(requete);   // Utilisation de stringstream pour savoir utiliser getline
    string longueurStr, champ;
    char *buffer;

    getline(ss, longueurStr, '#');
    int longueurTotale = stoi(longueurStr);

    int totalLus = 0;

    while (getline(ss, longueurStr, '#') && totalLus < longueurTotale) {

        int longueurChamp = stoi(longueurStr);
        char *buffer = new char[longueurChamp];

        ss.read(buffer, longueurChamp);
        champ = string(buffer, longueurChamp);

        champs.push_back(champ);
        delete[] buffer;

        totalLus += longueurChamp + longueurStr.size() + 1;     // le '+1' pour le '#'
    }

    return champs;
}