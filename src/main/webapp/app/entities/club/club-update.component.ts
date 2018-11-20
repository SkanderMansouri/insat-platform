import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';

@Component({
    selector: 'jhi-club-update',
    templateUrl: './club-update.component.html'
})
export class ClubUpdateComponent implements OnInit {
    club: IClub;
    isSaving: boolean;

    constructor(private clubService: ClubService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ club }) => {
            this.club = club;
        });
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
}
