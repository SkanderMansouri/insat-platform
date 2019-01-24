import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IInsatEvent } from 'app/shared/model/insat-event.model';
import { Principal } from 'app/core';
import { InsatEventService } from './insat-event.service';

@Component({
    selector: 'jhi-insat-event',
    templateUrl: './insat-event.component.html',
    styleUrls: ['EventList.css']
})
export class InsatEventComponent implements OnInit, OnDestroy {
    insatEvents: IInsatEvent[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private insatEventService: InsatEventService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.insatEventService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IInsatEvent[]>) => (this.insatEvents = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }

        this.insatEventService.query().subscribe(
            (res: HttpResponse<IInsatEvent[]>) => {
                this.insatEvents = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInInsatEvents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IInsatEvent) {
        return item.id;
    }

    registerChangeInInsatEvents() {
        this.eventSubscriber = this.eventManager.subscribe('insatEventListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
