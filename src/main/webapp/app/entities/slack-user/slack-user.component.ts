import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpErrorResponse, HttpResponse } from "@angular/common/http";
import { Subscription } from "rxjs";
import { JhiEventManager, JhiAlertService } from "ng-jhipster";

import { ISlackUser } from "app/shared/model/slack-user.model";
import { Principal } from "app/core";
import { SlackUserService } from "./slack-user.service";

@Component({
  selector: "jhi-slack-user",
  templateUrl: "./slack-user.component.html"
})
export class SlackUserComponent implements OnInit, OnDestroy {
  slackUsers: ISlackUser[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    private slackUserService: SlackUserService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private principal: Principal
  ) {}

  loadAll() {
    this.slackUserService.query().subscribe(
      (res: HttpResponse<ISlackUser[]>) => {
        this.slackUsers = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSlackUsers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISlackUser) {
    return item.id;
  }

  registerChangeInSlackUsers() {
    this.eventSubscriber = this.eventManager.subscribe(
      "slackUserListModification",
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
