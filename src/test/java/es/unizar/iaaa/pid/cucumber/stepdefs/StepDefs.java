package es.unizar.iaaa.pid.cucumber.stepdefs;

import cucumber.api.java.es.Cuando;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import es.unizar.iaaa.pid.PidmsApp;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static es.unizar.iaaa.pid.domain.PersistentIdentifier.computeSurrogateFromIdentifier;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment= RANDOM_PORT)
@ContextConfiguration(classes = PidmsApp.class)
public class StepDefs {

    private PersistentIdentifier ultimoPid;

    @Autowired
    protected PersistentIdentifierService servicioPid;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> ultimaRespuesta;

    @Dado("^un repositorio de PID$")
    public void un_repositorio_de_PID() throws Throwable {
        servicioPid.deleteAll();
    }

    @Dado("^que dicho PID está activo$")
    public void que_dicho_PID_está_activo() throws Throwable {
    }

    @Dado("^se resuelve a \"([^\"]*)\"$")
    public void se_resuelve_a_http_example_com_remote_dataset(String uri) throws Throwable {
        ultimoPid.getResource().setLocator(uri);
        servicioPid.save(ultimoPid);
    }

    @Cuando("^un cliente resuelve \"([^\"]*)\"$")
    public void un_cliente_resuelve_catalogo_dataset(String uri) throws Throwable {
        ultimaRespuesta =  this.restTemplate.getForEntity(uri, String.class);
    }

    @Entonces("^el cliente recibe una respuesta con el estado (\\d+)$")
    public void el_cliente_recibe_una_respuesta_con_el_estado(int estadoEsperado) throws Throwable {
        HttpStatus estadoEfectivo = ultimaRespuesta.getStatusCode();
        assertThat("el estado es incorrecto : "+ ultimaRespuesta.getBody(), estadoEfectivo.value(), is(estadoEsperado) );
    }

    @Entonces("^la cabecera \"([^\"]*)\" contiene \"([^\"]*)\"$")
    public void la_cabecera_contiene(String cabecera, String valorEsperado) throws Throwable {
        assertThat("la cabecera \""+cabecera+"\" no existe", ultimaRespuesta.getHeaders().containsKey(cabecera), is(true));
        List<String> valores = ultimaRespuesta.getHeaders().get(cabecera);
        assertThat("la cabecera \""+cabecera+"\" es multiple o vacía : "+valores, valores.size(), is(1));
        String valorEfectivo = valores.get(0);
        assertThat("la cabecera \""+cabecera+"\" no contiene \""+valorEfectivo+"\"", valorEfectivo, is(valorEsperado) );
    }

    @Dado("^que dicho PID no está activo$")
    public void que_dicho_PID_no_está_activo() throws Throwable {
        ultimoPid.getIdentifier().setEndLifespanVersion(Instant.now());
        ultimoPid.getRegistration().setItemStatus(ItemStatus.RETIRED);
        servicioPid.save(ultimoPid);
    }

    @Dado("^que contiene el PID de un conjunto de datos o una serie con \"code\" \"([^\"]*)\"$")
    public void que_contiene_el_PID_de_un_conjunto_de_datos_o_una_serie_con_dataset(String code) throws Throwable {
        ultimoPid = FixturesConstructorPid.creaConjunto(code);
        servicioPid.save(ultimoPid);
    }

    @Dado("^que no contiene el PID de un conjunto de datos o una serie con \"code\" \"([^\"]*)\"$")
    public void que_no_contiene_el_PID_de_un_conjunto_de_datos_o_una_serie_con_dataset(String code) throws Throwable {
        UUID id = computeSurrogateFromIdentifier(new Identifier().defaultNamespace().localId(code));
        if (servicioPid.findByUUID(id) != null) {
            servicioPid.delete(id);
        }
    }

    // HIC

    @Cuando("^un cliente resuelve \"([^\"]*)\" con cabecera \"([^\"]*)\" \"([^\"]*)\"$")
    public void un_cliente_resuelve_con_cabecera(String uri, String cabecera, String valor) throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.add(cabecera, valor);

        ultimaRespuesta =  this.restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @Cuando("^un cliente resuelve \"([^\"]*)\" sin cabecera \"([^\"]*)\"$")
    public void un_cliente_resuelve_sin_cabecera(String uri, String cabecera) throws Throwable {
        ultimaRespuesta =  this.restTemplate.getForEntity(uri, String.class);
    }

