import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { SlackOauthComponent } from './slack-oauth.component';

export const slackRoute: Route = {
    path: 'slack_oauth',
    component: SlackOauthComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Slack'
    },
    canActivate: [UserRouteAccessService]
};
