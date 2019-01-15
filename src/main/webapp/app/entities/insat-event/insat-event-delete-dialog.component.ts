import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInsatEvent } from 'app/shared/model/insat-event.model';
import { InsatEventService } from './insat-event.service';

@Component({
    selector: 'jhi-insat-event-delete-dialog',
    templateUrl: './insat-event-delete-dialog.component.html'
})
export class InsatEventDeleteDialogComponent {
    insatEvent: IInsatEvent;

    constructor(private insatEventService: InsatEventService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.insatEventService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'insatEventListModification',
                content: 'Deleted an insatEvent'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-insat-event-delete-popup',
    template: ''
})
export class InsatEventDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ insatEvent }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InsatEventDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.insatEvent = insatEvent;
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
