/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { ClubUpdateComponent } from 'app/entities/club/club-update.component';
import { ClubService } from 'app/entities/club/club.service';
import { Club } from 'app/shared/model/club.model';

describe('Component Tests', () => {
    describe('Club Management Update Component', () => {
        let comp: ClubUpdateComponent;
        let fixture: ComponentFixture<ClubUpdateComponent>;
        let service: ClubService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [ClubUpdateComponent]
            })
                .overrideTemplate(ClubUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ClubUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClubService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Club(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.club = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Club();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.club = entity;
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
