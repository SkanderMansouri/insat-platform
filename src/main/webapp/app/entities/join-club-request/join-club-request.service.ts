import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';

type EntityResponseType = HttpResponse<IJoinClubRequest>;
type EntityArrayResponseType = HttpResponse<IJoinClubRequest[]>;

@Injectable({ providedIn: 'root' })
export class JoinClubRequestService {
    public resourceUrl = SERVER_API_URL + 'api/join-club-requests';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/join-club-requests';

    constructor(private http: HttpClient) {}

    create(joinClubRequest: IJoinClubRequest): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(joinClubRequest);
        return this.http
            .post<IJoinClubRequest>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(joinClubRequest: IJoinClubRequest): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(joinClubRequest);
        return this.http
            .put<IJoinClubRequest>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IJoinClubRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IJoinClubRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IJoinClubRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(joinClubRequest: IJoinClubRequest): IJoinClubRequest {
        const copy: IJoinClubRequest = Object.assign({}, joinClubRequest, {
            requestTime:
                joinClubRequest.requestTime != null && joinClubRequest.requestTime.isValid()
                    ? joinClubRequest.requestTime.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.requestTime = res.body.requestTime != null ? moment(res.body.requestTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((joinClubRequest: IJoinClubRequest) => {
                joinClubRequest.requestTime = joinClubRequest.requestTime != null ? moment(joinClubRequest.requestTime) : null;
            });
        }
        return res;
    }
}
