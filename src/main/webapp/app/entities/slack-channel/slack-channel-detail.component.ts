import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { ISlackChannel } from "app/shared/model/slack-channel.model";

@Component({
  selector: "jhi-slack-channel-detail",
  templateUrl: "./slack-channel-detail.component.html"
})
export class SlackChannelDetailComponent implements OnInit {
  slackChannel: ISlackChannel;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ slackChannel }) => {
      this.slackChannel = slackChannel;
    });
  }

  previousState() {
    window.history.back();
  }
}
