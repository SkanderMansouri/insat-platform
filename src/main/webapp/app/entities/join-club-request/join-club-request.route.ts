import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JoinClubRequest } from 'app/shared/model/join-club-request.model';
import { JoinClubRequestService } from './join-club-request.service';
import { JoinClubRequestComponent } from './join-club-request.component';
import { JoinClubRequestDetailComponent } from './join-club-request-detail.component';
import { JoinClubRequestUpdateComponent } from './join-club-request-update.component';
import { JoinClubRequestDeletePopupComponent } from './join-club-request-delete-dialog.component';
import { ManageClubRequestComponent } from './manage-club-request.component';
import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';

@Injectable({ providedIn: 'root' })
export class JoinClubRequestResolve implements Resolve<IJoinClubRequest> {
    constructor(private service: JoinClubRequestService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<JoinClubRequest> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<JoinClubRequest>) => response.ok),
                map((joinClubRequest: HttpResponse<JoinClubRequest>) => joinClubRequest.body)
            );
        }
        return of(new JoinClubRequest());
    }
}

export const joinClubRequestRoute: Routes = [
    {
        path: 'join-club-request',
        component: JoinClubRequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.joinClubRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'join-club-request/:id/view',
        component: JoinClubRequestDetailComponent,
        resolve: {
            joinClubRequest: JoinClubRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.joinClubRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'join-club-request/new',
        component: JoinClubRequestUpdateComponent,
        resolve: {
            joinClubRequest: JoinClubRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.joinClubRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'join-club-request/:id/edit',
        component: JoinClubRequestUpdateComponent,
        resolve: {
            joinClubRequest: JoinClubRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.joinClubRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'manage-request',
        component: ManageClubRequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.joinClubRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const joinClubRequestPopupRoute: Routes = [
    {
        path: 'join-club-request/:id/delete',
        component: JoinClubRequestDeletePopupComponent,
        resolve: {
            joinClubRequest: JoinClubRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.joinClubRequest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
