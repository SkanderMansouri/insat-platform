import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatSharedModule } from 'app/shared';
import {
    SlackUserComponent,
    SlackUserDetailComponent,
    SlackUserUpdateComponent,
    SlackUserDeletePopupComponent,
    SlackUserDeleteDialogComponent,
    slackUserRoute,
    slackUserPopupRoute
} from './';

const ENTITY_STATES = [...slackUserRoute, ...slackUserPopupRoute];

@NgModule({
    imports: [InsatSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SlackUserComponent,
        SlackUserDetailComponent,
        SlackUserUpdateComponent,
        SlackUserDeleteDialogComponent,
        SlackUserDeletePopupComponent
    ],
    entryComponents: [SlackUserComponent, SlackUserUpdateComponent, SlackUserDeleteDialogComponent, SlackUserDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatSlackUserModule {}
