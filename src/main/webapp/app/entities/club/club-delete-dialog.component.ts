import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';

@Component({
    selector: 'jhi-club-delete-dialog',
    templateUrl: './club-delete-dialog.component.html'
})
export class ClubDeleteDialogComponent {
    club: IClub;

    constructor(private clubService: ClubService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clubService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'clubListModification',
                content: 'Deleted an club'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-club-delete-popup',
    template: ''
})
export class ClubDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ club }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ClubDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.club = club;
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
