import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IConfig } from '../shared/model/config.model';
import { createRequestOption } from 'app/shared';

type EntityResponseType = HttpResponse<string>;

@Injectable({ providedIn: 'root' })
export class ConfigService {
    public resourceUrl = SERVER_API_URL + 'api/config';
    public resourcecodeUrl = SERVER_API_URL + 'api/slack/oauth';

    constructor(private http: HttpClient) {}

    get(): Observable<EntityResponseType> {
        return this.http.get<string>(this.resourceUrl, { observe: 'response' });
    }
    getcode(param: string[]): Observable<EntityResponseType> {
        const code = createRequestOption(param);

        return this.http.get<string>(this.resourcecodeUrl, { observe: 'response', params: code });
    }
}
