#include "socketcliente.h"
#include <QDebug>

SocketCliente::SocketCliente()
{
    setConectado(false);
}

bool SocketCliente::connectar()
{
    descriptor = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);
    if(descriptor < 0)
        return false;
    info.sin_family = AF_INET;
    info.sin_addr.s_addr = inet_addr("192.168.43.120");
    info.sin_port = ntohs(5050);
    memset(&info.sin_zero,0,sizeof(info.sin_zero));

    if((::connect(descriptor,(sockaddr*)&info,(socklen_t)sizeof(info))) < 0)
     return false;
    pthread_t hilo;
    pthread_create(&hilo,0,SocketCliente::controlador,(void *)this);
    pthread_detach(hilo);
    //si la conexion es exitosa se activa la bandera
    setConectado(true);
    return true;
}


void * SocketCliente::controlador(void *obj)
{
    SocketCliente *padre = (SocketCliente*)obj;

    while (true) {
        string mensaje;
        while (1) {
            char buffer[1024] = {0};
            int bytes = recv(padre->descriptor,buffer,1024,0);
            mensaje.append(buffer,bytes);
            if(bytes <= 0)
            {
                close(padre->descriptor);
                pthread_exit(NULL);

            }
            if(bytes < 1024)
                break;
        }
        QString m = QString::fromStdString(mensaje);

        emit padre->NewMensaje(QString::fromStdString(mensaje));
    }
    close(padre->descriptor);
    pthread_exit(NULL);
}

bool SocketCliente::getConectado() const
{
    return conectado;
}

void SocketCliente::setConectado(bool value)
{
    conectado = value;
}


void SocketCliente::setMensaje(const char *msn)
{
    cout << "bytes enviados "<< send(descriptor,msn,strlen(msn),0) << endl;
}

