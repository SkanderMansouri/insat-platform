package com.pif.insatplatform.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Channel.
 */
@Entity
@Table(name = "channel")
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "channel_id", nullable = false)
    private String channelID;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "team_id", nullable = false)
    private String teamID;

    @NotNull
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @Column(name = "purpose")
    private String purpose;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelID() {
        return channelID;
    }

    public Channel channelID(String channelID) {
        this.channelID = channelID;
        return this;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public Channel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamID() {
        return teamID;
    }

    public Channel teamID(String teamID) {
        this.teamID = teamID;
        return this;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public Boolean isIsPrivate() {
        return isPrivate;
    }

    public Channel isPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPurpose() {
        return purpose;
    }

    public Channel purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Channel channel = (Channel) o;
        if (channel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Channel{" +
            "id=" + getId() +
            ", channelID='" + getChannelID() + "'" +
            ", name='" + getName() + "'" +
            ", teamID='" + getTeamID() + "'" +
            ", isPrivate='" + isIsPrivate() + "'" +
            ", purpose='" + getPurpose() + "'" +
            "}";
    }
}
