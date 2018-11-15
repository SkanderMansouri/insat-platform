/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { Observable, of } from "rxjs";
import { HttpHeaders, HttpResponse } from "@angular/common/http";

import { InsatplatformTestModule } from "../../../test.module";
import { IntegrationComponent } from "app/entities/integration/integration.component";
import { IntegrationService } from "app/entities/integration/integration.service";
import { Integration } from "app/shared/model/integration.model";

describe("Component Tests", () => {
  describe("Integration Management Component", () => {
    let comp: IntegrationComponent;
    let fixture: ComponentFixture<IntegrationComponent>;
    let service: IntegrationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [IntegrationComponent],
        providers: []
      })
        .overrideTemplate(IntegrationComponent, "")
        .compileComponents();

      fixture = TestBed.createComponent(IntegrationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IntegrationService);
    });

    it("Should call load all on init", () => {
      // GIVEN
      const headers = new HttpHeaders().append("link", "link;link");
      spyOn(service, "query").and.returnValue(
        of(
          new HttpResponse({
            body: [new Integration(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.integrations[0]).toEqual(
        jasmine.objectContaining({ id: 123 })
      );
    });
  });
});
