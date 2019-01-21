import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { FlatpickrModule } from 'angularx-flatpickr';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { HistoryComponent } from 'app/history/history.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { insatEventPopupRoute, insatEventRoute } from 'app/entities/insat-event';
const ENTITY_STATES = [...insatEventRoute, ...insatEventPopupRoute];
@NgModule({
    imports: [
        CommonModule,
        BrowserAnimationsModule,
        FormsModule,
        NgbModalModule,
        FlatpickrModule.forRoot(),
        CalendarModule.forRoot({
            provide: DateAdapter,
            useFactory: adapterFactory
        }),
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [HistoryComponent],
    exports: [HistoryComponent]
})
export class HistoryModule {}
