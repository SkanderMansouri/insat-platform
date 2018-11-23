/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsatTestModule } from '../../../test.module';
import { ClubDeleteDialogComponent } from 'app/entities/club/club-delete-dialog.component';
import { ClubService } from 'app/entities/club/club.service';

describe('Component Tests', () => {
    describe('Club Management Delete Component', () => {
        let comp: ClubDeleteDialogComponent;
        let fixture: ComponentFixture<ClubDeleteDialogComponent>;
        let service: ClubService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [ClubDeleteDialogComponent]
            })
                .overrideTemplate(ClubDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ClubDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClubService);
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
