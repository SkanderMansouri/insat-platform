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
import { IUser, User } from 'app/core/user/user.model';
import { Principal } from 'app/core';

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
    selector: 'jhi-user-history',
    changeDetection: ChangeDetectionStrategy.OnPush,
    styleUrls: ['user-history.css'],

    templateUrl: 'user-history.component.html'
})
export class UserHistoryComponent implements OnInit {
    @ViewChild('modalContent')
    modalContent: TemplateRef<any>;
    num: number;

    view: CalendarView = CalendarView.Month;

    CalendarView = CalendarView;
    user: User;
    viewDate: Date = new Date();

    modalData: {
        action: string;
        event: CalendarEvent;
    };
    date: Date = new Date();
    currentAccount: any;

    actions: CalendarEventAction[] = [];

    refresh: Subject<any> = new Subject();
    event: CalendarEvent;

    events: CalendarEvent[] = [];
    allEvents: CalendarEvent[] = [];

    activeDayIsOpen: any = true;
    insatEvents: InsatEvent[];
    insatAllEvents: InsatEvent[];
    insatEvent: InsatEvent;
    constructor(
        private modal: NgbModal,
        private activatedRoute: ActivatedRoute,
        private insatEventService: InsatEventService,
        private router: Router,
        private principal: Principal,
        private jhiAlertService: JhiAlertService
    ) {}
    ngOnInit() {
        this.insatEvents = [];
        this.insatEventService.notMemberEventsList().subscribe(clubsList => {
            this.insatAllEvents = clubsList;
            console.log(this.insatAllEvents[0].name);
            this.onAllSucc();
        });
        this.insatEventService.usereventsList().subscribe(List => {
            this.insatEvents = List.body;
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
    onAllSucc() {
        for (let i = 0; i < this.insatAllEvents.length; i++) {
            {
                this.date = new Date(this.insatAllEvents[i].date.toString());
                (this.event = {
                    id: this.insatAllEvents[i].id,
                    start: this.date,
                    end: this.date,
                    title: this.insatAllEvents[i].name,
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
                    this.addAnAllEvent(this.event);
            }
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
        let url = this.router.createUrlTree(['/insat-event', event.id, 'view']);
        this.router.navigateByUrl(url.toString());
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
    }
    addAnAllEvent(event: CalendarEvent): void {
        console.log('55555555555');
        this.allEvents.push(event);
        this.refresh.next();
    }
    clickdel(index: number) {
        console.log(this.insatEvents[index].date.toString());

        this.allEvents.push(this.events[index]);
        this.events.splice(index, 1);
        this.insatEventService.updateMembersList(this.insatEvents[index]).subscribe(() => {});

        this.refresh.next();
    }
    clickparticpate(index: number) {
        this.events.push(this.allEvents[index]);
        this.allEvents.splice(index, 1);
        this.refresh.next();
        this.insatEventService.AddToMembersList(this.insatAllEvents[index]).subscribe(() => {});
    }
}
