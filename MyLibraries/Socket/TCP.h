#ifndef TCP_H 

#define TCP_H
#define TAILLE_MAX_DATA 10000 
#include <string>
#include <string.h>
using namespace std;
int ServerSocket(int port);
int Accept(int sEcoute, char *ipClient);
int ClientSocket(char * ipServeur, int portServeur);
int Send(int sSocket, string trame);
int Receive(int sSocket, string trame);

#endif