#language: es
Característica: Los metadatos de un PID deben tener un PID propio y diferente que utiliza como
  base pid.inspire.gob.es. Además, el PID del recurso se debe poder resolver al metadato del PID
  mediante negociación de contenidos utilizando el tipo MIME application/vnd.inspire.pid

  # Escenarios para resolver PID de conjuntos de datos a su metadato

  Escenario: un cliente resuelve un PID activo de un conjunto de datos o una serie a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/dataset"
    Cuando un cliente resuelve "/catalogo/dataset" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"
    Y la cabecera "Location" contiene "http://pid.inspire.gob.es/catalogo/dataset"

  Escenario: un cliente resuelve un PID dado de baja de un conjunto de datos o una serie a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/catalogo/dataset" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"
    Y la cabecera "Location" contiene "http://pid.inspire.gob.es/catalogo/dataset"

  Escenario: un cliente resuelve un PID que no existe de un conjunto de datos o una serie a su metadato
    Dado un repositorio de PID
    Y que no contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Cuando un cliente resuelve "/catalogo/dataset" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 404

  # Escenarios para asegurar que el comportamiento normal de la resolución de PID a conjuntos de datos se ve afectado

  Escenario: un cliente resuelve un PID activo de un conjunto de datos o una serie
    Dado un repositorio de PID
    Y que contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/dataset"
    Cuando un cliente resuelve "/catalogo/dataset" sin cabecera "Accept"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"

  Escenario: un cliente resuelve un PID dado de baja de un conjunto de datos o una serie
    Dado un repositorio de PID
    Y que contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/catalogo/dataset" sin cabecera "Accept"
    Entonces el cliente recibe una respuesta con el estado 410
    Y la cabecera "Vary" contiene "Accept"

  # Escenarios para resolver PID de objetos espaciales no versionados a a su metadato

  Escenario: un cliente resuelve un PID activo de un objeto espacial no versionado a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/so"
    Cuando un cliente resuelve "/recurso/ns/id" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"
    Y la cabecera "Location" contiene "http://pid.inspire.gob.es/recurso/ns/id"

  Escenario: un cliente resuelve un PID dado de baja de objeto espacial no versionado a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/recurso/ns/id" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"
    Y la cabecera "Location" contiene "http://pid.inspire.gob.es/recurso/ns/id"

  Escenario: un cliente resuelve un PID que no existe de un objeto espacial no versionado a su metadato
    Dado un repositorio de PID
    Y que no contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Cuando un cliente resuelve "/recurso/ns/id" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 404

  # Escenarios para asegurar que el comportamiento normal de la resolución de PID de objetos espaciales no versionados se ve afectado

  Escenario: un cliente resuelve un PID activo de un objeto espacial no versionado
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/so"
    Cuando un cliente resuelve "/recurso/ns/id" sin cabecera "Accept"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"

  Escenario: un cliente resuelve un PID dado de baja de objeto espacial no versionado
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns" y "localId" "id"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/recurso/ns/id" sin cabecera "Accept"
    Entonces el cliente recibe una respuesta con el estado 410
    Y la cabecera "Vary" contiene "Accept"

  # Escenarios para resolver PID de objetos espaciales versionados a a su metadato

  Escenario: un cliente resuelve un PID activo de un objeto espacial versionado a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/so/v"
    Cuando un cliente resuelve "/recurso/ns/id/ver" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"
    Y la cabecera "Location" contiene "http://pid.inspire.gob.es/recurso/ns/id/ver"

  Escenario: un cliente resuelve un PID dado de baja de objeto espacial versionado a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/recurso/ns/id/ver" con cabecera "Accept" "application/vnd.inspire.pid"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"
    Y la cabecera "Location" contiene "http://pid.inspire.gob.es/recurso/ns/id/ver"

  Escenario: un cliente resuelve un PID que no existe de un objeto espacial no versionado a su metadato
    Dado un repositorio de PID
    Y que no contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Cuando un cliente resuelve "/recurso/ns/id/ver"
    Entonces el cliente recibe una respuesta con el estado 404

  # Escenarios para asegurar que el comportamiento normal de la resolución de PID de objetos espaciales versionados se ve afectado

  Escenario: un cliente resuelve un PID activo de un objeto espacial versionado a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/so/v"
    Cuando un cliente resuelve "/recurso/ns/id/ver" sin cabecera "Accept"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Vary" contiene "Accept"

  Escenario: un cliente resuelve un PID dado de baja de objeto espacial versionado a su metadato
    Dado un repositorio de PID
    Y que contiene el PID de un objeto espacial con "namespace" "ns", "localId" "id" y "versionId" "ver"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/recurso/ns/id/ver" sin cabecera "Accept"
    Entonces el cliente recibe una respuesta con el estado 410
    Y la cabecera "Vary" contiene "Accept"
