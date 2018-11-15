import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";

import { ISlackUser } from "app/shared/model/slack-user.model";
import { SlackUserService } from "./slack-user.service";

@Component({
  selector: "jhi-slack-user-update",
  templateUrl: "./slack-user-update.component.html"
})
export class SlackUserUpdateComponent implements OnInit {
  private _slackUser: ISlackUser;
  isSaving: boolean;

  constructor(
    private slackUserService: SlackUserService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ slackUser }) => {
      this.slackUser = slackUser;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.slackUser.id !== undefined) {
      this.subscribeToSaveResponse(
        this.slackUserService.update(this.slackUser)
      );
    } else {
      this.subscribeToSaveResponse(
        this.slackUserService.create(this.slackUser)
      );
    }
  }

  private subscribeToSaveResponse(
    result: Observable<HttpResponse<ISlackUser>>
  ) {
    result.subscribe(
      (res: HttpResponse<ISlackUser>) => this.onSaveSuccess(),
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
  get slackUser() {
    return this._slackUser;
  }

  set slackUser(slackUser: ISlackUser) {
    this._slackUser = slackUser;
  }
}
