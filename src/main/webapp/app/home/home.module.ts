import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { FacebookModule } from 'ngx-facebook';

@NgModule({
    imports: [InsatSharedModule, RouterModule.forChild([HOME_ROUTE]), FacebookModule.forRoot()],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatHomeModule {}
