#language: es
Característica: El resolver puede funcionar como un reverse proxy

  Esquema del escenario: un cliente resuelve el PID un recurso como un reverse proxy
    Dado un repositorio con PID para pruebas con el modo reverse proxy activado
    Cuando un cliente resuelva "<PID>"
    Entonces recibirá una respuesta con estado 200
    Y cabecera "Content-Location" igual a "<REMOTO>"
    Y contenido igual al contenido de "<REMOTO>"
    Ejemplos:
    |PID                |REMOTO              |
    |/catalogo/dataset  |http://example.com/ |
    |/recurso/ns/id     |http://example.com/ |
    |/recurso/ns/id/ver |http://example.com/ |
