import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { JobExecutionUpdateComponent } from 'app/entities/job-execution/job-execution-update.component';
import { JobExecutionService } from 'app/entities/job-execution/job-execution.service';
import { JobExecution } from 'app/shared/model/job-execution.model';

describe('Component Tests', () => {
  describe('JobExecution Management Update Component', () => {
    let comp: JobExecutionUpdateComponent;
    let fixture: ComponentFixture<JobExecutionUpdateComponent>;
    let service: JobExecutionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [JobExecutionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(JobExecutionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobExecutionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobExecutionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobExecution(123);
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
        const entity = new JobExecution();
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
