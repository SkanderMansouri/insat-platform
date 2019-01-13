/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsatTestModule } from '../../../test.module';
import { InsatEventDeleteDialogComponent } from 'app/entities/insat-event/insat-event-delete-dialog.component';
import { InsatEventService } from 'app/entities/insat-event/insat-event.service';

describe('Component Tests', () => {
    describe('InsatEvent Management Delete Component', () => {
        let comp: InsatEventDeleteDialogComponent;
        let fixture: ComponentFixture<InsatEventDeleteDialogComponent>;
        let service: InsatEventService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [InsatEventDeleteDialogComponent]
            })
                .overrideTemplate(InsatEventDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InsatEventDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InsatEventService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
