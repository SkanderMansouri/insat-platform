package insat.company.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import insat.company.platform.domain.enumeration.StatusEnumeration;

/**
 * A JoinClubRequest.
 */
@Entity
@Table(name = "join_club_request")
@Document(indexName = "joinclubrequest")
public class JoinClubRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "request_time", nullable = false)
    private LocalDate requestTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnumeration status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRequestTime() {
        return requestTime;
    }

    public JoinClubRequest requestTime(LocalDate requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    public void setRequestTime(LocalDate requestTime) {
        this.requestTime = requestTime;
    }

    public StatusEnumeration getStatus() {
        return status;
    }

    public JoinClubRequest status(StatusEnumeration status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusEnumeration status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public JoinClubRequest user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Club getClub() {
        return club;
    }

    public JoinClubRequest club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
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
        JoinClubRequest joinClubRequest = (JoinClubRequest) o;
        if (joinClubRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), joinClubRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JoinClubRequest{" +
            "id=" + getId() +
            ", requestTime='" + getRequestTime() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
