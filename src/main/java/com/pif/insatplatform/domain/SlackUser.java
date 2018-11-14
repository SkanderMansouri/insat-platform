package com.pif.insatplatform.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SlackUser.
 */
@Entity
@Table(name = "slack_users")
public class SlackUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_id")
    private Integer slackId;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "email")
    private String email;

    @Column(name = "team_id")
    private Integer teamId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlackId() {
        return slackId;
    }

    public SlackUser slackId(Integer slackId) {
        this.slackId = slackId;
        return this;
    }

    public void setSlackId(Integer slackId) {
        this.slackId = slackId;
    }

    public String getRealName() {
        return realName;
    }

    public SlackUser realName(String realName) {
        this.realName = realName;
        return this;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public SlackUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public SlackUser teamId(Integer teamId) {
        this.teamId = teamId;
        return this;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
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
        SlackUser slackUser = (SlackUser) o;
        if (slackUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), slackUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SlackUser{" +
            "id=" + getId() +
            ", slackId=" + getSlackId() +
            ", realName='" + getRealName() + "'" +
            ", email='" + getEmail() + "'" +
            ", teamId=" + getTeamId() +
            "}";
    }
}
