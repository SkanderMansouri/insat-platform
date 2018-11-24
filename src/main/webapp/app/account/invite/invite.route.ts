import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { InviteComponent } from './invite.component';

export const inviteRoute: Route = {
    path: 'invite',
    component: InviteComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.account.invite'
    },
    canActivate: [UserRouteAccessService]
};
