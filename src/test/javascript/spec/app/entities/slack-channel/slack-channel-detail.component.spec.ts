/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import { of } from "rxjs";

import { InsatplatformTestModule } from "../../../test.module";
import { SlackChannelDetailComponent } from "app/entities/slack-channel/slack-channel-detail.component";
import { SlackChannel } from "app/shared/model/slack-channel.model";

describe("Component Tests", () => {
  describe("SlackChannel Management Detail Component", () => {
    let comp: SlackChannelDetailComponent;
    let fixture: ComponentFixture<SlackChannelDetailComponent>;
    const route = ({
      data: of({ slackChannel: new SlackChannel(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [SlackChannelDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SlackChannelDetailComponent, "")
        .compileComponents();
      fixture = TestBed.createComponent(SlackChannelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe("OnInit", () => {
      it("Should call load all on init", () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.slackChannel).toEqual(
          jasmine.objectContaining({ id: 123 })
        );
      });
    });
  });
});
