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
    for (size_t i = 0; i < champs.size(); i++) {
        std::cout << "Element " << i << ": " << champs[i] << endl;
    }



    //champs = ["sdlfjlsfd", ] - > ["5#sdlfjlsfd", ]
    string requete;

    //printf("------CreateTrame----------\n");
    
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

    cout << "-----2 CreateTrame, TRAME cree = " << requete << endl << endl;
    
    return requete;
}

vector<string> ReadTrame(string requete) 
{
    
    printf("---ReadTrame -- 1 requete = %s\n", requete.c_str());
    vector<string> champs;      // Vecteur pour stocker les champs extraits
    stringstream ss(requete);   // Utilisation de stringstream pour savoir utiliser getline
    string longueurStr, champ;
    char *buffer;

    printf("---ReadTrame -- 2\n");
    //printf("------ReadTrame----------\n");

    // Lire la requête tant qu'il y a des données
    while (getline(ss, longueurStr, '#')) {

        printf("---ReadTrame -- 3\n");
        cout << "longueurStr = " << longueurStr << endl;

        buffer = new char[stoi(longueurStr)];


        //cout << "---ReadTrame--- longueurStr = " << longueurStr << endl;
        printf("---ReadTrame -- 4\n");

        ss.read(buffer, stoi(longueurStr));



        //cout << "---ReadTrame--- buffer = " << buffer << endl;
        printf("---ReadTrame -- 5\n");

        champ = string(buffer, stoi(longueurStr));

        printf("---ReadTrame -- 6\n");
        //cout << "---ReadTrame--- champ = " << champ << endl;

        champs.push_back(string(buffer, stoi(longueurStr)));

        printf("---ReadTrame -- 7\n");

    }

    printf("---ReadTrame -- 8 JE SORS\n");

    delete[] buffer;

    printf("---ReadTrame -- 9 JE SORS\n");

    return champs;
}