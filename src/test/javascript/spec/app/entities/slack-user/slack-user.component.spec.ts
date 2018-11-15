/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { Observable, of } from "rxjs";
import { HttpHeaders, HttpResponse } from "@angular/common/http";

import { InsatplatformTestModule } from "../../../test.module";
import { SlackUserComponent } from "app/entities/slack-user/slack-user.component";
import { SlackUserService } from "app/entities/slack-user/slack-user.service";
import { SlackUser } from "app/shared/model/slack-user.model";

describe("Component Tests", () => {
  describe("SlackUser Management Component", () => {
    let comp: SlackUserComponent;
    let fixture: ComponentFixture<SlackUserComponent>;
    let service: SlackUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [SlackUserComponent],
        providers: []
      })
        .overrideTemplate(SlackUserComponent, "")
        .compileComponents();

      fixture = TestBed.createComponent(SlackUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SlackUserService);
    });

    it("Should call load all on init", () => {
      // GIVEN
      const headers = new HttpHeaders().append("link", "link;link");
      spyOn(service, "query").and.returnValue(
        of(
          new HttpResponse({
            body: [new SlackUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.slackUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
