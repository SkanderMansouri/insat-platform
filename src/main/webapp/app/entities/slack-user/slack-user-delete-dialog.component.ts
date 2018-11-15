import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from "@ng-bootstrap/ng-bootstrap";
import { JhiEventManager } from "ng-jhipster";

import { ISlackUser } from "app/shared/model/slack-user.model";
import { SlackUserService } from "./slack-user.service";

@Component({
  selector: "jhi-slack-user-delete-dialog",
  templateUrl: "./slack-user-delete-dialog.component.html"
})
export class SlackUserDeleteDialogComponent {
  slackUser: ISlackUser;

  constructor(
    private slackUserService: SlackUserService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss("cancel");
  }

  confirmDelete(id: number) {
    this.slackUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: "slackUserListModification",
        content: "Deleted an slackUser"
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: "jhi-slack-user-delete-popup",
  template: ""
})
export class SlackUserDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ slackUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          SlackUserDeleteDialogComponent as Component,
          { size: "lg", backdrop: "static" }
        );
        this.ngbModalRef.componentInstance.slackUser = slackUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate([{ outlets: { popup: null } }], {
              replaceUrl: true,
              queryParamsHandling: "merge"
            });
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate([{ outlets: { popup: null } }], {
              replaceUrl: true,
              queryParamsHandling: "merge"
            });
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
