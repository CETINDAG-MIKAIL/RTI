#include "TCP.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h> 
#include <unistd.h> 

#define NI_MAXHOST 50


int main() {
    int port = 8080;
    char clientIp[NI_MAXHOST];

    // Démarrer le serveur
    int serverSocket = ServerSocket(port);
    printf("Serveur à l'écoute sur le port %d\n", port);

    // Attendre une connexion et récupérer l'adresse IP du client
    int serviceSocket = Accept(serverSocket, clientIp);

    if (serviceSocket > 0) {
        printf("Connexion acceptée avec le client ayant l'adresse IP : %s\n", clientIp);

        // ------------------------- Réception du texte du Client -------------------------
        char buffer[50] = {0};

        printf("\n\n- - - - - Reception du client - - - - -\n");
        Receive(serviceSocket, buffer);
        printf("Message reçu du client : %s\n", buffer);


        // ------------------------- Envoi de texte au Client -------------------------
        char texte[80];
        int nbEcrits;

        sprintf(texte,"Je vais bien merci ;) !");

        printf("\n\n- - - - - Envoie au client - - - - -\n");
        if ((nbEcrits = Send(serviceSocket,texte,strlen(texte))) < 0)
        {
            perror("Erreur de Send");
            close(serviceSocket);
            close(serverSocket);
            exit(1);
        }

        printf("NbEcrits = %d\n",nbEcrits);
        printf("Ecrit = --%s--\n",texte);
    }

    close(serviceSocket);
    close(serverSocket);

    return 0;
}