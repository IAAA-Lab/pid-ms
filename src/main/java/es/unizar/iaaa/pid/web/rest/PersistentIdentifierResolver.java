package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.config.ApplicationProperties;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.PersistentIdentifier;
import es.unizar.iaaa.pid.domain.enumeration.ResourceType;
import es.unizar.iaaa.pid.service.PersistentIdentifierService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

/**
 * REST controller for resolving PersistentIdentifier.
 */
@RestController
public class PersistentIdentifierResolver {
    private static final MediaType PID_MEDIA_TYPE = MediaType.parseMediaType("application/vnd.inspire.pid");

    private ApplicationProperties properties;

    PersistentIdentifierService persistentIdentifierService;

    public PersistentIdentifierResolver(PersistentIdentifierService persistentIdentifierService, ApplicationProperties properties) {
        this.persistentIdentifierService = persistentIdentifierService;
        this.properties = properties;
    }

    @RequestMapping(path = "/catalogo/{code}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> resolverMetadatoConjunto(@PathVariable String code,
                                                           @RequestHeader(name = "Accept", required = false) String accept) {
        Identifier identifier = new Identifier()
            .namespace("catalogo")
            .localId(code);
        UUID surrogate = PersistentIdentifier.computeSurrogateFromIdentifier(identifier);

        PersistentIdentifier pid = persistentIdentifierService.findByUUID(surrogate);
        return resolve(pid, accept);
    }

    @RequestMapping(path = "/recurso/{namespace}/{localId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> resolverObjetoEspacialNoVersionado(@PathVariable String namespace, @PathVariable String localId,
                                                                     @RequestHeader(name = "Accept", required = false) String accept) {
        Identifier identifier = new Identifier()
            .namespace(namespace)
            .localId(localId);
        UUID surrogate = PersistentIdentifier.computeSurrogateFromIdentifier(identifier);

        PersistentIdentifier pid = persistentIdentifierService.findByUUID(surrogate);
        return resolve(pid, accept);
    }

    @RequestMapping(path = "/recurso/{namespace}/{localId}/{versionId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> resolverObjetoEspacialVersionado(@PathVariable String namespace,
                                                                   @PathVariable String localId,
                                                                   @PathVariable String versionId,
                                                                   @RequestHeader(name = "Accept", required = false) String accept) {
        Identifier identifier = new Identifier()
            .namespace(namespace)
            .localId(localId)
            .versionId(versionId);
        UUID surrogate = PersistentIdentifier.computeSurrogateFromIdentifier(identifier);

        PersistentIdentifier pid = persistentIdentifierService.findByUUID(surrogate);
        return resolve(pid, accept);
    }

    private ResponseEntity<byte[]> resolve(PersistentIdentifier pid, String accept) {
        if (!StringUtils.isEmpty(accept)) {
            for(MediaType media: MediaType.parseMediaTypes(accept)) {
                if (PID_MEDIA_TYPE.includes(media))
                    return resolverMetadatoPid(pid);
            }
        }
        if (pid!=null && pid.isResolverProxyMode()) {
            return resolverProxyPid(pid);
        }
        return resolverRedireccionPid(pid);
    }

    // TODO Este código potencialmente fallará con un WFS 2.0
    private ResponseEntity<byte[]> resolverProxyPid(PersistentIdentifier pid) {
        String resourceLocator = pid.getResource().getLocator();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate rest = new RestTemplate(requestFactory);
        ResponseEntity<byte[]> responseEntity = rest.getForEntity(resourceLocator, byte[].class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(responseEntity.getHeaders());
        headers.set("Content-Location", resourceLocator);
        return new ResponseEntity<>(responseEntity.getBody(), headers, responseEntity.getStatusCode());
    }

    private ResponseEntity<byte[]> resolverRedireccionPid(PersistentIdentifier pid) {
        HttpHeaders cabeceras = new HttpHeaders();
        HttpStatus estado = HttpStatus.NOT_FOUND;
        if (pid != null) {
            String resourceLocator = pid.getResource().getLocator();
            cabeceras.set("Vary", "Accept");
            switch (pid.getRegistration().getItemStatus()) {
                case ISSUED:
                case LAPSED:
                    cabeceras.setLocation(URI.create(resourceLocator));
                    estado = HttpStatus.TEMPORARY_REDIRECT;
                    break;
                case RETIRED:
                case ANNULLED:
                    estado = HttpStatus.GONE;
            }
        }
        return new ResponseEntity<>(cabeceras, estado);
    }

    private ResponseEntity<byte[]> resolverMetadatoPid(PersistentIdentifier pid) {
        HttpHeaders cabeceras = new HttpHeaders();
        HttpStatus estado = HttpStatus.NOT_FOUND;
        String metadatosBase = properties.getResolver().getMetadataBase();
        if (pid != null) {
            ResourceType resourceType = pid.getResource().getResourceType();

            String localizacion = null;
            switch (resourceType) {
                case DATASET:
                    localizacion = String.format("%s/%s/%s", metadatosBase, pid.getIdentifier().getNamespace(), pid.getIdentifier().getLocalId());
                    break;
                case SPATIAL_OBJECT:
                    localizacion = String.format("%s/recurso/%s/%s", metadatosBase, pid.getIdentifier().getNamespace(), pid.getIdentifier().getLocalId());
                    if (pid.getIdentifier().getVersionId() != null) {
                        localizacion = String.format("%s/%s", localizacion, pid.getIdentifier().getVersionId());
                    }
            }
            cabeceras.setLocation(URI.create(localizacion));
            cabeceras.set("Vary", "Accept");
            estado = HttpStatus.TEMPORARY_REDIRECT;
        }
        return new ResponseEntity<>(cabeceras, estado);
    }}
