#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <pthread.h>
#include <iostream>
#include <string>
#include <vector>
#include <fstream>

#include "FileHelper.h"

using namespace std;

int GetSettingInt(string nomFichier, string nomParam)
{
	int setting;
    string line;
	ifstream configFile(nomFichier);
	if (configFile.is_open()) {

         while (getline(configFile, line)) {
            if (line.find(nomParam + "=") == 0) {
                printf("-----**************$ nomParam.length() = %d\n\n", nomParam.length());
                cout << "))))))))" <<  line.substr(nomParam.length()+1).c_str() << endl;

                setting = atoi(line.substr(nomParam.length()+1).c_str());      // Extraire la valeur du paramètre
                printf("-----**************$ setting = %d\n\n", setting);
            } 
        }

        configFile.close();

    } else {
        perror("Impossible d'ouvrir le fichier.");
        return -1;
    }


    return setting;
}

string GetSettingString(string nomFichier, string nomParam)
{
	string setting;
    string line;
	ifstream configFile(nomFichier);

	if (configFile.is_open()) {

        while (getline(configFile, line)) {

            if (line.find(nomParam + "=") == 0) {
                setting = line.substr(nomParam.length());      // Extraire la valeur du paramètre
            }
        }

        configFile.close();

    } else {
        perror("Impossible d'ouvrir le fichier.");
        return "";
    }

    return setting;
}