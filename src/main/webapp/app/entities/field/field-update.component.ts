import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IField } from 'app/shared/model/field.model';
import { FieldService } from './field.service';

@Component({
    selector: 'jhi-field-update',
    templateUrl: './field-update.component.html'
})
export class FieldUpdateComponent implements OnInit {
    field: IField;
    isSaving: boolean;

    constructor(private fieldService: FieldService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ field }) => {
            this.field = field;
        });
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
        result.subscribe((res: HttpResponse<IField>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
