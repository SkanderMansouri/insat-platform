/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { JoinClubRequestDetailComponent } from 'app/entities/join-club-request/join-club-request-detail.component';
import { JoinClubRequest } from 'app/shared/model/join-club-request.model';

describe('Component Tests', () => {
    describe('JoinClubRequest Management Detail Component', () => {
        let comp: JoinClubRequestDetailComponent;
        let fixture: ComponentFixture<JoinClubRequestDetailComponent>;
        const route = ({ data: of({ joinClubRequest: new JoinClubRequest(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [JoinClubRequestDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(JoinClubRequestDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(JoinClubRequestDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.joinClubRequest).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
