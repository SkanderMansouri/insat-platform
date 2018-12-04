/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { JoinClubRequestUpdateComponent } from 'app/entities/join-club-request/join-club-request-update.component';
import { JoinClubRequestService } from 'app/entities/join-club-request/join-club-request.service';
import { JoinClubRequest } from 'app/shared/model/join-club-request.model';

describe('Component Tests', () => {
    describe('JoinClubRequest Management Update Component', () => {
        let comp: JoinClubRequestUpdateComponent;
        let fixture: ComponentFixture<JoinClubRequestUpdateComponent>;
        let service: JoinClubRequestService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [JoinClubRequestUpdateComponent]
            })
                .overrideTemplate(JoinClubRequestUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(JoinClubRequestUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JoinClubRequestService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new JoinClubRequest(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.joinClubRequest = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new JoinClubRequest();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.joinClubRequest = entity;
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
