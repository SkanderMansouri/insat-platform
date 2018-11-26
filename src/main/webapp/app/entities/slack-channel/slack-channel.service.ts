import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISlackChannel } from 'app/shared/model/slack-channel.model';

type EntityResponseType = HttpResponse<ISlackChannel>;
type EntityArrayResponseType = HttpResponse<ISlackChannel[]>;

@Injectable({ providedIn: 'root' })
export class SlackChannelService {
    public resourceUrl = SERVER_API_URL + 'api/slack-channels';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/slack-channels';

    constructor(private http: HttpClient) {}

    create(slackChannel: ISlackChannel): Observable<EntityResponseType> {
        return this.http.post<ISlackChannel>(this.resourceUrl, slackChannel, { observe: 'response' });
    }

    update(slackChannel: ISlackChannel): Observable<EntityResponseType> {
        return this.http.put<ISlackChannel>(this.resourceUrl, slackChannel, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISlackChannel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISlackChannel[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISlackChannel[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
