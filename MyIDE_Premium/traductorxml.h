#ifndef TRADUCTORXML_H
#define TRADUCTORXML_H

#include <QtXml>

class TraductorXML
{
public:
    TraductorXML();
    QDomDocument documento;
    QDomElement raiz;

    void EscrbirImagenXML(QString nombre,QList<int> byteArray);
    void LeerDatosXML();

};

#endif // TRADUCTORXML_H
