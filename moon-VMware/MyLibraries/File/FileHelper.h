#ifndef FILEHELPER_H 
#define FILEHELPER_H

#include <string>

using namespace std;

int GetSettingInt(string nomFichier, string nomParam);
string GetSettingString(string nomFichier, string nomParam);

#endif