import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatSharedModule } from 'app/shared';
import { InsatAdminModule } from 'app/admin/admin.module';
import {
    InsatEventComponent,
    InsatEventDetailComponent,
    InsatEventUpdateComponent,
    InsatEventDeletePopupComponent,
    InsatEventDeleteDialogComponent,
    insatEventRoute,
    insatEventPopupRoute
} from './';

const ENTITY_STATES = [...insatEventRoute, ...insatEventPopupRoute];

@NgModule({
    imports: [InsatSharedModule, InsatAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InsatEventComponent,
        InsatEventDetailComponent,
        InsatEventUpdateComponent,
        InsatEventDeleteDialogComponent,
        InsatEventDeletePopupComponent
    ],
    entryComponents: [InsatEventComponent, InsatEventUpdateComponent, InsatEventDeleteDialogComponent, InsatEventDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatInsatEventModule {}
