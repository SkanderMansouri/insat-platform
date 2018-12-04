import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';
import { JoinClubRequestService } from './join-club-request.service';
import { IUser, UserService } from 'app/core';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club';

@Component({
    selector: 'jhi-join-club-request-update',
    templateUrl: './join-club-request-update.component.html'
})
export class JoinClubRequestUpdateComponent implements OnInit {
    joinClubRequest: IJoinClubRequest;
    isSaving: boolean;

    users: IUser[];

    clubs: IClub[];
    requestTimeDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private joinClubRequestService: JoinClubRequestService,
        private userService: UserService,
        private clubService: ClubService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ joinClubRequest }) => {
            this.joinClubRequest = joinClubRequest;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.clubService.query().subscribe(
            (res: HttpResponse<IClub[]>) => {
                this.clubs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.joinClubRequest.id !== undefined) {
            this.subscribeToSaveResponse(this.joinClubRequestService.update(this.joinClubRequest));
        } else {
            this.subscribeToSaveResponse(this.joinClubRequestService.create(this.joinClubRequest));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IJoinClubRequest>>) {
        result.subscribe((res: HttpResponse<IJoinClubRequest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackClubById(index: number, item: IClub) {
        return item.id;
    }
}
