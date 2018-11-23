import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { ISlackUser } from 'app/shared/model/slack-user.model';
import { SlackUserService } from 'app/entities/slack-user';

@Component({
    selector: 'jhi-club-update',
    templateUrl: './club-update.component.html'
})
export class ClubUpdateComponent implements OnInit {
    club: IClub;
    isSaving: boolean;

    slackusers: ISlackUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private clubService: ClubService,
        private slackUserService: SlackUserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ club }) => {
            this.club = club;
        });
        this.slackUserService.query().subscribe(
            (res: HttpResponse<ISlackUser[]>) => {
                this.slackusers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.club.id !== undefined) {
            this.subscribeToSaveResponse(this.clubService.update(this.club));
        } else {
            this.subscribeToSaveResponse(this.clubService.create(this.club));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IClub>>) {
        result.subscribe((res: HttpResponse<IClub>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSlackUserById(index: number, item: ISlackUser) {
        return item.id;
    }
}
