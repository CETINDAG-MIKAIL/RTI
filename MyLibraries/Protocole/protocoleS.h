#ifndef PROTOCOLES_H
#define PROTOCOLES_H


bool PROTOCOLE(int socket, string requete);
bool PROTOCOLE_Login(const char* user,const char* password);
int PROTOCOLE_Operation(char op,int a,int b);
void PROTOCOLE_Close();

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);


#endif