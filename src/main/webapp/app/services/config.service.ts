import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IConfig } from '../shared/model/config.model';

type EntityResponseType = HttpResponse<string>;

@Injectable({ providedIn: 'root' })
export class ConfigService {
    public resourceUrl = SERVER_API_URL + 'api/config';

    constructor(private http: HttpClient) {}

    get(): Observable<EntityResponseType> {
        return this.http.get<string>(this.resourceUrl, { observe: 'response' });
    }
}
