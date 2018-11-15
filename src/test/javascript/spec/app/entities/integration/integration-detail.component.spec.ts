/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import { of } from "rxjs";

import { InsatplatformTestModule } from "../../../test.module";
import { IntegrationDetailComponent } from "app/entities/integration/integration-detail.component";
import { Integration } from "app/shared/model/integration.model";

describe("Component Tests", () => {
  describe("Integration Management Detail Component", () => {
    let comp: IntegrationDetailComponent;
    let fixture: ComponentFixture<IntegrationDetailComponent>;
    const route = ({
      data: of({ integration: new Integration(123) })
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsatplatformTestModule],
        declarations: [IntegrationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IntegrationDetailComponent, "")
        .compileComponents();
      fixture = TestBed.createComponent(IntegrationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe("OnInit", () => {
      it("Should call load all on init", () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.integration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
