#include "TCP.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h> 
#include <unistd.h> 

int main() {
    char *ipServeur;
    int portServeur = 8080;

    strcpy(ipServeur, "0.0.0.0"); // 127.0.0.1

    // Démarrer le client et se connecter au serveur
    int clientSocket = ClientSocket(ipServeur, portServeur);


    if (clientSocket > 0) {
        printf("Connecté au serveur.\n");

        // ------------------------- Envoi de texte au Serveur -------------------------
        
        char message[] = "Bonjour, serveur !";

        printf("\n\n- - - - - Envoie au serveur - - - - -\n");
        Send(clientSocket, message, sizeof(message));
        printf("Message envoyé au serveur : %s\n", message);


        // ------------------------- Réception du texte du Serveur -------------------------
        char buffer[100];
        int nbLus;

        printf("\n\n- - - - - Reception du serveur - - - - -\n");
        if ((nbLus = Receive(clientSocket,buffer)) < 0)
        {
            perror("Erreur de Receive");
            close(clientSocket);
            exit(1);
        }

        printf("NbLus = %d\n",nbLus);
        // buffer[nbLus] = 0;                   // pourquoi mettre 0 dans le buffer[...]
        printf("Lu = --%s--\n",buffer);
    }

    close(clientSocket);

    return 0;
}
