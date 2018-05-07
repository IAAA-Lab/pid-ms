package es.unizar.iaaa.pid.web.rest.util;

import es.unizar.iaaa.pid.security.SecurityUtils;
import es.unizar.iaaa.pid.service.DTOService;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ControllerUtil {

    private static final String ERROR_KEY_ID_EXISTS = "idexists";

    private static final Logger log = LoggerFactory.getLogger(ControllerUtil.class);

    public static class Check<T> {
        private Supplier<Boolean> check = () -> false;
        private HttpStatus response;
        private String entityName;
        private String key;
        private String message;

        public Check(Supplier<Boolean> check, HttpStatus response, String entityName, String key, String message) {
            this.check = check;
            this.response = response;
            this.entityName = entityName;
            this.key = key;
            this.message = message;
        }

        public Check() {
        }

        private ResponseEntity<T> response() {
            return ResponseEntity
                .status(response)
                .headers(HeaderUtil.createFailureAlert(entityName, key, message))
                .build();
        }

        public static <T> Check<T> none() {
            return new Check<>();
        }
    }

    public static class ControllerContext<ID, T> {
        private String entityName;
        private UriComponentsBuilder uriBuilder;
        private DTOService<ID, T> service;
        private Supplier<Boolean> forbidWhen = () -> false;
        private Function<Pageable, Page<T>> findAll;
        private Function<Pageable, Page<T>> findAllAuthenticated;
        private Function<ID, T> findOne;
        private Function<ID, T> findOneAuthenticated;
        private Check<T> badRequestWhen = Check.none();
        private boolean mustBeAuthenticated = false;
        private Consumer<T> customise = (dto) -> {};

        private ControllerContext(@NotNull DTOService<ID, T> service) {
            this.service = service;
            this.findAll = service::findAll;
            this.findAllAuthenticated = service::findAll;
            this.findOne = service::findOne;
            this.findOneAuthenticated = service::findOne;
        }

        public ResponseEntity<T> doGet(ID id) {
            if (mustBeAuthenticated && !SecurityUtils.isAuthenticated()) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .headers(alertNotAuthenticated())
                    .build();
            }
            T dto = SecurityUtils.isAuthenticated()?
                findOneAuthenticated.apply(id):
                findOne.apply(id);
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dto));
        }


        public ResponseEntity<List<T>> doGet(Pageable pageable) {
            if (mustBeAuthenticated && !SecurityUtils.isAuthenticated()) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .headers(alertNotAuthenticated())
                    .build();
            }
            Page<T> page = SecurityUtils.isAuthenticated()?
                findAllAuthenticated.apply(pageable):
                findAll.apply(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, uriBuilder.toUriString());
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }

        /**
         * POST - Create a new DTO.
         *
         * @param dto the DTO to create
         * @return a ResponseEntity with
         *      status Created (created), Forbidden (creation forbidden) or BadRequest (DTO contains an id)
         */
        public ResponseEntity<T> doPost(T dto) {
            customise.accept(dto);
            if (isPresent(dto, "id")) {
                return ResponseEntity
                    .badRequest()
                    .headers(alertIdExists())
                    .build();
            }
            if (forbidWhen.get()) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .headers(alertForbiddenAccessForAdd())
                    .build();
            }
            if (badRequestWhen.check.get()) {
                return badRequestWhen.response();
            }
            T result = service.save(dto);
            String id = getAsString(result, "id");
            return ResponseEntity
                .created(uriBuilder.buildAndExpand(id).toUri())
                .headers(HeaderUtil.createEntityCreationAlert(entityName, id))
                .body(result);
        }

        /**
         * PUT - Update an existing DTO.
         *
         * @param id the "id" of the DTO.
         * @param dto the DTO to update
         * @return a ResponseEntity with
         *      status Ok (modified), Forbidden(modification forbidden) or NotFound (resource doesn't exist)
         */
        public ResponseEntity<T> doPut(ID id, T dto) {
            if (service.findOne(id) == null) {
                return ResponseEntity
                    .notFound()
                    .headers(alertEntityDoesNotExist())
                    .build();
            }
            if (forbidWhen.get()) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .headers(alertForbiddenAccessForModify())
                    .build();
            }
            overwriteProperty(dto, "id", id);
            T result = service.save(dto);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(entityName, id.toString()))
                .body(result);
        }

        public ResponseEntity<Void> doDelete(ID id) {
            if (service.findOne(id) == null) {
                return ResponseEntity
                    .notFound()
                    .headers(alertEntityDoesNotExist())
                    .build();
            }
            if (forbidWhen.get()) {
                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .headers(alertForbiddenAccessForDelete())
                    .build();

            }
            service.delete(id);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityDeletionAlert(entityName, id.toString()))
                .build();
        }

        private HttpHeaders alertNotAuthenticated() {
            return HeaderUtil.createFailureAlert(entityName, "notAuthenticated",
                "You must be authenticated to show the properties of an "+capitalLeter(entityName));
        }

        private HttpHeaders alertForbiddenAccessForAdd() {
            return HeaderUtil.createFailureAlert(entityName,
                "notCapacityToAdd" + capitalLeter(entityName),
                "You don't have privileges to add "+capitalLeter(entityName));
        }
        private HttpHeaders alertForbiddenAccessForModify() {
            return HeaderUtil.createFailureAlert(entityName,
                "notCapacityToModify" + capitalLeter(entityName),
                "You don't have privileges to modify "+capitalLeter(entityName));
        }
        private HttpHeaders alertForbiddenAccessForDelete() {
            return HeaderUtil.createFailureAlert(entityName,
                "notCapacityToDelete" + capitalLeter(entityName),
                "You don't have privileges to delete "+capitalLeter(entityName));
        }

        private HttpHeaders alertEntityDoesNotExist() {
            return HeaderUtil.createFailureAlert(entityName,
                capitalLeter(entityName) + "NotExist",
                "The "+capitalLeter(entityName)+" which want to be modifid does not exist");
        }


        private HttpHeaders alertIdExists() {
            return HeaderUtil.createFailureAlert(entityName,
                ERROR_KEY_ID_EXISTS,
                "A new " + capitalLeter(entityName) + " cannot already have an ID");
        }

        private String capitalLeter(String input) {
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }

        private void overwriteProperty(T object, String name, Object value) {
            try {
                PropertyUtils.setSimpleProperty(object, name, value);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.error("This error should not happen when modifying property "+object, e);
            }
        }

        private String getAsString(T dto, String object) {
            try {
                Object value = PropertyUtils.getSimpleProperty(dto, object);
                return value != null? value.toString() : null;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("This error should not happen wjem accessing property "+object, e);
                return null;
            }
        }

        private boolean isPresent(T dto, String name) {
            try {
                return PropertyUtils.getSimpleProperty(dto, name) != null;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("This error should not happen accessing property "+name, e);
                return false;
            }
        }

        public ControllerContext<ID, T> forbidWhen(Supplier<Boolean> forbidWhen) {
            this.forbidWhen = forbidWhen;
            return this;
        }

        public ControllerContext<ID, T> list(Function<Pageable, Page<T>> list) {
            this.findAll = list;
            return this;
        }

        public ControllerContext<ID, T> listAuthenticated(Function<Pageable, Page<T>> listAuthenticated) {
            this.findAllAuthenticated = listAuthenticated;
            return this;
        }

        public ControllerContext<ID, T> get(Function<ID, T> get) {
            this.findOne = get;
            return this;
        }

        public ControllerContext<ID, T> getAuthenticated(Function<ID, T> getAuthenticated) {
            this.findOneAuthenticated = getAuthenticated;
            return this;
        }

        public ControllerContext<ID, T> mustBeAuthenticated() {
            this.mustBeAuthenticated = true;
            return this;
        }

        public ControllerContext<ID, T> badRequestWhen(Supplier<Boolean> badRequestWhen, String key, String message) {
            this.badRequestWhen = new Check<T>(badRequestWhen, HttpStatus.BAD_REQUEST, entityName, key, message);
            return this;
        }

        public ControllerContext<ID, T> customise(Consumer<T> customise) {
            this.customise = customise;
            return this;
        }
    }

    public static <ID, T> ControllerContext<ID, T> with(String entityName, UriComponentsBuilder uriBuilder, DTOService<ID, T> service) {
        ControllerContext<ID, T>  result = new ControllerContext<>(service);
        result.entityName = entityName;
        result.uriBuilder = uriBuilder;
        return result;
    }

    public static <ID, T> ControllerContext<ID, T> with(UriComponentsBuilder uriBuilder, DTOService<ID, T> service) {
        ControllerContext<ID, T>  result = new ControllerContext<>(service);
        result.uriBuilder = uriBuilder;
        return result;
    }

    public static <ID, T> ControllerContext<ID, T> with(String entityName, DTOService<ID, T> service) {
        ControllerContext<ID, T>  result = new ControllerContext<>(service);
        result.entityName = entityName;
        return result;
    }

    public static <ID, T> ControllerContext<ID, T> with(DTOService<ID, T> service) {
        return new ControllerContext<>(service);
    }

}
