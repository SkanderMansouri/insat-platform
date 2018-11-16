import { Component, OnInit, OnDestroy } from "@angular/core";
import {
  HttpErrorResponse,
  HttpHeaders,
  HttpResponse
} from "@angular/common/http";
import { Subscription } from "rxjs";
import { JhiEventManager, JhiParseLinks, JhiAlertService } from "ng-jhipster";

import { ISlackChannel } from "app/shared/model/slack-channel.model";
import { Principal } from "app/core";

import { ITEMS_PER_PAGE } from "app/shared";
import { SlackChannelService } from "./slack-channel.service";

@Component({
  selector: "jhi-slack-channel",
  templateUrl: "./slack-channel.component.html"
})
export class SlackChannelComponent implements OnInit, OnDestroy {
  slackChannels: ISlackChannel[];
  currentAccount: any;
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  queryCount: any;
  reverse: any;
  totalItems: number;

  constructor(
    private slackChannelService: SlackChannelService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private parseLinks: JhiParseLinks,
    private principal: Principal
  ) {
    this.slackChannels = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = "id";
    this.reverse = true;
  }

  loadAll() {
    this.slackChannelService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ISlackChannel[]>) =>
          this.paginateSlackChannels(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  reset() {
    this.page = 0;
    this.slackChannels = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSlackChannels();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISlackChannel) {
    return item.id;
  }

  registerChangeInSlackChannels() {
    this.eventSubscriber = this.eventManager.subscribe(
      "slackChannelListModification",
      response => this.reset()
    );
  }

  sort() {
    const result = [this.predicate + "," + (this.reverse ? "asc" : "desc")];
    if (this.predicate !== "id") {
      result.push("id");
    }
    return result;
  }

  private paginateSlackChannels(data: ISlackChannel[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get("link"));
    this.totalItems = parseInt(headers.get("X-Total-Count"), 10);
    for (let i = 0; i < data.length; i++) {
      this.slackChannels.push(data[i]);
    }
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
