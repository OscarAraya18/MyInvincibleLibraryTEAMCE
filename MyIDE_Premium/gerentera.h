#ifndef GERENTERA_H
#define GERENTERA_H

#include <QObject>
#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkReply>

class GerenteRA : public QObject
{
    Q_OBJECT
public:
    explicit GerenteRA(QObject *parent = nullptr);
    void makeRequest(QString endpointRequest);
    void makePost(QString endpointrequest, QString dataToSend);

signals:
    void dataReadyRead(QByteArray);

public slots:
    void readRead(QNetworkReply *reply);

 private:
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

};

#endif // GERENTERA_H
