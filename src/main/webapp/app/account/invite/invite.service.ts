import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
type EntityResponseType = HttpResponse<any>;

@Injectable({ providedIn: 'root' })
export class InviteService {
    constructor(private http: HttpClient) {}

    save(mail: string): Observable<EntityResponseType> {
        return this.http.post(SERVER_API_URL + 'api/account/invite', mail, { observe: 'response' });
    }
}
