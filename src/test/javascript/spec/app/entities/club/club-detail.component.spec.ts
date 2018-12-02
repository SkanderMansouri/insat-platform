/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { ClubDetailComponent } from 'app/entities/club/club-detail.component';
import { Club } from 'app/shared/model/club.model';

describe('Component Tests', () => {
    describe('Club Management Detail Component', () => {
        let comp: ClubDetailComponent;
        let fixture: ComponentFixture<ClubDetailComponent>;
        const route = ({ data: of({ club: new Club(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [ClubDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ClubDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ClubDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.club).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
