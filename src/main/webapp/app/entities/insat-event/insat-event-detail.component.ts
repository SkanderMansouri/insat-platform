import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInsatEvent } from 'app/shared/model/insat-event.model';

@Component({
    selector: 'jhi-insat-event-detail',
    templateUrl: './insat-event-detail.component.html',
    styleUrls: ['EventDetail.css']
})
export class InsatEventDetailComponent implements OnInit {
    insatEvent: IInsatEvent;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ insatEvent }) => {
            this.insatEvent = insatEvent;
        });
    }

    previousState() {
        window.history.back();
    }
}
