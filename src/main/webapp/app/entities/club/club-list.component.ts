import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IClub } from 'app/shared/model/club.model';
import { Principal } from 'app/core';
import { ClubService } from './club.service';

@Component({
    selector: 'jhi-club',
    templateUrl: './club-list.component.html',
    styleUrls: ['club.css']
})
export class ClubListComponent implements OnInit, OnDestroy {
    clubs: IClub[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    UsersClubs: IClub[];
    notMemberClubsList: IClub[];
    usersRequest: IClub[];
    refresh: Subject<any> = new Subject();

    constructor(
        private clubService: ClubService,
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
            this.clubService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IClub[]>) => (this.clubs = res.body), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.clubService.query().subscribe(
            (res: HttpResponse<IClub[]>) => {
                this.clubs = res.body;
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
        this.registerChangeInClubs();
        this.UsersClubs = [];
        this.clubService.clubsUserList().subscribe(clubsList => {
            this.UsersClubs = clubsList;
        });
        this.notMemberClubsList = [];
        this.clubService.NotMemberClubList().subscribe(clubsList => {
            this.notMemberClubsList = clubsList;
        });
        this.usersRequest = [];
        this.clubService.RequestsList().subscribe(clubsList => {
            this.usersRequest = clubsList;
        });
        this.clubService.refresh.subscribe(() => {
            this.getAllRequest();
        });
        this.clubService.refresh.subscribe(() => {
            this.getAllNonMemberClubs();
        });
    }

    getAllNonMemberClubs() {
        this.clubService.NotMemberClubList().subscribe(clubsList => {
            this.notMemberClubsList = clubsList;
        });
    }
    getAllRequest() {
        this.clubService.RequestsList().subscribe(clubsList => {
            this.usersRequest = clubsList;
        });
    }
    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IClub) {
        return item.id;
    }

    registerChangeInClubs() {
        this.eventSubscriber = this.eventManager.subscribe('clubListModification', response => this.loadAll());
    }
    createJoinRequest(idClub: number) {
        this.clubService.createRequest(idClub).subscribe(request => {});
    }
    deleteJoinRequest(idClub: number) {
        this.clubService.deleteRequest(idClub).subscribe(request => {});
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
