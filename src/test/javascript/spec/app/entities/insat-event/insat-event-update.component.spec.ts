/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { InsatEventUpdateComponent } from 'app/entities/insat-event/insat-event-update.component';
import { InsatEventService } from 'app/entities/insat-event/insat-event.service';
import { InsatEvent } from 'app/shared/model/insat-event.model';

describe('Component Tests', () => {
    describe('InsatEvent Management Update Component', () => {
        let comp: InsatEventUpdateComponent;
        let fixture: ComponentFixture<InsatEventUpdateComponent>;
        let service: InsatEventService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [InsatEventUpdateComponent]
            })
                .overrideTemplate(InsatEventUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InsatEventUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InsatEventService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new InsatEvent(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.insatEvent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new InsatEvent();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.insatEvent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
