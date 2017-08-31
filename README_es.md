[![Build Status](https://travis-ci.org/IAAA-Lab/pid-ms.png)](https://travis-ci.org/IAAA-Lab/pid-ms)

# INSPIRE PID Management System

Esta aplicación ha sido generada usando JHipster 4.7.0, puede encontrar documentación y ayuda en [https://jhipster.github.io/documentation-archive/v4.7.0](https://jhipster.github.io/documentation-archive/v4.7.0).


## Desarrollo

Antes de pode compilar este proyecto, debe instalar y configurar las siguiente dependencias en su máquina:

1. [Node.js][]: Se usa Node para ejecutar un servidor web de desarrollo y compilar el proyecto.
	Dependiento de su sistema, usted puede instalar Node a partir de los fuentes o una distribución pre-enpaquetada.
2. [Yarn][]: Se usa Yarn para gestionar las dependencias de Node.
	Dependiento de su sistema, usted puede instalar Yarn a partir de los fuentes o una distribución pre-enpaquetada.

Después de instalar Node, debería ser capaz de ejecutar el siguiente comando para instalar las herramientas de desarrollo.
Solamente necesitará ejecutar este comando cuando las dependencias cambien en [package.json](package.json).

    yarn install

Se usa [Gulp][] como sistema de compilación. Instale la herramienta Gulp command-line globalmente con: 

    yarn global add gulp-cli

Ejecute los siguientes comandos en dos terminales separados para crear una esperiencia de desarrollo donde su navegador autorecargue cuando los ficheros cambien en su disco duro.

    ./mvnw
    gulp

[Bower][] se usa para gestionar las dependencias CSS y JavaScript en esta aplicación. Puede actualizar las dependencias especificando la nueva versión en [bower.json](bower.json). Puede también ejecutar `bower update` y `bower install` para gestionar dependencias.

Añada el flag `-h` en cualquier comando para ver como puede usarlo. Por ejemplo, `bower update -h`.

Para obtener más instrucciones sobre como desarrollar con JHipster, eche un vistazo a [Using JHipster in development][].


## Compilando para producción

Para optimizar la aplicación pidms para producción, ejecute:

    ./mvnw -Pprod clean package

Este concatenará y minificará los ficheros CSS y JavaScript. También modificará `index.html`referenciando a los nuevos ficheros.
Para asegurar que todo funciona, ejecute:

    java -jar target/*.war

Entonces navegue a [http://localhost:8080](http://localhost:8080) en su navegador.

Diríjase a [Using JHipster in production][] para más detalles.

## Testing

Para lanzar los tests de su aplicación, ejecute

    ./mvnw clean test

### Tests de clientes

Los test unitarios son ejecutados por [Karma][] y escritos con [Jasmine][]. Estan localizados en [src/test/javascript/](src/test/javascript/) y pueden ser ejecutados con:

    gulp test

Para más información, diríjase a [Running tests page][].

## Usando docker para simplificar el desarrollo (opcional)
Puede usar Docker para mejorar su experiencia de desarrollo con JHipster. Un gran número de configuraciones docker-compose estan disponibles en el directorio [src/main/docker](src/main/docker) para lanzar servicios de terceros.
Por ejemplo, para inicilaizar una base de datos postgresql en un contenedor de docker, ejecute:

    docker-compose -f src/main/docker/postgresql.yml up -d

Para pararla y borrar el contenedor, ejecute:

    docker-compose -f src/main/docker/postgresql.yml down

También puede dockerizar completamente su aplicación y todos los servicios que dependan de ella.
Para lograr esto, primero compile una imagen de docker de su aplicación ejecutando:

    ./mvnw package -Pprod docker:build

Entonces ejecute:

    docker-compose -f src/main/docker/app.yml up -d

Para más información diríjase a [Using Docker and Docker-Compose][], esta página también contiene información sobre el sub-generador de docker-compose (`jhipster docker-compose`), que está disponible para generar configuraciones de docker para una o muchas JHipster aplicaciones.

## Integración contínua (opcional)

Para configurar CI para su proyecto, ejecute el sub-generador ci-cd (`jhipster ci-cd`), este le permitirá generar los ficheros de configuración para un gran número de sistemas de integración contínua. Consulte la página [Setting up Continuous Integration][] para más información.

[JHipster Homepage and latest documentation]: https://jhipster.github.io
[JHipster 4.7.0 archive]: https://jhipster.github.io/documentation-archive/v4.7.0

[Using JHipster in development]: https://jhipster.github.io/documentation-archive/v4.7.0/development/
[Service Discovery and Configuration with the JHipster-Registry]: https://jhipster.github.io/documentation-archive/v4.7.0/microservices-architecture/#jhipster-registry
[Using Docker and Docker-Compose]: https://jhipster.github.io/documentation-archive/v4.7.0/docker-compose
[Using JHipster in production]: https://jhipster.github.io/documentation-archive/v4.7.0/production/
[Running tests page]: https://jhipster.github.io/documentation-archive/v4.7.0/running-tests/
[Setting up Continuous Integration]: https://jhipster.github.io/documentation-archive/v4.7.0/setting-up-ci/


[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/
