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

    cout << "trame recue = " << requete << endl;
    getline(ss, longueurStr, '#');
    int longueurTotale = stoi(longueurStr);
    cout << "Longueur totale de la trame : " << longueurTotale << endl;

    
    int totalLus = longueurStr.size() + 1;

    printf("1111---- totalLus = %d\n", totalLus);


     while (getline(ss, longueurStr, '#') && totalLus < longueurTotale) {
        printf("2222---- totalLus = %d\n", totalLus);

        int longueurChamp = stoi(longueurStr);  
        printf("----------- longueurChamp = %d\n", longueurChamp);
        
        if (totalLus + longueurChamp > longueurTotale) {
            break;  
        }


        char *buffer = new char[longueurChamp];
        ss.read(buffer, longueurChamp);         
        champ = string(buffer, longueurChamp);  
        
        champs.push_back(champ);
        delete[] buffer;  

        totalLus += longueurChamp + longueurStr.size();

        printf("33333---- totalLus = %d\n", totalLus);
    }


    //for(int i=0 ; i<champs.size() ; i++) {
    //    cout << champs[i] << endl;
    //}

    return champs;
}