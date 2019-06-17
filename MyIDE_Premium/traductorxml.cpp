#include "traductorxml.h"
#include <QtXml>
#include <QDebug>

TraductorXML::TraductorXML()
{
    raiz = documento.createElement("Molde");
    documento.appendChild(raiz);



}

void TraductorXML::EscrbirImagenXML(QString nombre, QList<int> byteArray)
{
    QDomElement imagen = documento.createElement("Imagen");
    imagen.setAttribute("Nombre", nombre);
    raiz.appendChild(imagen);
    //agregar los elementos de la lista al XML
    for(int b = 0; b < byteArray.size(); b++){
        QDomElement byte = documento.createElement("BYTE");
        byte.setAttribute("Numero",QString::number(byteArray.at(b)));
        imagen.appendChild(byte);
    }
    QString info = documento.toString();
    qDebug() <<info;

    //escribirlo en un archivo.xml
    QFile file("/home/kevin/QtProyects/MyXML.xml");
    if(!file.open(QIODevice::WriteOnly | QIODevice::Text)){
        qDebug()<<"Error al escribir en el archivo";

    }
    else{
        QTextStream stream(&file);
        stream << documento.toString();
        file.close();
        qDebug() <<"Terminado";
    }


}

void TraductorXML::LeerDatosXML()
{
    QFile file(("/home/kevin/QtProyects/MyXML.xml"));
    if(!file.open(QIODevice::ReadOnly | QIODevice::Text)){
        qDebug()<<"Error al leer el archivo";
    }
    else{
        if(!documento.setContent(&file)){
            qDebug()<<"Error al cargar el documento";
        }
        file.close();

    }
    //conseguir el elemento de la raiz
    QDomElement raiz = documento.firstChildElement();

    //conseguir el elemento imagen
    QDomNodeList imagen = raiz.elementsByTagName("Imagen");

    qDebug()<<"Total de elementos"<< imagen.count();
    for(int i = 0; i<imagen.count(); i++){
        QDomNode listaNodo = imagen.at(i);

        //convertir a elementos
        if(listaNodo.isElement()){
            QDomElement elemento = listaNodo.toElement();
            qDebug()<<elemento.attribute("Nombre");

            elemento = documento.firstChildElement();
            QDomNodeList BYTE = elemento.elementsByTagName("BYTE");
            qDebug()<<"Total de bytes"<<BYTE.count();
            for(int n = 0; n <BYTE.size();n++){
                QDomNode listaBytes = BYTE.at(n);
                //convertir a elementos
                if(listaBytes.isElement()){
                    QDomElement byteElemento = listaBytes.toElement();
                    qDebug()<<byteElemento.attribute("Numero");
                }
            }
        }
    }




}
