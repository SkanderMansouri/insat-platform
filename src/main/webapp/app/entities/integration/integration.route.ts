import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Integration } from 'app/shared/model/integration.model';
import { IntegrationService } from './integration.service';
import { IntegrationComponent } from './integration.component';
import { IntegrationDetailComponent } from './integration-detail.component';
import { IntegrationUpdateComponent } from './integration-update.component';
import { IntegrationDeletePopupComponent } from './integration-delete-dialog.component';
import { IIntegration } from 'app/shared/model/integration.model';

@Injectable({ providedIn: 'root' })
export class IntegrationResolve implements Resolve<IIntegration> {
    constructor(private service: IntegrationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Integration> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Integration>) => response.ok),
                map((integration: HttpResponse<Integration>) => integration.body)
            );
        }
        return of(new Integration());
    }
}

export const integrationRoute: Routes = [
    {
        path: 'integration',
        component: IntegrationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insatApp.integration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'integration/:id/view',
        component: IntegrationDetailComponent,
        resolve: {
            integration: IntegrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.integration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'integration/new',
        component: IntegrationUpdateComponent,
        resolve: {
            integration: IntegrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.integration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'integration/:id/edit',
        component: IntegrationUpdateComponent,
        resolve: {
            integration: IntegrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.integration.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const integrationPopupRoute: Routes = [
    {
        path: 'integration/:id/delete',
        component: IntegrationDeletePopupComponent,
        resolve: {
            integration: IntegrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.integration.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
