#ifndef PROTOCOLES_H
#define PROTOCOLES_H


string PROTOCOLE(int socket, string requete);
void PROTOCOLE_Close();

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);


#endif