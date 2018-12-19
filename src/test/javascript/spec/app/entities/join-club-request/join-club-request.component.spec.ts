/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InsatTestModule } from '../../../test.module';
import { JoinClubRequestComponent } from 'app/entities/join-club-request/join-club-request.component';
import { JoinClubRequestService } from 'app/entities/join-club-request/join-club-request.service';
import { JoinClubRequest } from 'app/shared/model/join-club-request.model';

describe('Component Tests', () => {
    describe('JoinClubRequest Management Component', () => {
        let comp: JoinClubRequestComponent;
        let fixture: ComponentFixture<JoinClubRequestComponent>;
        let service: JoinClubRequestService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [JoinClubRequestComponent],
                providers: []
            })
                .overrideTemplate(JoinClubRequestComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(JoinClubRequestComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JoinClubRequestService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new JoinClubRequest(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.joinClubRequests[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
