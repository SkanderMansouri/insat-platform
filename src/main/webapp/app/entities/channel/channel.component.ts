import { Component, OnInit, OnDestroy } from "@angular/core";
import {
  HttpErrorResponse,
  HttpHeaders,
  HttpResponse
} from "@angular/common/http";
import { Subscription } from "rxjs";
import { JhiEventManager, JhiParseLinks, JhiAlertService } from "ng-jhipster";

import { IChannel } from "app/shared/model/channel.model";
import { Principal } from "app/core";

import { ITEMS_PER_PAGE } from "app/shared";
import { ChannelService } from "./channel.service";

@Component({
  selector: "jhi-channel",
  templateUrl: "./channel.component.html"
})
export class ChannelComponent implements OnInit, OnDestroy {
  channels: IChannel[];
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
    private channelService: ChannelService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private parseLinks: JhiParseLinks,
    private principal: Principal
  ) {
    this.channels = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = "id";
    this.reverse = true;
  }

  loadAll() {
    this.channelService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IChannel[]>) =>
          this.paginateChannels(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  reset() {
    this.page = 0;
    this.channels = [];
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
    this.registerChangeInChannels();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IChannel) {
    return item.id;
  }

  registerChangeInChannels() {
    this.eventSubscriber = this.eventManager.subscribe(
      "channelListModification",
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

  private paginateChannels(data: IChannel[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get("link"));
    this.totalItems = parseInt(headers.get("X-Total-Count"), 10);
    for (let i = 0; i < data.length; i++) {
      this.channels.push(data[i]);
    }
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
