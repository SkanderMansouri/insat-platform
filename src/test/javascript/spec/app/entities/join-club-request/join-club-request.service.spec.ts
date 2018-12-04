/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { JoinClubRequestService } from 'app/entities/join-club-request/join-club-request.service';
import { IJoinClubRequest, JoinClubRequest, StatusEnumeration } from 'app/shared/model/join-club-request.model';

describe('Service Tests', () => {
    describe('JoinClubRequest Service', () => {
        let injector: TestBed;
        let service: JoinClubRequestService;
        let httpMock: HttpTestingController;
        let elemDefault: IJoinClubRequest;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(JoinClubRequestService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new JoinClubRequest(0, currentDate, StatusEnumeration.PENDING);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        requestTime: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a JoinClubRequest', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        requestTime: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        requestTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new JoinClubRequest(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a JoinClubRequest', async () => {
                const returnedFromService = Object.assign(
                    {
                        requestTime: currentDate.format(DATE_FORMAT),
                        status: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        requestTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of JoinClubRequest', async () => {
                const returnedFromService = Object.assign(
                    {
                        requestTime: currentDate.format(DATE_FORMAT),
                        status: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        requestTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a JoinClubRequest', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
