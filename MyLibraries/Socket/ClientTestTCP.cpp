mikail.ctdg
mikail.ctdg
Invisible

Luca.gg — Aujourd’hui à 00:25
pourtant je suis vert
Image
mikail.ctdg — Aujourd’hui à 01:02
protocolC.cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <string>
Afficher plus
message.txt
5 Ko
protocoleC.h
#ifndef PROTOCOLEC_H
#define PROTOCOLEC_H

#include <string>

using namespace std;
Afficher plus
message.txt
1 Ko
mainwindow.cpp
#include "mainwindowclientbookencoder.h"
#include "ui_mainwindowclientbookencoder.h"
#include "unistd.h"
#include <QInputDialog>
#include <QMessageBox>
#include <iostream>
Afficher plus
message.txt
16 Ko
Luca.gg — Aujourd’hui à 08:33
en vrai si tu sais faire gerAuteur dans le train ce serait bien (c’est copier-collé de subject)
mikail.ctdg — Aujourd’hui à 08:37
J’allais plutôt lire la théorie de rti 🤡
Luca.gg — Aujourd’hui à 08:42
pas con aussi 😅
mikail.ctdg — Aujourd’hui à 08:42
Vu que j’avais rater le dernier cours
Luca.gg — Aujourd’hui à 08:43
oui je me souviens
mikail.ctdg — Aujourd’hui à 09:18
Mec
Luca.gg — Aujourd’hui à 09:18
ouais ?
mikail.ctdg — Aujourd’hui à 09:19
C’est pas du tout comme les sujets
Image
Tu me donnes tous les livres la
Je pensais recevoir un id et le nom
Luca.gg — Aujourd’hui à 09:19
encore
Luca.gg — Aujourd’hui à 09:20
non tu vas recevoir id + nom de famille + prénom
j’ai sûrement mis la table books au lieu de authors
dés que je suis dans le train je look, dans 12 min
mikail.ctdg — Aujourd’hui à 09:22
Okk
mikail.ctdg — Aujourd’hui à 09:31
Ajout d’un livre
Client
Luca.gg — Aujourd’hui à 09:32
?
mikail.ctdg — Aujourd’hui à 09:33
Image
Cote serveur
Image
Image
Luca.gg — Aujourd’hui à 09:35
dans Encodage_S, il faut mettre authors au lieu de books
Image
mikail.ctdg — Aujourd’hui à 09:35
Luca.gg — Aujourd’hui à 09:35
et ton erreur pq il n’y a pas de nom et prénom de l’auteur ?
mikail.ctdg — Aujourd’hui à 09:36
Jsp on verra
Luca.gg — Aujourd’hui à 09:37
j’ai fait aucune modification, j’attends que tu fasses un git push
mikail.ctdg — Aujourd’hui à 09:41
Je suis à la gare
Et j’ai pas hit push
Luca.gg — Aujourd’hui à 09:41
tkt on fera à midi
mikail.ctdg — Aujourd’hui à 09:41
Okk
Luca.gg — Aujourd’hui à 09:43
comme un con j’ai voulu tester un truc, pour pas changer mon code et que ça fasse de la merde sur github, je me suis dit que j’allais copier le dossier
je l’ai collé à la racine RTI et du coup ça a tout écrasé sans rien me demander (donc les test que j’ai fait dans un dossier à part sont partis)
mes tests persos que j’ai pas sur github
mikail.ctdg — Aujourd’hui à 09:48
👴
Tu vas me rendre chauve
mikail.ctdg — Aujourd’hui à 19:45
Chaud dans 5 min
Luca.gg — Aujourd’hui à 19:46
parfait pour moi
mikail.ctdg — Aujourd’hui à 19:46
Vdd on a labo rti jsp si on peut présenter
Luca.gg — Aujourd’hui à 19:50
possible
je sais pas
appel ?
mikail.ctdg — Aujourd’hui à 19:51
Je redémarre mon pc
Problème de réseau
Luca.gg — Aujourd’hui à 19:51
tkt
mikail.ctdg — Aujourd’hui à 19:52
Et aussi faut que j’envoie un mail à mon entreprise comme quoi j’accepte ça prend 2 sec
Luca.gg — Aujourd’hui à 19:52
oui pas trop tard
mikail.ctdg
 a commencé un appel.
 — Aujourd’hui à 19:53
Luca.gg — Aujourd’hui à 20:55
bdhelper.cpp
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <string>
Afficher plus
message.txt
5 Ko
encodage_s.cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>
Afficher plus
message.txt
14 Ko
﻿
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <string>
#include <mysql.h>
#include <vector>
#include <sstream>
#include <mysql/mysql.h>

