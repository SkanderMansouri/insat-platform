import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";

import { ISlackChannel } from "app/shared/model/slack-channel.model";
import { SlackChannelService } from "./slack-channel.service";

@Component({
  selector: "jhi-slack-channel-update",
  templateUrl: "./slack-channel-update.component.html"
})
export class SlackChannelUpdateComponent implements OnInit {
  private _slackChannel: ISlackChannel;
  isSaving: boolean;

  constructor(
    private slackChannelService: SlackChannelService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ slackChannel }) => {
      this.slackChannel = slackChannel;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.slackChannel.id !== undefined) {
      this.subscribeToSaveResponse(
        this.slackChannelService.update(this.slackChannel)
      );
    } else {
      this.subscribeToSaveResponse(
        this.slackChannelService.create(this.slackChannel)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<ISlackChannel>>
  ) {
    result.subscribe(
      (res: HttpResponse<ISlackChannel>) => this.onSaveSuccess(),
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }
  get slackChannel() {
    return this._slackChannel;
  }

  set slackChannel(slackChannel: ISlackChannel) {
    this._slackChannel = slackChannel;
  }
}
