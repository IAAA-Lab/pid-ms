package es.unizar.iaaa.pid.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.unizar.iaaa.pid.service.GroupMemberService;
import es.unizar.iaaa.pid.web.rest.util.HeaderUtil;
import es.unizar.iaaa.pid.service.dto.GroupMemberDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GroupMember.
 */
@RestController
@RequestMapping("/api")
public class GroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(GroupMemberResource.class);

    private static final String ENTITY_NAME = "groupMember";

    private final GroupMemberService groupMemberService;

    public GroupMemberResource(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    /**
     * POST  /group-members : Create a new groupMember.
     *
     * @param groupMemberDTO the groupMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupMemberDTO, or with status 400 (Bad Request) if the groupMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/group-members")
    @Timed
    public ResponseEntity<GroupMemberDTO> createGroupMember(@Valid @RequestBody GroupMemberDTO groupMemberDTO) throws URISyntaxException {
        log.debug("REST request to save GroupMember : {}", groupMemberDTO);
        if (groupMemberDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new groupMember cannot already have an ID")).body(null);
        }
        GroupMemberDTO result = groupMemberService.save(groupMemberDTO);
        return ResponseEntity.created(new URI("/api/group-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /group-members : Updates an existing groupMember.
     *
     * @param groupMemberDTO the groupMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupMemberDTO,
     * or with status 400 (Bad Request) if the groupMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the groupMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/group-members")
    @Timed
    public ResponseEntity<GroupMemberDTO> updateGroupMember(@Valid @RequestBody GroupMemberDTO groupMemberDTO) throws URISyntaxException {
        log.debug("REST request to update GroupMember : {}", groupMemberDTO);
        if (groupMemberDTO.getId() == null) {
            return createGroupMember(groupMemberDTO);
        }
        GroupMemberDTO result = groupMemberService.save(groupMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /group-members : get all the groupMembers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupMembers in body
     */
    @GetMapping("/group-members")
    @Timed
    public List<GroupMemberDTO> getAllGroupMembers() {
        log.debug("REST request to get all GroupMembers");
        return groupMemberService.findAll();
    }

    /**
     * GET  /group-members/:id : get the "id" groupMember.
     *
     * @param id the id of the groupMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/group-members/{id}")
    @Timed
    public ResponseEntity<GroupMemberDTO> getGroupMember(@PathVariable Long id) {
        log.debug("REST request to get GroupMember : {}", id);
        GroupMemberDTO groupMemberDTO = groupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupMemberDTO));
    }

    /**
     * DELETE  /group-members/:id : delete the "id" groupMember.
     *
     * @param id the id of the groupMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/group-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroupMember(@PathVariable Long id) {
        log.debug("REST request to delete GroupMember : {}", id);
        groupMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
