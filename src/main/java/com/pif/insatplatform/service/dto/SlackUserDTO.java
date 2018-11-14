package com.pif.insatplatform.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SlackUser entity.
 */
public class SlackUserDTO implements Serializable {

    private Long id;

    private Integer slackId;

    private String realName;

    private String email;

    private Integer teamId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlackId() {
        return slackId;
    }

    public void setSlackId(Integer slackId) {
        this.slackId = slackId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SlackUserDTO slackUserDTO = (SlackUserDTO) o;
        if (slackUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slackUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SlackUserDTO{" +
            "id=" + getId() +
            ", slackId=" + getSlackId() +
            ", realName='" + getRealName() + "'" +
            ", email='" + getEmail() + "'" +
            ", teamId=" + getTeamId() +
            "}";
    }
}
