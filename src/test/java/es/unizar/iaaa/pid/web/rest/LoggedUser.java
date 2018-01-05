package es.unizar.iaaa.pid.web.rest;

import es.unizar.iaaa.pid.domain.Organization;
import es.unizar.iaaa.pid.domain.OrganizationMember;
import es.unizar.iaaa.pid.domain.User;
import es.unizar.iaaa.pid.domain.enumeration.Capacity;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class LoggedUser {

    @Autowired
    protected EntityManager em;

    protected User loggedUser;

    protected User loggedUser() {
        loggedUser = UserResourceIntTest.createEntity();
        em.persist(loggedUser);
        em.flush();
        return loggedUser;
    }

    @Before
    public void setupLoggedUser() {
        loggedUser = loggedUser();
    }

    protected void linkOrganizationToLoggedUser(Organization organization) {
        OrganizationMember om = new OrganizationMember();
        om.setCapacity(Capacity.ADMIN);
        om.setOrganization(organization);
        om.setUser(loggedUser);
        em.persist(om);
        em.flush();
    }
}
