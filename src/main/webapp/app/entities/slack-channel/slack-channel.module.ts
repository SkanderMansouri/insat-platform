import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatplatformSharedModule } from 'app/shared';
import {
  SlackChannelComponent,
  SlackChannelDetailComponent,
  SlackChannelUpdateComponent,
  SlackChannelDeletePopupComponent,
  SlackChannelDeleteDialogComponent,
  slackChannelRoute,
  slackChannelPopupRoute
} from './';

const ENTITY_STATES = [...slackChannelRoute, ...slackChannelPopupRoute];

@NgModule({
  imports: [InsatplatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SlackChannelComponent,
    SlackChannelDetailComponent,
    SlackChannelUpdateComponent,
    SlackChannelDeleteDialogComponent,
    SlackChannelDeletePopupComponent
  ],
  entryComponents: [
    SlackChannelComponent,
    SlackChannelUpdateComponent,
    SlackChannelDeleteDialogComponent,
    SlackChannelDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatplatformSlackChannelModule {}
