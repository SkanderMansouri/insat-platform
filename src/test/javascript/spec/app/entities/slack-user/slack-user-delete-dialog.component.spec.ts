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
import { SlackUserDeleteDialogComponent } from "app/entities/slack-user/slack-user-delete-dialog.component";
import { SlackUserService } from "app/entities/slack-user/slack-user.service";

describe("Component Tests", () => {
  describe("SlackUser Management Delete Component", () => {
    let comp: SlackUserDeleteDialogComponent;
    let fixture: ComponentFixture<SlackUserDeleteDialogComponent>;
    let service: SlackUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [SlackUserDeleteDialogComponent]
      })
        .overrideTemplate(SlackUserDeleteDialogComponent, "")
        .compileComponents();
      fixture = TestBed.createComponent(SlackUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SlackUserService);
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
