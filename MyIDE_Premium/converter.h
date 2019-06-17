#ifndef CONVERTER_H
#define CONVERTER_H

#include <include/rapidjson/document.h>
#include <QString>


using namespace rapidjson;
using namespace std;
/**
 * @brief The Converter class permite generar e interpretar JSON mediante el uso de la librería rapijson.
 */
class Converter {
private:
    /**
     * @brief instance Unica instancia de la clase Converter.
     */
    static Converter* instance;
    /**
     * @brief Converter Constructor de la clase Converter.
     */
    Converter();
public:
    /**
     * @brief getInstance Devuelve la isntancia de la clase Converter.
     * @return  La instancia de la clase Converter.
     */
    static Converter* getInstance();
    /**
     * @brief BytesToJson Convierte una lista de bytes, nombre de la imagen y el nombre de la
     * galeria destino a formato JSON.
     * @param lista La lista que contiene los bytes de la imagen.
     * @param nombre El nombre de la imagen.
     * @param galeria El nombre de la galeria destino.
     * @return un string JSON.
     */
    string BytesToJson(QList<int> lista,QString nombre,QString galeria);
    /**
     * @brief Scripts Redacta un string en formato JSON de las lineas escritas
     * en el editor de codigo de metadata.
     * @param lineas Una lista de lineas.
     * @return un string JSON.
     */
    string Scripts(QStringList lineas);
    /**
     * @brief NombreGaleria Redacta un string en formato JSON que contiene el
     * nombre de una galeria.
     * @param nombre El nombre de la galeria.
     * @return un string JSON.
     */
    string NombreGaleria(QString nombre);
    /**
     * @brief Commit Redacta un string en formato JSON que contiene la palabra
     * commit para indicarle al servidor la instruccion.
     * @return un string JSON.
     */
    string Commit();
    /**
     * @brief Rollback Redacta un string en formato JSON que contiene la palabra
     * rollback para indicarle al servidor la instruccion.
     * @return un string JSON.
     */
    string Rollback();
    /**
     * @brief EliminarDiscos Redacta un string en formato JSON que contiene
     * el numero del disco que se desea eliminar.
     * @param disco Disco a eliminar.
     * @return un string JSON.
     */
    string EliminarDiscos(int disco);
    /**
     * @brief funcionGeneral Se encarga de interpretar todos los mensajes que provienen del
     * servidor para luego redirigirlos.
     */
    void funcionGeneral(const char*);
    /**
     * @brief m Va a contener la columna seleccionada.
     */
    QString m;
    /**
     * @brief m2 Va a contener el nombre de la imagen que se desea mostrar.
     */
    QString m2;
};

#endif // CONVERTER_H
