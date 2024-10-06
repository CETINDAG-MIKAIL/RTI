#ifndef PROTOCOLES_H
#define PROTOCOLES_H


string PROTOCOLE(int socket, string requete);
bool PROTOCOLE_Login(string user, string password);
int PROTOCOLE_Operation(char op,int a,int b);
void PROTOCOLE_Close();

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);


#endif