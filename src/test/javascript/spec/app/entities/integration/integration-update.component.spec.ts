/* tslint:disable max-line-length */
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick
} from "@angular/core/testing";
import { HttpResponse } from "@angular/common/http";
import { Observable, of } from "rxjs";

import { InsatplatformTestModule } from "../../../test.module";
import { IntegrationUpdateComponent } from "app/entities/integration/integration-update.component";
import { IntegrationService } from "app/entities/integration/integration.service";
import { Integration } from "app/shared/model/integration.model";

describe("Component Tests", () => {
  describe("Integration Management Update Component", () => {
    let comp: IntegrationUpdateComponent;
    let fixture: ComponentFixture<IntegrationUpdateComponent>;
    let service: IntegrationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [IntegrationUpdateComponent]
      })
        .overrideTemplate(IntegrationUpdateComponent, "")
        .compileComponents();

      fixture = TestBed.createComponent(IntegrationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IntegrationService);
    });

    describe("save", () => {
      it(
        "Should call update service on save for existing entity",
        fakeAsync(() => {
          // GIVEN
          const entity = new Integration(123);
          spyOn(service, "update").and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.integration = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );

      it(
        "Should call create service on save for new entity",
        fakeAsync(() => {
          // GIVEN
          const entity = new Integration();
          spyOn(service, "create").and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.integration = entity;
          // WHEN
          comp.save();
          tick(); // simulate async

          // THEN
          expect(service.create).toHaveBeenCalledWith(entity);
          expect(comp.isSaving).toEqual(false);
        })
      );
    });
  });
});
