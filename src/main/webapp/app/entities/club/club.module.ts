import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatSharedModule } from 'app/shared';
import { InsatAdminModule } from 'app/admin/admin.module';
import {
    ClubComponent,
    ClubDeleteDialogComponent,
    ClubDeletePopupComponent,
    ClubDetailComponent,
    clubPopupRoute,
    clubRoute,
    ClubUpdateComponent
} from './';
import { ClubListComponent } from './club-list.component';
import { AllClubComponent } from './all-club.component';

const ENTITY_STATES = [...clubRoute, ...clubPopupRoute];

@NgModule({
    imports: [InsatSharedModule, InsatAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClubComponent,
        ClubDetailComponent,
        ClubUpdateComponent,
        ClubDeleteDialogComponent,
        ClubDeletePopupComponent,
        ClubListComponent,
        AllClubComponent
    ],
    entryComponents: [ClubComponent, ClubUpdateComponent, ClubDeleteDialogComponent, ClubDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatClubModule {}
