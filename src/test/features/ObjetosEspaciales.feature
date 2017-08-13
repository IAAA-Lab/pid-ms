#language: es
Característica: A partir del Reglamento 1089/201, que indica que los objetos espaciales se
  identifican por los atributos "namespace" que tiene forma de URI, "localId" y opcionalmente
  por un atributo "versionId" el PID interno de un conjunto de datos o de una serie debe ser
  {"namespace": valor, "localId": valor, "versionId": valor [opcional]}. El esquema URI definido
  en la NTI-RISP que define el espacio "recurso" como el carácter de este tipos de recurso.

  Escenario: un cliente resuelve un PID activo de un objeto espacial no versionado
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/so"
    Cuando un cliente resuelve "/recurso/ns/id"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Location" contiene "http://example.com/remote/so"

  Escenario: un cliente resuelve un PID dado de baja de objeto espacial no versionado
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/recurso/ns/id"
    Entonces el cliente recibe una respuesta con el estado 410

  Escenario: un cliente resuelve un PID que no existe de un objeto espacial no versionado
    Dado un repositorio de PID
    Y que no contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Cuando un cliente resuelve "/recurso/ns/id"
    Entonces el cliente recibe una respuesta con el estado 404

  Escenario: un cliente resuelve un PID activo de un objeto espacial versionado
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/so/v"
    Cuando un cliente resuelve "/recurso/ns/id/ver"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Location" contiene "http://example.com/remote/so/v"

  Escenario: un cliente resuelve un PID dado de baja de objeto espacial versionado
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/recurso/ns/id/ver"
    Entonces el cliente recibe una respuesta con el estado 410

  Escenario: un cliente resuelve un PID que no existe de un objeto espacial no versionado
    Dado un repositorio de PID
    Y que no contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Cuando un cliente resuelve "/recurso/ns/id/ver"
    Entonces el cliente recibe una respuesta con el estado 404
