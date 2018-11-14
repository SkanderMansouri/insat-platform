package com.pif.insatplatform.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Channel entity. This class is used in ChannelResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /channels?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChannelCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter channelID;

    private StringFilter name;

    private StringFilter teamID;

    private BooleanFilter isPrivate;

    private StringFilter purpose;

    public ChannelCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getChannelID() {
        return channelID;
    }

    public void setChannelID(StringFilter channelID) {
        this.channelID = channelID;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getTeamID() {
        return teamID;
    }

    public void setTeamID(StringFilter teamID) {
        this.teamID = teamID;
    }

    public BooleanFilter getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(BooleanFilter isPrivate) {
        this.isPrivate = isPrivate;
    }

    public StringFilter getPurpose() {
        return purpose;
    }

    public void setPurpose(StringFilter purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "ChannelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (channelID != null ? "channelID=" + channelID + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (teamID != null ? "teamID=" + teamID + ", " : "") +
                (isPrivate != null ? "isPrivate=" + isPrivate + ", " : "") +
                (purpose != null ? "purpose=" + purpose + ", " : "") +
            "}";
    }

}
