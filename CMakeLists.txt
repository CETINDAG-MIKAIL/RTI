cmake_minimum_required(VERSION 3.10)

# Nom du projet
project(ProjetRTI)

# Spécifier le standard C++
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Trouver les paquets Qt (Widgets pour les interfaces graphiques)
find_package(Qt5 COMPONENTS Widgets REQUIRED)

include_directories(MyLibraries/BD MyLibraries/Protocole MyLibraries/Socket)

# Fichiers sources du serveur
set(SERVER_SOURCES
    MyLibraries/Socket/TCP.cpp
    Serveurs/Encodage_S/Encodage_S.cpp
)

# Fichiers sources du client
set(CLIENT_SOURCES
    MyLibraries/Socket/TCP.cpp
    MyLibraries/Protocole/protocole.cpp
    MyLibraries/BD/BDHelper.cpp   
    Clients/Encodage_C/Encodage_C.cpp
    ClientBookEncoderQt/main.cpp
    ClientBookEncoderQt/mainwindowclientbookencoder.cpp
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

# Lier la bibliothèque pthread pour le serveur
target_link_libraries(ServerApp pthread)

# Créer l'exécutable du client et ajouter les fichiers sources, MOC générés
add_executable(ClientApp
    ${CLIENT_SOURCES}
    ${CLIENT_MOC_SRCS}         # Fichiers générés par MOC (seulement pour le client)
)

# Lier Qt5::Widgets et pthread au client
target_link_libraries(ClientApp Qt5::Widgets pthread)
