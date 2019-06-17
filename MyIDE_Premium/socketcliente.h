#ifndef SOCKETCLIENTE_H
#define SOCKETCLIENTE_H
#include <sys/socket.h>
#include <sys/types.h>
#include <netdb.h>
#include <string.h>
#include <string>
#include <iostream>
#include <pthread.h>
#include <unistd.h>
#include <QObject>
#include <arpa/inet.h>
#include <imagenid.h>

using namespace std;
/**
 * @brief The SocketCliente class Permite establecer una conexion con un servidor.
 */
class SocketCliente: public QObject
{
    Q_OBJECT
public:
    /**
     * @brief SocketCliente Constructor de la clase SocketCliente.
     */
    SocketCliente();
    /**
     * @brief connectar Intenta conectarse al servidor y notifica el estado.
     * @return Un estado de conexion.
     */
    bool connectar();
    /**
     * @brief setMensaje Permite enviar un mensaje al servidor,
     * @param msn El mensaje que se desea enviar.
     */
    void setMensaje(const char *msn);
    /**
     * @brief getConectado Permite saber el estado actual de la conexion.
     * @return Un estado booleano.
     */
    bool getConectado() const;
    /**
     * @brief setConectado Permite modificar el estado de la conexion actual.
     * @param value El nuevo estado de la conexion.
     */
    void setConectado(bool value);
    /**
     * @brief galerias Una lista con el nombre de todas las galerias creadas.
     */
    QList<QString> galerias;
    /**
     * @brief listaImagenes Una lista con todos los identificadores de imagenes.
     */
    QList<ImagenID*> listaImagenes;

private:
    /**
     * @brief descriptor Identificador del servidor.
     */
    int descriptor;
    /**
     * @brief info Informacion.
     */
    sockaddr_in info;
    /**
     * @brief controlador Controla la comunicacion con el servidor una vez se haya conectado.
     * @param obj objeto puntero.
     * @return Un puntero.
     */
    static void * controlador(void *obj);
    /**
     * @brief conectado Bandera de estado de conexion.
     */
    bool conectado;

signals:
    /**
     * @brief NewMensaje Notifica si hay un nuevo mensaje.
     * @param msn El mensaje recibido,
     */
    void NewMensaje(QString msn);
};
#endif // SOCKETCLIENTE_H
