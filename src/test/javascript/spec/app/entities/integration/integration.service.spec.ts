/* tslint:disable max-line-length */
import { TestBed, getTestBed } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController
} from "@angular/common/http/testing";
import { IntegrationService } from "app/entities/integration/integration.service";
import { Integration } from "app/shared/model/integration.model";
import { SERVER_API_URL } from "app/app.constants";

describe("Service Tests", () => {
  describe("Integration Service", () => {
    let injector: TestBed;
    let service: IntegrationService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      injector = getTestBed();
      service = injector.get(IntegrationService);
      httpMock = injector.get(HttpTestingController);
    });

    describe("Service methods", () => {
      it("should call correct URL", () => {
        service.find(123).subscribe(() => {});

        const req = httpMock.expectOne({ method: "GET" });

        const resourceUrl = SERVER_API_URL + "api/integrations";
        expect(req.request.url).toEqual(resourceUrl + "/" + 123);
      });

      it("should create a Integration", () => {
        service.create(new Integration(null)).subscribe(received => {
          expect(received.body.id).toEqual(null);
        });

        const req = httpMock.expectOne({ method: "POST" });
        req.flush({ id: null });
      });

      it("should update a Integration", () => {
        service.update(new Integration(123)).subscribe(received => {
          expect(received.body.id).toEqual(123);
        });

        const req = httpMock.expectOne({ method: "PUT" });
        req.flush({ id: 123 });
      });

      it("should return a Integration", () => {
        service.find(123).subscribe(received => {
          expect(received.body.id).toEqual(123);
        });

        const req = httpMock.expectOne({ method: "GET" });
        req.flush({ id: 123 });
      });

      it("should return a list of Integration", () => {
        service.query(null).subscribe(received => {
          expect(received.body[0].id).toEqual(123);
        });

        const req = httpMock.expectOne({ method: "GET" });
        req.flush([new Integration(123)]);
      });

      it("should delete a Integration", () => {
        service.delete(123).subscribe(received => {
          expect(received.url).toContain("/" + 123);
        });

        const req = httpMock.expectOne({ method: "DELETE" });
        req.flush(null);
      });

      it("should propagate not found response", () => {
        service.find(123).subscribe(null, (_error: any) => {
          expect(_error.status).toEqual(404);
        });

        const req = httpMock.expectOne({ method: "GET" });
        req.flush("Invalid request parameters", {
          status: 404,
          statusText: "Bad Request"
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
