import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClub } from 'app/shared/model/club.model';

@Component({
    selector: 'jhi-club-detail',
    templateUrl: './club-detail.component.html'
})
export class ClubDetailComponent implements OnInit {
    club: IClub;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ club }) => {
            this.club = club;
        });
    }

    previousState() {
        window.history.back();
    }
}
