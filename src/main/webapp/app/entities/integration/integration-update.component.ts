import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IIntegration } from 'app/shared/model/integration.model';
import { IntegrationService } from './integration.service';
import { IUser, UserService } from 'app/core';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-integration-update',
    templateUrl: './integration-update.component.html'
})
export class IntegrationUpdateComponent implements OnInit {
    integration: IIntegration;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private integrationService: IntegrationService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ integration }) => {
            this.integration = integration;
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
        if (this.integration.id !== undefined) {
            this.subscribeToSaveResponse(this.integrationService.update(this.integration));
        } else {
            this.subscribeToSaveResponse(this.integrationService.create(this.integration));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIntegration>>) {
        result.subscribe((res: HttpResponse<IIntegration>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
