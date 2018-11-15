import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpErrorResponse, HttpResponse } from "@angular/common/http";
import { Subscription } from "rxjs";
import { JhiEventManager, JhiAlertService } from "ng-jhipster";

import { IIntegration } from "app/shared/model/integration.model";
import { Principal } from "app/core";
import { IntegrationService } from "./integration.service";

@Component({
  selector: "jhi-integration",
  templateUrl: "./integration.component.html"
})
export class IntegrationComponent implements OnInit, OnDestroy {
  integrations: IIntegration[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    private integrationService: IntegrationService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private principal: Principal
  ) {}

  loadAll() {
    this.integrationService.query().subscribe(
      (res: HttpResponse<IIntegration[]>) => {
        this.integrations = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInIntegrations();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IIntegration) {
    return item.id;
  }

  registerChangeInIntegrations() {
    this.eventSubscriber = this.eventManager.subscribe(
      "integrationListModification",
      response => this.loadAll()
    );
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
