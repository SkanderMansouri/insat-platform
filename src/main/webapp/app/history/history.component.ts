import { Component, ChangeDetectionStrategy, ViewChild, TemplateRef } from '@angular/core';
import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarView } from 'angular-calendar';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
    @ViewChild('dataTable')
    table;
    dataTable: any;
    @ViewChild('modalContent')
    modalContent: TemplateRef<any>;
    num: number;

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
            label: '✏️',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.handleEvent('edit', event);
            }
        },
        {
            label: '❌ ',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.handleEvent('delete', event);
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
        private router: Router,
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
                color: {
                    primary: '#' + (0x1000000 + Math.random() * 0xffffff).toString(16).substr(1, 6),
                    secondary: '#' + (0x1000000 + Math.random() * 0xffffff).toString(16).substr(1, 6)
                },
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
        action = action === 'Clicked' ? 'edit' : action;
        this.modalData = { event, action };
        let url = this.router.createUrlTree(['/', { outlets: { popup: 'insat-event/' + event.id + '/delete' } }]);

        if (action === 'edit') {
            url = this.router.createUrlTree(['/insat-event', event.id, 'edit']);
        }
        this.router.navigateByUrl(url.toString());
        if (action === 'delete') {
            this.insatEventService.event.subscribe(data => {
                if (data) {
                    this.events.indexOf(event, this.num);
                    this.events.splice(this.num, 1);
                    this.refresh.next();
                }
            });
        }
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
    clickdel(index: number) {
        console.log('hoy');
        this.insatEventService.event.subscribe(data => {
            console.log('index', index); // {data: 'some data'}
            if (data) {
                this.events.splice(index, 1);
                this.refresh.next();
            }
        });
    }
}
