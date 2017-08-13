#language: es
Característica: A partir del Reglamento 1205/2008, que indica que las series y conjuntos de datos
  espaciales se identifican por los atributos "namespace" que tiene forma de URI y "code", y del
  esquema URI definido en la NTI-RISP que define el espacio "catálogo" para los conjuntos de
  datos reutilizables el PID interno de un conjunto de datos o de una serie debe ser {"namespace":
  "catalogo" [fijo], "code": valor}

  Escenario: un cliente resuelve un PID activo de un conjunto de datos o una serie
    Dado un repositorio de PID
    Y que contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Y que dicho PID está activo
    Y se resuelve a "http://example.com/remote/dataset"
    Cuando un cliente resuelve "/catalogo/dataset"
    Entonces el cliente recibe una respuesta con el estado 307
    Y la cabecera "Location" contiene "http://example.com/remote/dataset"

  Escenario: un cliente resuelve un PID dado de baja de un conjunto de datos o una serie
    Dado un repositorio de PID
    Y que contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Y que dicho PID no está activo
    Cuando un cliente resuelve "/catalogo/dataset"
    Entonces el cliente recibe una respuesta con el estado 410

  Escenario: un cliente resuelve un PID que no existe de un conjunto de datos o una serie
    Dado un repositorio de PID
    Y que no contiene el PID de un conjunto de datos o una serie con "code" "dataset"
    Cuando un cliente resuelve "/catalogo/dataset"
    Entonces el cliente recibe una respuesta con el estado 404

