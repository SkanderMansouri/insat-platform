import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SlackChannel } from 'app/shared/model/slack-channel.model';
import { SlackChannelService } from './slack-channel.service';
import { SlackChannelComponent } from './slack-channel.component';
import { SlackChannelDetailComponent } from './slack-channel-detail.component';
import { SlackChannelUpdateComponent } from './slack-channel-update.component';
import { SlackChannelDeletePopupComponent } from './slack-channel-delete-dialog.component';
import { ISlackChannel } from 'app/shared/model/slack-channel.model';

@Injectable({ providedIn: 'root' })
export class SlackChannelResolve implements Resolve<ISlackChannel> {
    constructor(private service: SlackChannelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<SlackChannel> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SlackChannel>) => response.ok),
                map((slackChannel: HttpResponse<SlackChannel>) => slackChannel.body)
            );
        }
        return of(new SlackChannel());
    }
}

export const slackChannelRoute: Routes = [
    {
        path: 'slack-channel',
        component: SlackChannelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.slackChannel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'slack-channel/:id/view',
        component: SlackChannelDetailComponent,
        resolve: {
            slackChannel: SlackChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.slackChannel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'slack-channel/new',
        component: SlackChannelUpdateComponent,
        resolve: {
            slackChannel: SlackChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.slackChannel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'slack-channel/:id/edit',
        component: SlackChannelUpdateComponent,
        resolve: {
            slackChannel: SlackChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.slackChannel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const slackChannelPopupRoute: Routes = [
    {
        path: 'slack-channel/:id/delete',
        component: SlackChannelDeletePopupComponent,
        resolve: {
            slackChannel: SlackChannelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insatApp.slackChannel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
