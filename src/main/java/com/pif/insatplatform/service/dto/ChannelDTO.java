package com.pif.insatplatform.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Channel entity.
 */
public class ChannelDTO implements Serializable {

    private Long id;

    @NotNull
    private String channelID;

    @NotNull
    private String name;

    @NotNull
    private String teamID;

    @NotNull
    private Boolean isPrivate;

    private String purpose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public Boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChannelDTO channelDTO = (ChannelDTO) o;
        if (channelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), channelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChannelDTO{" +
            "id=" + getId() +
            ", channelID='" + getChannelID() + "'" +
            ", name='" + getName() + "'" +
            ", teamID='" + getTeamID() + "'" +
            ", isPrivate='" + isIsPrivate() + "'" +
            ", purpose='" + getPurpose() + "'" +
            "}";
    }
}
