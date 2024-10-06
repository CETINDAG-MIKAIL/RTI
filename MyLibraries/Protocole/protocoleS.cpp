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
            if (PROTOCOLE_Login(user, password))
            {
                ajoute(socket);
                return CreateTrame(vector<string>{"LOGIN", "ok", "Client logge", to_string(socket)});            } 
            else
            {
                return CreateTrame(vector<string>{"LOGIN", "ko", "Mauvais identifiants !"});
            }
        }
    }
    
    // ***** LOGOUT *****************************************
    if (typeRequete == "LOGOUT")
    {
        printf("\t[THREAD %p] LOGOUT\n",pthread_self());
        retire(socket);
        return CreateTrame(vector<string>{"LOGOUT", "ok"});
    }

    // ***** GET Author **********************************
/*    if (typeRequete == "GET_AUTHORS")
    {
        char *id_author; 
        char *last_name_author;
        char *first_name_author;
        
        ptr = strtok(NULL, "#");
        id_author = ptr;
        last_name_author = strtok(NULL, "#");
        first_name_author = strtok(NULL, "#");
        
        printf("\t[THREAD %p] GET_AUTHORS %s %s %s\n", pthread_self(), id_author, last_name_author, first_name_author);
        
        if (estPresent(socket) == -1)
        {
            return CreateTrame(vector<string>{"GET_AUTHORS", "ko", "Client non logge !"});
        }
        else
        {
            int resultat = REQ_GET_AUTHORS(id_author, last_name_author, first_name_author);
            if (resultat == -1)
            {
                return CreateTrame(vector<string>{"GET_AUTHORS", "ko", "Erreur lors de la récupération des auteurs !"});
            }
            else
            {
                return CreateTrame(vector<string>{"GET_AUTHORS", "ok", to_string(resultat)});
            }
        }
    }
*/

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
    return "";
}


//***** Traitement des requetes *************************************
bool PROTOCOLE_Login(string user, string password)
{
    if ((user == "luca") && (password == "abc123"))
        return true;

    if ((user == "mikail") && (password == "xyz456"))
        return true;

    return false;
}

int REQ_GET_AUTHORS(char id_author,char last_name_author,char first_name_author)
{
    // if (getAuthor("authors") == 1) {
    //     printf("Requête exécutée avec succès.\n");
    // } else {
    //     printf("Erreur lors de l'exécution de la requête.\n");
    // }
    return 0;
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