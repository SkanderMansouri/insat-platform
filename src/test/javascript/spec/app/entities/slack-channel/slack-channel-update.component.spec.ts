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
import { SlackChannelUpdateComponent } from "app/entities/slack-channel/slack-channel-update.component";
import { SlackChannelService } from "app/entities/slack-channel/slack-channel.service";
import { SlackChannel } from "app/shared/model/slack-channel.model";

describe("Component Tests", () => {
  describe("SlackChannel Management Update Component", () => {
    let comp: SlackChannelUpdateComponent;
    let fixture: ComponentFixture<SlackChannelUpdateComponent>;
    let service: SlackChannelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [SlackChannelUpdateComponent]
      })
        .overrideTemplate(SlackChannelUpdateComponent, "")
        .compileComponents();

      fixture = TestBed.createComponent(SlackChannelUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SlackChannelService);
    });

    describe("save", () => {
      it(
        "Should call update service on save for existing entity",
        fakeAsync(() => {
          // GIVEN
          const entity = new SlackChannel(123);
          spyOn(service, "update").and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.slackChannel = entity;
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
          const entity = new SlackChannel();
          spyOn(service, "create").and.returnValue(
            of(new HttpResponse({ body: entity }))
          );
          comp.slackChannel = entity;
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
