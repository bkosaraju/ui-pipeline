import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { JobConfigUpdateComponent } from 'app/entities/job-config/job-config-update.component';
import { JobConfigService } from 'app/entities/job-config/job-config.service';
import { JobConfig } from 'app/shared/model/job-config.model';

describe('Component Tests', () => {
  describe('JobConfig Management Update Component', () => {
    let comp: JobConfigUpdateComponent;
    let fixture: ComponentFixture<JobConfigUpdateComponent>;
    let service: JobConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [JobConfigUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(JobConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JobConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JobConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new JobConfig(123);
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
        const entity = new JobConfig();
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
