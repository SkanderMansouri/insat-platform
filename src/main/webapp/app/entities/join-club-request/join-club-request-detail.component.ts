import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';

@Component({
    selector: 'jhi-join-club-request-detail',
    templateUrl: './join-club-request-detail.component.html'
})
export class JoinClubRequestDetailComponent implements OnInit {
    joinClubRequest: IJoinClubRequest;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ joinClubRequest }) => {
            this.joinClubRequest = joinClubRequest;
        });
    }

    previousState() {
        window.history.back();
    }
}
