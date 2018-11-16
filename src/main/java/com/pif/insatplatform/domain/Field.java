package com.pif.insatplatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Field.
 */
@Entity
@Table(name = "field")
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "section", nullable = false)
    private String section;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User belongs;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public Field section(String section) {
        this.section = section;
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getYear() {
        return year;
    }

    public Field year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public User getBelongs() {
        return belongs;
    }

    public Field belongs(User user) {
        this.belongs = user;
        return this;
    }

    public void setBelongs(User user) {
        this.belongs = user;
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
        Field field = (Field) o;
        if (field.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), field.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Field{" +
            "id=" + getId() +
            ", section='" + getSection() + "'" +
            ", year=" + getYear() +
            "}";
    }
}
