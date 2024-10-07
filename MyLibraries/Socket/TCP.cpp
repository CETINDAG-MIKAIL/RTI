#include "TCP.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <sys/types.h> 
#include <sys/socket.h> 
#include <netdb.h> 

#include <iostream>
#include <string>

using namespace std;


int CreationSocket()
{
	int numSocket;

	if ((numSocket  = socket(AF_INET, SOCK_STREAM, 0)) == -1) 
	{ 
		perror("Erreur de socket()"); 
		exit(1); 
	} 
	printf("socket creee = %d\n",numSocket);

	return numSocket;
}

int ServerSocket(int port) 
{
	int sEcoute = CreationSocket();

	// Construction de l'adresse
	struct addrinfo hints;
	struct addrinfo *results;
	char portStr[6]; 

	sprintf(portStr, "%d", port); 

	memset(&hints,0,sizeof(struct addrinfo)); 
	hints.ai_family = AF_INET; 
	hints.ai_socktype = SOCK_STREAM; 
	hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV;   // pour une connexion passive 

	if (getaddrinfo(NULL,portStr, &hints, &results) != 0) 
		exit(1);


	// Liaison de la socket à l'adresse 
	if (bind(sEcoute,results->ai_addr,results->ai_addrlen) < 0) 
	{ 
		perror("Erreur de bind()"); 
		exit(1); 
	} 
	freeaddrinfo(results); 
	printf("bind() reussi !\n");

	return sEcoute;
}

int Accept(int sEcoute,char *ipClient)
{
	// Mise à l'écoute de la socket
	if (listen(sEcoute,SOMAXCONN) == -1)
	{
		perror("Erreur de listen()");
		exit(1);
	}
	printf("listen() reussi !\n");


	// Attente d'une connexion
	int sService;
	if ((sService = accept(sEcoute,NULL,NULL)) == -1)
	{
		perror("Erreur de accept()");
		exit(1);
	}
	printf("accept() reussi !");
	printf("socket de service = %d\n",sService);


	// Recuperation d'information sur le client connecte
	struct sockaddr_in adrClient;

	socklen_t adrClientLen = sizeof(struct sockaddr_in); // nécessaire

	getpeername(sService,(struct sockaddr*)&adrClient,&adrClientLen);
	
	char host[NI_MAXHOST];
 	char port[NI_MAXSERV];
	getnameinfo((struct sockaddr*)&adrClient,adrClientLen,host,NI_MAXHOST,port,NI_MAXSERV,NI_NUMERICSERV | NI_NUMERICHOST);
	printf("Client connecte --> Adresse IP: %s -- Port: %s\n",host,port);
	strcpy(ipClient, host);
	
	return sService;
}

int ClientSocket(char *ipServeur,int portServeur)
{
	int sClient = CreationSocket();

	// Construction de l'adresse
	struct addrinfo hints;
	struct addrinfo *results;
	char portStr[6];

    sprintf(portStr, "%d", portServeur);

	memset(&hints,0,sizeof(struct addrinfo)); 

	hints.ai_family = AF_INET; 
	hints.ai_socktype = SOCK_STREAM; 
	hints.ai_flags = AI_NUMERICSERV;   // pour une connexion passive 

	//printf("-------1\n");
	
	if (getaddrinfo(ipServeur, portStr, &hints, &results) != 0) 
		exit(1);
	//printf("-------2\n");
	//printf("-------3 : sClient = %d\n", sClient);
	//printf("-------4 connect = %d\n", connect(sClient, results->ai_addr, results->ai_addrlen));

	// Demande de connexion 
	if (connect(sClient, results->ai_addr, results->ai_addrlen) == -1) 
	{ 
	//printf("-------5\n");
		perror("Erreur de connect()"); 
		exit(1); 
	} 

	printf("connect() reussi !\n");

	return sClient;
}

int Send(int sSocket, string trame) 
{
	//printf("------SENDDDDDDD----------\n");
	//printf("-----SEND----sSocket = %d------1-----\n", sSocket);
	printf("-----SEND----trame.c_str() = %s------1-----\n", trame.c_str());
	//printf("-----SEND----taille trame = %d------1-----\n", trame.size());

	return write(sSocket, trame.c_str(), trame.size());
}

string Receive(int sSocket) 
{
	printf("------RECEIVE----------\n");
	char buffer[1024];

	int bytesRead = read(sSocket, buffer ,sizeof(buffer)-1);
	
	if (bytesRead < 0) {
        perror("Erreur de lecture");
        return "";
    }

    buffer[bytesRead] = '\0';
	string maRequete = buffer;
	return maRequete;
}