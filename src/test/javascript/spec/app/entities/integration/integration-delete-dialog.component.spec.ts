/* tslint:disable max-line-length */
import {
  ComponentFixture,
  TestBed,
  inject,
  fakeAsync,
  tick
} from "@angular/core/testing";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Observable, of } from "rxjs";
import { JhiEventManager } from "ng-jhipster";

import { InsatplatformTestModule } from "../../../test.module";
import { IntegrationDeleteDialogComponent } from "app/entities/integration/integration-delete-dialog.component";
import { IntegrationService } from "app/entities/integration/integration.service";

describe("Component Tests", () => {
  describe("Integration Management Delete Component", () => {
    let comp: IntegrationDeleteDialogComponent;
    let fixture: ComponentFixture<IntegrationDeleteDialogComponent>;
    let service: IntegrationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [IntegrationDeleteDialogComponent]
      })
        .overrideTemplate(IntegrationDeleteDialogComponent, "")
        .compileComponents();
      fixture = TestBed.createComponent(IntegrationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IntegrationService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe("confirmDelete", () => {
      it(
        "Should call delete service on confirmDelete",
        inject(
          [],
          fakeAsync(() => {
            // GIVEN
            spyOn(service, "delete").and.returnValue(of({}));

            // WHEN
            comp.confirmDelete(123);
            tick();

            // THEN
            expect(service.delete).toHaveBeenCalledWith(123);
            expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
            expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
          })
        )
      );
    });
  });
});
