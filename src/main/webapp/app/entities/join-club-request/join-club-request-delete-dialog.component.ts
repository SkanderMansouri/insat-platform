import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJoinClubRequest } from 'app/shared/model/join-club-request.model';
import { JoinClubRequestService } from './join-club-request.service';

@Component({
    selector: 'jhi-join-club-request-delete-dialog',
    templateUrl: './join-club-request-delete-dialog.component.html'
})
export class JoinClubRequestDeleteDialogComponent {
    joinClubRequest: IJoinClubRequest;

    constructor(
        private joinClubRequestService: JoinClubRequestService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.joinClubRequestService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'joinClubRequestListModification',
                content: 'Deleted an joinClubRequest'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-join-club-request-delete-popup',
    template: ''
})
export class JoinClubRequestDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ joinClubRequest }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(JoinClubRequestDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.joinClubRequest = joinClubRequest;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
