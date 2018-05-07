package es.unizar.iaaa.pid.domain;

import es.unizar.iaaa.pid.domain.enumeration.ItemStatus;
import es.unizar.iaaa.pid.domain.enumeration.ProcessStatus;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.Instant;

@Embeddable
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status")
    private ProcessStatus processStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @Column(name = "last_change_date")
    private Instant lastChangeDate;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @Column(name = "last_revision_date")
    private Instant lastRevisionDate;

    @Column(name = "next_renewal_date")
    private Instant nextRenewalDate;

    @Column(name = "annullation_date")
    private Instant annullationDate;


    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public Registration processStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
        return this;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public Registration itemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
        return this;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Instant getLastChangeDate() {
        return lastChangeDate;
    }

    public Registration lastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
        return this;
    }

    public void setLastChangeDate(Instant lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public Registration registrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Instant getLastRevisionDate() {
        return lastRevisionDate;
    }

    public Registration lastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
        return this;
    }

    public void setLastRevisionDate(Instant lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public Instant getNextRenewalDate() {
        return nextRenewalDate;
    }

    public Registration nextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
        return this;
    }

    public void setNextRenewalDate(Instant nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public Instant getAnnullationDate() {
        return annullationDate;
    }

    public Registration annullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
        return this;
    }

    public void setAnnullationDate(Instant annullationDate) {
        this.annullationDate = annullationDate;
    }

    @Override
    public String toString() {
        return "Registration{" +
            "processStatus='" + getProcessStatus() + "'" +
            ", itemStatus='" + getItemStatus() + "'" +
            ", lastChangeDate='" + getLastChangeDate() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", lastRevisionDate='" + getLastRevisionDate() + "'" +
            ", nextRenewalDate='" + getNextRenewalDate() + "'" +
            ", annullationDate='" + getAnnullationDate() + "'" +
            "}";
    }}
