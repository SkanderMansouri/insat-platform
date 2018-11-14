import { Injectable } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import {
  Resolve,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Routes
} from "@angular/router";
import { UserRouteAccessService } from "app/core";
import { of } from "rxjs";
import { map } from "rxjs/operators";
import { SlackUser } from "app/shared/model/slack-user.model";
import { SlackUserService } from "./slack-user.service";
import { SlackUserComponent } from "./slack-user.component";
import { SlackUserDetailComponent } from "./slack-user-detail.component";
import { SlackUserUpdateComponent } from "./slack-user-update.component";
import { SlackUserDeletePopupComponent } from "./slack-user-delete-dialog.component";
import { ISlackUser } from "app/shared/model/slack-user.model";

@Injectable({ providedIn: "root" })
export class SlackUserResolve implements Resolve<ISlackUser> {
  constructor(private service: SlackUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params["id"] ? route.params["id"] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(map((slackUser: HttpResponse<SlackUser>) => slackUser.body));
    }
    return of(new SlackUser());
  }
}

export const slackUserRoute: Routes = [
  {
    path: "slack-user",
    component: SlackUserComponent,
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "SlackUsers"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "slack-user/:id/view",
    component: SlackUserDetailComponent,
    resolve: {
      slackUser: SlackUserResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "SlackUsers"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "slack-user/new",
    component: SlackUserUpdateComponent,
    resolve: {
      slackUser: SlackUserResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "SlackUsers"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "slack-user/:id/edit",
    component: SlackUserUpdateComponent,
    resolve: {
      slackUser: SlackUserResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "SlackUsers"
    },
    canActivate: [UserRouteAccessService]
  }
];

export const slackUserPopupRoute: Routes = [
  {
    path: "slack-user/:id/delete",
    component: SlackUserDeletePopupComponent,
    resolve: {
      slackUser: SlackUserResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "SlackUsers"
    },
    canActivate: [UserRouteAccessService],
    outlet: "popup"
  }
];
