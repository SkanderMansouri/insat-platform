import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { InsatEvent } from 'app/shared/model/insat-event.model';
import { InsatEventService } from './insat-event.service';
import { InsatEventComponent } from './insat-event.component';
import { InsatEventDetailComponent } from './insat-event-detail.component';
import { InsatEventUpdateComponent } from './insat-event-update.component';
import { InsatEventDeletePopupComponent } from './insat-event-delete-dialog.component';
import { IInsatEvent } from 'app/shared/model/insat-event.model';

@Injectable({ providedIn: 'root' })
export class InsatEventResolve implements Resolve<IInsatEvent> {
    constructor(private service: InsatEventService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<InsatEvent> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<InsatEvent>) => response.ok),
                map((insatEvent: HttpResponse<InsatEvent>) => insatEvent.body)
            );
        }
        return of(new InsatEvent());
    }
}

export const insatEventRoute: Routes = [
    {
        path: 'insat-event',
        component: InsatEventComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.insatEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'insat-event/:id/view',
        component: InsatEventDetailComponent,
        resolve: {
            insatEvent: InsatEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.insatEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'insat-event/new',
        component: InsatEventUpdateComponent,
        resolve: {
            insatEvent: InsatEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.insatEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'insat-event/:id/edit',
        component: InsatEventUpdateComponent,
        resolve: {
            insatEvent: InsatEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.insatEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const insatEventPopupRoute: Routes = [
    {
        path: 'insat-event/:id/delete',
        component: InsatEventDeletePopupComponent,
        resolve: {
            insatEvent: InsatEventResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.insatEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
