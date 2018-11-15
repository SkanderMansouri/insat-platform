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
import { Integration } from "app/shared/model/integration.model";
import { IntegrationService } from "./integration.service";
import { IntegrationComponent } from "./integration.component";
import { IntegrationDetailComponent } from "./integration-detail.component";
import { IntegrationUpdateComponent } from "./integration-update.component";
import { IntegrationDeletePopupComponent } from "./integration-delete-dialog.component";
import { IIntegration } from "app/shared/model/integration.model";

@Injectable({ providedIn: "root" })
export class IntegrationResolve implements Resolve<IIntegration> {
  constructor(private service: IntegrationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params["id"] ? route.params["id"] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(
          map((integration: HttpResponse<Integration>) => integration.body)
        );
    }
    return of(new Integration());
  }
}

export const integrationRoute: Routes = [
  {
    path: "integration",
    component: IntegrationComponent,
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Integrations"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "integration/:id/view",
    component: IntegrationDetailComponent,
    resolve: {
      integration: IntegrationResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Integrations"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "integration/new",
    component: IntegrationUpdateComponent,
    resolve: {
      integration: IntegrationResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Integrations"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "integration/:id/edit",
    component: IntegrationUpdateComponent,
    resolve: {
      integration: IntegrationResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Integrations"
    },
    canActivate: [UserRouteAccessService]
  }
];

export const integrationPopupRoute: Routes = [
  {
    path: "integration/:id/delete",
    component: IntegrationDeletePopupComponent,
    resolve: {
      integration: IntegrationResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Integrations"
    },
    canActivate: [UserRouteAccessService],
    outlet: "popup"
  }
];
