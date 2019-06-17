#include "converter.h"
#include <include/rapidjson/document.h>
#include <vector>
#include <include/rapidjson/fwd.h>
#include <include/rapidjson/stringbuffer.h>
#include <include/rapidjson/writer.h>
#include <string>
#include <string.h>
#include <QDebug>
#include <iostream>
#include <sys/socket.h>



using namespace std;
//using namespace rapidjson;


Converter* Converter::instance = 0;

Converter* Converter::getInstance()
{
    if (instance == 0)
    {
        instance = new Converter();
    }

    return instance;
}

string Converter::BytesToJson(QList<int> lista, QString nombre,QString galeria)
{
    StringBuffer a;
    Writer<StringBuffer> writer2(a);
    writer2.StartObject();
    writer2.Key("ID");
    writer2.Int(1);
    writer2.Key("Nombre");
    writer2.String(nombre.toStdString().c_str());
    writer2.Key("Galeria");
    writer2.String(galeria.toStdString().c_str());
    writer2.Key("Imagen");
    writer2.StartArray();

    for(int i=0; i < lista.size(); i++) {
        writer2.Int(lista[i]);
    }
    writer2.EndArray();
    writer2.EndObject();
    return a.GetString();
}

string Converter::Scripts(QStringList lineas)
{
    StringBuffer a;
    Writer<StringBuffer> writer2(a);
    writer2.StartObject();
    writer2.Key("ID");
    writer2.Int(2);
    writer2.Key("Scripts");
    writer2.StartArray();
    for(int i=0; i < lineas.size(); i++) {
        writer2.String(lineas[i].toStdString().c_str());
    }
    writer2.EndArray();
    writer2.EndObject();
    return a.GetString();
}

string Converter::NombreGaleria(QString nombre)
{
    StringBuffer s;
    Writer<StringBuffer> writer(s);
    writer.StartObject();
    writer.Key("ID");
    writer.Int(5);
    writer.Key("nombre");
    writer.String(nombre.toStdString().c_str());
    writer.EndObject();
    return s.GetString();
}

string Converter::Commit()
{
    StringBuffer s;
    Writer<StringBuffer> writer(s);
    writer.StartObject();
    writer.Key("ID");
    writer.Int(3);
    writer.EndObject();
    return s.GetString();

}

string Converter::Rollback()
{
    StringBuffer a;
    Writer<StringBuffer> writer(a);
    writer.StartObject();
    writer.Key("ID");
    writer.Int(4);
    writer.EndObject();
    return a.GetString();

}

string Converter::EliminarDiscos(int disco)
{
    StringBuffer a;
    Writer<StringBuffer> writer(a);
    writer.StartObject();
    writer.Key("ID");
    writer.Int(6);
    writer.Key("Disco");
    writer.Int(disco);
    writer.EndObject();
    return a.GetString();
}
Converter::Converter() {

}

void Converter:: funcionGeneral(const char* json){
    m = "";
    m2 = "";
    Document document;
    document.Parse(json);

    if(document.HasMember("autor")){
        qDebug()<<"leyendo el member autor";
        m = document["autor"].GetString();
        qDebug()<<m;
        m2 = document["nombre"].GetString();
        qDebug()<<m2;
    }
    else if(document.HasMember("tamano")){
        qDebug()<<"leyendo el member tamaño";
        m = document["tamano"].GetString();
        qDebug()<<m;
        m2 = document["nombre"].GetString();
        qDebug()<<m2;
    }
    else if(document.HasMember("descripcion")){
        qDebug()<<"leyendo el member descripción";
        m = document["descripcion"].GetString();
        qDebug()<<m;
        m2 = document["nombre"].GetString();
        qDebug()<<m2;
    }
    else if(document.HasMember("anio")){
        qDebug()<<"leyendo el member año";
        m = document["anio"].GetString();
        qDebug()<<m;
        m2 = document["nombre"].GetString();
        qDebug()<<m2;
    }
    else if(document.HasMember("nombre")){
        qDebug()<<"leyendo el member nombre";
        m = document["nombre"].GetString();
        qDebug()<<m;
        m2 = document["nombre"].GetString();
        qDebug()<<m2;
    }

}
