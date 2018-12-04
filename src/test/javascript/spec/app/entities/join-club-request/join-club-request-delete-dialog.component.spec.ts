/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsatTestModule } from '../../../test.module';
import { JoinClubRequestDeleteDialogComponent } from 'app/entities/join-club-request/join-club-request-delete-dialog.component';
import { JoinClubRequestService } from 'app/entities/join-club-request/join-club-request.service';

describe('Component Tests', () => {
    describe('JoinClubRequest Management Delete Component', () => {
        let comp: JoinClubRequestDeleteDialogComponent;
        let fixture: ComponentFixture<JoinClubRequestDeleteDialogComponent>;
        let service: JoinClubRequestService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [JoinClubRequestDeleteDialogComponent]
            })
                .overrideTemplate(JoinClubRequestDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(JoinClubRequestDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JoinClubRequestService);
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
