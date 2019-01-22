import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { HistoryComponent } from './history.component';

export const historyRoute: Route = {
    path: 'history',
    component: HistoryComponent,
    data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'global.menu.account.history'
    },
    canActivate: [UserRouteAccessService]
};
