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

    printf("------CreateTrame----------\n");
    
    string longueurChamp;

    for(int i=0 ; i < champs.size() ; i++) 
    {
        longueurChamp = to_string(champs[i].size());
        requete += longueurChamp;
        requete += '#';
        requete += champs[i];

        //cout << "-----1 TRAME cree = " << requete << endl; 
    }
    //requete += "$";

    //cout << "-----2 TRAME cree = " << requete << endl; 
    
    return requete;
}

vector<string> ReadTrame(string requete) {
    
    vector<string> champs;      // Vecteur pour stocker les champs extraits
    stringstream ss(requete);   // Utilisation de stringstream pour savoir utiliser getline
    string longueurStr, champ;
    char buffer[100];

    printf("------ReadTrame----------\n");

    // Lire la requête tant qu'il y a des données
    while (getline(ss, longueurStr, '#')) {

        //cout << "---ReadTrame--- longueurStr = " << longueurStr << endl;

        ss.read(buffer, stoi(longueurStr));


        // faire malloc au lieu de char buffer[100]

        // if (!getline(ss, champ, '#')) { 
        //     break; // Sortir si on ne trouve pas de prochain champ
        // }

        //cout << "---ReadTrame--- buffer = " << buffer << endl;
        champ = string(buffer, stoi(longueurStr));

        //cout << "---ReadTrame--- champ = " << champ << endl;

        champs.push_back(string(buffer, stoi(longueurStr)));
    }

    return champs;
}