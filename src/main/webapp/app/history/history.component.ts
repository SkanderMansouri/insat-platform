import { Component, ChangeDetectionStrategy, ViewChild, TemplateRef } from '@angular/core';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarView } from 'angular-calendar';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IInsatEvent, InsatEvent } from 'app/shared/model/insat-event.model';
import { JhiAlertService } from 'ng-jhipster';
import { InsatEventService } from 'app/entities/insat-event/insat-event.service';
import { OnInit } from '@angular/core';
import * as moment from 'moment';
import _date = moment.unitOfTime._date;
import { Moment } from 'moment';
import { IClub } from 'app/shared/model/club.model';
import { IUser } from 'app/core/user/user.model';

const colors: any = {
    red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
    },
    blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
    },
    yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
    }
};

@Component({
    selector: 'jhi-history',
    changeDetection: ChangeDetectionStrategy.OnPush,
    styleUrls: ['history.css'],

    templateUrl: 'history.component.html'
})
export class HistoryComponent implements OnInit {
    @ViewChild('modalContent')
    modalContent: TemplateRef<any>;

    view: CalendarView = CalendarView.Month;

    CalendarView = CalendarView;

    viewDate: Date = new Date();

    modalData: {
        action: string;
        event: CalendarEvent;
    };
    date: Date = new Date();
    actions: CalendarEventAction[] = [
        {
            label: '<i class="fa fa-fw fa-pencil"></i>',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.handleEvent('Edited', event);
            }
        },
        {
            label: '<i class="fa fa-fw fa-times"></i>',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.events = this.events.filter(iEvent => iEvent !== event);
                this.handleEvent('Deleted', event);
            }
        }
    ];

    refresh: Subject<any> = new Subject();
    event: CalendarEvent;

    events: CalendarEvent[] = [];

    activeDayIsOpen: any = true;
    insatEvents: InsatEvent[];
    insatEvent: InsatEvent;
    constructor(
        private modal: NgbModal,
        private activatedRoute: ActivatedRoute,
        private insatEventService: InsatEventService,
        private jhiAlertService: JhiAlertService
    ) {}
    ngOnInit() {
        this.insatEvents = [];

        this.insatEventService.eventsList().subscribe(clubsList => {
            this.insatEvents = clubsList;
            this.onSucc();
        });
    }
    onSucc() {
        for (let i = 0; i < this.insatEvents.length; i++) {
            console.log(this.insatEvents[i].date.toString());
            this.date = new Date(this.insatEvents[i].date.toString());
            (this.event = {
                id: this.insatEvents[i].id,
                start: this.date,
                end: this.date,
                title: this.insatEvents[i].name,
                color: colors.yellow,
                actions: this.actions,
                allDay: true,
                resizable: {
                    beforeStart: true,
                    afterEnd: true
                },
                draggable: true
            }),
                this.addAnEvent(this.event);
        }
    }

    dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
        if (isSameMonth(date, this.viewDate)) {
            this.viewDate = date;
            if ((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) || events.length === 0) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
            }
        }
        console.log('111111111111');
    }
    eventTimesChanged({ event, newStart, newEnd }: CalendarEventTimesChangedEvent): void {
        event.start = newStart;
        event.end = newEnd;
        this.handleEvent('Dropped or resized', event);
        this.refresh.next();
        console.log('22222222222');
    }

    handleEvent(action: string, event: CalendarEvent): void {
        this.modalData = { event, action };
        this.modal.open(this.modalContent, { size: 'lg' });
        console.log('333333333333333');
    }

    addEvent(): void {
        this.events.push({
            title: 'New event',
            start: startOfDay(new Date()),
            end: endOfDay(new Date()),
            color: colors.red,
            draggable: true,
            resizable: {
                beforeStart: true,
                afterEnd: true
            }
        });
        this.refresh.next();
        console.log('444444444444');
    }
    trackId(index: number, item: InsatEvent) {
        return item.id;
        console.log('55555555555');
    }
    addAnEvent(event: CalendarEvent): void {
        this.events.push(event);
        this.refresh.next();
        console.log('55555555555');
    }
}