    @Dado("^que contiene el PID de un objeto espacial con \"namespace\" \"([^\"]*)\" y \"localId\" \"([^\"]*)\"$")
    public void que_contiene_el_PID_de_un_objeto_espacial(String namespace, String localId) throws Throwable {
        ultimoPid = FixturesConstructorPid.creaObjetoEspacial(namespace, localId, null);
        servicioPid.save(ultimoPid);
    }

    @Dado("^que no contiene el PID de un objeto espacial con \"namespace\" \"([^\"]*)\" y \"localId\" \"([^\"]*)\"$")
    public void que_no_contiene_el_PID_de_un_objeto_espacial(String namespace, String localId) throws Throwable {
        UUID id = computeSurrogateFromIdentifier(new Identifier().namespace(namespace).localId(localId));
        if (servicioPid.findByUUID(id) != null) {
            servicioPid.delete(id);
        }
    }

    @Dado("^que contiene el PID de un objeto espacial con \"namespace\" \"([^\"]*)\", \"localId\" \"([^\"]*)\" y \"versionId\" \"([^\"]*)\"$")
    public void que_contiene_el_PID_de_un_objeto_espacial_versionado(String namespace, String localId, String versionId) throws Throwable {
        ultimoPid = FixturesConstructorPid.creaObjetoEspacialVersionado(namespace, localId, versionId, null);
        servicioPid.save(ultimoPid);
    }

    @Dado("^que no contiene el PID de un objeto espacial con \"namespace\" \"([^\"]*)\", \"localId\" \"([^\"]*)\" y \"versionId\" \"([^\"]*)\"$")
    public void que_no_contiene_el_PID_de_un_objeto_espacial(String namespace, String localId, String versionId) throws Throwable {
        UUID id = computeSurrogateFromIdentifier(new Identifier().namespace(namespace).localId(localId).versionId(versionId));
        if (servicioPid.findByUUID(id) != null) {
            servicioPid.delete(id);
        }
    }

    @Cuando("^un cliente resuelva \"([^\"]*)\"$")
    public void un_cliente_resuelva(String uri) throws Throwable {
        ultimaRespuesta =  this.restTemplate.getForEntity(uri, String.class);
    }

    @Entonces("^recibirá una respuesta con estado (\\d+)$")
    public void recibirá_una_respuesta_con_estado(int estadoEsperado) throws Throwable {
        HttpStatus estadoEfectivo = ultimaRespuesta.getStatusCode();
        assertThat("el estado es incorrecto : "+ ultimaRespuesta.getBody(), estadoEfectivo.value(), is(estadoEsperado) );
    }

    @Entonces("^cabecera \"([^\"]*)\" igual a \"([^\"]*)\"$")
    public void cabecera_igual_a(String cabecera, String valorEsperado) throws Throwable {
        assertThat("la cabecera \""+cabecera+"\" no existe", ultimaRespuesta.getHeaders().containsKey(cabecera), is(true));
        List<String> valores = ultimaRespuesta.getHeaders().get(cabecera);
        assertThat("la cabecera \""+cabecera+"\" es multiple o vacía : "+valores, valores.size(), is(1));
        String valorEfectivo = valores.get(0);
        assertThat("la cabecera \""+cabecera+"\" no contiene \""+valorEfectivo+"\"", valorEfectivo, is(valorEsperado) );
    }

    @Entonces("^contenido igual al contenido de \"([^\"]*)\"$")
    public void contenido_igual_al_contenido_de(String url) throws Throwable {
        RestTemplate restTemplate = new RestTemplate();
        assertThat("los contenidos no son iguales", ultimaRespuesta.getBody(), is(restTemplate.getForEntity(url, String.class).getBody()));
    }

    @Dado("^un repositorio con PID para pruebas con el modo reverse proxy activado$")
    public void un_repositorio_con_PID_para_pruebas() throws Throwable {
        PersistentIdentifier ultimoPid = FixturesConstructorPid.creaConjunto("dataset");
        ultimoPid.getResource().setLocator("http://example.com/");
        ultimoPid.setResolverProxyMode(true);
        servicioPid.save(ultimoPid);
        ultimoPid = FixturesConstructorPid.creaObjetoEspacial("ns", "id", null);
        ultimoPid.getResource().setLocator("http://example.com/");
        ultimoPid.setResolverProxyMode(true);
        servicioPid.save(ultimoPid);
        ultimoPid = FixturesConstructorPid.creaObjetoEspacialVersionado("ns", "id", "ver", null);
        ultimoPid.getResource().setLocator("http://example.com/");
        ultimoPid.setResolverProxyMode(true);
        servicioPid.save(ultimoPid);
    }
}
