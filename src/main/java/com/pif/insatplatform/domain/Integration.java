package com.pif.insatplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Integration.
 */
@Entity
@Table(name = "integration")
public class Integration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "jhi_scope")
    private String scope;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "bot_id")
    private String botId;

    @Column(name = "bot_access_token")
    private String botAccessToken;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "team_url")
    private String teamUrl;

    @OneToMany(mappedBy = "integration")
    private Set<SlackUser> userIdSlackUserRelationships = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integration accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public Integration scope(String scope) {
        this.scope = scope;
        return this;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTeamId() {
        return teamId;
    }

    public Integration teamId(String teamId) {
        this.teamId = teamId;
        return this;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integration teamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getBotId() {
        return botId;
    }

    public Integration botId(String botId) {
        this.botId = botId;
        return this;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    public String getBotAccessToken() {
        return botAccessToken;
    }

    public Integration botAccessToken(String botAccessToken) {
        this.botAccessToken = botAccessToken;
        return this;
    }

    public void setBotAccessToken(String botAccessToken) {
        this.botAccessToken = botAccessToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integration userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTeamUrl() {
        return teamUrl;
    }

    public Integration teamUrl(String teamUrl) {
        this.teamUrl = teamUrl;
        return this;
    }

    public void setTeamUrl(String teamUrl) {
        this.teamUrl = teamUrl;
    }

    public Set<SlackUser> getUserIdSlackUserRelationships() {
        return userIdSlackUserRelationships;
    }

    public Integration userIdSlackUserRelationships(Set<SlackUser> slackUsers) {
        this.userIdSlackUserRelationships = slackUsers;
        return this;
    }

    public void setUserIdSlackUserRelationships(Set<SlackUser> slackUsers) {
        this.userIdSlackUserRelationships = slackUsers;
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
        Integration integration = (Integration) o;
        if (integration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), integration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Integration{" +
            "id=" + getId() +
            ", accessToken='" + getAccessToken() + "'" +
            ", scope='" + getScope() + "'" +
            ", teamId='" + getTeamId() + "'" +
            ", teamName='" + getTeamName() + "'" +
            ", botId='" + getBotId() + "'" +
            ", botAccessToken='" + getBotAccessToken() + "'" +
            ", userId=" + getUserId() +
            ", teamUrl='" + getTeamUrl() + "'" +
            "}";
    }
}
