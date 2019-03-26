import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';
import { Principal } from 'app/core';
import { JoinClubRequestService } from './join-club-request.service';

@Component({
    selector: 'jhi-manage-club-request',
    templateUrl: './manage-club-request.component.html',
    styles: []
})
export class ManageClubRequestComponent implements OnInit, OnDestroy {
    joinClubRequests: IJoinClubRequest[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    joinClubRequestPendings: IJoinClubRequest[];
    refresh: Subject<any> = new Subject();

    constructor(
        private joinClubRequestService: JoinClubRequestService,
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
            this.joinClubRequestService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IJoinClubRequest[]>) => (this.joinClubRequests = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.joinClubRequestService.query().subscribe(
            (res: HttpResponse<IJoinClubRequest[]>) => {
                this.joinClubRequests = res.body;
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
        this.registerChangeInJoinClubRequests();

        this.joinClubRequestPendings = [];
        this.joinClubRequestService.RequestsPendingList().subscribe(requestsList => {
            this.joinClubRequestPendings = requestsList;
        });
    }

    updateRequestPendingList() {
        this.joinClubRequestService.RequestsPendingList().subscribe(requestsList => {
            this.joinClubRequestPendings = requestsList;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IJoinClubRequest) {
        return item.id;
    }

    registerChangeInJoinClubRequests() {
        this.eventSubscriber = this.eventManager.subscribe('joinClubRequestListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
