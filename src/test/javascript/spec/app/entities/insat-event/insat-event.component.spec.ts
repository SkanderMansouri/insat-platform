/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InsatTestModule } from '../../../test.module';
import { InsatEventComponent } from 'app/entities/insat-event/insat-event.component';
import { InsatEventService } from 'app/entities/insat-event/insat-event.service';
import { InsatEvent } from 'app/shared/model/insat-event.model';

describe('Component Tests', () => {
    describe('InsatEvent Management Component', () => {
        let comp: InsatEventComponent;
        let fixture: ComponentFixture<InsatEventComponent>;
        let service: InsatEventService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [InsatEventComponent],
                providers: []
            })
                .overrideTemplate(InsatEventComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InsatEventComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InsatEventService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new InsatEvent(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.insatEvents[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
