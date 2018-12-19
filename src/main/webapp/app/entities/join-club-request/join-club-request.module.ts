import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatSharedModule } from 'app/shared';
import { InsatAdminModule } from 'app/admin/admin.module';
import {
    JoinClubRequestComponent,
    JoinClubRequestDetailComponent,
    JoinClubRequestUpdateComponent,
    JoinClubRequestDeletePopupComponent,
    JoinClubRequestDeleteDialogComponent,
    joinClubRequestRoute,
    joinClubRequestPopupRoute
} from './';

const ENTITY_STATES = [...joinClubRequestRoute, ...joinClubRequestPopupRoute];

@NgModule({
    imports: [InsatSharedModule, InsatAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        JoinClubRequestComponent,
        JoinClubRequestDetailComponent,
        JoinClubRequestUpdateComponent,
        JoinClubRequestDeleteDialogComponent,
        JoinClubRequestDeletePopupComponent
    ],
    entryComponents: [
        JoinClubRequestComponent,
        JoinClubRequestUpdateComponent,
        JoinClubRequestDeleteDialogComponent,
        JoinClubRequestDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatJoinClubRequestModule {}
