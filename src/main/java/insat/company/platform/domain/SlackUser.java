package insat.company.platform.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SlackUser.
 */
@Entity
@Table(name = "slack_user")
@Document(indexName = "slackuser")
public class SlackUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "slack_id")
    private String slackId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "is_bot")
    private Boolean isBot;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public SlackUser teamId(String teamId) {
        this.teamId = teamId;
        return this;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getSlackId() {
        return slackId;
    }

    public SlackUser slackId(String slackId) {
        this.slackId = slackId;
        return this;
    }

    public void setSlackId(String slackId) {
        this.slackId = slackId;
    }

    public String getUserName() {
        return userName;
    }

    public SlackUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Boolean isIsBot() {
        return isBot;
    }

    public SlackUser isBot(Boolean isBot) {
        this.isBot = isBot;
        return this;
    }

    public void setIsBot(Boolean isBot) {
        this.isBot = isBot;
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
            ", teamId='" + getTeamId() + "'" +
            ", slackId='" + getSlackId() + "'" +
            ", userName='" + getUserName() + "'" +
            ", email='" + getEmail() + "'" +
            ", isBot='" + isIsBot() + "'" +
            "}";
    }
}
