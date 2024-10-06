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
    //champs = ["sdlfjlsfd", ] - > ["5#sdlfjlsfd", ]
    string requete;

    printf("------SEND----------\n");
    
    string longueurChamp=0; 
    for(int i=0 ; i < champs.size() ; i++) 
    {
        longueurChamp = to_string(champs[i].size());
        requete += longueurChamp;
        requete += '#';
        requete += champs[i];

        printf("TRAME : %s\n", requete); 
    }
    requete += "$";


    return requete;
}

vector<string> ReadTrame(string requete) {
    
    vector<string> champs;  // Vecteur pour stocker les champs extraits
    stringstream ss(requete);  // Utilisation de stringstream pour savoir utiliser getline
    string longueurStr, champ;

    printf("------RECEIVE----------\n");

    // Lire la requête tant qu'il y a des données
    while (getline(ss, longueurStr, '#')) { 
        if (!getline(ss, champ, '#')) { 
            break; // Sortir si on ne trouve pas de prochain champ
        }
        printf("champ : %s\n", champ); 
        champs.push_back(champ);
    }

    return champs;
}