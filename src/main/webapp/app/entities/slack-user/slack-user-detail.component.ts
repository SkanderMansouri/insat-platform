import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { ISlackUser } from "app/shared/model/slack-user.model";

@Component({
  selector: "jhi-slack-user-detail",
  templateUrl: "./slack-user-detail.component.html"
})
export class SlackUserDetailComponent implements OnInit {
  slackUser: ISlackUser;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ slackUser }) => {
      this.slackUser = slackUser;
    });
  }

  previousState() {
    window.history.back();
  }
}
