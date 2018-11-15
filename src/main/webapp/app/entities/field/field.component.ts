import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpErrorResponse, HttpResponse } from "@angular/common/http";
import { Subscription } from "rxjs";
import { JhiEventManager, JhiAlertService } from "ng-jhipster";

import { IField } from "app/shared/model/field.model";
import { Principal } from "app/core";
import { FieldService } from "./field.service";

@Component({
  selector: "jhi-field",
  templateUrl: "./field.component.html"
})
export class FieldComponent implements OnInit, OnDestroy {
  fields: IField[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    private fieldService: FieldService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private principal: Principal
  ) {}

  loadAll() {
    this.fieldService.query().subscribe(
      (res: HttpResponse<IField[]>) => {
        this.fields = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInFields();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IField) {
    return item.id;
  }

  registerChangeInFields() {
    this.eventSubscriber = this.eventManager.subscribe(
      "fieldListModification",
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
