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
import { SlackChannelDeleteDialogComponent } from "app/entities/slack-channel/slack-channel-delete-dialog.component";
import { SlackChannelService } from "app/entities/slack-channel/slack-channel.service";

describe("Component Tests", () => {
  describe("SlackChannel Management Delete Component", () => {
    let comp: SlackChannelDeleteDialogComponent;
    let fixture: ComponentFixture<SlackChannelDeleteDialogComponent>;
    let service: SlackChannelService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [SlackChannelDeleteDialogComponent]
      })
        .overrideTemplate(SlackChannelDeleteDialogComponent, "")
        .compileComponents();
      fixture = TestBed.createComponent(SlackChannelDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SlackChannelService);
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
