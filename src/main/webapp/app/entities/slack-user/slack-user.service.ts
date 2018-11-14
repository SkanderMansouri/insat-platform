import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";

import { SERVER_API_URL } from "app/app.constants";
import { createRequestOption } from "app/shared";
import { ISlackUser } from "app/shared/model/slack-user.model";

type EntityResponseType = HttpResponse<ISlackUser>;
type EntityArrayResponseType = HttpResponse<ISlackUser[]>;

@Injectable({ providedIn: "root" })
export class SlackUserService {
  private resourceUrl = SERVER_API_URL + "api/slack-users";

  constructor(private http: HttpClient) {}

  create(slackUser: ISlackUser): Observable<EntityResponseType> {
    return this.http.post<ISlackUser>(this.resourceUrl, slackUser, {
      observe: "response"
    });
  }

  update(slackUser: ISlackUser): Observable<EntityResponseType> {
    return this.http.put<ISlackUser>(this.resourceUrl, slackUser, {
      observe: "response"
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISlackUser>(`${this.resourceUrl}/${id}`, {
      observe: "response"
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISlackUser[]>(this.resourceUrl, {
      params: options,
      observe: "response"
    });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {
      observe: "response"
    });
  }
}
