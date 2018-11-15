import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { JhiAlertService } from "ng-jhipster";

import { IField } from "app/shared/model/field.model";
import { FieldService } from "./field.service";
import { IUser, UserService } from "app/core";

@Component({
  selector: "jhi-field-update",
  templateUrl: "./field-update.component.html"
})
export class FieldUpdateComponent implements OnInit {
  private _field: IField;
  isSaving: boolean;

  users: IUser[];

  constructor(
    private jhiAlertService: JhiAlertService,
    private fieldService: FieldService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ field }) => {
      this.field = field;
    });
    this.userService.query().subscribe(
      (res: HttpResponse<IUser[]>) => {
        this.users = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    if (this.field.id !== undefined) {
      this.subscribeToSaveResponse(this.fieldService.update(this.field));
    } else {
      this.subscribeToSaveResponse(this.fieldService.create(this.field));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IField>>) {
    result.subscribe(
      (res: HttpResponse<IField>) => this.onSaveSuccess(),
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

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
  get field() {
    return this._field;
  }

  set field(field: IField) {
    this._field = field;
  }
}
