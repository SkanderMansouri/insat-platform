package insat.company.platform.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A InsatEvent.
 */
@Entity
@Table(name = "insat_event")
@Document(indexName = "insatevent")
public class InsatEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "place")
    private String place;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "insat_event_club",
               joinColumns = @JoinColumn(name = "insat_events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "clubs_id", referencedColumnName = "id"))
    private Set<Club> clubs = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "insat_event_member",
               joinColumns = @JoinColumn(name = "insat_events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "insat_event_participant",
               joinColumns = @JoinColumn(name = "insat_events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "participants_id", referencedColumnName = "id"))
    private Set<User> participants = new HashSet<>();

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

    public InsatEvent name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public InsatEvent date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public InsatEvent place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public InsatEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public InsatEvent clubs(Set<Club> clubs) {
        this.clubs = clubs;
        return this;
    }

    public InsatEvent addClub(Club club) {
        this.clubs.add(club);
        club.getInsatEvents().add(this);
        return this;
    }

    public InsatEvent removeClub(Club club) {
        this.clubs.remove(club);
        club.getInsatEvents().remove(this);
        return this;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }

    public Set<User> getMembers() {
        return members;
    }

    public InsatEvent members(Set<User> users) {
        this.members = users;
        return this;
    }

    public InsatEvent addMember(User user) {
        this.members.add(user);
        user.getInsatEvents().add(this);
        return this;
    }

    public InsatEvent removeMember(User user) {
        this.members.remove(user);
        user.getInsatEvents().remove(this);
        return this;
    }

    public void setMembers(Set<User> users) {
        this.members = users;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public InsatEvent participants(Set<User> users) {
        this.participants = users;
        return this;
    }

    public InsatEvent addParticipant(User user) {
        this.participants.add(user);
        user.getInsatEvents().add(this);
        return this;
    }

    public InsatEvent removeParticipant(User user) {
        this.participants.remove(user);
        user.getInsatEvents().remove(this);
        return this;
    }

    public void setParticipants(Set<User> users) {
        this.participants = users;
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
        InsatEvent insatEvent = (InsatEvent) o;
        if (insatEvent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insatEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsatEvent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", place='" + getPlace() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
