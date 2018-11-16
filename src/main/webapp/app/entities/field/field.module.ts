import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { RouterModule } from "@angular/router";

import { InsatplatformSharedModule } from "app/shared";
import { InsatplatformAdminModule } from "app/admin/admin.module";
import {
  FieldComponent,
  FieldDetailComponent,
  FieldUpdateComponent,
  FieldDeletePopupComponent,
  FieldDeleteDialogComponent,
  fieldRoute,
  fieldPopupRoute
} from "./";

const ENTITY_STATES = [...fieldRoute, ...fieldPopupRoute];

@NgModule({
  imports: [
    InsatplatformSharedModule,
    InsatplatformAdminModule,
    RouterModule.forChild(ENTITY_STATES)
  ],
  declarations: [
    FieldComponent,
    FieldDetailComponent,
    FieldUpdateComponent,
    FieldDeleteDialogComponent,
    FieldDeletePopupComponent
  ],
  entryComponents: [
    FieldComponent,
    FieldUpdateComponent,
    FieldDeleteDialogComponent,
    FieldDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatplatformFieldModule {}
