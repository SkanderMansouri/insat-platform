/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { InsatEventDetailComponent } from 'app/entities/insat-event/insat-event-detail.component';
import { InsatEvent } from 'app/shared/model/insat-event.model';

describe('Component Tests', () => {
    describe('InsatEvent Management Detail Component', () => {
        let comp: InsatEventDetailComponent;
        let fixture: ComponentFixture<InsatEventDetailComponent>;
        const route = ({ data: of({ insatEvent: new InsatEvent(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [InsatEventDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InsatEventDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InsatEventDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.insatEvent).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
