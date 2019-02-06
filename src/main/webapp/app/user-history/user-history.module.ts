import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { FlatpickrModule } from 'angularx-flatpickr';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { insatEventPopupRoute, insatEventRoute } from 'app/entities/insat-event';
import { UserHistoryComponent } from 'app/user-history/user-history.component';
import { DataTableModule } from 'angular-6-datatable';
import { InsatSharedModule } from 'app/shared';
const ENTITY_STATES = [...insatEventRoute, ...insatEventPopupRoute];
@NgModule({
    imports: [
        CommonModule,
        InsatSharedModule,
        BrowserAnimationsModule,
        FormsModule,
        NgbModalModule,
        DataTableModule,
        FlatpickrModule.forRoot(),
        CalendarModule.forRoot({
            provide: DateAdapter,
            useFactory: adapterFactory
        }),
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [UserHistoryComponent],
    exports: [UserHistoryComponent]
})
export class UserHistoryModule {}
