package insat.company.platform.domain;


import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToOne
    @JoinColumn(unique = true)
    private User president;

    @ManyToMany
    @JoinTable(name = "club_member",
        joinColumns = @JoinColumn(name = "clubs_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>();

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

    public void setName(String name) {
        this.name = name;
    }

    public Club name(String name) {
        this.name = name;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Club domain(String domain) {
        this.domain = domain;
        return this;
    }

    public User getPresident() {
        return president;
    }

    public void setPresident(User user) {
        this.president = user;
    }

    public Club president(User user) {
        this.president = user;
        return this;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> users) {
        this.members = users;
    }

    public Club members(Set<User> users) {
        this.members = users;
        return this;
    }

    public Club addMember(User user) {
        this.members.add(user);
        user.getClubs().add(this);
        return this;
    }

    public Club removeMember(User user) {
        this.members.remove(user);
        user.getClubs().remove(this);
        return this;
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
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", domain='" + getDomain() + "'" +
            "}";
    }
}
