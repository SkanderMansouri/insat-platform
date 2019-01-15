import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IInsatEvent } from 'app/shared/model/insat-event.model';
import { InsatEventService } from './insat-event.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-insat-event-update',
    templateUrl: './insat-event-update.component.html'
})
export class InsatEventUpdateComponent implements OnInit {
    insatEvent: IInsatEvent;
    isSaving: boolean;

    clubs: IClub[];

    users: IUser[];
    date: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private insatEventService: InsatEventService,
        private clubService: ClubService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ insatEvent }) => {
            this.insatEvent = insatEvent;
            this.date = null;
        });
        this.clubService.query().subscribe(
            (res: HttpResponse<IClub[]>) => {
                this.clubs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.insatEvent.date = moment(this.date, DATE_TIME_FORMAT);
        if (this.insatEvent.id !== undefined) {
            this.subscribeToSaveResponse(this.insatEventService.update(this.insatEvent));
        } else {
            console.log(' date is  {} ', this.insatEvent.date);
            this.subscribeToSaveResponse(this.insatEventService.create(this.insatEvent));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInsatEvent>>) {
        result.subscribe((res: HttpResponse<IInsatEvent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackClubById(index: number, item: IClub) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
