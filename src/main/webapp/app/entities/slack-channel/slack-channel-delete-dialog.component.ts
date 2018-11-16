import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

import {
  NgbActiveModal,
  NgbModal,
  NgbModalRef
} from "@ng-bootstrap/ng-bootstrap";
import { JhiEventManager } from "ng-jhipster";

import { ISlackChannel } from "app/shared/model/slack-channel.model";
import { SlackChannelService } from "./slack-channel.service";

@Component({
  selector: "jhi-slack-channel-delete-dialog",
  templateUrl: "./slack-channel-delete-dialog.component.html"
})
export class SlackChannelDeleteDialogComponent {
  slackChannel: ISlackChannel;

  constructor(
    private slackChannelService: SlackChannelService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss("cancel");
  }

  confirmDelete(id: number) {
    this.slackChannelService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: "slackChannelListModification",
        content: "Deleted an slackChannel"
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: "jhi-slack-channel-delete-popup",
  template: ""
})
export class SlackChannelDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ slackChannel }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(
          SlackChannelDeleteDialogComponent as Component,
          { size: "lg", backdrop: "static" }
        );
        this.ngbModalRef.componentInstance.slackChannel = slackChannel;
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