#include "BDHelper.h"

using namespace std;

MYSQL* connexion;

int ConnexionBD()
{
    connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,"localhost", "Student", "PassStudent1_", "PourStudent", 0, NULL, 0) == NULL)
    {
        return -1;
    }
    return 1;
}


int DeconnexionBD() {
    if(connexion != NULL) {
        mysql_close(connexion);
        return 1;
    }
    return -1;
}


int InsererTuple(const char* table, const char* colonnes, const char* valeurs) 
{
    int longeur = strlen(table) + strlen(colonnes) + strlen(valeurs) + 30;
    char* requete = (char*)malloc(longeur * sizeof(char));

    sprintf(requete, "INSERT INTO %s (%s) VALUES (%s)", table, colonnes, valeurs);

    if (mysql_query(connexion,requete) != 0) {
        free(requete);
        return -1;
    }
    free(requete);
    return 1;       // requête exécutée avec succès
}


int ModifierTuple(char* table, char* modifications, char* condition) 
{
    int longeur = strlen(table) + strlen(modifications) + strlen(condition) + 30;
    char* requete = (char*)malloc(longeur * sizeof(char));

    sprintf(requete, "UPDATE %s SET %s WHERE %s", table, modifications, condition);

    if (mysql_query(connexion, requete)) {
        free(requete);
        return -1;
    }
    free(requete);
    return 1;       // requête exécutée avec succès
}


int SupprimerTuple(char* table, char* condition)
{
    int longeur = strlen(table) + strlen(condition) + 30;
    char* requete = (char*)malloc(longeur * sizeof(char));

    sprintf(requete, "DELETE FROM %s WHERE %s", table, condition);

    if (mysql_query(connexion,requete) != 0) {
        free(requete);
        return -1;
    }
    free(requete);
    return 1;       // requête exécutée avec succès
}



vector<string> GetAllFromTable(const char* table) 
{
    vector<string> tuples;
    int longeur = strlen(table) + 30;
    char* requete = (char*)malloc(longeur * sizeof(char));

    sprintf(requete, "SELECT * FROM %s", table);

    if (mysql_query(connexion,requete) != 0) {
        free(requete);
        return vector<string>{};
    }


    // Affichage du Result Set
    MYSQL_RES *ResultSet;
    
    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        return vector<string>{};
    }
    
    MYSQL_ROW ligne;
    int nbChamps = mysql_num_fields(ResultSet);
    


    while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
    {
        for(int i=0 ; i < nbChamps ; i++) {
            tuples.push_back(ligne[i]);
        }
    }

    return tuples;
}

vector<string> GetTuple(const char* table, const char* condition)
{
    int longueur = strlen(table) + strlen(condition) + 40;  // Espace supp pour "SELECT * FROM et WHERE"
    char* requete = (char*)malloc(longueur * sizeof(char));


    sprintf(requete, "SELECT * FROM %s WHERE %s", table, condition);

    printf("-----GetTuple, requete = %s\n", requete);////////////////////////////////


    if (connexion == NULL) {
        printf("Erreur : connexion MySQL non initialisée.\n");
printf("\n-----GetTuple #### 1 ####\n\n");/////////////////////////////////////////////////
        return {};
    }


    if (requete == NULL) {
        printf("Erreur : allocation mémoire échouée.\n");
printf("\n-----GetTuple #### 2 ####\n\n");/////////////////////////////////////////////////
        return {};
    }


    if (mysql_query(connexion, requete) != 0) {
        free(requete);
printf("\n-----GetTuple #### 3 ####\n\n");/////////////////////////////////////////////////
printf("Erreur MySQL : %s\n", mysql_error(connexion));
        return {};
    }
    free(requete);

    MYSQL_RES* ResultSet = mysql_store_result(connexion);
    if (ResultSet == NULL) {
    printf("\n-----GetTuple #### 4 ####\n\n");/////////////////////////////////////////////////
        return {};
    }
    printf("\n-----GetTuple #### 5 ####\n\n");/////////////////////////////////////////////////
    // Traitement des résultats
    vector<string> tuples;
    MYSQL_ROW ligne;
    int nbChamps = mysql_num_fields(ResultSet);

    while ((ligne = mysql_fetch_row(ResultSet)) != NULL) {
        for (int i = 0; i < nbChamps; ++i) {
            if (ligne[i] != NULL) {
                tuples.push_back(string(ligne[i]));
            } else {
                tuples.push_back("NULL");
            }
        }
    }

    mysql_free_result(ResultSet);


     for (const string& tuple : tuples) {
        cout << tuple << endl;
    }

    return tuples;
}