#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

#include "Encodage_S.h"
#include "TCP.h"
#include "BDHelper.h"
#include "protocoleS.h"
#include "protocole.h"

int sEcoute;
// Gestion du pool de threads

#define NB_THREADS_POOL 2
#define TAILLE_FILE_ATTENTE 20

int socketsAcceptees[TAILLE_FILE_ATTENTE];      // queue<int> et faire pop et pup pour mettre à la fin et retirer au début
                                                // au moins plus besoin des indices de lecture et écriture
int indiceEcriture=0, indiceLecture=0;

pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t condSocketsAcceptees;

int main()
{
    int portServeur = 50069;

    // Initialisation socketsAcceptees
    pthread_mutex_init(&mutexSocketsAcceptees,NULL);
    pthread_cond_init(&condSocketsAcceptees,NULL);

    for (int i=0 ; i<TAILLE_FILE_ATTENTE ; i++)
        socketsAcceptees[i] = -1;


    // ---------- Armement des signaux ---------- //
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;

    if (sigaction(SIGINT,&A,NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }

    // ---------- Creation de la socket d'écoute ---------- //
    if ((sEcoute = ServerSocket(portServeur)) == -1)
    {
        perror("Erreur de ServeurSocket");
        exit(1);
    }

    // ---------- Creation du pool de threads ---------- //
    printf("Création du pool de threads.\n");
    pthread_t th;

    for (int i=0 ; i<NB_THREADS_POOL ; i++)
        pthread_create(&th,NULL,FctThreadClient,NULL);


    // Mise en boucle du serveur
    int sService;
    char ipClient[50];

    printf("Demarrage du serveur.\n");

    while(1)
    {
        printf("Attente d'une connexion...\n");
        printf("------- sEcoute = %d\n", sEcoute);

        if ((sService = Accept(sEcoute, ipClient)) == -1)
        {
            perror("Erreur de Accept");
            close(sEcoute);
            // SMOP_Close();
            exit(1);
        }

        printf("Connexion acceptée : IP = %s socket = %d\n",ipClient,sService);

        // Insertion en liste d'attente et réveil d'un thread du pool
        // (Production d'une tâche)
        pthread_mutex_lock(&mutexSocketsAcceptees);
        socketsAcceptees[indiceEcriture] = sService;        // !!!
        indiceEcriture++;

        if (indiceEcriture == TAILLE_FILE_ATTENTE) 
            indiceEcriture = 0;

        pthread_mutex_unlock(&mutexSocketsAcceptees);
        pthread_cond_signal(&condSocketsAcceptees);
    }
}


void* FctThreadClient(void* p)
{
    int sService;

    while(1)
    {
        printf("\t[THREAD %p] Attente socket...\n",pthread_self());
        // Attente d'une tâche
        pthread_mutex_lock(&mutexSocketsAcceptees);

        while (indiceEcriture == indiceLecture)
            pthread_cond_wait(&condSocketsAcceptees,&mutexSocketsAcceptees);

        sService = socketsAcceptees[indiceLecture];
        socketsAcceptees[indiceLecture] = -1;
        indiceLecture++;

        if (indiceLecture == TAILLE_FILE_ATTENTE) 
            indiceLecture = 0;

        pthread_mutex_unlock(&mutexSocketsAcceptees);

        // Traitement de la connexion (consommation de la tâche)
        printf("\t[THREAD %p] Je m'occupe de la socket %d\n",
        pthread_self(),sService);
        TraitementConnexion(sService);
    }
}


void HandlerSIGINT(int s)
{
    printf("\nArret du serveur.\n");
    close(sEcoute);
    pthread_mutex_lock(&mutexSocketsAcceptees);

    for (int i=0 ; i<TAILLE_FILE_ATTENTE ; i++) {
        if (socketsAcceptees[i] != -1) 
            close(socketsAcceptees[i]);
    }

    pthread_mutex_unlock(&mutexSocketsAcceptees);
    // SMOP_Close();
    exit(0);
}


void TraitementConnexion(int sService)
{
    int nbEcrits, nbLus ;
    bool onContinue = true;
    string requeteRecue,reponse;

    while (onContinue)
    {
        printf("\t[THREAD %p] Attente requete...\n",pthread_self());



        requeteRecue = Receive(sService);

        printf("TraitementConnexion------receive fait-----\n");
        cout << endl << "1-------- requeteRecue = ---" << requeteRecue << "------" << endl << endl;


        // ***** Reception Requete ******************
        if (requeteRecue.empty())
        {
            perror("Requete recu vide");
            close(sService);
            HandlerSIGINT(0);
        }
   
        //printf("\t[THREAD %p] Requete recue = %s\n",pthread_self(), requeteRecue);

        // char reponseBuffer[1024];               // Assuming reponse will be max 1024 chars, adjust if needed
        // strcpy(requete, requeteRecue.c_str());  // Copy the string to the char buffer

        //cout << "2-------- sService = " << sService << endl;
        
        reponse = PROTOCOLE(sService, requeteRecue);

        cout << "3-------- reponse = " << reponse << endl;

        // Convert reponseBuffer back to std::string to send
        // reponse = std::string(reponseBuffer);

        // ***** Traitement de la requete ***********
        // onContinue = SMOP(requete,reponse,sService);
        // ***** Envoi de la reponse ****************

        if ((nbEcrits = Send(sService, reponse)) < 0)
        {
            perror("Erreur de Send");
            close(sService);
            HandlerSIGINT(0);
        }


        //cout << "4-------- nbEcrits = " << nbEcrits << endl;

        //printf("\t[THREAD %p] Reponse envoyee = %s\n", pthread_self(), reponse);

        //cout << "4-------- onContinue = " << onContinue << endl;
        
        if (!onContinue) 
            printf("\t[THREAD %p] Fin de connexion de la socket %d\n", pthread_self(), sService);
    }
}

bool VerifierLogin(string user, string password)
{
    string valeurs;
    string nomTable = "employees"; // Ajout de la déclaration
    string nomColonnes = "last_name, first_name, birth_date"; // Ajout de la déclaration
    vector<string> tuples;

    if(ConnexionBD() == 1) {
        printf("Connexion etablie !\n");

        valeurs = "login = \'" + user + "\' AND password = \'" + password + "\';";

        cout << "------nomTable = " << nomTable << endl;
        cout << "------valeurs = " << valeurs << endl;


        tuples = getTuple(nomTable.c_str(), valeurs.c_str());



        if(tuples.size() > 0) {
            DeconnexionBD();
            return true;
        } else {
            DeconnexionBD();
            return false;
        }
    }
    else {
        return false;
    }
}

string AjoutAuteurS(vector<string> champs) // le premier champ du vector champs est le nom de la requête, donc ADD_AUTHOR
{
    string valeurs;
    string typeRequete = "ADD_AUTHOR";
    string nomTable = "authors"; // Ajout de la déclaration
    string nomColonnes = "last_name, first_name, birth_date"; // Ajout de la déclaration

    return ajouterTupleBD(nomTable, nomColonnes, champs, typeRequete);
}



string AjoutSujetS(vector<string> champs)
{
    string valeurs;
    string typeRequete = "ADD_SUBJECT";
    string nomTable = "subjects";
    string nomColonnes = "name";

    return ajouterTupleBD(nomTable, nomColonnes, champs, typeRequete);
}



string AjoutLivreS(vector<string> champs)
{
    string valeurs;
    string typeRequete = "ADD_BOOK";
    string nomTable = "books";
    string nomColonnes = "author_id, subject_id, title, isbn, page_count, stock_quantity, price, publish_year INT";


    return ajouterTupleBD(nomTable, nomColonnes, champs, typeRequete);
}

string ajouterTupleBD(string nomTable, string nomColonnes, vector<string> champs, string typeRequete)
{
    string valeurs;

    if(ConnexionBD() == 1) {
        printf("Connexion etablie !\n");


        if(champs.size() <= 1) {
            DeconnexionBD();
            return CreateTrame(vector<string>{typeRequete, "ko", "Champs insuffisants !"});
        }

        valeurs += "\"" + champs[1];

        for(int i=2 ; champs.size() > i ; i++) {
            valeurs += "\", \"";
            valeurs += champs[i];
        }

        valeurs += "\"";

        cout << "------valeurs = " << valeurs << endl;


        if(InsererTuple(nomTable.c_str(), nomColonnes.c_str(), valeurs.c_str()) == 1) {
            DeconnexionBD();
            return CreateTrame(vector<string>{typeRequete, "ok", "Tuple bien insere."});
        } else {
            DeconnexionBD();
            return CreateTrame(vector<string>{typeRequete, "ko", "Erreur insertion tuple."});
        }
    }
    else {
        return CreateTrame(vector<string>{typeRequete, "ko", "Erreur de connexion a la base de donnees."});
    }

    return CreateTrame(vector<string>{typeRequete, "ok", "Ajout reussi."});
}