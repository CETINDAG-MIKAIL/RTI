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

    cout << endl << endl << "PARTIE CRITIQUE..." << endl;   //---------------------------------

    cout << "\ttrame recue = " << requete << endl;

    getline(ss, longueurStr, '#');
    int longueurTotale = stoi(longueurStr);

    cout << "\tLongueur totale de la trame : " << longueurTotale << endl << endl;


    int totalLus = longueurStr.size() + 1;

    printf("\t1 ---- totalLus = %d\n", totalLus);


    while (getline(ss, longueurStr, '#') && totalLus < longueurTotale) {
        printf("\t2 ---- totalLus = %d\n", totalLus);

        int longueurChamp = stoi(longueurStr);

        printf("\t3 ----- longueurChamp = %d\n", longueurChamp);

        if (totalLus + longueurChamp > longueurTotale) {
            printf("\t4 ------ on sort de la boucle while, on a assez lu : %d", totalLus + longueurChamp);
            break;
        }


        char *buffer = new char[longueurChamp];
        ss.read(buffer, longueurChamp);
        champ = string(buffer, longueurChamp);

        cout << "\t5 ----- Champ lu : " << champ << endl;

        champs.push_back(champ);
        delete[] buffer;

        totalLus += longueurChamp + longueurStr.size();

        printf("\t6 ---- totalLus = %d\n", totalLus);

        printf("\t7 ---- prochaine condition de la boucle while :\n");
        printf("\t\tgetline(ss, longueurStr, '#') -> encore des données à lire dans le flux.\n");
        printf("\t\ttotalLus < longueurTotale : %d < %d\n\n", totalLus, longueurTotale);
    }

    cout << "FIN PARTIE CRITIQUE..." << endl << endl;


    //for(int i=0 ; i<champs.size() ; i++) {
    //    cout << champs[i] << endl;
    //}

    return champs;
}