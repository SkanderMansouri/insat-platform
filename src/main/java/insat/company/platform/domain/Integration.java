package insat.company.platform.domain;


import javax.persistence.*;

import insat.company.platform.service.dto.IntegrationDTO;
import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Integration.
 */
@Entity
@Table(name = "integration")
@Document(indexName = "integration")
public class Integration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "jhi_scope")
    private String scope;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_url")
    private String teamUrl;

   @ManyToOne
   @JoinColumn(name = "user_id",referencedColumnName = "id")
   private User user ;

   public Integration(){}

   public Integration(IntegrationDTO integrationDTO, User user){
       this.user = user;
       this.id = integrationDTO.getId();
       this.teamName = integrationDTO.getTeamName();
       this.teamId = integrationDTO.getTeamId();
       this.scope = integrationDTO.getScope();
       this.teamUrl = integrationDTO.getTeamUrl();
   }

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
            ", teamId='" + getTeamId() + "'" +
            ", scope='" + getScope() + "'" +
            ", teamName='" + getTeamName() + "'" +
            ", teamUrl='" + getTeamUrl() + "'" +
            "}";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
