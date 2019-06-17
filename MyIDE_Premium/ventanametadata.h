#ifndef VENTANAMETADATA_H
#define VENTANAMETADATA_H


#include <QWidget>
#include "converter.h"
namespace Ui {
class ventanaMetadata;
}
/**
 * @brief The ventanaMetadata class Despliega una interfaz que permite escribir lineas de codigo para
 * trabajar con la metadata de las imagenes. Tambien permite eliminar discos, hacer commit y rollbac.
 */
class ventanaMetadata : public QWidget
{
    Q_OBJECT

public:
    /**
     * @brief ventanaMetadata Constructor de la clase.
     * @param parent Padre.
     */
    explicit ventanaMetadata(QWidget *parent = nullptr);
    ~ventanaMetadata();
    /**
     * @brief verificarSintaxis Valida si lo que se ha escrito esta en sintaxis SQL.
     * @return Un estado de aceptacion.
     */
    bool verificarSintaxis();
    /**
     * @brief generarImagen Pasa de un ByteAarray a una imagen.
     * @param ba Un byteArray.
     */
    void generarImagen(QByteArray ba);



private slots:
    /**
     * @brief on_volverButton_clicked Valida los eventos del boton volver.
     */
    void on_volverButton_clicked();
    /**
     * @brief on_ejecutarButton_clicked Envia las lineas de codigo al servidor.
     */
    void on_ejecutarButton_clicked();
    /**
     * @brief printMensaje Recibe los mensajes del servidor, los manda a una instancia de la clase
     * Converter y luego envia el mensaje a la consola.
     */
    void printMensaje(QString);
    /**
     * @brief on_commitButton_clicked Indica la instruccion de commit al servidor.
     */
    void on_commitButton_clicked();
    /**
     * @brief on_rollbackButton_clicked Indica la nstruccion de rollback al servidor.
     */
    void on_rollbackButton_clicked();
    /**
     * @brief on_discoButton_clicked Le envia un mensaje al servidor indicando el disco que se
     * desea eliminar.
     */
    void on_discoButton_clicked();

private:
    /**
     * @brief ui Instancia de la interfaz grafica.
     */
    Ui::ventanaMetadata *ui;
    /**
     * @brief lineas Lista de las lineas de codigo escritas.
     */
    QStringList lineas;
    /**
     * @brief converter Iinstancia de la clase Converter.
     */
    Converter *converter;

};

#endif // VENTANAMETADATA_H
