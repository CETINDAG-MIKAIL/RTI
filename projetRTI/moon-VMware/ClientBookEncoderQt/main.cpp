#include "mainwindowclientbookencoder.h"
#include <signal.h> 
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <iostream>
#include <string>

using namespace std;

#include "TCP.h"

#include <QApplication>

void HandlerSIGINT(int);

int main(int argc, char *argv[])
{
    char ipServeur[30] = {"192.168.216.128"};

    QApplication a(argc, argv);
    MainWindowClientBookEncoder w;
    w.show();


    // Armement des signaux 
    struct sigaction A; 
    A.sa_flags = 0; 
    sigemptyset(&A.sa_mask); 
    A.sa_handler = HandlerSIGINT; 
    if (sigaction(SIGINT, &A, NULL) == -1) 
    { 
        perror("Erreur de sigaction"); 
        exit(1); 
    } 


    return a.exec();
}


void HandlerSIGINT(int s) 
{ 
    printf("\nArret du client.\n"); 

    exit(0); 
}
