/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import { of } from "rxjs";

import { InsatplatformTestModule } from "../../../test.module";
import { SlackUserDetailComponent } from "app/entities/slack-user/slack-user-detail.component";
import { SlackUser } from "app/shared/model/slack-user.model";

describe("Component Tests", () => {
  describe("SlackUser Management Detail Component", () => {
    let comp: SlackUserDetailComponent;
    let fixture: ComponentFixture<SlackUserDetailComponent>;
    const route = ({
      data: of({ slackUser: new SlackUser(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [SlackUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SlackUserDetailComponent, "")
        .compileComponents();
      fixture = TestBed.createComponent(SlackUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe("OnInit", () => {
      it("Should call load all on init", () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.slackUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
