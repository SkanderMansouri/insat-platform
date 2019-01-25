import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { UserHistoryComponent } from 'app/user-history/user-history.component';

export const userhistoryRoute: Route = {
    path: 'userhistory',
    component: UserHistoryComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.account.userhistory'
    },
    canActivate: [UserRouteAccessService]
};
