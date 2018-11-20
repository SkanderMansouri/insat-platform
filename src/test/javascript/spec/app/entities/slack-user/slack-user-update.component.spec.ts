/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsatTestModule } from '../../../test.module';
import { SlackUserUpdateComponent } from 'app/entities/slack-user/slack-user-update.component';
import { SlackUserService } from 'app/entities/slack-user/slack-user.service';
import { SlackUser } from 'app/shared/model/slack-user.model';

describe('Component Tests', () => {
    describe('SlackUser Management Update Component', () => {
        let comp: SlackUserUpdateComponent;
        let fixture: ComponentFixture<SlackUserUpdateComponent>;
        let service: SlackUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [SlackUserUpdateComponent]
            })
                .overrideTemplate(SlackUserUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SlackUserUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SlackUserService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SlackUser(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.slackUser = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SlackUser();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.slackUser = entity;
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
