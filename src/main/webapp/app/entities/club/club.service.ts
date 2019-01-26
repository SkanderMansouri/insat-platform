import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Club, IClub } from 'app/shared/model/club.model';
import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';

type EntityResponseType = HttpResponse<IClub>;
type EntityArrayResponseType = HttpResponse<IClub[]>;

@Injectable({ providedIn: 'root' })
export class ClubService {
    public resourceUrl = SERVER_API_URL + 'api/clubs';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/clubs';

    constructor(private http: HttpClient) {}

    create(club: IClub): Observable<EntityResponseType> {
        return this.http.post<IClub>(this.resourceUrl, club, { observe: 'response' });
    }

    update(club: IClub): Observable<EntityResponseType> {
        console.log('i was hereeeee {}', club.id);
        return this.http.put<IClub>(this.resourceUrl, club, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IClub>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IClub[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IClub[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    clubsList(): Observable<Club[]> {
        return this.http.get<Club[]>(SERVER_API_URL + 'api/clubs/list');
    }
    clubsUserList(): Observable<Club[]> {
        return this.http.get<Club[]>(SERVER_API_URL + 'api/users/clubs');
    }
    createRequest(id: number): Observable<EntityResponseType> {
        return this.http.get<IJoinClubRequest>(`${'api/clubs/join'}/${id}`, { observe: 'response' });
    }
}
