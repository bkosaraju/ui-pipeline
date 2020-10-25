import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { JobTaskOrderUpdateComponent } from 'app/entities/job-task-order/job-task-order-update.component';
import { JobTaskOrderService } from 'app/entities/job-task-order/job-task-order.service';
import { JobTaskOrder } from 'app/shared/model/job-task-order.model';

describe('Component Tests', () => {
  describe('JobTaskOrder Management Update Component', () => {
    let comp: JobTaskOrderUpdateComponent;
    let fixture: ComponentFixture<JobTaskOrderUpdateComponent>;
    let service: JobTaskOrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [JobTaskOrderUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(JobTaskOrderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobTaskOrderUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobTaskOrderService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobTaskOrder(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobTaskOrder();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
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
