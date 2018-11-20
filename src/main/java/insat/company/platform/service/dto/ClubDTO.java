package insat.company.platform.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Club entity.
 */
public class ClubDTO implements Serializable {

    private Long id;

    private String name;

    private String president;

    private String field;

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

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClubDTO clubDTO = (ClubDTO) o;
        if (clubDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clubDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClubDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", president='" + getPresident() + "'" +
            ", field='" + getField() + "'" +
            "}";
    }
}
