#include "mainwindowclientbookencoder.h"
#include <signal.h> 
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <iostream>
#include <string>

using namespace std;

#include "TCP.h"

#include <QApplication>

void HandlerSIGINT(int);

int sClient;

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindowClientBookEncoder w;
    w.show();


    return a.exec();


    // Armement des signaux 
    struct sigaction A; 
    A.sa_flags = 0; 
    sigemptyset(&A.sa_mask); 
    A.sa_handler = HandlerSIGINT; 
    if (sigaction(SIGINT,&A,NULL) == -1) 
    { 
        perror("Erreur de sigaction"); 
        exit(1); 
    } 


    // Connexion sur le serveur  
    if ((sClient = ClientSocket(argv[1], atoi(argv[2]))) == -1) 
    { 
        perror("Erreur de ClientSocket"); 
        exit(1); 
    } 
    printf("Connecte sur le serveur.\n");


    char requete[200], reponse[200];
    string requeteString, reponseString; 
    int nbEcrits, nbLus; 

    while(1) 
    { 
        printf("Requete a envoyer (<CTRL-C> four fin) : "); 
        fgets(requete, 200, stdin); 
        requete[strlen(requete)-1] = 0;                         // pour retirer le '\n'

        requeteString = requete;
    
        // ***** Envoi de la requete ****************** 
        if ((nbEcrits = Send(sClient, requeteString)) == -1) 
        { 
            perror("Erreur de Send"); 
            HandlerSIGINT(0); 
        } 
    
        printf("Requete envoyee = %s\n",requete); 
    
        // ***** Attente de la reponse **************** 
        reponseString = Receive(sClient);

        if (reponseString.empty()) 
        { 
            perror("Erreur de Receive"); 
            HandlerSIGINT(0); 
        } 

        printf("Reponse recue = %s\n", reponseString); 
    } 
}


void HandlerSIGINT(int s) 
{ 
    printf("\nArret du client.\n"); 
    close(sClient); 
    exit(0); 
}
