cmake_minimum_required(VERSION 3.10)

# Nom du projet
project(ProjetRTI)

# Trouver les paquets MySQL
set(CMAKE_PREFIX_PATH /usr/lib/x86_64-linux-gnu/cmake/mysql)  # Ajuste ce chemin si nécessaire

# Spécifier le standard C++
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Ajouter le répertoire contenant libmysqlclient
link_directories(/usr/lib64/mysql)

# Trouver les paquets Qt (Widgets pour les interfaces graphiques)
find_package(Qt5 COMPONENTS Widgets REQUIRED)

# Inclure les répertoires où se trouvent les headers
include_directories(
    MyLibraries/BD
    MyLibraries/Protocole
    MyLibraries/Socket
    MyLibraries/File
    Encodage_S
    /usr/include/mysql  # Chemin vers mysql.h
)

# Fichiers sources du serveur
set(SERVER_SOURCES
    MyLibraries/Socket/TCP.cpp
    MyLibraries/Protocole/protocole.cpp   # Ajout du fichier protocol pour le serveur
    MyLibraries/BD/BDHelper.cpp           # Ajout du fichier BDHelper pour la base de données
    Encodage_S/Encodage_S.cpp
    MyLibraries/Protocole/protocoleS.cpp
    MyLibraries/File/FileHelper.cpp
)

# Fichiers sources du client
set(CLIENT_SOURCES
    MyLibraries/Socket/TCP.cpp
    MyLibraries/Protocole/protocole.cpp   # Déjà inclus pour le client
    MyLibraries/BD/BDHelper.cpp           # Déjà inclus pour le client
    ClientBookEncoderQt/main.cpp
    ClientBookEncoderQt/mainwindowclientbookencoder.cpp
    MyLibraries/Protocole/protocoleC.cpp
    MyLibraries/File/FileHelper.cpp
)

# Fichiers headers devant être traités par MOC (Meta-Object Compiler de Qt) pour le client
set(CLIENT_HEADERS
    ClientBookEncoderQt/mainwindowclientbookencoder.h
)

# Gérer l'exécution du MOC pour les fichiers header (seulement pour le client)
qt5_wrap_cpp(CLIENT_MOC_SRCS ${CLIENT_HEADERS})

# Créer l'exécutable du serveur
add_executable(ServerApp
    ${SERVER_SOURCES}
)

# Lier la bibliothèque pthread et MySQL pour le serveur
target_link_libraries(ServerApp PRIVATE pthread mysqlclient)

# Créer l'exécutable du client et ajouter les fichiers sources, MOC générés
add_executable(ClientApp
    ${CLIENT_SOURCES}
    ${CLIENT_MOC_SRCS}         # Fichiers générés par MOC (seulement pour le client)
)

# Lier les bibliothèques Qt5::Widgets, pthread et MySQL pour le client
target_link_libraries(ClientApp PRIVATE Qt5::Widgets pthread mysqlclient)