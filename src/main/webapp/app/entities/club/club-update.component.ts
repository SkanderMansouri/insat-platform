import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-club-update',
    templateUrl: './club-update.component.html',
    styleUrls: ['clubUpdate.css']
})
export class ClubUpdateComponent implements OnInit {
    club: IClub;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private clubService: ClubService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ club }) => {
            this.club = club;
        });
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
        if (this.club.id !== undefined) {
            this.subscribeToSaveResponse(this.clubService.update(this.club));
        } else {
            this.subscribeToSaveResponse(this.clubService.create(this.club));
        }
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
}
