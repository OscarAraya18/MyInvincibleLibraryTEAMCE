#include "ventanaprincipal.h"
#include <QApplication>
#include <QIcon>
#include <QDebug>
#include "socketcliente.h"

SocketCliente *sock;
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    QPixmap pix(":/recursos/imagenes/L.ico");
    QIcon icono;
    icono.addPixmap(pix);
    a.setWindowIcon(icono);
    sock = new SocketCliente();
    //ciclo hasta que se conecte al servidor
    while(sock->getConectado() == false){
           if(sock->connectar()){
               qDebug()<<"conectado";
               sock->setConectado(true);
           }
    }
    VentanaPrincipal w;
    w.show();



    return a.exec();
}
