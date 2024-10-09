#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

#include "protocoleS.h"
#include "protocole.h"
#include "Encodage_S.h"

//***** Etat du protocole : liste des clients loggés ****************
int clients[10];
int nbClients = 0;

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;


//***** Parsing de la requete et creation de la reponse *************
string PROTOCOLE(int socket, string requete)
{
    string reponse, typeRequete;
    vector<string> requeteVec;

    requeteVec = ReadTrame(requete);

    // ***** Récupération nom de la requete *****************
    if (requeteVec.size() == 0) 
    {
        return CreateTrame(vector<string>{"ko", "Nombre d'arguments insuffisant !"});
    }
    typeRequete = requeteVec[0];


    // ***** LOGIN ******************************************
    if (typeRequete == "LOGIN") 
    {
        string user, password;
        
        user = requeteVec[1];
        password = requeteVec[2];

        printf("\t[THREAD %p] LOGIN de %s\n", pthread_self(), user);

        if (estPresent(socket) >= 0) // client déjà loggé
        {
            return CreateTrame(vector<string>{"LOGIN", "ko", "Client deja logge !"});
        }
        else
        {
            if (VerifierLogin(user, password))
            {
                ajoute(socket);
                return CreateTrame(vector<string>{"LOGIN", "ok", "Client logge"});
            }
            else
            {
                return CreateTrame(vector<string>{"LOGIN", "ko", "Mauvais identifiants !"});
            }
        }
    }
    
    // ***** LOGOUT *****************************************
    if (typeRequete == "LOGOUT")
    {
        printf("\t[THREAD %p] LOGOUT\n", pthread_self());
        retire(socket);
        return CreateTrame(vector<string>{"LOGOUT", "ok", "Deconnexion reussie."});
    }

    // ***** Ajout Author **********************************
    if (typeRequete == "ADD_AUTHOR")
    {   
        if (estPresent(socket) == -1) {
            return CreateTrame(vector<string>{"ADD_AUTHOR", "ko", "Client non logge !"});
        }
        else
        {
            return AjoutAuteurS(ReadTrame(requete));
        }
    }

    // ***** Ajout Subject **********************************
    if (typeRequete == "ADD_SUBJECT")
    {   
        if (estPresent(socket) == -1) {
            return CreateTrame(vector<string>{"ADD_SUBJECT", "ko", "Client non logge !"});
        }
        else
        {
            return AjoutSujetS(ReadTrame(requete));
        }
    }

    // ***** Ajout Book **********************************
    if (typeRequete == "ADD_BOOK")
    {   
        if (estPresent(socket) == -1) {
            return CreateTrame(vector<string>{"ADD_BOOK", "ko", "Client non logge !"});
        }
        else
        {
            return AjoutLivreS(ReadTrame(requete));
        }
    }

    // ***** GET Authors **********************************
    if (typeRequete == "GET_AUTHORS")
    {
        
        if (estPresent(socket) == -1) {
            return CreateTrame(vector<string>{"GET_AUTHORS", "ko", "Client non logge !"});
        }
        else
        {
            return GetAuteursS(ReadTrame(requete));
        }
    }

    // ***** GET Subject **********************************
    if (typeRequete == "GET_SUBJECTS")
    {
        
        if (estPresent(socket) == -1) {
            return CreateTrame(vector<string>{"GET_SUBJECTS", "ko", "Client non logge !"});
        }
        else
        {
            return GetSujetsS(ReadTrame(requete));
        }
    }

    // ***** GET Books **********************************
    // if (typeRequete == "GET_BOOKS")
    // {
        
    //     if (estPresent(socket) == -1) {
    //         return CreateTrame(vector<string>{"GET_BOOKS", "ko", "Client non logge !"});
    //     }
    //     else
    //     {
    //         return GetLivresS(ReadTrame(requete));
    //     }
    // }

    return CreateTrame(vector<string>{"ko", "Aucun type de requete !"});
}


//***** Gestion de l'état du protocole ******************************
int estPresent(int socket)
{
    int indice = -1;

    pthread_mutex_lock(&mutexClients);

    for(int i=0 ; i<nbClients ; i++) {
        if (clients[i] == socket) { 
            indice = i; 
            break;
        }
    }

    pthread_mutex_unlock(&mutexClients);
    
    return indice;
}

void ajoute(int socket)
{
    pthread_mutex_lock(&mutexClients);
    clients[nbClients] = socket;
    nbClients++;
    pthread_mutex_unlock(&mutexClients);
}

void retire(int socket)
{
    int pos = estPresent(socket);
    
    if (pos == -1)
        return;
    
    pthread_mutex_lock(&mutexClients);
    
    for (int i=pos ; i<=nbClients-2 ; i++)
        clients[i] = clients[i+1];
    
    nbClients--;
    
    pthread_mutex_unlock(&mutexClients);
}

//***** Fin prématurée **********************************************
void PROTOCOLE_Close()
{
    pthread_mutex_lock(&mutexClients);

    for (int i=0 ; i<nbClients ; i++)
        close(clients[i]);
    
    pthread_mutex_unlock(&mutexClients);
}