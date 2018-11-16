import { Injectable } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import {
  Resolve,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Routes
} from "@angular/router";
import { JhiPaginationUtil, JhiResolvePagingParams } from "ng-jhipster";
import { UserRouteAccessService } from "app/core";
import { of } from "rxjs";
import { map } from "rxjs/operators";
import { Field } from "app/shared/model/field.model";
import { FieldService } from "./field.service";
import { FieldComponent } from "./field.component";
import { FieldDetailComponent } from "./field-detail.component";
import { FieldUpdateComponent } from "./field-update.component";
import { FieldDeletePopupComponent } from "./field-delete-dialog.component";
import { IField } from "app/shared/model/field.model";

@Injectable({ providedIn: "root" })
export class FieldResolve implements Resolve<IField> {
  constructor(private service: FieldService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params["id"] ? route.params["id"] : null;
    if (id) {
      return this.service
        .find(id)
        .pipe(map((field: HttpResponse<Field>) => field.body));
    }
    return of(new Field());
  }
}

export const fieldRoute: Routes = [
  {
    path: "field",
    component: FieldComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ["ROLE_USER"],
      defaultSort: "id,asc",
      pageTitle: "Fields"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "field/:id/view",
    component: FieldDetailComponent,
    resolve: {
      field: FieldResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Fields"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "field/new",
    component: FieldUpdateComponent,
    resolve: {
      field: FieldResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Fields"
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: "field/:id/edit",
    component: FieldUpdateComponent,
    resolve: {
      field: FieldResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Fields"
    },
    canActivate: [UserRouteAccessService]
  }
];

export const fieldPopupRoute: Routes = [
  {
    path: "field/:id/delete",
    component: FieldDeletePopupComponent,
    resolve: {
      field: FieldResolve
    },
    data: {
      authorities: ["ROLE_USER"],
      pageTitle: "Fields"
    },
    canActivate: [UserRouteAccessService],
    outlet: "popup"
  }
];
