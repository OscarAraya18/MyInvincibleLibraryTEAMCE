#include "gerentera.h"

GerenteRA::GerenteRA(QObject *parent) : QObject(parent)
{
    connect(manager,SIGNAL(finished(QNetworkReply*)),this,SLOT(readRead(QNetworkReply*)));

}

void GerenteRA::makeRequest(QString endpointRequest)
{
    manager->get(QNetworkRequest(QUrl(endpointRequest)));

}

void GerenteRA::makePost(QString endpointrequest, QString dataToSend)
{

    manager->post(QNetworkRequest(QUrl(endpointrequest)),dataToSend.toUtf8());

}

void GerenteRA::readRead(QNetworkReply *reply)
{
    QByteArray myData;
    myData = reply->readAll();
    qDebug()<< myData;
    emit(dataReadyRead(myData));

}
