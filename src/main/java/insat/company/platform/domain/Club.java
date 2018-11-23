package insat.company.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@Document(indexName = "club")
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "domain")
    private String domain;

    @Column(name = "members")
    private List<SlackUser> members;

    @OneToOne(mappedBy = "president")
    @JsonIgnore
    private SlackUser president;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Club name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public Club domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public List<SlackUser> getMembers() {
        return members;
    }

    public Club members(List<SlackUser> members){
        this.members = members;
        return this;
    }

    public void setMembers(List<SlackUser> members) {
        this.members = members;
    }

    public SlackUser getPresident() {
        return president;
    }

    public Club president(SlackUser slackUser) {
        this.president = slackUser;
        return this;
    }

    public void setPresident(SlackUser slackUser) {
        this.president = slackUser;
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
        Club club = (Club) o;
        if (club.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), club.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Club{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", domain='" + domain + '\'' +
            ", members=" + members +
            '}';
    }

}
